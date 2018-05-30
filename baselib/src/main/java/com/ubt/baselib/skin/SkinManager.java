package com.ubt.baselib.skin;

import android.app.Application;
import android.os.Build;
import android.os.LocaleList;
import android.support.v7.app.AppCompatDelegate;

import com.ubt.baselib.R;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.utils.SPUtils;
import com.vise.log.ViseLog;

import java.util.Locale;

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
                .setLaguage(SPUtils.getInstance().getString(Constant1E.CURRENT_APP_LANGUAGE))
                //.addInflater(new MyTextViewLayoutInflater())
//                .setSkinStatusBarColorEnable(false)             // 关闭状态栏换肤
//                .setSkinWindowBackgroundEnable(false)           // 关闭windowBackground换肤
//                .setSkinAllActivityEnable(false)                // true: 默认所有的Activity都换肤; false: 只有实现SkinCompatSupportable接口的Activity换肤
                .loadSkin();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        SkinCompatResources.getString(application, R.string.app_name);
    }

    /**
     * 获取手机系统语言
     *
     * @return
     */
    private String getLocalLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        ViseLog.d("当前手机语言===" + locale.getLanguage());

        return locale.getLanguage() + "_" + locale.getCountry();
    }


    /**
     * 根据手机系统语言再从语言包中选择一种语言
     *
     * @param id 语言包的id
     * @return
     */
    public String getLanguageByLocal(int id) {
        String loaclLanguage = getLocalLanguage();
        String[] languagesUp = getSkinArrayResource(id);
        String Language = "en";
        ViseLog.d("loaclLanguage" + loaclLanguage);
        for (int i = 0; i < languagesUp.length; i++) {
            ViseLog.d("up====" + languagesUp[i]);
            if (loaclLanguage.toLowerCase().contains(languagesUp[i])
                    || loaclLanguage.toLowerCase().equals(languagesUp[i].toLowerCase())) {
                Language = languagesUp[i];
                break;
            }
        }
        return Language;
    }


    /**
     * 根据不同语言加载
     *
     * @param language
     */
    public void loadSkin(String language, final SkinListener listener) {
        SkinCompatManager.getInstance().setLaguage(language).loadSkin(Constant1E.LANGUAGE_NAME, new SkinCompatManager.SkinLoaderListener() {
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


    /**
     * 默认英文语言
     */
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
