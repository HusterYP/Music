# 夜间模式
1. 夜间模式的实现,这里需要注意的是,不要在XMl中指定主题,因为这样的话,通过的代码设置的主题就没用了;同时需要在调用super.onCreate()之前通过代码改变主题;参考博文:https://blog.csdn.net/u010687392/article/details/48088571
2. 将当前的状态模式保存在Preference中
3. 如果在其他Activity中改变模式的话,那么当退出该Activity的时候,之前创建的Activity会出现闪烁的现象,所以这里设置的是在MainActivity中提供选项改变

# 头像设置
1. 凡是涉及到图片操作,都应该进行相应的压缩,可以参考[官方文档](https://developer.android.com/training/camera/photobasics.html#TaskPath),在通过调用本地应用得到图片的时候会有选项设置是否得到缩略图
2. 当将得到的头像进行本地缓存的时候,即存储在应用私有文件;但是此时存储的时候,会因为图片比较大,所以存储会慢,虽然没有导致严重的ANR,但是也有明显的界面延迟,一个很明显的现象是,当调用本地相册选取图片的时候,迟迟无法返回到本应用,或者中间出现一段较长时间的黑屏
3. 另一个就是,在刚开始进入应用时,先从本地得到头像进行设置,但是这里会因为图片比较大,造成一段较长时间的启动,同时也会造成初次的侧滑菜单会出现明显的卡顿现象(之后不会再卡顿,以为直接缓存在内存中了);另外就是,这种情况下点击夜间模式和日间模式的切换也会有明显的卡顿
4. 下为,没有压缩和处理的缓存和获取图片

        //头像本地缓存
        private void saveAvatar(Bitmap bitmap) {
            if (bitmap != null) {
                FileOutputStream out = null;
                try {
                    File file = new File(getFilesDir(), AVATAR);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                } catch (Exception e) {
                } finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //获取本地保存头像(如果有的话)
        private Bitmap getAvatar() {
            File file = new File(getFilesDir(), AVATAR);
            if (!file.exists())
                return null;
            FileInputStream in = null;
            Bitmap bitmap = null;
            try {
                in = new FileInputStream(file);
                bitmap = BitmapFactory.decodeStream(in);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }

5. 解决办法是
> 1. 图片压缩: [文档](https://developer.android.com/topic/performance/graphics/load-bitmap.html#read-bitmap)  (**注**: 图片压缩问题见Notes/细节/Android.md中15-16部分)
> 2. 异步缓存和获取

# 主界面
1. actionBar上的三个图标,可能因为比较小,难以点击,这是因为找图片时候的问题,图片尺寸不对
2. 当结合使用TabLayout和ViewPager的时候,需要注意,此时可以不直接设置TabLayout的每个TabItem的标题,当使用TabLayout.setUpWithViewPager()的时候,会自动绑定ViewPager中Fragment的Title,此时只需要覆盖ViewPager的Adapter的getTitle()方法即可
3. 关于TabLayout上Indicator的滑动动画可以参见开源库: https://github.com/ogaclejapan/SmartTabLayout
4. 设置TabLayout上Indicator的宽度:https://stackoverflow.com/questions/40480675/android-tab-layout-wrap-tab-indicator-width-with-respect-to-tab-title
5. 图片从网上的随机请求用的API(其中的200和100指的是返回图片的大小): https://picsum.photos/450/150/?image=3

# Banner
1. URI和URL的区别是什么??
2. 有时候onCreateView()(在其他中如onCreate())会多次调用,那么如果在该方法中进行异步网络请求的话,那岂不是会new多个??
3. 实现了图片压缩,缓存(缓存在Cache),网络请求图片
4. 图片的缓存和异步请求实现:
> 1. Adapter,BitmapManager,BitmapDataAsync,这三者之间的关系是,BitmapManager充当了Adapter和BitmapDataAsync的桥梁,避免了Adapter和BitmapDataAsync的直接联系,同时可以在BitmapManager中进行缓存和图片处理(压缩之类的)
> 2. 通过AsyncTask异步请求,同时通过接口将结果返回给BitmapManager
5. TODO: Banner底部指示器

# 主界面底部播放栏
1. 自定义主界面下面的播放按钮时,使用自定义属性,用到Color自定义属性的时候,出现了图形显示不出来的情况,原因是使用`mPaint.setColor()`为画笔设置int类型颜色的时候,应该设置的ARGB,而不是RGB,自己的默认颜色定义为RGB(如`0xFF0000`),所以导致画笔透明,应该使用形若`0xFFFF0000`形式(也可以将颜色资源设置在Color.xml文件中,然后在onDraw() 中通过getResource()得到)
2. 自定义的ProgressBar不能改变进度,和其中的一个`mIndeterminate`变量有关(与该变量无关,参见:ProgressBar分为确定的和不确定的，上面说的播放进度、缓存等就是确定的。相反地，不确定的就是不清楚、不确定一个操作需要多长时间来完成，这个时候就需要用的不确定的ProgressBar了。这个是由属性android:indeterminate来控制的，如果设置为true的话，那么ProgressBar就可能是圆形的滚动条或者水平的滚动条（由样式决定）。默认情况下，如果是水平进度条，那么就是确定的); 之所以不能改变进度是因为自己的逻辑写错了,参见下面的逻辑,progress++是先用progress,然后再让progress++,但是因为在其之前先使用了` progress = progressBar.getProgress();`所以造成,每次progress的值是没有改变的(垃圾-_-)

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progress = progressBar.getProgress();
            // progressBar.setProgress(++progress);//正确写法应该是这样的
            progressBar.setProgress(progress++);//我最开始写的是这样的
            Log.d("@HusterYP",String.valueOf(progress));
            if (progress <= 100) {
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };
3. TODO: 底部播放导航栏中当标题过长时,会以滚动的形式出现
4. ViewPager除了使用Fragment来填充以外,还可以使用View来填充,需要注意的是,使用View来填充的时候,记住在复写的`instantiateItem()`方法中,将inflate好的View添加进container中,否则显示不出来(即`container.addView(view);`)([参考博文](https://www.cnblogs.com/fuly550871915/p/4922953.html))
5. 如果要对整个ViewPager设置点击事件(onClickListener),如果用ViewPager.setOnClickListener()是不行的(更深层次的原因可以分析ViewPager中的onTouchEvent方法,(见<<Android开发艺术探索>>)),只能通过对ViewPager中整个Item的ViewGroup进行设置点击事件,达到曲线救国的目的

# 音乐播放界面
1. 背景为将图片进行模糊处理后设置:[参考博客](https://blog.csdn.net/zuiwuyuan/article/details/52125804)
2. 当自定义长条形的ProgressBar的时候,setProgress()和在xml中设置progress都不起作用,原因是需要设置`style="@android:style/Widget.ProgressBar.Horizontal"`属性;[参见文档](https://developer.android.com/reference/android/widget/ProgressBar) (但是这里有疑问的地方是,为什么上面的自定义圆形进度条没有设置该属性还是可以呢??)
3. TODO: 提取进度条的库
4. AS的`Analyze -> Code Cleanup`功能了解一下

# 音乐播放
1. [文档](https://developer.android.com/guide/topics/media/mediaplayer)
