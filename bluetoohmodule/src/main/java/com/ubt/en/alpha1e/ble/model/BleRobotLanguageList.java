package com.ubt.en.alpha1e.ble.model;

import com.baoyz.pg.Parcelable;
import com.ubt.baselib.utils.GsonImpl;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

@Parcelable
public class BleRobotLanguageList extends BleBaseModel {

    public String page = "";  //分页
    public String[] language = null; //支持的语言列表的数组

    public BleRobotLanguageList thiz;

    @Override
    public BleRobotLanguageList getThiz(String json) {

        try {
            thiz = mMapper.readValue(json, BleRobotLanguageList.class);
            return thiz;
        } catch (Exception e) {
            thiz = null;
            return null;
        }
    }

    public static ArrayList<BleRobotLanguageList> getModelList(String json) {
        ArrayList<BleRobotLanguageList> result = new ArrayList<BleRobotLanguageList>();
        try {
            JSONArray j_list = new JSONArray(json);
            for (int i = 0; i < j_list.length(); i++) {
                result.add(new BleRobotLanguageList().getThiz(j_list.get(i).toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getModeslStr(ArrayList<BleRobotLanguageList> infos) {

        try {
            return mMapper.writeValueAsString(infos);
        } catch (Exception e) {
            String error = e.getMessage();
            return Convert_fail;
        }
    }

    public static String getString(BleRobotLanguageList info)
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
                "page=" + page +
                ", language=" + language +
                '}';
    }
}
