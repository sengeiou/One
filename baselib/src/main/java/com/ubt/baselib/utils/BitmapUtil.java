package com.ubt.baselib.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author：liuhai
 * @date：2018/4/27 10:20
 * @modifier：ubt
 * @modify_date：2018/4/27 10:20
 * [A brief description]
 * version
 */

public class BitmapUtil {

    /**
     * 缩放倍数
     *
     * @param res
     * @param resId
     * @param rate
     * @return
     */
    public static Bitmap compressImage(Resources res, int resId, int rate) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = rate;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);

    }
}
