package com.ubt.en.alpha1e.ble.model;

import com.baoyz.pg.Parcelable;
import com.ubt.baselib.utils.GsonImpl;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

@Parcelable
public class BleRobotLanguageInfo extends BleBaseModel {

    public String lang = "";  //语言名
    public String version = ""; //芯片的语言包的版本号
    public String firmware_ver = ""; //芯片的固件的版本号

    public BleRobotLanguageInfo thiz;

    @Override
    public BleRobotLanguageInfo getThiz(String json) {

        try {
            thiz = mMapper.readValue(json, BleRobotLanguageInfo.class);
            return thiz;
        } catch (Exception e) {
            thiz = null;
            return null;
        }
    }

    public static ArrayList<BleRobotLanguageInfo> getModelList(String json) {
        ArrayList<BleRobotLanguageInfo> result = new ArrayList<BleRobotLanguageInfo>();
        try {
            JSONArray j_list = new JSONArray(json);
            for (int i = 0; i < j_list.length(); i++) {
                result.add(new BleRobotLanguageInfo().getThiz(j_list.get(i).toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getModeslStr(ArrayList<BleRobotLanguageInfo> infos) {

        try {
            return mMapper.writeValueAsString(infos);
        } catch (Exception e) {
            String error = e.getMessage();
            return Convert_fail;
        }
    }

    public static String getString(BleRobotLanguageInfo info)
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
        return "BleRobotLanguageInfo{" +
                "lang=" + lang +
                ", version=" + version +
                ", firmware_ver=" + firmware_ver +
                ", seq=" + seq +
                ", event=" + event +
                '}';
    }
}
