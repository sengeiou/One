package com.ubt.en.alpha1e.ble.model;

import com.baoyz.pg.Parcelable;
import com.ubt.baselib.utils.GsonImpl;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

@Parcelable
public class BleDownloadLanguageRsp extends BleBaseModel {

    public int result = 0;  //0 ( 成功) 1 (失败) 2 (被意外终止，或者被取消)

    public int progress = 0;

    public String name = "";

    public String language = "";

    public BleDownloadLanguageRsp thiz;

    @Override
    public BleDownloadLanguageRsp getThiz(String json) {

        try {
            thiz = mMapper.readValue(json, BleDownloadLanguageRsp.class);
            return thiz;
        } catch (Exception e) {
            thiz = null;
            return null;
        }
    }

    public static ArrayList<BleDownloadLanguageRsp> getModelList(String json) {
        ArrayList<BleDownloadLanguageRsp> result = new ArrayList<BleDownloadLanguageRsp>();
        try {
            JSONArray j_list = new JSONArray(json);
            for (int i = 0; i < j_list.length(); i++) {
                result.add(new BleDownloadLanguageRsp().getThiz(j_list.get(i).toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getModeslStr(ArrayList<BleDownloadLanguageRsp> infos) {

        try {
            return mMapper.writeValueAsString(infos);
        } catch (Exception e) {
            String error = e.getMessage();
            return Convert_fail;
        }
    }

    public static String getString(BleDownloadLanguageRsp info)
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
        return "BleDownloadLanguageRsp{" +
                "result=" + result +
                ",progress=" + progress +
                ",name=" + name +
                ",langauage=" + language +
                '}';
    }
}
