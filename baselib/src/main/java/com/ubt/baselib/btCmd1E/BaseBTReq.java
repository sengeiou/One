package com.ubt.baselib.btCmd1E;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/11/21 18:03
 * @描述:
 */

public class BaseBTReq {

    private static int seq = 1;//随机数

    private UbtBTProtocol btcmd;

    protected void initReq(byte cmd, byte[] param){
        btcmd = new UbtBTProtocol(cmd, param);
    }

    public byte[] toByteArray(){
        return btcmd.toRawBytes();
    }

    public String toString(){
        return btcmd.toString();
    }

    public static int getSeq(){
        return seq++;
    }
}
