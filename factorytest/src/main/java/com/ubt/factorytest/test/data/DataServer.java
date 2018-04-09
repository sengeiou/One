package com.ubt.factorytest.test.data;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ubt.factorytest.R;
import com.ubt.factorytest.test.recycleview.TestClickEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/11/17 10:40
 * @描述:
 */

public class DataServer {
    private static final String TAG = "DataServer";
    private Context mContext;
    List<TestClickEntity> dataCache = new ArrayList<>();

    public DataServer(Context context){
        mContext = context;
    }

    public List<TestClickEntity> getTestInitData(){
        dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "开机时间",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_START_TIME));
        dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "软件版本",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_SOFT_VERSION));
        dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "硬件版本",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_HARDWARE_VER));
        dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "当前电量",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_ELECTRICCHARGE));
        dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "灯效测试",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_LEDTEST));
        dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "蓝牙灵敏度",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_BTSENSITIVITY));
        dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "唤醒测试",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_WAKEUPTEST));
        dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "打断测试",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_INTERRUOTTEST));
        dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_SPEAKER, "喇叭测试",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_SPEAKERTEST));
        dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_MIC, "录音测试",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_RECORD_TEST));
        dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "红外测试",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_PIRTEST));
        dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "陀螺仪测试",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_GSENSIRTEST));
       /* dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "Wifi配网",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_WIFITEST));*/
        dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "测试报告保存到文件",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_SAVETESTPROFILE));
        /*dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "整机动作测试",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_ACTION_TEST));
        dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "整机老化测试",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_AGEING_TEST));*/
        dataCache.add(new TestClickEntity(TestClickEntity.CLICK_ITEM_VIEW, "恢复出厂设置",
                R.mipmap.click_untest,TestClickEntity.TEST_ITEM_ROBOTRESET));


        return dataCache;
    }

    public void setDataResult(int poisition, String result){
        if(dataCache.size() > poisition && !TextUtils.isEmpty(result)) {
            dataCache.get(poisition).setTestResult(result);
        }else{
            Log.e(TAG,"setDataResult  dataCache.size()="+dataCache.size()+"  poisition="+poisition);
        }
    }

    public void setDataisPass(int poisition, boolean isPass){
        if(dataCache.size() > poisition) {
            dataCache.get(poisition).setPass(isPass);
        }else{
            Log.e(TAG,"setDataisPass dataCache.size()="+dataCache.size()+"  poisition="+poisition);
        }
    }

    /**
     * 标记该测试项已经测试完成
     * @param poisition
     */
    public void setHasTested(int poisition){
        if(dataCache.size() > poisition) {
            dataCache.get(poisition).setTested(true);
        }else{
            Log.e(TAG,"setHasTested dataCache.size()="+dataCache.size()+"  poisition="+poisition);
        }
    }

    public List<TestClickEntity> getDataCache(){
        return dataCache;
    }

    public void clearDataCache(){
           dataCache.clear();
    }

    public boolean isTestComplete(){
        boolean isOK = true;

        for(TestClickEntity entity: dataCache){
            if(!entity.isTested()  && !(entity.getTestID() == TestClickEntity.TEST_ITEM_SAVETESTPROFILE)
                    &&!(entity.getTestID() == TestClickEntity.TEST_ITEM_ROBOTRESET)){
                isOK = false;
            }
        }

        return isOK;
    }
}
