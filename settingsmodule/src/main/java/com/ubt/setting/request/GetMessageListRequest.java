package com.ubt.setting.request;

import com.ubt.baselib.utils.http1E.BaseRequest;

/**
 * @author：liuhai
 * @date：2017/12/27 19:15
 * @modifier：ubt
 * @modify_date：2017/12/27 19:15
 * [A brief description]
 * version
 */

public class GetMessageListRequest extends BaseRequest {
    private int limit;
    private int offset;

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

    @Override
    public String toString() {
        return "GetMessageListRequest{" +
                "limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}
