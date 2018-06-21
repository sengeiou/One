package com.ubt.baselib.customView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ubt.baselib.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * @author：liuhai
 * @date：2017/11/9 16:57
 * @modifier：ubt
 * @modify_date：2017/11/9 16:57
 * [A brief description]
 * version
 */

public class BaseLoadingDialog extends Dialog {

    /**
     * LoadingDialog
     */
    private static BaseLoadingDialog loadDialog;
    /**
     * isCancelable, the dialog dimiss or undimiss flag
     */
    private static boolean isCancelable;
    /**
     * if the dialog don't dimiss, what is the tips.
     */
    private String tipMsg;

    private int drawableId;

    private static Disposable mDisposable;

    /**
     * the LoadingDialog constructor
     *
     * @param ctx        Context
     * @param cancelable boolean
     * @param tipMsg     String
     */

    public BaseLoadingDialog(final Context ctx, boolean cancelable, String tipMsg, int drawableId) {
        super(ctx);
        isCancelable = cancelable;
        this.tipMsg = tipMsg;
        this.drawableId = drawableId;
        //this.getContext().setTheme(android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.base_dialog_loading);
        this.setCancelable(!cancelable);
        this.setCanceledOnTouchOutside(!cancelable);
        //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //一定要在setContentView之后调用，否则无效
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().getDecorView().getBackground().setAlpha(120);
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();

        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();

        // Dialog宽度
        lp.width = (int) (dm.density * 180);
        lp.height = (int) (dm.density * 150);
        Window window = getWindow();
        window.setAttributes(lp);

        // 必须放在加载布局后
        //setparams();
        TextView tv = (TextView) findViewById(R.id.tv_load);
        if (!TextUtils.isEmpty(tipMsg)) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(tipMsg);
        }

        ProgressBar progressBar = findViewById(R.id.ble_connect_loading);
        if (drawableId != 0) {
            progressBar.setIndeterminateDrawable(ctx.getResources().getDrawable(drawableId));
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isCancelable) {
                if (loadDialog != null && loadDialog.isShowing()) {
                    loadDialog.dismiss();
                    loadDialog = null;
                    if (mDisposable != null) {
                        mDisposable.dispose();
                        mDisposable = null;
                    }
                }

                return true;
            }
        }
        return true;
    }

    /**
     * show the dialog
     *
     * @param context
     */
    public static void show(Context context) {
        show(context, null, true, 0);
    }

    /**
     * 设置可以取消
     *
     * @param context
     */
    public static void show(int time, Context context) {
        show(context, null, true, 0);
        if (time > 0) {
            mDisposable = Observable.timer(time, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            isCancelable = false;
                            loadDialog.setCancelable(true);
                            loadDialog.setCanceledOnTouchOutside(true);
                        }
                    });
        }
    }


    /**
     * show the dialog
     *
     * @param context Context
     * @param message String
     */
    public static void show(Context context, String message) {
        show(context, message, true, 0);
    }

    /**
     * show the dialog
     *
     * @param context Context
     * @param message String
     */
    public static void show(Context context, int time, String message) {
        show(context, message, true, 0);
        if (time > 0) {
            mDisposable = Observable.timer(time, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            isCancelable = false;
                            loadDialog.setCancelable(true);
                            loadDialog.setCanceledOnTouchOutside(true);
                        }
                    });
        }
    }

    /**
     * show the dialog
     *
     * @param context    Context
     * @param resourceId resourceId
     */
    public static void show(Context context, int resourceId) {
        show(context, context.getResources().getString(resourceId), true, 0);
    }

    public static void show(Context context, String message, int drawableId) {
        show(context, message, true, drawableId);
    }


    /**
     * show the dialog
     *
     * @param context    Context
     * @param message    String, show the message to user when isCancel is true.
     * @param cancelable boolean, true is can't dimiss，false is can dimiss
     */
    private static void show(Context context, String message, boolean cancelable, int drawableId) {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (loadDialog != null && loadDialog.isShowing()) {
            return;
        }
        loadDialog = new BaseLoadingDialog(context, cancelable, message, drawableId);
        loadDialog.show();
    }

    /**
     * dismiss the dialog
     */
    public static void dismiss(Context context) {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
        try {
            if (context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    loadDialog = null;
                    return;
                }
            }

            if (loadDialog != null && loadDialog.isShowing()) {
                Context loadContext = loadDialog.getContext();
                if (loadContext != null && loadContext instanceof Activity) {
                    if (((Activity) loadContext).isFinishing()) {
                        loadDialog = null;
                        return;
                    }
                }
                loadDialog.dismiss();
                loadDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            loadDialog = null;
        }
    }

}
