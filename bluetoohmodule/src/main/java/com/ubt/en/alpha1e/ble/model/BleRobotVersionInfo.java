package com.ubt.en.alpha1e.ble.model;

import com.baoyz.pg.Parcelable;
import com.ubt.baselib.utils.GsonImpl;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

@Parcelable
public class BleRobotVersionInfo extends BleBaseModel {

    public String lang = "";  //语言名
    public String langlong = "";  //语言名
    public String version = ""; //芯片的指令集的版本号
    public String firmware_ver = ""; //芯片的固件的版本号
    public String new_version = ""; //最新的语言包的版本号
    public String new_firmware_ver = ""; //最新的固件版本号
    public String resource_ver="";//芯片的语言包的版本号
    public BleRobotVersionInfo thiz;

    @Override
    public BleRobotVersionInfo getThiz(String json) {

        try {
            thiz = mMapper.readValue(json, BleRobotVersionInfo.class);
            return thiz;
        } catch (Exception e) {
            thiz = null;
            return null;
        }
    }

    public static ArrayList<BleRobotVersionInfo> getModelList(String json) {
        ArrayList<BleRobotVersionInfo> result = new ArrayList<BleRobotVersionInfo>();
        try {
            JSONArray j_list = new JSONArray(json);
            for (int i = 0; i < j_list.length(); i++) {
                result.add(new BleRobotVersionInfo().getThiz(j_list.get(i).toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getModeslStr(ArrayList<BleRobotVersionInfo> infos) {

        try {
            return mMapper.writeValueAsString(infos);
        } catch (Exception e) {
            String error = e.getMessage();
            return Convert_fail;
        }
    }

    public static String getString(BleRobotVersionInfo info)
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
        return "BleRobotVersionInfo{" +
                "lang=" + lang +
                ", langlong=" + langlong +
                ", version=" + version +
                ", firmware_ver=" + firmware_ver +
                ", seq=" + seq +
                ", event=" + event +
                '}';
    }
}
