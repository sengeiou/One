package com.ubt.en.alpha1e.ble.model;

import com.baoyz.pg.Parcelable;
import com.ubt.baselib.utils.GsonImpl;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

@Parcelable
public class BleBaseModelInfo extends BleBaseModel {

    public BleBaseModelInfo thiz;

    @Override
    public BleBaseModelInfo getThiz(String json) {

        try {
            thiz = mMapper.readValue(json, BleBaseModelInfo.class);
            return thiz;
        } catch (Exception e) {
            thiz = null;
            return null;
        }
    }

    public static ArrayList<BleBaseModelInfo> getModelList(String json) {
        ArrayList<BleBaseModelInfo> result = new ArrayList<BleBaseModelInfo>();
        try {
            JSONArray j_list = new JSONArray(json);
            for (int i = 0; i < j_list.length(); i++) {
                result.add(new BleBaseModelInfo().getThiz(j_list.get(i).toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getModeslStr(ArrayList<BleBaseModelInfo> infos) {

        try {
            return mMapper.writeValueAsString(infos);
        } catch (Exception e) {
            String error = e.getMessage();
            return Convert_fail;
        }
    }

    public static String getString(BleBaseModelInfo info)
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
        return "BleBaseModelInfo{" +
                "seq=" + seq +
                ", event=" + event +
                '}';
    }
}
