package com.ubt.en.alpha1e.action.presenter;

import android.content.Context;

import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.contact.SaveActionContact;
import com.ubt.en.alpha1e.action.model.ActionTypeModel;

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
}
