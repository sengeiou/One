package com.ubt.en.alpha1e.action.dialog;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.model.PrepareDataModel;
import com.ubt.en.alpha1e.action.model.PrepareMusicModel;
import com.ubt.en.alpha1e.action.util.ActionConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：liuhai
 * @date：2017/11/16 14:07
 * @modifier：ubt
 * @modify_date：2017/11/16 14:07
 * [A brief description]
 * version
 */

public class PrepareActionUtil implements BaseQuickAdapter.OnItemClickListener, OnClickListener {
    private Context mContext;
    private int mType = -1;
    List<PrepareDataModel> list = new ArrayList<>();
    ActionAdapter actionAdapter;
    private TextView tvCancle;
    private TextView tvConfirm;
    private OnDialogListener mDialogListener;
    private PrepareDataModel selectDataModel;

    private static DialogPlus mDialogPlus;

    public PrepareActionUtil(Context context) {
        this.mContext = context;
    }

    /**
     * 显示对话框
     *
     * @param type
     */
    public void showActionDialog(int type, OnDialogListener mDialogListener) {
        this.mDialogListener = mDialogListener;
        this.mType = type;
        String title = "";
        if (type == 1) {
            title = SkinManager.getInstance().getTextById(R.string.ui_create_basic_action);
            list = ActionConstant.getBasicActionList(mContext);
        } else if (type == 2) {
            title = SkinManager.getInstance().getTextById(R.string.ui_create_advance_action);
            list = ActionConstant.getHighActionList(mContext);
        }
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_aciton_select, null);
        ViewHolder viewHolder = new ViewHolder(contentView);
        TextView tvTitle = contentView.findViewById(R.id.title_actions);
        tvTitle.setText(title);
        tvCancle = contentView.findViewById(R.id.tv_cancel);
        tvConfirm = contentView.findViewById(R.id.tv_confirm);
        tvConfirm.setText(SkinManager.getInstance().getTextById(R.string.ui_common_add));
        tvCancle.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = contentView.findViewById(R.id.rv_actions);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 5);
        recyclerView.setLayoutManager(layoutManager);
        actionAdapter = new ActionAdapter(R.layout.item_actions, list);
        actionAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(actionAdapter);
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = (int) ((display.getWidth()) * 0.8); //设置宽度
        mDialogPlus = DialogPlus.newDialog(mContext)
                .setContentHolder(viewHolder)
                .setContentBackgroundResource(R.drawable.action_dialog_filter_rect)
                .setGravity(Gravity.CENTER)
                .setContentWidth(width)
                .setOnClickListener(this)
                .setCancelable(true)
                .create();
        mDialogPlus.show();
        selectDataModel = null;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        selectDataModel = (PrepareDataModel) adapter.getData().get(position);
        for (int i = 0; i < list.size(); i++) {
            if (selectDataModel.getPrepareName().equals(list.get(i).getPrepareName())) {
                list.get(i).setSelected(true);
            } else {
                list.get(i).setSelected(false);
            }
        }
        tvConfirm.setTextColor(mContext.getResources().getColor(R.color.base_blue));
        tvConfirm.setEnabled(true);
        actionAdapter.notifyDataSetChanged();
        if (mType == 1 || mType == 2) {
            if (null != mDialogListener && selectDataModel != null) {
                mDialogListener.playAction(selectDataModel);
            }
        }

    }

    @Override
    public void onClick(DialogPlus dialog, View view) {
        int i = view.getId();
        if (i == R.id.tv_cancel) {
            dialog.dismiss();

        } else if (i == R.id.tv_confirm) {
            if (selectDataModel == null) {
                return;
            }
            if (null != mDialogListener) {
                mDialogListener.onActionConfirm(selectDataModel);
            }
            dialog.dismiss();

        } else {
        }


    }

    public static void dismiss() {
        if (mDialogPlus != null) {
            mDialogPlus.dismiss();
            mDialogPlus = null;
        }
    }


    private class ActionAdapter extends BaseQuickAdapter<PrepareDataModel, BaseViewHolder> {


        public ActionAdapter(@LayoutRes int layoutResId, @Nullable List<PrepareDataModel> data) {
            super(layoutResId, data);
        }


        @Override
        protected void convert(BaseViewHolder helper, PrepareDataModel item) {
            helper.setText(R.id.tv_action_name, item.getPrepareName());
            ((ImageView) helper.getView(R.id.iv_action_icon)).setImageResource(item.getDrawableId());
            ImageView ivSelect = helper.getView(R.id.iv_action_select);
            ivSelect.setVisibility(item.isSelected() ? View.VISIBLE : View.GONE);
        }
    }


    public interface OnDialogListener {

        void onActionConfirm(PrepareDataModel prepareDataModel);

        void playAction(PrepareDataModel prepareDataModel);

        void onMusicConfirm(PrepareMusicModel prepareMusicModel);

        void onMusicDelete(PrepareMusicModel prepareMusicModel);
    }


}
