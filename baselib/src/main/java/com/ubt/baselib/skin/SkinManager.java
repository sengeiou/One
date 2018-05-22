package com.ubt.baselib.skin;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.ubt.baselib.R;

import skin.support.SkinCompatManager;
import skin.support.content.res.SkinCompatResources;

/**
 * @author：liuhai
 * @date：2018/4/13 15:12
 * @modifier：ubt
 * @modify_date：2018/4/13 15:12
 * [A brief description]
 * version
 */

public class SkinManager {
    private Application mContext;
    private static SkinManager instance;


    public SkinManager() {

    }

    public void init(Application application) {
        mContext = application;
        initSkin(application);
    }

    public static SkinManager getInstance() {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager();
                }
            }
        }
        return instance;
    }

    public void initSkin(Application application) {
        SkinCompatManager.withoutActivity(application)
                .addStrategy(new CustomSDCardLoader())          // 自定义加载策略，指定SDCard路径
                //.addInflater(new MyTextViewLayoutInflater())
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
     * 根据不同语言加载
     *
     * @param language
     */
    public void loadSkin(String language, final SkinListener listener) {
        SkinCompatManager.getInstance().setLaguage(language).loadSkin("night.skin", new SkinCompatManager.SkinLoaderListener() {
            @Override
            public void onStart() {
                listener.onStart();
            }

            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onFailed(String errMsg) {
                listener.onFailed(errMsg);
            }
        }, CustomSDCardLoader.SKIN_LOADER_STRATEGY_SDCARD);

    }


    public void restoreDefaultTheme() {
        SkinCompatManager.getInstance().restoreDefaultTheme();
    }

    /**
     * 根据Id获取String
     *
     * @param id
     * @return
     */
    public String getTextById(int id) {

        //return mContext.getResources().getString(id);
        return SkinCompatResources.getString(mContext, id);
    }

    public String[] getSkinArrayResource(int id) {
        return SkinCompatResources.getStringArray(mContext, id);
    }

    public interface SkinListener {
        /**
         * 开始加载.
         */
        void onStart();

        /**
         * 加载成功.
         */
        void onSuccess();

        /**
         * 加载失败.
         *
         * @param errMsg 错误信息.
         */
        void onFailed(String errMsg);
    }

}
