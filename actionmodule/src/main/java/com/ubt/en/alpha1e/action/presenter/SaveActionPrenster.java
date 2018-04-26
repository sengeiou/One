package com.ubt.en.alpha1e.action.presenter;

import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.contact.SaveActionContact;
import com.ubt.en.alpha1e.action.model.ActionTypeModel;

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
    private List<ActionTypeModel> mTypeModelList = new ArrayList<>();


    public List<ActionTypeModel> getTypeModelList() {
        return mTypeModelList;
    }

    @Override
    public List<ActionTypeModel> getActionTypeData() {
        mTypeModelList.clear();
        for (int i = 0; i < imageIds.length; i++) {
            ActionTypeModel model = new ActionTypeModel();
            model.setActionName(SkinManager.getInstance().getTextById(imageNames[i]));
            model.setActionDescrion(SkinManager.getInstance().getTextById(desHintKeys[i]));
            model.setDrawableId(imageIds[i]);
            if (i == 0) {
                model.setSelected(true);
            }
            mTypeModelList.add(model);
        }
        return mTypeModelList;
    }

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
}
