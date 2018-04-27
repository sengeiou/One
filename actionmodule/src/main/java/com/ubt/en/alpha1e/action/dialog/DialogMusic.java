package com.ubt.en.alpha1e.action.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ubt.baselib.skin.SkinManager;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.util.CourseArrowAminalUtil;
import com.vise.log.ViseLog;


/**
 * @author wmma
 * @className
 * @description
 * @date
 * @update
 */


public class DialogMusic extends Dialog {

    private static final String TAG = "DialogMusic";
    private DialogMusic dialogMusic;

    private Context context;

    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView tvTitle;
    private TextView tvContent;
    private LinearLayout llTips;

    private ImageView ivSureArrow;

    OnMusicDialogListener mDialogListener;
    private int type = 0;


    public DialogMusic(Context context, OnMusicDialogListener mDialogListener, int type) {
        super(context);
        dialogMusic = this;
        this.context = context;
        this.mDialogListener = mDialogListener;
        this.type = type;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogMusic.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.action_dialog_music_tips);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogMusic.getWindow().getAttributes();
        lp.width = (int) ((display.getWidth()) * 0.6); //设置宽度
        dialogMusic.getWindow().setAttributes(lp);
        dialogMusic.setCanceledOnTouchOutside(false);
        initView();
    }


    private void initView() {
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvContent = (TextView) findViewById(R.id.tv_tip_content);
        llTips = (LinearLayout) findViewById(R.id.ll_tips);
        ivSureArrow = findViewById(R.id.iv_sure_arrow);

        if (type == 3) {
            CourseArrowAminalUtil.startViewAnimal(true, ivSureArrow, 2);
        }
        if (type == 1||type==3) {
            llTips.setVisibility(View.GONE);
            tvTitle.setText(SkinManager.getInstance().getTextById(R.string.ui_create_auto_record));
            tvContent.setText(SkinManager.getInstance().getTextById(R.string.ui_create_auto_record_tips));
            tvConfirm.setText(SkinManager.getInstance().getTextById(R.string.ui_create_start));
        } else {
            llTips.setVisibility(View.VISIBLE);
        }

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type != 3) {
                    dismiss();
                }
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0) {
                    ViseLog.d(TAG, "select music");
                    if (null != mDialogListener) {
                        mDialogListener.setMusic();
                    }
                } else if (type == 1 || type == 3) {
                    if (null != mDialogListener) {
                        mDialogListener.startAutoRead();
                    }
                }
                dismiss();

            }
        });


    }

    public interface OnMusicDialogListener {
        void setMusic();

        void startAutoRead();
    }


}
