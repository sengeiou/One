package com.ubt.en.alpha1e.action.model.request;

/**
 * @author：liuhai
 * @date：2018/5/15 14:25
 * @modifier：ubt
 * @modify_date：2018/5/15 14:25
 * [A brief description]
 * version
 */

public class DeleteActionRequest {
    private int actionId;
    private String userId;
    private String token;

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
