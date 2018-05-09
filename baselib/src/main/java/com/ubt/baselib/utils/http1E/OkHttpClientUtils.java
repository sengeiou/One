package com.ubt.baselib.utils.http1E;

import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.SPUtils;
import com.vise.log.ViseLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/3/21.
 */
public class OkHttpClientUtils {

    /**
     * 获取json数据
     *
     * @param url
     * @param params
     * @return
     */
    public static RequestCall getJsonByPostRequest(String url, String params) {
        return getJsonByPostRequest(url, params, -1);
    }

    /**
     * 获取json数据
     *
     * @param url
     * @param params
     * @param id
     * @return
     */
    public static RequestCall getJsonByPostRequest(String url, String params, int id) {
        ViseLog.d( "url===" + url + "request==" + params.toString());
        return OkHttpUtils.postString()
                .url(url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(params)
                .id(id)
                .build();

    }

    /**
     * 带文件请求
     *
     * @param url
     * @param id
     * @return
     */
    public static RequestCall getJsonByPostRequest(String url, File file, BaseRequest request, int id) {
        request.setUserId(SPUtils.getInstance().getString(Constant1E.SP_USER_ID));
        request.setToken(SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN));

        ViseLog.d( "url===" + url + "request==" + request.toString());
        if (null != file) {
            return OkHttpUtils.post()
                    .addFile("headPic", file.getName(), file)
                    .url(url)
                    .params(GsonImpl.get().getMap(request))//
                    .id(id)
                    .build()//
                    ;
        } else {
            return OkHttpUtils.post()
                    .url(url)
                    .params(GsonImpl.get().getMap(request))//
                    .id(id)
                    .build();
        }
    }

    /**
     * 带多个文件请求
     *
     * @param url
     * @param id
     * @return
     */
    public static RequestCall getJsonByPostRequest(String url,BaseRequest request, Map<String,File> fileMap, int id) {
        request.setUserId(SPUtils.getInstance().getString(Constant1E.SP_USER_ID));
        request.setToken(SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN));
        String params = GsonImpl.get().toJson(request);
        ViseLog.d("url = " + url + "  request = " + request + "   fileMap = " + fileMap);
        if (null != fileMap && !fileMap.isEmpty()) {
            return OkHttpUtils.post()
                    .files("fileMap", fileMap)
                    .url(url)
                    .params(GsonImpl.get().getMap(request))//
                    .id(id)
                    .build();
        } else {
            return getJsonByPostRequest(url,request,id);
        }
    }

    /**
     * 一般post请求
     *
     * @param url
     * @param request
     * @param id
     * @return
     */
    public static RequestCall getJsonByPostRequest(String url, BaseRequest request, int id) {
        request.setUserId(SPUtils.getInstance().getString(Constant1E.SP_USER_ID));
        request.setToken(SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN));
        String params = GsonImpl.get().toJson(request);
        ViseLog.d("url===" + url + "___params==" + params);
        return OkHttpUtils.postString()
                .url(url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(params)
                .id(id)
                .build();

    }

    /**
     * 获取json数据
     *
     * @param url
     * @param id
     * @return
     */
    public static RequestCall getJsonByGetRequest(String url, int id) {
        return OkHttpUtils.get()
                .url(url)
                .id(id)
                .build();

    }

    /**
     * 获取json数据
     *
     * @param url
     * @param id
     * @return
     */
    public static RequestCall getJsonByGetRequest(String url, String token, int id) {
        return OkHttpUtils.get()
                .url(url)
                .id(id)
                .addHeader("authorization", token)
                .build();

    }

    /**
     * 获取json数据
     *
     * @param url
     * @param params
     * @param id
     * @return
     */
    public static RequestCall getJsonByPostRequest(String url, String params, String token, int id) {
        return OkHttpUtils.postString()
                .url(url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(params)
                .addHeader("authorization", token)
                .id(id)
                .build();

    }

    public static RequestCall getJsonByPatchRequest(String url, String params, String token, int id) {
        return OkHttpUtils.patch()
                .url(url)
                .requestBody(params)
                .addHeader("authorization", token)
                .id(id)
                .build();

    }


    public static RequestCall getJsonByPutRequest(String url, String params, int id) {
        ViseLog.d( "url:" + url + "__params:" + params);
        return OkHttpUtils.put()
                .url(url)
                .requestBody(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), params))
                .id(id)
                .build();
    }


    public static RequestCall getDownloadFile(String url) {
        return OkHttpUtils.get()
                .url(url)
                .build();
    }

}
