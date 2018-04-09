package com.ubt.factorytest.test.data.btcmd;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.util.Log;

import com.ubt.factorytest.R;
import com.ubt.factorytest.bluetooth.ubtbtprotocol.ProtocolPacket;
import com.ubt.factorytest.test.data.DataServer;
import com.ubt.factorytest.test.data.IFactoryListener;
import com.ubt.factorytest.test.recycleview.TestClickEntity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;


/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/12/25 10:19
 * @描述:
 */
public class FactoryToolTest {
    FactoryTool factoryTool;
    private IdlingResource idlingresource;


    @Before
    public void init(){
        if(factoryTool == null){
            factoryTool = FactoryTool.getInstance();
            factoryTool.init();
        }
        idlingresource = factoryTool.getIdlingresource();
        Espresso.registerIdlingResources(idlingresource);
    }
    @Test
    public void parseBTCmd000() throws Exception {
        byte[] data = {(byte) 0xfb, (byte)0xbf, (byte)0x06, 0x08, 0x00, (byte)0x0e, (byte)0xed };
        factoryTool.parseBTCmd(data, new IFactoryListener() {
            @Override
            public void onProtocolPacket(ProtocolPacket packet) {
                Log.i("FactoryToolTest", "parseBTCmd000 cmd = "+packet.getmCmd());
            }
        });
    }
    @Test
    public void parseBTCmd001() throws Exception {
        byte[] data = {(byte) 0xfb, (byte)0xbf, (byte)0x80, 0x52, 0x7b, 0x22, 0x66, 0x72, 0x6f, 0x6e, 0x74, 0x22, 0x3a,
                0x22, 0x2d, 0x31, 0x34, 0x22, 0x2c, 0x22, 0x62, 0x61, 0x63, 0x6b, 0x22, 0x3a, 0x22,
                0x31, 0x34, 0x22, 0x2c, 0x22, 0x6c, 0x65, 0x66, 0x74, 0x22, 0x3a, 0x22, 0x31, 0x33,
                0x22, 0x2c, 0x22, 0x72, 0x69, 0x67, 0x68, 0x74, 0x22, 0x3a, 0x22, 0x2d, 0x31, 0x33,
                0x22, 0x2c, 0x22, 0x66, 0x72, 0x6f, 0x6e, 0x74, 0x41, 0x63, 0x63, 0x65, 0x6c, 0x22,
                0x3a, 0x22, 0x2d, 0x30, 0x2e, 0x32, 0x34, 0x35, 0x39, 0x37, 0x39, 0x22, 0x2c, 0x22,
                0x6c, 0x65, 0x66, 0x74, 0x41, 0x63, 0x63, 0x65, 0x6c, 0x22, 0x3a, 0x22, 0x2d, 0x30,
                0x2e, 0x32, 0x33, 0x31, 0x35, 0x31, 0x33, 0x22, 0x2c, 0x22, 0x75, 0x70, 0x01, 0x63,
                0x63, 0x65, 0x6c, 0x22, 0x3a, 0x22, 0x30, 0x2e, 0x37, 0x39, 0x38, 0x30, 0x35, 0x39,
                0x22, 0x7d, (byte)0xfd, (byte)0xed };
        factoryTool.parseBTCmd(data, null);
    }

