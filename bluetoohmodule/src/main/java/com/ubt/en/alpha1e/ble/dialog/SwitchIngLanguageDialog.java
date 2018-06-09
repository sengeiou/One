package com.ubt.en.alpha1e.ble.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ubt.baselib.skin.SkinManager;
import com.ubt.en.alpha1e.ble.R;


/**
 * @author：liuhai
 * @date：2017/11/9 16:57
 * @modifier：ubt
 * @modify_date：2017/11/9 16:57
 * [A brief description]
 * version
 */

public class SwitchIngLanguageDialog extends Dialog {

    /**
     * SwitchIngLanguageDialog
     */
    private SwitchIngLanguageDialog mDialog;
    /**
     * cancelable, the dialog dimiss or undimiss flag
     */
    private boolean cancelable = true;
    /**
     * if the dialog don't dimiss, what is the tips.
     */

    private TextView tvProgress = null;
    private TextView tvTitle = null;

    private ProgressBar pbProgress = null;

    private int type = 0;//0为语言包 1 为胸口版

    /**
     * the SwitchIngLanguageDialog constructor
     *
     * @param ctx  Context
     */

    public SwitchIngLanguageDialog(final Context ctx,int type) {
        super(ctx);
        mDialog = this;

        //this.getContext().setTheme(android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.ble_dialog_language_switching);

        //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //一定要在setContentView之后调用，否则无效
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().getDecorView().getBackground().setAlpha(120);
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();

        int width = (int) ((display.getWidth()) * 0.55); //设置宽度

        // Dialog宽度
        lp.width = width;
        lp.height = display.getHeight();

        Window window = getWindow();
        window.setAttributes(lp);
        // 必须放在加载布局后

        tvTitle = findViewById(R.id.tv_title);
        tvProgress = findViewById(R.id.tv_progress);
        pbProgress = findViewById(R.id.pb_progress);

        if(type == 0){
            tvTitle.setText(SkinManager.getInstance().getTextById(R.string.about_robot_language_changing));
        }else {
            tvTitle.setText(SkinManager.getInstance().getTextById(R.string.about_robot_updating));
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!cancelable) {
                return true;
            }
        }
        return true;
    }

    public void doShow(){
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }

        mDialog.show();
    }

    public SwitchIngLanguageDialog setProgress(int progress){
        tvProgress.setText( progress + "%");
        pbProgress.setProgress(progress);
        return mDialog;
    }

    public SwitchIngLanguageDialog setCancel(boolean cancelable){
        mDialog.cancelable = cancelable;

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);

        return mDialog;
    }

}
