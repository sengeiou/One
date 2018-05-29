package com.ubt.en.alpha1e.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.ubt.baselib.globalConst.BaseHttpEntity;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.model1E.BaseResponseModel;
import com.ubt.baselib.model1E.UserInfoModel;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.FileUtils;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.baselib.utils.ZipUtils;
import com.ubt.baselib.utils.http1E.OkHttpClientUtils;
import com.ubt.en.alpha1e.R;
import com.ubt.en.alpha1e.model.GetLanguageRequest;
import com.ubt.en.alpha1e.model.LanguageDownResponse;
import com.ubt.loginmodule.LoginHttpEntity;
import com.ubt.loginmodule.requestModel.GetUserInfoRequest;
import com.vise.log.ViseLog;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class WelcomPrenster extends BasePresenterImpl<WelcomContact.View> implements WelcomContact.Presenter {


    /**
     * 初始化语言包
     * 首先判断用户信息是否存在，用户存在则请求最新的语言包，
     * 用户不存在则判断之前是否已经存在语言包版本号
     * ，如果存在语言包版本号，则说明之前本地已经存在了，直接返回结果
     * 如果不存在语言包版本号则说明是第一次安装，先复制assets里面的语言包到SD卡然后加载
     * *
     *
     * @param context
     */
    @Override
    public void initLanugage(final Context context) {

        if (SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO) != null) {
            getLanguageType(context);
        } else {
            //判断本地是否有语言包
            String assetPath = FileUtils.getCacheDirectory(context, "") + Constant1E.LANUGAGE_PATH +
                    File.separator + Constant1E.LANGUAGE_NAME;
            ViseLog.d("assets===" + assetPath);
            ViseLog.d("是否存在" + FileUtils.isFileExists(assetPath));
            if (!FileUtils.isFileExists(assetPath)) {
                ViseLog.d("文件不存在，将Assets文件下的语言包复制到SD卡");
                //复制assets下的语言包到SD卡，skinPath为空则复制失败，直接返回借宿，skinPath不为空则说明复制成功然后加载语言包。
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        e.onNext(FileUtils.copySkinFromAssets(context, Constant1E.LANGUAGE_NAME));
                        ViseLog.d("rxjava subscribe===" + FileUtils.copySkinFromAssets(context, Constant1E.LANGUAGE_NAME));
                    }
                }).subscribeOn(Schedulers.io())               //在IO线程进行网络请求
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String skinPath) throws Exception {
                                ViseLog.d("rxjava===accept" + skinPath);
                                if (TextUtils.isEmpty(skinPath)) {
                                    if (mView != null) {
                                        mView.updateLanguageCompleted();
                                    }
                                } else {
                                    /**
                                     * 根据手机系统语言再从语言包中选择一种语言
                                     */
                                    final String Language = SkinManager.getInstance().getLanguageByLocal(R.array.main_ui_lanuages_up);
                                    ViseLog.d("当前语言包===" + Language);
                                    SkinManager.getInstance().loadSkin(Language, new SkinManager.SkinListener() {
                                        @Override
                                        public void onStart() {
                                        }

                                        @Override
                                        public void onSuccess() {
                                            if (mView != null) {
                                                mView.updateLanguageCompleted();
                                            }
                                            SPUtils.getInstance().put(Constant1E.CURRENT_APP_LANGUAGE, Language);
                                        }

                                        @Override
                                        public void onFailed(String errMsg) {
                                            if (mView != null) {
                                                mView.updateLanguageCompleted();
                                            }
                                        }
                                    });
                                }
                            }
                        });


            } else {
                if (mView != null) {
                    mView.updateLanguageCompleted();
                }
            }


        }
    }


    /**
     * 获取语言包最新版本
     *
     * @param context
     */

    private void getLanguageType(final Context context) {
        GetLanguageRequest request = new GetLanguageRequest();
        request.setUserId(String.valueOf(SPUtils.getInstance().getInt(Constant1E.SP_USER_ID)));
        request.setToken(SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN));
        request.setType("1");
        request.setVersion(SPUtils.getInstance().getString(Constant1E.CURRENT_APP_LANGUAGE_VERSION));
        ViseLog.d(request.toString());
        ViseLog.d(LoginHttpEntity.GET_LANGUAGE_TYPE);
        ViseLog.d(BaseHttpEntity.BASIC_UBX_SYS);

        ViseHttp.POST(LoginHttpEntity.GET_LANGUAGE_TYPE)
                .baseUrl("http://10.10.1.14:8080/")
                .setJson(GsonImpl.get().toJson(request))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ViseLog.d("getLanguageType==" + response);

                        BaseResponseModel<LanguageDownResponse> baseResponseModel = GsonImpl.get().toObject(response,
                                new TypeToken<BaseResponseModel<LanguageDownResponse>>() {
                                }.getType());
                        if (baseResponseModel.status) {
                            LanguageDownResponse languageDownResponse = baseResponseModel.models;
                            if (languageDownResponse != null) {
                                downLoadLanguage(languageDownResponse, context);
                            } else {
                                if (mView != null) {
                                    mView.updateLanguageCompleted();
                                }
                            }
                        } else {
                            if (mView != null) {
                                mView.updateLanguageCompleted();
                            }
                        }
                    }

                    @Override
                    public void onFail(int i, String s) {
                        ViseLog.d(s);
                        if (mView != null) {
                            mView.updateLanguageCompleted();
                        }
                    }
                });

    }


    /**
     * 下载压缩包及解压
     *
     * @param response
     * @param context
     */
    private void downLoadLanguage(final LanguageDownResponse response, final Context context) {
        final String path = FileUtils.getCacheDirectory(context, "") + Constant1E.LANUGAGE_DOWN_PATh + File.separator;//下载路径
        final String destPath = FileUtils.getCacheDirectory(context, "") + Constant1E.LANUGAGE_PATH;//解压文件路径

        OkHttpClientUtils.getDownloadFile(response.getUrl()).execute(new FileCallBack(path, Constant1E.LANGUAGE_ZIP_NAME) {
            @Override
            public void onError(Call call, Exception e, int id) {
                ViseLog.d("downLoadLanguage onError:" + e.getMessage());
                if (mView != null) {
                    mView.updateLanguageCompleted();
                }
            }

            @Override
            public void onResponse(final File dowloadFile, int id) {
                ViseLog.d("downLoadLanguage onResponse:" + dowloadFile.getAbsolutePath());

                ViseLog.d("zippath==" + dowloadFile.getAbsolutePath() + "    destPath = " + destPath);

                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        //判断本地是否有语言包
                        String skinPath = FileUtils.getCacheDirectory(context, "") + Constant1E.LANUGAGE_PATH +
                                File.separator + Constant1E.LANGUAGE_NAME;
                        File file = new File(skinPath);
                        ViseLog.d("assets===" + skinPath);
                        if (file.exists()) {
                            file.delete();
                        }
                        ZipUtils.unzipFile(dowloadFile.getAbsolutePath(), destPath);
                        e.onNext(1);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        ViseLog.d("integer" + integer);
                        SPUtils.getInstance().put(Constant1E.CURRENT_APP_LANGUAGE_VERSION, response.getVersion());
                        if (mView != null) {
                            mView.updateLanguageCompleted();
                        }
                    }
                });

            }

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                //  ViseLog.d("downloadVideo inProgress:" + progress);
            }
        });

    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest();
        getUserInfoRequest.setToken(SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN));
        getUserInfoRequest.setUserId(SPUtils.getInstance().getInt(Constant1E.SP_USER_ID));

        ViseHttp.POST(LoginHttpEntity.USER_GET_INFO).baseUrl(LoginHttpEntity.BASE_URL)
                .setJson(GsonImpl.get().toJson(getUserInfoRequest))
                .connectTimeOut(5)
                .request(new ACallback<String>() {

                    @Override
                    public void onSuccess(String data) {
                        ViseLog.d("USER_GET_INFO onSuccess:" + data);
                        BaseResponseModel<UserInfoModel> baseResponseModel = GsonImpl.get().toObject(data,
                                new TypeToken<BaseResponseModel<UserInfoModel>>() {
                                }.getType());
                        if (baseResponseModel.status) {
                            UserInfoModel userInfoModel = baseResponseModel.models;
                            ViseLog.d("userInfoModel:" + (userInfoModel == null));
                            if (userInfoModel != null) {
                                SPUtils.getInstance().saveObject(Constant1E.SP_USER_INFO, userInfoModel);
                            }
                        }
                        if (mView != null) {
                            mView.getUserInfoCompleted();
                        }
                    }

                    @Override
                    public void onFail(int code, String errmsg) {
                        ViseLog.d("USER_GET_INFO onFail:" + code + "  errmsg:" + errmsg);
                        if (mView != null) {
                            mView.getUserInfoCompleted();
                        }
                    }
                });
    }


    private void zip1(Context context) {
        String destPath = FileUtils.getCacheDirectory(context, "") + "skins/" + "language.skin";
        String zipPath = FileUtils.getCacheDirectory(context, "") + "skins/" + "language.zip";
        try {
            ZipUtils.zipFile(destPath, zipPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
