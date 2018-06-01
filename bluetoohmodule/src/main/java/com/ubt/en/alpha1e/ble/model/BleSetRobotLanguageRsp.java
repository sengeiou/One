package com.ubt.en.alpha1e.ble.model;

import com.baoyz.pg.Parcelable;
import com.ubt.baselib.utils.GsonImpl;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

@Parcelable
public class BleSetRobotLanguageRsp extends BleBaseModel {

    public int rescode = 0;  //0 ( 成功)，１：不支持的语言　 2：电量低不能切换

    public BleSetRobotLanguageRsp thiz;

    @Override
    public BleSetRobotLanguageRsp getThiz(String json) {

        try {
            thiz = mMapper.readValue(json, BleSetRobotLanguageRsp.class);
            return thiz;
        } catch (Exception e) {
            thiz = null;
            return null;
        }
    }

    public static ArrayList<BleSetRobotLanguageRsp> getModelList(String json) {
        ArrayList<BleSetRobotLanguageRsp> result = new ArrayList<BleSetRobotLanguageRsp>();
        try {
            JSONArray j_list = new JSONArray(json);
            for (int i = 0; i < j_list.length(); i++) {
                result.add(new BleSetRobotLanguageRsp().getThiz(j_list.get(i).toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getModeslStr(ArrayList<BleSetRobotLanguageRsp> infos) {

        try {
            return mMapper.writeValueAsString(infos);
        } catch (Exception e) {
            String error = e.getMessage();
            return Convert_fail;
        }
    }

    public static String getString(BleSetRobotLanguageRsp info)
    {
        try {
            return  GsonImpl.get().toJson(info);

        }catch (Exception e)
        {
            return  "";
        }
    }

    @Override
    public String toString() {
        return "BleRobotLanguageList{" +
                "rescode=" + rescode +
                '}';
    }
}
