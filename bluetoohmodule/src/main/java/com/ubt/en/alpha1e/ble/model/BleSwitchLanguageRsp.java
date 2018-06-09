package com.ubt.en.alpha1e.ble.model;

import com.baoyz.pg.Parcelable;
import com.ubt.baselib.utils.GsonImpl;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

@Parcelable
public class BleSwitchLanguageRsp extends BleBaseModel {

    public int result = 0;  //0 ( 成功) 1 (失败) 2 (被意外终止，或者被取消)

    public int progess = 0;

    public String name = "";

    public String language;

    public BleSwitchLanguageRsp thiz;

    @Override
    public BleSwitchLanguageRsp getThiz(String json) {

        try {
            thiz = mMapper.readValue(json, BleSwitchLanguageRsp.class);
            return thiz;
        } catch (Exception e) {
            thiz = null;
            return null;
        }
    }

    public static ArrayList<BleSwitchLanguageRsp> getModelList(String json) {
        ArrayList<BleSwitchLanguageRsp> result = new ArrayList<BleSwitchLanguageRsp>();
        try {
            JSONArray j_list = new JSONArray(json);
            for (int i = 0; i < j_list.length(); i++) {
                result.add(new BleSwitchLanguageRsp().getThiz(j_list.get(i).toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getModeslStr(ArrayList<BleSwitchLanguageRsp> infos) {

        try {
            return mMapper.writeValueAsString(infos);
        } catch (Exception e) {
            String error = e.getMessage();
            return Convert_fail;
        }
    }

    public static String getString(BleSwitchLanguageRsp info)
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
        return "BleSwitchLanguageRsp{" +
                "result=" + result +
                ",progess=" + progess +
                ",name=" + name +
                ",language=" + language +
                '}';
    }
}
