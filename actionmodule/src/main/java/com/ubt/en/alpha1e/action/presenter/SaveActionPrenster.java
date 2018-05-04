package com.ubt.en.alpha1e.action.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ubt.baselib.globalConst.BaseHttpEntity;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.BitmapUtil;
import com.ubt.baselib.utils.FileUtils;
import com.ubt.baselib.utils.TimeTools;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.contact.SaveActionContact;
import com.ubt.en.alpha1e.action.model.ActionTypeModel;
import com.ubt.en.alpha1e.action.model.SaveActionRequest;
import com.ubt.en.alpha1e.action.util.FileTools;
import com.ubt.en.alpha1e.action.util.Md5;
import com.ubt.htslib.HtsHelper;
import com.ubt.htslib.IHtsHelperListener;
import com.ubt.htslib.base.NewActionInfo;
import com.vise.log.ViseLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class SaveActionPrenster extends BasePresenterImpl<SaveActionContact.View> implements SaveActionContact.Presenter {
    private int[] imageIds = {R.drawable.img_actiontype_dance, R.drawable.img_actiontype_story, R.drawable.img_actiontype_action,
            R.drawable.img_actiontype_song, R.drawable.img_actiontype_science};
    private int[] desHintKeys = {R.string.ui_distribute_desc_dance,
            R.string.ui_distribute_desc_story,
            R.string.ui_distribute_desc_sport,
            R.string.ui_distribute_desc_song,
            R.string.ui_distribute_desc_education};
    final int[] imageNames = {
            R.string.ui_square_dance,
            R.string.ui_square_story,
            R.string.ui_square_sport,
            R.string.ui_square_childrensong,
            R.string.ui_square_science};

    final int[] type1 = {R.drawable.action_dance_1b,
            R.drawable.action_dance_2b,
            R.drawable.action_dance_3b};

    final int[] type2 = {R.drawable.action_story_1b,
            R.drawable.action_story_2b,
            R.drawable.action_story_3b};

    final int[] type3 = {R.drawable.action_sport_1b,
            R.drawable.action_sport_2b,
            R.drawable.action_sport_3b};

    final int[] type4 = {R.drawable.action_er_1b,
            R.drawable.action_er_2b,
            R.drawable.action_er_3b};

    final int[] type5 = {R.drawable.action_science_1b,
            R.drawable.action_science_2b,
            R.drawable.action_science_3b};

    final int[][] typelist = {type1, type2, type3, type4, type5};

    private List<ActionTypeModel> mTypeModelList = new ArrayList<>();


    private Context mContext;

    private boolean isSaveSuccess = false;

    private long originActionId = 0;

    public void init(Context context) {
        this.mContext = context;
        initActionTypeData();
    }


    public List<ActionTypeModel> getTypeModelList() {
        return mTypeModelList;
    }

    /**
     * 初始化动作类型数据
     *
     * @return
     */
    @Override
    public void initActionTypeData() {
        for (int i = 0; i < imageIds.length; i++) {
            ActionTypeModel model = new ActionTypeModel();
            model.setActionType(i + 1);
            model.setActionName(SkinManager.getInstance().getTextById(imageNames[i]));
            model.setActionDescrion(SkinManager.getInstance().getTextById(desHintKeys[i]));
            model.setDrawableId(imageIds[i]);
            model.setImageTypeArray(typelist[i]);
            if (i == 0) {
                model.setSelected(true);
            }
            mTypeModelList.add(model);
        }
    }

    /**
     * 设置选中数据
     *
     * @param position
     */
    @Override
    public void selectActionMode(int position) {
        for (int i = 0; i < mTypeModelList.size(); i++) {
            mTypeModelList.get(i).setSelected(false);
        }
        mTypeModelList.get(position).setSelected(true);
        if (mView != null) {
            mView.notifyDataSetChanged();
        }
    }


    /**
     * 检测名称正确性
     *
     * @param name
     * @param maxLenth
     * @param isSpc
     * @param formate
     * @return
     */
    public boolean isRightName(String name, int maxLenth, boolean isSpc,
                               String formate) {

        if (!isSpc && name.contains(" ")) {
            ToastUtils.showShort(SkinManager.getInstance().getTextById(R.string.ui_action_name_spaces));
            return false;
        }

        // "[0-9A-Za-z_]*"

        if (!formate.equals("") && !name.matches(formate)) {
            ToastUtils.showShort(SkinManager.getInstance().getTextById(R.string.ui_action_name_error));
            return false;
        }

        int lenth = 0;
        try {
            lenth = name.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            ToastUtils.showShort(SkinManager.getInstance().getTextById(R.string.ui_remote_synchoronize_unknown_error));
            return false;
        }

        if (maxLenth > 0 && lenth > maxLenth) {
            ToastUtils.showShort(SkinManager.getInstance().getTextById(R.string.ui_action_name_too_long));
            return false;
        }

        return true;
    }


    /**
     * 保存动作文件
     *
     * @param actionModel
     * @param actionInfo
     */
    @Override
    public void saveNewAction(ActionTypeModel actionModel, NewActionInfo actionInfo, String musicDir) {
        new SaveAsycTask(actionModel, actionInfo, musicDir).execute();
    }


    public class SaveAsycTask extends AsyncTask<Void, Integer, Boolean> {
        ActionTypeModel selectActionType;
        NewActionInfo actionInfo;
        String musicDir;

        public SaveAsycTask(ActionTypeModel selectActionType, NewActionInfo actionInfo, String musicDir) {
            this.selectActionType = selectActionType;
            this.actionInfo = actionInfo;
            this.musicDir = musicDir;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            boolean saveResult = false;

            String actionHeadUrl = getActionTypeImage(selectActionType.getLeftSelectedImage());
            ViseLog.d("actionHeadUrl=" + actionHeadUrl);
            actionInfo.actionHeadUrl = actionHeadUrl;
            if (originActionId != 0) {
                actionInfo.actionOriginalId = originActionId;
            } else {
                actionInfo.actionOriginalId = System.currentTimeMillis();
            }

            actionInfo.actionId = actionInfo.actionOriginalId;
            final String mDir = FileTools.actions_new_cache + File.separator + actionInfo.actionOriginalId + "";
            String mFileName = actionInfo.actionOriginalId + ".hts";
            String filePath = mDir + File.separator + mFileName;
            actionInfo.actionPath_local = filePath;
            actionInfo.actionDir_local = mDir;
            actionInfo.actionZip_local = FileTools.actions_new_cache + File.separator + actionInfo.actionOriginalId + ".zip";
            File path = new File(mDir);
            if (!path.exists()) {
                path.mkdirs();
            }
            ViseLog.d("mChangeNewActionInfo=" + actionInfo.toString());
            ViseLog.d("musicDir=" + musicDir);
            HtsHelper.writeHts(actionInfo, filePath, new IHtsHelperListener() {
                @Override
                public void onHtsWriteFinish(boolean isSuccess) {
                    if (isSuccess) {
                        if (musicDir != "" && musicDir != null) {
                            FileUtils.copyFile(musicDir, mDir + File.separator + actionInfo.actionOriginalId + ".mp3");
                        }
                        try {
                            FileUtils.doZip(actionInfo.actionDir_local, actionInfo.actionZip_local);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ViseLog.d("保存Zip包成功");
                        //saveActionToServer();
                        isSaveSuccess = true;
                    } else {
                        //保存失败
                        isSaveSuccess = false;
                    }
                }

                @Override
                public void onGetNewActionInfoFinish(boolean isSuccess) {
                    isSaveSuccess = false;
                }
            });

            return isSaveSuccess;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                saveActionToServer(actionInfo, selectActionType);
            } else {
                if (mView != null) {
                    mView.saveActionFailed();
                }
            }
        }


    }

    // -----------------------------------------------------------------------------------------
    public static final String APP_TYPE_KEY = "appType";
    public static final String APP_TYPE_VALUE = "1";
    public static final String SERVICE_VERSION_KEY = "serviceVersion";
    public static final String SERVICE_VERSION_VALUE = "V1.0.0.1";
    public static final String REQUEST_KEY = "requestKey";
    public static final String REQUEST_TIME_KEY = "requestTime";
    public static final String ENCRYPTION_KEY = "UBTech832%1293*6";
    public static final String TOKEN_KEY = "token";
    public static final String SYSTEM_CIG_LANGUAGE = "systemLanguage";

    public void saveActionToServer(NewActionInfo newActionInfo, ActionTypeModel typeModel) {

        boolean saveResult;

        final String BASIC_UBX_SYS = "http://10.10.1.14:8080/alpha1e/"; //测试环境
        /**
         * 保存动作
         */
        final String SAVE_ACTION = BASIC_UBX_SYS + "original/upload";

        String requestKey = Md5.getMD5(TimeTools.getTimeVal()
                + "UBTech832%1293*6", 32);

        File file = new File(FileTools.actions_new_cache + File.separator + newActionInfo.actionId + ".zip");
        File imageFile;
        if (!TextUtils.isEmpty(newActionInfo.actionHeadUrl)) {
            ViseLog.d("writeImage to server");
            imageFile = new File(newActionInfo.actionHeadUrl);
        } else {
            imageFile = new File(FileTools.actions_new_cache + File.separator + "Images/" + "default.jpg");

        }

        SaveActionRequest request = new SaveActionRequest();
        request.setActionoriginalid(newActionInfo.actionOriginalId + "");
        request.setActionuserid(BaseHttpEntity.getUserId());
        request.setActiondesciber(typeModel.getActionDescrion());
        request.setServiceversion("V1.0.0.1");
        request.setActionname("test");
        request.setSystemlanguage("CN");
        request.setActiontime(newActionInfo.actionTime + "");
        request.setActiontype("2");
        request.setRequestkey(requestKey);

        Map<String, String> params = getBasicParamsMap(mContext);

        params.put("actionOriginalId", newActionInfo.actionOriginalId + "");
        params.put("actionUserId", BaseHttpEntity.getUserId() + "");
        params.put("actionName", "test");
        params.put("actionDesciber", typeModel.getActionDescrion());
        params.put("actionType", newActionInfo.actionType + "");
        params.put("actionTime", newActionInfo.actionTime + "");
        // String url = HttpAddress.getRequestUrl(HttpAddress.Request_type.createaction_upload);
        // String url = HttpAddress.getRequestUrl(HttpEntity.SAVE_ACTION);
        originActionId = newActionInfo.actionOriginalId;
        ViseLog.d("maptojson", new Gson().toJson(params));
        OkHttpUtils.post()//
                .addFile("mFile1", file.getName(), file)//
                .addFile("mFile2", imageFile.getName(), imageFile)
                .url(SAVE_ACTION)//
                .params(params)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        ViseLog.d("onResponse:" + e.getMessage());
                        if (mView != null) {
                            mView.saveActionFailed();
                        }
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        ViseLog.d("onResponse:" + s);
                        try {
                            JSONObject json = new JSONObject(s);
                            if ((Boolean) json.get("status")) {
                                originActionId = 0;
                                if (mView != null) {
                                    mView.saveActionSuccess();
                                }
                            } else {
                                if (mView != null) {
                                    mView.saveActionFailed();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (mView != null) {
                                mView.saveActionFailed();
                            }
                        }

                    }
                });

    }

    public Map<String, String> getBasicParamsMap(Context context) {
        Map<String, String> basicMap = new HashMap<>();
        String[] req = getRequestInfo();
        String REQUEST_TIME_VALUE = req[0];
        String REQUEST_VALUE = req[1];
        basicMap.put(APP_TYPE_KEY, APP_TYPE_VALUE);
        basicMap.put(SERVICE_VERSION_KEY, SERVICE_VERSION_VALUE);
        basicMap.put(REQUEST_TIME_KEY, REQUEST_TIME_VALUE);
        //basicMap.put(SYSTEM_CIG_LANGUAGE, context.getResources().getConfiguration().locale.getCountry());
        basicMap.put(SYSTEM_CIG_LANGUAGE, "EN");
        basicMap.put(REQUEST_KEY, REQUEST_VALUE);
        return basicMap;


    }

    public static String[] getRequestInfo() {
        String[] req = new String[2];
        req[0] = TimeTools.getTimeVal();
        req[1] = Md5.getMD5(req[0]
                + ENCRYPTION_KEY, 32);
        return req;
    }

    /**
     * 将图片Id保存至本地SD卡
     *
     * @param drawableId
     */
    public String getActionTypeImage(int drawableId) {
        Bitmap bitmap = BitmapUtil.compressImage(mContext.getResources(), drawableId, 2);
        String actionHeadUrl = FileTools.actions_new_cache + File.separator + "Images/" + System.currentTimeMillis() + ".jpg";
        File f = new File(actionHeadUrl);
        File path = new File(f.getParent());
        if (!path.exists()) {
            path.mkdirs();
        }
        try {
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
            ViseLog.d("writeImage finish");
        } catch (Exception e) {
            e.printStackTrace();
            actionHeadUrl = "";
        }
        return actionHeadUrl;
    }


}
