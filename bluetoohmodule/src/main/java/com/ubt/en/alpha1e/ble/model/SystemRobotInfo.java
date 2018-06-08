package com.ubt.en.alpha1e.ble.model;

import com.baoyz.pg.Parcelable;
import com.ubt.baselib.utils.GsonImpl;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

@Parcelable
public class SystemRobotInfo extends BaseModel {

    public int status = 0;        //1 下载中 2 下载成功 0 下载失败
    public String curVersion = "";  //当前版本
    public String toVersion = ""; //最新版本

    public SystemRobotInfo thiz;

    @Override
    public SystemRobotInfo getThiz(String json) {

        try {
            thiz = mMapper.readValue(json, SystemRobotInfo.class);
            return thiz;
        } catch (Exception e) {
            thiz = null;
            return null;
        }
    }

    public static ArrayList<SystemRobotInfo> getModelList(String json) {
        ArrayList<SystemRobotInfo> result = new ArrayList<SystemRobotInfo>();
        try {
            JSONArray j_list = new JSONArray(json);
            for (int i = 0; i < j_list.length(); i++) {
                result.add(new SystemRobotInfo().getThiz(j_list.get(i).toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getModeslStr(ArrayList<SystemRobotInfo> infos) {

        try {
            return mMapper.writeValueAsString(infos);
        } catch (Exception e) {
            String error = e.getMessage();
            return Convert_fail;
        }
    }

    public static String getString(SystemRobotInfo info) {
        try {
            return GsonImpl.get().toJson(info);

        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String toString() {
        return "SystemRobotInfo{" +
                "status=" + status +
                ", curVersion='" + curVersion + '\'' +
                ", toVersion='" + toVersion + '\'' +
                ", thiz=" + thiz +
                '}';
    }
}
