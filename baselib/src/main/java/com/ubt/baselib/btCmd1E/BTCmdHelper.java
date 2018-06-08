package com.ubt.baselib.btCmd1E;

import android.bluetooth.BluetoothDevice;

import com.ubt.baselib.BlueTooth.BTReadData;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 15:09
 * @描述:
 */

public class BTCmdHelper {
    /**
     * 解析接收到的蓝牙命令
     * @param cmd
     * @factoryListener 数据包解析回调
     */
    public synchronized static void  parseBTCmd(final byte[] cmd, IProtolPackListener ppListener){
        formatBTData(cmd, ppListener);
    }

    private static void formatBTData(byte[] data, IProtolPackListener ppListener){
        ProtocolPacket pack = new ProtocolPacket();
        for (int i = 0; i < data.length; i++) {
            if (pack.setData_(data[i])) {
                // 一帧数据接收完成
                pack.setmParamLen(pack.getmParam().length);
                if(ppListener != null){ //package返回本地接口
                    ppListener.onProtocolPacket(pack);
                    pack = new ProtocolPacket();
                }
            }
        }
    }

    /**
     *
     * @param cmd
     * @param bluetoothDevice
     * @param dataListener
     * @return 直接解析出BTReadData
     */
    public synchronized static  void parseBTCmd(final byte[] cmd, BluetoothDevice bluetoothDevice,IBTReadDataListener dataListener){

        formatBTData(cmd, bluetoothDevice, dataListener);
    }

    private static void formatBTData(byte[] data, BluetoothDevice bluetoothDevice, IBTReadDataListener dataListener){
        ProtocolPacket pack = new ProtocolPacket();
        for (int i = 0; i < data.length; i++) {
            if (pack.setData_(data[i])) {
                // 一帧数据接收完成
                pack.setmParamLen(pack.getmParam().length);
                if(dataListener != null){ //package返回本地接口
                    BTReadData readData = new BTReadData();
                    readData.setBluetoothDevice(bluetoothDevice);
                    readData.setDatas(pack.getRawData());
                    readData.setPack(pack);
                    dataListener.onData(readData);
                    pack = new ProtocolPacket();
                }
            }
        }
    }
}
