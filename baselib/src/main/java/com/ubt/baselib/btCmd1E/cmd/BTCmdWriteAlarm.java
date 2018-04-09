package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

import java.io.UnsupportedEncodingException;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 17:11
 * @描述: 设置闹铃时间
 */

public class BTCmdWriteAlarm extends BaseBTReq {
    byte cmd = BTCmd.DV_WRITE_ALARM;
    byte[] parm = {0x0};

    public BTCmdWriteAlarm(AlarmInfo info) {
        if (info != null) {
            parm = packWriteAlarmInfo(info);
        }
        initReq(cmd, parm);
    }

    private byte[] packWriteAlarmInfo(AlarmInfo info) {
        byte usable = 0;
        byte repeat = 0;
        if (info.isOpen) {
            usable = 1;
        }
        if (info.isRepaet) {
            repeat = 1;
        }
        byte hh = info.hh;
        byte mm = info.mm;
        byte ss = info.ss;

        try {
            byte[] name = info.actionName.getBytes("GBK");
            byte len = (byte) name.length;
            byte[] param = new byte[6 + len];

            param[0] = usable;
            param[1] = repeat;
            param[2] = hh;
            param[3] = mm;
            param[4] = ss;
            param[5] = len;
            System.arraycopy(name, 0, param, 6, len);
            return param;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    class AlarmInfo {
        public boolean isOpen;// 是否开启闹钟
        public boolean isRepaet;// 是否重复
        public byte hh;//
        public byte mm;
        public byte ss;
        public String actionName;// 动作名

        public boolean isOpen() {
            return isOpen;
        }

        public void setOpen(boolean isOpen) {
            this.isOpen = isOpen;
        }

        public boolean isRepaet() {
            return isRepaet;
        }

        public void setRepaet(boolean isRepaet) {
            this.isRepaet = isRepaet;
        }

        public byte getHh() {
            return hh;
        }

        public void setHh(byte hh) {
            this.hh = hh;
        }

        public byte getMm() {
            return mm;
        }

        public void setMm(byte mm) {
            this.mm = mm;
        }

        public byte getSs() {
            return ss;
        }

        public void setSs(byte ss) {
            this.ss = ss;
        }

        public String getActionName() {
            return actionName;
        }

        public void setActionName(String actionName) {
            this.actionName = actionName;
        }
    }
}
