package com.ubt.en.alpha1e.action.model.request;

/**
 * @author：liuhai
 * @date：2018/5/15 14:25
 * @modifier：ubt
 * @modify_date：2018/5/15 14:25
 * [A brief description]
 * version
 */

public class GetMessageListRequest {
    private int limit;
    private int offset;
    private String userId;
    private String token;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
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
