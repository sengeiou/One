package com.ubt.loginmodule;

import com.ubt.baselib.globalConst.BaseHttpEntity;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/18 13:17
 * @描述:
 */

public class LoginHttpEntity extends BaseHttpEntity{
 /*   public static final String THRID_LOGIN_URL = BASIC_THIRD_LOGIN_URL + "user/login/third";
    public static final String GET_USER_INFO = BASIC_UBX_SYS + "user/get";
    public static final String REQUEST_SMS_CODE = BASIC_UBX_SYS + "user/register";
    public static final String BIND_ACCOUNT = BASIC_UBX_SYS + "user/bind";
    public static final String UPDATE_USERINFO = BASIC_UBX_SYS + "user/update";

    //for email account login
    public static final String LOGIN_USE_EMAIL = "xxx";*/

    /**
     * login base url
     */
    public static final String BASE_URL = "http://10.10.1.14:8080/alpha1e/overseas/user/";
    public static final String BASE_LOGIN_URL = "https://test79.ubtrobot.com/user-service-rest/v2/"; //测试环境
//    public static final String BASE_LOGIN_URL = "https://account.ubtrobot.com/user-service-rest/v2/";  //正式环境

    public static final String RESET = "user/password/reset";
    public static final String LOGIN = "user/login";
    public static final String LOGIN_THIRD = "user/login/third";
    public static final String LOGIN_THIRD_FIRST = "user/login/third/isfirst";
    public static final String VALIDATE_CODE = "user/captcha/verify-result";
    public static final String FIND_PASSWORD = BASE_URL + "backPwd";
    public static final String REGISTER = "user/register";
    public static final String GET_CODE = "htmlEmail/ebotcaptcha";
    public static final String USER_UPDATE = BASE_URL + "update";
    public static final String USER_GET_INFO = "get";



}

