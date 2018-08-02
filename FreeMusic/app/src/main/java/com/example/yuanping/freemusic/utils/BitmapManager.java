package com.example.yuanping.freemusic.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yuanping on 4/14/18.
 * 图片工具类
 */

public class BitmapManager {
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * @param fileName: 存储的路径
     * @param bitmap
     * @param sample:   压缩率(0.0 - 1.0),实际使用时为sample*100
     */
    public static void saveBitmap(final String fileName, final Bitmap bitmap, final float sample) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (bitmap != null) {
                    FileOutputStream out = null;
                    try {
                        File file = new File(fileName);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, (int) (sample * 100), out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public static Bitmap getBitmap(String fileName) {
        File file = new File(fileName);
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

    public static Bitmap getBitmap(File file) {
        if (file == null) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            InputStream in = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream in, int reqWidth, int
            reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        options.inSampleSize = calculateInSampleSizeFromOptions(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(in);
    }

    public static List<Bitmap> decodeSampledBitmapFromBitmaps(List<Bitmap> bitmaps, int reqWidth,
                                                              int reqHeight) {
        List<Bitmap> result = new ArrayList<>();
        for (int i = 0; i < bitmaps.size(); i++) {
            result.add(Bitmap.createScaledBitmap(bitmaps.get(i), reqWidth, reqHeight, false));
        }
        return result;
    }

    //一般是对于不能通过BitmapFactory压缩的图片,就使用这种方式压缩
    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap, int reqWidth, int
            reqHeight) {
        if (bitmap == null) {
            return null;
        }
        int sampleSize = calculateInSampleSizeFromOriginBitmap(bitmap, reqWidth, reqHeight);
        if (sampleSize > 1) {
            int width = bitmap.getWidth() / sampleSize;
            int height = bitmap.getHeight() / sampleSize;
            return Bitmap.createScaledBitmap(bitmap, width, height, true);
        }
        return bitmap;
    }

    //计算压缩率
    public static int calculateInSampleSizeFromOptions(BitmapFactory.Options options, int
            reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int sampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / sampleSize) >= reqHeight
                    && (halfWidth / sampleSize) >= reqWidth) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }

    public static int calculateInSampleSizeFromOriginBitmap(Bitmap origin, int reqWidth, int
            reqHeight) {
        int width = origin.getWidth();
        int height = origin.getHeight();
        int sampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / sampleSize) >= reqHeight
                    && (halfWidth / sampleSize) >= reqWidth) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }

    //模糊化处理图片
    public static Bitmap blurBitmap(Bitmap origin, int scaleRatio, int blurRadius) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(origin,
                origin.getWidth() / scaleRatio,
                origin.getHeight() / scaleRatio,
                false);
        return FastBlur.doBlur(scaledBitmap, blurRadius, true);
    }

    // 根据原来的图片得到给定尺寸的圆形图片
    public static Bitmap getCircleBitmap(Bitmap origin, int radius) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        int originRadius = Math.min(width, height) / 2;
        Bitmap target = Bitmap.createBitmap(originRadius * 2, originRadius * 2, Bitmap.Config
                .ARGB_8888);
        Canvas canvas = new Canvas(target);
        Paint paint = new Paint();
        canvas.drawCircle(originRadius, originRadius, originRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect srcRect = new Rect(width - originRadius * 2, height - originRadius * 2, originRadius *
                2, originRadius * 2);
        Rect destRect = new Rect(0, 0, originRadius * 2, originRadius * 2);
        canvas.drawBitmap(origin, srcRect, destRect, paint);
        return Bitmap.createScaledBitmap(target, radius * 2, radius * 2, false);
    }

    //计算拉伸率
   /* public static int calculateClamSizeFromBitmap(Bitmap origin, int reqWidth, int reqHeight) {
        int w = origin.getWidth();
        int h = origin.getHeight();
        int clamSize = 1;
        if (reqWidth > w || reqHeight > h) {
            final int halfHeight = reqHeight / 2;
            final int halfWidth = reqWidth / 2;
            while ((halfHeight / clamSize) >= h
                    || (halfWidth / clamSize) >= w) {
                clamSize *= 2;
            }
        }
        return clamSize;
    }

    //拉伸或者压缩图片到固定尺寸
    public static Bitmap scaleToFixSizeFromBitmap(Bitmap origin, int reqWidth, int reqHeight) {
        int w = origin.getWidth();
        int h = origin.getHeight();
        int scaleSize = 1;
        if (w > reqWidth || h > reqHeight) {
            scaleSize = calculateInSampleSizeFromOriginBitmap(origin, reqWidth, reqHeight);
            return Bitmap.createScaledBitmap(origin, w / scaleSize, h / scaleSize, false);
        }
        scaleSize = calculateClamSizeFromBitmap(origin, reqWidth, reqHeight);
        Log.d("@HusterYP", String.valueOf(w * scaleSize + " ; " + h * scaleSize + " ; " + w + " ;" +
                " " + h + " ; " + reqWidth + " ; " + reqHeight));
        int realWidth = Math.max(w * scaleSize,reqWidth);
        int realHeight = Math.max(h*scaleSize,reqHeight);
        return Bitmap.createScaledBitmap(origin, realWidth,realHeight, false);
    }*/
}
