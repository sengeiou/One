package com.ubt.baselib;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.ubt.baselib.globalConst.BaseHttpEntity;
import com.ubt.baselib.skin.CustomSDCardLoader;

import skin.support.SkinCompatManager;
import skin.support.content.res.SkinCompatResources;
import skin.support.utils.MyTextViewLayoutInflater;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/5 14:07
 * @描述: 初始化基础库
 */

public class ConfigureBaseLib {

    private static ConfigureBaseLib instance;
    private boolean isIssue = false;

    private ConfigureBaseLib() {
    }

    public static ConfigureBaseLib getInstance() {
        if (instance == null) {
            synchronized (ConfigureBaseLib.class) {
                if (instance == null) {
                    instance = new ConfigureBaseLib();
                }
            }
        }
        return instance;
    }


    public void init(boolean isIssue) {
        this.isIssue = isIssue;
        BaseHttpEntity.init(this.isIssue);
    }

    public void initSkin(Application application) {
        SkinCompatManager.withoutActivity(application)
                .addStrategy(new CustomSDCardLoader())          // 自定义加载策略，指定SDCard路径
                .addInflater(new MyTextViewLayoutInflater())
//                .setSkinStatusBarColorEnable(false)             // 关闭状态栏换肤
//                .setSkinWindowBackgroundEnable(false)           // 关闭windowBackground换肤
//                .setSkinAllActivityEnable(false)                // true: 默认所有的Activity都换肤; false: 只有实现SkinCompatSupportable接口的Activity换肤
                .loadSkin();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        SkinCompatResources.getString(application, R.string.app_name);
    }

    /**
     * 加载皮肤包
     *
     * @param boolValue
     */
    public void setSkinConfig(boolean boolValue) {
        if (boolValue) {
            SkinCompatManager.getInstance().loadSkin("night.skin", null, SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
        } else {
            SkinCompatManager.getInstance().restoreDefaultTheme();
        }
    }

    /**
     * 根据Id获取String
     *
     * @param context
     * @param id
     * @return
     */
    public static String getTextById(Context context, int id) {
        return SkinCompatResources.getString(context, id);
    }

}
