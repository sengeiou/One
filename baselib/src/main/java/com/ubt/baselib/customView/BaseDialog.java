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

public class BaseDialog {

    DialogParams mParams;

    private DialogPlus dialogPlus;

    public BaseDialog(DialogParams mParams) {
        this.mParams = mParams;
        createDialogPlus();
    }


    public DialogPlus getDialogPlus() {
        return dialogPlus;
    }


    private void createDialogPlus() {
        View contentView = LayoutInflater.from(mParams.getContext()).inflate(R.layout.base_dialog_layout, null);
        ViewHolder viewHolder = new ViewHolder(contentView);
        //设置标题
        TextView title = contentView.findViewById(R.id.tv_title);
        View titleLineView = contentView.findViewById(R.id.base_view_title_line);
        if (mParams.getTitleId() != 0) {
            title.setVisibility(View.VISIBLE);
            titleLineView.setVisibility(View.VISIBLE);
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
        int height = (int) ((display.getHeight()) * 0.6); //设置宽度
        dialogPlus = DialogPlus.newDialog(mParams.getContext())
                .setContentHolder(viewHolder)
                .setGravity(Gravity.CENTER)
                .setContentBackgroundResource(R.drawable.base_rect_background)
                .setContentWidth(width)
                .setContentHeight(height)
                .setCancelable(false)
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

        public DialogPlus create() {
            BaseDialog baseDialog = new BaseDialog(mParams);
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

        private ButtonOnClickListener mButtonOnClickListener;

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

        public ButtonOnClickListener getButtonOnClickListener() {
            return mButtonOnClickListener;
        }

        public void setButtonOnClickListener(ButtonOnClickListener buttonOnClickListener) {
            mButtonOnClickListener = buttonOnClickListener;
        }
    }

    public interface ButtonOnClickListener {
         void onClick(final DialogPlus dialog, View view);
    }


}
