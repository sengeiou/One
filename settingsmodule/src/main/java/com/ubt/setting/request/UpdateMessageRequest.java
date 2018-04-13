package com.ubt.setting.request;

import com.ubt.baselib.utils.http1E.BaseRequest;

/**
 * @author：liuhai
 * @date：2017/12/27 19:49
 * @modifier：ubt
 * @modify_date：2017/12/27 19:49
 * [A brief description]
 * version
 */

public class UpdateMessageRequest extends BaseRequest {
    private int messageId;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "UpdateMessageRequest{" +
                "messageId=" + messageId +
                '}';
    }
}