package com.ubt.baselib;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.ubt.baselib.BlueTooth.BTHeartBeatManager;
import com.ubt.baselib.BlueTooth.BlueToothListenerImpl;
import com.ubt.baselib.btCmd1E.cmd.BTCmdHeartBeat;
import com.ubt.baselib.customView.DynamicTimeFormat;
import com.ubt.baselib.customView.MyRefreshFoot;
import com.ubt.baselib.customView.MyRefreshHead;
import com.ubt.baselib.globalConst.BaseHttpEntity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.ContextUtils;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;
import com.vise.netexpand.convert.GsonConverterFactory;
import com.vise.utils.assist.SSLUtil;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.loader.LoaderManager;

import org.litepal.LitePal;

import java.util.HashMap;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

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


    public void init(Application appContext, boolean isIssue) {
        this.isIssue = isIssue;
        BaseHttpEntity.init(this.isIssue);

        SkinManager.getInstance().init(appContext);
        ContextUtils.init(appContext);
        initLog();
        initNet(appContext);
        LoaderManager.getLoader().init(appContext);

        BlueClientUtil.getInstance().init(appContext);
        BlueClientUtil.getInstance().setBlueListener(new BlueToothListenerImpl());
        BTHeartBeatManager.getInstance().init(appContext, new BTCmdHeartBeat().toByteArray(), 5000);
        initSmartRefresh();
        LitePal.initialize(appContext);
        appContext.registerActivityLifecycleCallbacks(new MyLifecycleCallback());
    }

    private void initLog() {
        ViseLog.getLogConfig()
                .configTagPrefix("alpha1e")
                .configAllowLog(true)//是否输出日志
                .configShowBorders(false);//是否排版显示
        ViseLog.plant(new LogcatTree());//添加打印日志信息到Logcat的树
    }

    private void initNet(Context appContext) {
        ViseHttp.init(appContext);
        ViseHttp.CONFIG()
                //配置请求主机地址
                .baseUrl(BaseHttpEntity.BASIC_UBX_SYS)
                //配置全局请求头
                .globalHeaders(new HashMap<String, String>())
                //配置全局请求参数
                .globalParams(new HashMap<String, String>())
                //配置读取超时时间，单位秒
                .readTimeout(30)
                //配置写入超时时间，单位秒
                .writeTimeout(30)
                //配置连接超时时间，单位秒
                .connectTimeout(30)
                //配置请求失败重试次数
                .retryCount(3)
                //配置请求失败重试间隔时间，单位毫秒
                .retryDelayMillis(1000)
                //配置是否使用cookie
                .setCookie(true)
                //配置自定义cookie
//                .apiCookie(new ApiCookie(this))
                //配置是否使用OkHttp的默认缓存
                .setHttpCache(true)
                //配置OkHttp缓存路径
//                .setHttpCacheDirectory(new File(ViseHttp.getContext().getCacheDir(), ViseConfig.CACHE_HTTP_DIR))
                //配置自定义OkHttp缓存
//                .httpCache(new Cache(new File(ViseHttp.getContext().getCacheDir(), ViseConfig.CACHE_HTTP_DIR), ViseConfig.CACHE_MAX_SIZE))
                //配置自定义离线缓存
//                .cacheOffline(new Cache(new File(ViseHttp.getContext().getCacheDir(), ViseConfig.CACHE_HTTP_DIR), ViseConfig.CACHE_MAX_SIZE))
                //配置自定义在线缓存
//                .cacheOnline(new Cache(new File(ViseHttp.getContext().getCacheDir(), ViseConfig.CACHE_HTTP_DIR), ViseConfig.CACHE_MAX_SIZE))
                //配置开启Gzip请求方式，需要服务器支持
//                .postGzipInterceptor()
                //配置应用级拦截器
//                .interceptor(new HttpLogInterceptor()
//                        .setLevel(HttpLogInterceptor.Level.BODY))
                //配置网络拦截器
//                .networkInterceptor(new NoCacheInterceptor())
                //配置转换工厂
                .converterFactory(GsonConverterFactory.create())
                //配置适配器工厂
                .callAdapterFactory(RxJava2CallAdapterFactory.create())
                //配置请求工厂
//                .callFactory(new Call.Factory() {
//                    @Override
//                    public Call newCall(Request request) {
//                        return null;
//                    }
//                })
                //配置连接池
//                .connectionPool(new ConnectionPool())
                //配置主机证书验证
//                .hostnameVerifier(new SSLUtil.UnSafeHostnameVerifier("http://192.168.1.100/"))
                //配置SSL证书验证
                .SSLSocketFactory(SSLUtil.getSslSocketFactory(null, null, null))
        //配置主机代理
//                .proxy(new Proxy(Proxy.Type.HTTP, new SocketAddress() {}))
        ;

    }


    private void initSmartRefresh() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.black);//全局设置主题颜色
                return new MyRefreshHead(context).setTimeFormat(new DynamicTimeFormat("更新于 %s")).setEnableLastTime(false)
                        .setDrawableMarginRight(5);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new MyRefreshFoot(context).setDrawableSize(20).setDrawableMarginRight(5);
            }
        });
    }


}
