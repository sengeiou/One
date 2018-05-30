package com.ubt.en.alpha1e.ble.presenter;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.model1E.BaseResponseModel;
import com.ubt.baselib.model1E.UserInfoModel;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.en.alpha1e.ble.BleHttpEntity;
import com.ubt.en.alpha1e.ble.Contact.RobotLanguageContact;
import com.ubt.en.alpha1e.ble.model.BaseModel;
import com.ubt.en.alpha1e.ble.model.RobotLanguage;
import com.ubt.en.alpha1e.ble.requestModel.GetRobotLanguageRequest;
import com.vise.log.ViseLog;
import com.vise.utils.assist.JSONUtil;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class RobotLanguagePresenter extends BasePresenterImpl<RobotLanguageContact.View> implements RobotLanguageContact.Presenter {


    @Override
    public void getRobotLanguageList() {
        GetRobotLanguageRequest robotLanguageRequest = new GetRobotLanguageRequest();
        robotLanguageRequest.setToken(SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN));
        robotLanguageRequest.setUserId(SPUtils.getInstance().getInt(Constant1E.SP_USER_ID));
        robotLanguageRequest.setEquipmentSeq("");

        ViseHttp.POST(BleHttpEntity.GET_ROBOT_LANGUAGE)
                .setJson(GsonImpl.get().toJson(robotLanguageRequest))
                .connectTimeOut(5)
                .request(new ACallback<String>() {

                    @Override
                    public void onSuccess(String data) {
                        ViseLog.d("USER_GET_INFO onSuccess:" + data);
                        if(mView == null){
                            return;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            boolean status = jsonObject.getBoolean("status");
                            if(status){
                                JSONObject modelsObject = jsonObject.getJSONObject("models");
                                ViseLog.d("modelsObject = " + modelsObject);

                                /*JSONArray modelsArray = jsonObject.getJSONArray("models");
                                ViseLog.d("modelsArray = " + modelsArray);*/

                                String modelsString = jsonObject.getString("models");
                                ViseLog.d("modelsString = " + modelsString);

                                List<RobotLanguage> robotLanguages = dealRobotLanguageData(modelsString);
                                mView.setRobotLanguageList(true, robotLanguages);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            mView.setRobotLanguageList(false, null);
                        } catch (Exception e){
                            e.printStackTrace();
                            mView.setRobotLanguageList(false, null);
                        }

                    }

                    @Override
                    public void onFail(int code, String errmsg) {
                        ViseLog.d("USER_GET_INFO onFail:" + code + "  errmsg:" + errmsg);
                        if(mView != null){
                            mView.setRobotLanguageList(false, null);
                        }
                    }
                });
    }

    private List<RobotLanguage> dealRobotLanguageData(String dataString){
        List<RobotLanguage> languages = new ArrayList<>();

        if(!TextUtils.isEmpty(dataString)){
            dataString = dataString.replace("{","").replace("}","").replace("\"","");
            ViseLog.d("dataString = " + dataString);
            if(!TextUtils.isEmpty(dataString)){
                String[] value = dataString.split(",");
                for(String val : value){
                    RobotLanguage robotLanguage = new RobotLanguage();
                    robotLanguage.setLanguageName(val.split(":")[0]);
                    robotLanguage.setLanguageSingleName(val.split(":")[1]);
                    robotLanguage.setSelect(false);
                    languages.add(robotLanguage);
                }
            }
        }
        return languages;
    }
}
