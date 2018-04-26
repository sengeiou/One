package com.ubt.baselib.globalConst;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/18 13:06
 * @描述: 全局的常量定义
 */

public interface Constant1E {
    /**
     * 机器人productId 和DSN
     */
    String SP_ROBOT_PRODUCT_ID = "sp_robot_product_id";
    String SP_ROBOT_DSN = "sp_robot_dsn";

    /**
     * 用户账号属性
     */
    String SP_USER_ID = "sp_login_userId";
    String SP_LOGIN_TOKEN = "sp_login_token";
    /**
     * 用户信息保存
     */
    String SP_USER_INFO = "sp_user_info";
    String SP_USER_IMAGE = "sp_user_image";
    String SP_USER_NICKNAME = "sp_user_nickname";
    String PRINCIPLE_PROGRESS = "sp_principle_progress_";
    String SP_SHOW_REMOTE_PUBLISH = "sp_show_remote_publish";
    String SP_AUTO_UPGRADE = "sp_auto_upgrade";
    String PRINCIPLE_ENTER_PROGRESS = "sp_principle_enter_progress";
    String SP_SHOW_COMMON_GUIDE = "sp_show_common_guide";


    int KEY_NICK_NAME = 1;  //昵称
    int KEY_NICK_SEX = 2; //性别
    int KEY_NICK_AGE = 3; //年龄
    int KEY_NICK_GRADE = 4; //年级
    int KEY_NICK_HEAD = 5; //头像

    public static final String EMPTY_NICK_NAME = "empty_nick_name";


}
