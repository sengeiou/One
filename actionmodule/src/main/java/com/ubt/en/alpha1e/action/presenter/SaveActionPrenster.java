package com.ubt.en.alpha1e.action.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.BitmapUtil;
import com.ubt.baselib.utils.FileUtils;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.contact.SaveActionContact;
import com.ubt.en.alpha1e.action.model.ActionTypeModel;
import com.ubt.en.alpha1e.action.util.FileTools;
import com.ubt.htslib.HtsHelper;
import com.ubt.htslib.IHtsHelperListener;
import com.ubt.htslib.base.NewActionInfo;
import com.vise.log.ViseLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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


    public class SaveAsycTask extends AsyncTask<Void, Integer, Void> {
        ActionTypeModel selectActionType;
        NewActionInfo actionInfo;
        String musicDir;

        public SaveAsycTask(ActionTypeModel selectActionType, NewActionInfo actionInfo, String musicDir) {
            this.selectActionType = selectActionType;
            this.actionInfo = actionInfo;
            this.musicDir = musicDir;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String actionHeadUrl = getActionTypeImage(selectActionType.getLeftSelectedImage());
            ViseLog.d("actionHeadUrl=" + actionHeadUrl);
            actionInfo.actionHeadUrl = actionHeadUrl;
            actionInfo.actionOriginalId = System.currentTimeMillis();
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
                    } else {
                        //保存失败
                        //isSaveSuccess = false;
                    }
                }

                @Override
                public void onGetNewActionInfoFinish(boolean isSuccess) {

                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

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
