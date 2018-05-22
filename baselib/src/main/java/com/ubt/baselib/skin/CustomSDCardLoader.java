package com.ubt.baselib.skin;

import android.content.Context;

import java.io.File;

import skin.support.load.SkinSDCardLoader;
import skin.support.utils.SkinFileUtils;

/**
 * @author：liuhai
 * @date：2018/4/3 16:18
 * @modifier：ubt
 * @modify_date：2018/4/3 16:18
 * [A brief description]
 * version
 */

public class CustomSDCardLoader extends SkinSDCardLoader {
    public static final int SKIN_LOADER_STRATEGY_SDCARD = Integer.MAX_VALUE;

    @Override
    protected String getSkinPath(Context context, String skinName) {
        return new File(SkinFileUtils.getSkinDir(context), skinName).getAbsolutePath();
    }



    @Override
    public int getType() {
        return SKIN_LOADER_STRATEGY_SDCARD;
    }
}
