package com.ubt.baselib.customView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.ubt.baselib.R;
import com.ubt.baselib.skin.SkinManager;

/**
 * @author：liuhai
 * @date：2018/4/23 11:09
 * @modifier：ubt
 * @modify_date：2018/4/23 11:09
 * [A brief description]
 * version
 */

public class BaseGdprDialog {

    DialogParams mParams;

    private DialogPlus dialogPlus;

    public BaseGdprDialog(DialogParams mParams) {
        this.mParams = mParams;
        createDialogPlus();
    }


    public DialogPlus getDialogPlus() {
        return dialogPlus;
    }


    private void createDialogPlus() {
        View contentView = LayoutInflater.from(mParams.getContext()).inflate(R.layout.base_dialog_gdpr_layout, null);
        ViewHolder viewHolder = new ViewHolder(contentView);
        //设置标题
        TextView title = contentView.findViewById(R.id.tv_title);
        View titleLineView = contentView.findViewById(R.id.base_view_title_line);
        if (mParams.getTitleId() != 0) {
            title.setVisibility(View.VISIBLE);
//            titleLineView.setVisibility(View.VISIBLE);
            title.setText(SkinManager.getInstance().getTextById(mParams.getTitleId()));
        } else {
            title.setVisibility(View.GONE);
            titleLineView.setVisibility(View.GONE);
        }
        //设置内容
        TextView tvContent = contentView.findViewById(R.id.tv_content);
        if (mParams.getMessageId() != 0) {
            tvContent.setText(SkinManager.getInstance().getTextById(mParams.getMessageId()));
        }

        if (!TextUtils.isEmpty(mParams.getMessage())) {
            tvContent.setText(mParams.getMessage());
        }

        //设置底部按钮
        Button left = contentView.findViewById(R.id.button_confirm);
        Button right = contentView.findViewById(R.id.button_cancle);

        View bottomLine = contentView.findViewById(R.id.bottom_line);
        if (mParams.getLeftButtonId() != 0) {
            left.setVisibility(View.VISIBLE);
            bottomLine.setVisibility(View.VISIBLE);
            left.setText(SkinManager.getInstance().getTextById(mParams.getLeftButtonId()));
        }

        if (mParams.getRightButtonId() != 0) {
            right.setVisibility(View.VISIBLE);
            right.setText(SkinManager.getInstance().getTextById(mParams.getRightButtonId()));
        }

        if (mParams.getLeftButtonColor() != 0) {
            ColorStateList colorStateList = mParams.getContext().getResources().getColorStateList(mParams.getLeftButtonColor());
            left.setTextColor(colorStateList);
        }

        if (mParams.getRightButtonColor() != 0) {
            ColorStateList colorStateList = mParams.getContext().getResources().getColorStateList(mParams.getRightButtonColor());
            right.setTextColor(colorStateList);
        }

        WindowManager windowManager = (WindowManager) mParams.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = (int) ((display.getWidth()) * 0.6); //设置宽度
        int height = (int) ((display.getHeight()) * 0.7); //设置宽度
        int screenWidth = Math.max(width, height); //设置宽度
        int screenHeight= Math.min(width,height);
        dialogPlus = DialogPlus.newDialog(mParams.getContext())
                .setContentHolder(viewHolder)
                .setGravity(Gravity.CENTER)
                .setContentBackgroundResource(R.drawable.base_rect_background)
                .setContentWidth(screenWidth)
                .setContentHeight(screenHeight)
                .setCancelable(mParams.isCancleable())
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {
                        if (mParams.getOnDissmissListener()!=null){
                            mParams.getOnDissmissListener().onDissmiss();
                        }
                    }
                })
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (mParams.getButtonOnClickListener() != null) {
                            mParams.getButtonOnClickListener().onClick(dialog, view);
                        }
                    }
                })
                .create();
    }


    public static class Builder {

        DialogParams mParams;
        private Context mContext;

        public Builder(Context context) {
            mParams = new DialogParams();
            this.mContext = context;
            mParams.setContext(context);
        }

        /**
         * 设置标题文字
         *
         * @param titleId
         * @return
         */
        public Builder setTitle(int titleId) {
            mParams.setTitleId(titleId);
            return this;
        }

        public Builder setMessage(int messageId) {
            mParams.setMessageId(messageId);
            return this;
        }

        /**
         * 设置内容文字
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            mParams.setMessage(message);
            return this;
        }

        /**
         * 设置左边确定按钮文字
         *
         * @param leftButtonId
         * @return
         */
        public Builder setConfirmButtonId(int leftButtonId) {
            mParams.setLeftButtonId(leftButtonId);
            return this;
        }

        /**
         * 设置右边取消按钮文字
         *
         * @param rightButtonID
         * @return
         */
        public Builder setCancleButtonID(int rightButtonID) {
            mParams.setRightButtonId(rightButtonID);
            return this;
        }

        /**
         * 设置右边确定按钮文字颜色
         *
         * @param color
         * @return
         */
        public Builder setConfirmButtonColor(int color) {
            mParams.setLeftButtonColor(color);
            return this;
        }

        /**
         * 设置左边确定按钮文字颜色
         *
         * @param color
         * @return
         */
        public Builder setCancleButtonColor(int color) {
            mParams.setRightButtonColor(color);
            return this;
        }

        /**
         * 设置按钮点击事件
         *
         * @param buttonOnClickListener
         * @return
         */
        public Builder setButtonOnClickListener(ButtonOnClickListener buttonOnClickListener) {
            mParams.setButtonOnClickListener(buttonOnClickListener);
            return this;
        }
        /**
         * 设置按钮点击事件
         *
         * @param onDissmissListener
         * @return
         */
        public Builder setOnDissmissListener(OnDissmissListener onDissmissListener) {
            mParams.setOnDissmissListener(onDissmissListener);
            return this;
        }


        public Builder setCancleable(boolean cancleable){
            mParams.setCancleable(cancleable);
            return this;
        }

        public DialogPlus create() {
            BaseGdprDialog baseDialog = new BaseGdprDialog(mParams);
            return baseDialog.getDialogPlus();
        }

    }


    public static class DialogParams {

        private Context mContext;

        private int titleId;

        private int messageId;

        private int leftButtonId;

        private int leftButtonColor;

        private int rightButtonId;

        private int rightButtonColor;
        private String message;
        private boolean cancleable;//点击外部是否可以取消
        private ButtonOnClickListener mButtonOnClickListener;
        private OnDissmissListener mOnDissmissListener;
        public Context getContext() {
            return mContext;
        }

        public void setContext(Context context) {
            mContext = context;
        }

        public int getTitleId() {
            return titleId;
        }

        public void setTitleId(int titleId) {
            this.titleId = titleId;
        }

        public int getMessageId() {
            return messageId;
        }

        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getLeftButtonId() {
            return leftButtonId;
        }

        public void setLeftButtonId(int leftButtonId) {
            this.leftButtonId = leftButtonId;
        }

        public int getRightButtonId() {
            return rightButtonId;
        }

        public void setRightButtonId(int rightButtonId) {
            this.rightButtonId = rightButtonId;
        }

        public int getLeftButtonColor() {
            return leftButtonColor;
        }

        public void setLeftButtonColor(int leftButtonColor) {
            this.leftButtonColor = leftButtonColor;
        }

        public int getRightButtonColor() {
            return rightButtonColor;
        }

        public void setRightButtonColor(int rightButtonColor) {
            this.rightButtonColor = rightButtonColor;
        }

        public boolean isCancleable() {
            return cancleable;
        }

        public void setCancleable(boolean cancleable) {
            this.cancleable = cancleable;
        }

        public ButtonOnClickListener getButtonOnClickListener() {
            return mButtonOnClickListener;
        }

        public void setButtonOnClickListener(ButtonOnClickListener buttonOnClickListener) {
            mButtonOnClickListener = buttonOnClickListener;
        }

        public OnDissmissListener getOnDissmissListener() {
            return mOnDissmissListener;
        }

        public void setOnDissmissListener(OnDissmissListener onDissmissListener) {
            mOnDissmissListener = onDissmissListener;
        }
    }

    public interface ButtonOnClickListener {
         void onClick(final DialogPlus dialog, View view);
    }


    public interface OnDissmissListener {
        void onDissmiss();
    }

}
