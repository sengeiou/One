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
    public static final String BASE_URL =BaseHttpEntity.BASIC_UBX_SYS /*"https://interface.ubtrobot.com/alpha1e/overseas/user/"*/; //正式环境
//    public static final String BASE_URL = "http://10.10.1.14:8080/alpha1e/overseas/user/";  //测试环境
//    public static final String BASE_LOGIN_URL = "https://test79.ubtrobot.com/user-service-rest/v2/"; //测试环境
    public static final String BASE_LOGIN_URL = BaseHttpEntity.BASE_UBX_COMMON; //正式环境

    public static final String RESET = "user-service-rest/v2/user/password/reset";
    public static final String LOGIN = "user-service-rest/v2/user/login";
    public static final String LOGIN_THIRD = "user-service-rest/v2/user/login/third";
    public static final String LOGIN_THIRD_FIRST = "user-service-rest/v2/user/login/third/isfirst";
    public static final String VALIDATE_CODE = "user-service-rest/v2/user/captcha/verify-result";
//    public static final String FIND_PASSWORD = BASE_URL + "backPwd";
    public static final String REGISTER = "user-service-rest/v2/user/register";
    public static final String GET_CODE = "user-service-rest/v2/htmlEmail/ebotcaptcha";
    public static final String USER_UPDATE = "alpha1e/overseas/user/update";
    public static final String USER_GET_INFO = "alpha1e/overseas/user/get";
    /**
     * 请求语言包
     */
    public static final String GET_LANGUAGE_TYPE="alpha1e/version/checkAppLanguage";

}

