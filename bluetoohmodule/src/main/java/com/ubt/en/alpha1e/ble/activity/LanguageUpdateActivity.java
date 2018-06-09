package com.ubt.en.alpha1e.ble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ubt.baselib.utils.ActivityTool;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.R2;
import com.vise.log.ViseLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LanguageUpdateActivity extends AppCompatActivity {

    String progress;

    Unbinder mUnbinder;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.pb_progress)
    ProgressBar mPbProgress;
    @BindView(R2.id.tv_progress)
    TextView mTvProgress;
    @BindView(R2.id.btn_ok)
    Button mBtnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_update);
        //设置窗口对齐屏幕宽度
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;//设置对话框置顶显示
        win.setAttributes(lp);
        //设置点击外部空白处可以关闭Activity
        this.setFinishOnTouchOutside(false);
        mUnbinder = ButterKnife.bind(this);
        setIntentData(getIntent());
        ActivityTool.addActivity(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //ViseLog.d();
        setIntentData(intent);


    }

    public void setIntentData(Intent intent) {
        progress = intent.getStringExtra("progress");
        ViseLog.e("LanguageUpdateActivity=setIntentData=" + progress);
        mPbProgress.setProgress(Integer.parseInt(progress));
        mTvProgress.setText(progress + "%");
        if (Integer.parseInt(progress) == 100) {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        ActivityTool.removeActivity(this);
    }
}