    @Test
    public void getReqBytes(){
        TestClickEntity item;
        for(int i = TestClickEntity.TEST_ITEM_START_TIME; i <= TestClickEntity.TEST_ITEM_AGEING_TEST; i++){
            item = new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "开机时间",
                    R.mipmap.click_untest, i);
            factoryTool.getReqBytes(item);
        }


    }

    @Test
    public void getItemPositon() throws Exception {
        final DataServer dataServer = new DataServer(InstrumentationRegistry.getTargetContext());
        dataServer.getTestInitData();
        IFactoryListener listener = new IFactoryListener(){

            @Override
            public void onProtocolPacket(ProtocolPacket packet) {
                Log.i("FactoryToolTest", "parseBTCmd000 cmd = "+packet.getmCmd());
                factoryTool.getItemPositon(dataServer,packet);
            }
        };

        byte[] data = {(byte) 0xfb, (byte)0xbf, 0x06, BaseBTReq.HEART_CMD, 0x00, (byte)0x0e, (byte)0xed };
        FactoryTool.getInstance().parseBTCmd(data,listener);

        data = new byte[]{(byte) 0xfb, (byte)0xbf, 0x08, BaseBTReq.START_TIME, 0x31, 0x32, 0x00, 0x4e, (byte)0xed };
        factoryTool.parseBTCmd(data, listener);

        data = new byte[]{(byte) 0xfb, (byte)0xbf, 0x15, BaseBTReq.SOFT_VERSION, 0x61,0x6c,0x70,
                0x68, 0x61, 0x31, 0x65, 0x5f, 0x76, 0x31, 0x2e, 0x30, 0x2e, 0x39, 0x2e, 0x39, (byte)0xf4, (byte)0xed };
        factoryTool.parseBTCmd(data, listener);

        data = new byte[]{(byte) 0xfb, (byte)0xbf, 0x12, BaseBTReq.HARDWARE_VER, 0x61, 0x6c, 0x70,
                0x68, 0x61, 0x31, 0x65, 0x5f, 0x52, 0x65, 0x76, 0x41, 0x0a, (byte)0xa5, (byte)0xed };
        factoryTool.parseBTCmd(data, listener);

        data = new byte[]{(byte) 0xfb, (byte)0xbf, 0x09, BaseBTReq.ELECTRICCHARGE, 0x20, (byte) 0x98, 0x00, 0x64, 0x3d, (byte)0xed };
        factoryTool.parseBTCmd(data, listener);

        data = new byte[]{(byte) 0xfb, (byte)0xbf, 0x06, BaseBTReq.TEST_LED, 0x00, (byte)0xea, (byte)0xed };
        factoryTool.parseBTCmd(data, listener);

        data = new byte[]{(byte) 0xfb, (byte)0xbf, 0x06, BaseBTReq.RESET_ROBOT, 0x00, (byte)0xeb, (byte)0xed };
        factoryTool.parseBTCmd(data, listener);

        data = new byte[]{(byte) 0xfb, (byte)0xbf, 0x06, BaseBTReq.PIR_TEST, 0x1e, (byte)0x74, (byte)0xed };
        factoryTool.parseBTCmd(data, listener);

        data = new byte[]{(byte) 0xfb, (byte)0xbf, 0x06, BaseBTReq.GSENSIR_TEST, 0x1e, (byte)0x76, (byte)0xed };
        factoryTool.parseBTCmd(data, listener);

        data = new byte[]{(byte) 0xfb, (byte)0xbf, 0x06, BaseBTReq.PRESSHEAD_UP, 0x00, (byte)0x76, (byte)0xed };
        factoryTool.parseBTCmd(data, listener);
        data = new byte[]{(byte) 0xfb, (byte)0xbf, 0x06, BaseBTReq.PRESSHEAD_UP, 0x00, (byte)0x76, (byte)0xed };
        factoryTool.parseBTCmd(data, listener);
        data = new byte[]{(byte) 0xfb, (byte)0xbf, 0x06, BaseBTReq.PRESSHEAD_UP, 0x00, (byte)0x76, (byte)0xed };
        factoryTool.parseBTCmd(data, listener);
        data = new byte[]{(byte) 0xfb, (byte)0xbf, 0x06, BaseBTReq.PRESSHEAD_UP, 0x00, (byte)0x76, (byte)0xed };
        factoryTool.parseBTCmd(data, listener);

        data = new byte[]{(byte) 0xfb, (byte)0xbf, 0x06, BaseBTReq.WAKEUP_UP, 0x00, (byte)0x7e, (byte)0xed };
        factoryTool.parseBTCmd(data, listener);

        data = new byte[]{(byte) 0xfb, (byte)0xbf, 0x06, BaseBTReq.WAKEUP_UP, 0x00, (byte)0x7e, (byte)0xed };
        factoryTool.parseBTCmd(data, null);


    }

    @Test
    public void currentVol() throws Exception {
        factoryTool.setCurrentVol(1);
        Assert.assertThat(factoryTool.getCurrentVol(), equalTo(1));
    }

    @Test
    public void testCmd(){
        byte[] cmd = new HeartBeat().toByteArray();
        byte[] data = {(byte) 0xfb, (byte)0xbf, 0x06, BaseBTReq.HEART_CMD, 0x00, (byte)0x0e, (byte)0xed };
        Assert.assertArrayEquals(data, cmd);
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(idlingresource);
    }
}