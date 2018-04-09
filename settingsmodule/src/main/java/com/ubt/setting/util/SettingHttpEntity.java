package com.ubt.setting.util;

import com.ubt.baselib.globalConst.BaseHttpEntity;

/**
 * @author：liuhai
 * @date：2018/1/25 14:38
 * @modifier：ubt
 * @modify_date：2018/1/25 14:38
 * [A brief description]
 * version
 */

public class SettingHttpEntity extends BaseHttpEntity{


    public static final String REQUEST_SMS_CODE = BASIC_UBX_SYS + "user/register";
    public static final String GET_USER_INFO = BASIC_UBX_SYS + "user/get";
    public static final String BIND_ACCOUNT = BASIC_UBX_SYS + "user/bind";
    public static final String UPDATE_USERINFO = BASIC_UBX_SYS + "user/update";
    public static final String MODIFY_MANAGE_PASSWORD = BASIC_UBX_SYS + "user/updateUserPwd";
    public static final String SET_USER_PASSWORD = BASIC_UBX_SYS + "user/addUserPwd";
    public static final String VERIDATA_CODE = BASIC_UBX_SYS + "user/validateCode";
    public static final String ADD_FEEDBACK = BASIC_UBX_SYS + "user/addFeedback";
    public static final String CHECK_APP_UPDATE = BASIC_UBX_SYS + "sys/appUpgrade";


    /**
     * 获取消息列表
     */
    public static final String MESSAGE_GET_LIST = BASIC_UBX_SYS + "message/listByPage";
    /**
     * 获取未读消息数量
     */
    public static final String MESSAGE_UNREAD_TOTAL = BASIC_UBX_SYS + "message/countUnread";

    /**
     * 获取消息列表
     */
    public static final String MESSAGE_UPDATE_STATU = BASIC_UBX_SYS + "message/update";

    /**
     * 删除消息
     */
    public static final String MESSAGE_DELETE = BASIC_UBX_SYS + "message/deleteByMessageId";
    /**
     * 获取原创列表
     */
    public static final String ACTION_DYNAMIC_LIST = BASIC_UBX_SYS + "original/listByPage";
    /**
     * 删除动作ByID
     */
    public static final String ACTION_DYNAMIC_DELETE = BASIC_UBX_SYS + "original/deleteByActionId";
    /**
     * 保存动作
     */
    public static final String SAVE_ACTION = BASIC_UBX_SYS + "original/upload";
}
