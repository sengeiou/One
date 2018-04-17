package com.ubt.en.alpha1e;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ubt.baselib.commonModule.ModuleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ModuleUtils.Main_MainActivity)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.right_bar_top)
    ImageView rightBarTop;
    @BindView(R.id.right_bar_center)
    ImageView rightBarCenter;
    @BindView(R.id.right_bar_bottom)
    ImageView rightBarBottom;
    @BindView(R.id.iv_robot_status)
    ImageView ivRobotStatus;
    @BindView(R.id.iv_robot_status_error)
    ImageView ivRobotStatusError;
    @BindView(R.id.tv_charging_backgroup)
    TextView tvChargingBattary;
    @BindView(R.id.iv_play_center)
    ImageView ivPlayCenter;
    @BindView(R.id.iv_voice_cmd)
    ImageView ivVoiceCmd;
    @BindView(R.id.iv_actions)
    ImageView ivActions;
    @BindView(R.id.iv_blockly)
    ImageView ivBlockly;
    @BindView(R.id.iv_community)
    ImageView ivCommunity;
    @BindView(R.id.iv_joystick)
    ImageView ivJoystick;


    private int prePower = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    /**
     * 刷新机器人电池状态
     */
    private void refreshBatteryStatus(int power) {
        if (prePower == power) {
            return;
        }

        Drawable img = null;
        if (power <= 20 && prePower > 20) {
            img = getResources().getDrawable(R.mipmap.charging_red);
        } else if (power > 20 && power <= 40 && (prePower <= 20 || prePower > 40)) {
            img = getResources().getDrawable(R.mipmap.charging_yellow);
        } else if (power > 40 && power <= 90 && (prePower <= 40 || prePower > 90)) {
            img = getResources().getDrawable(R.mipmap.charging_green);
        } else if (power > 90 && prePower <= 90) {
            img = getResources().getDrawable(R.mipmap.charging_full);
        }

        prePower = power;
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        if (img != null) {
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            tvChargingBattary.setCompoundDrawables(img, null, null, null);
        }

        tvChargingBattary.setText(power + "%");
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.iv_robot_status, R.id.iv_play_center, R.id.iv_voice_cmd, R.id.iv_actions,
            R.id.iv_blockly, R.id.iv_community, R.id.iv_joystick,
            R.id.right_bar_top, R.id.right_bar_center, R.id.right_bar_bottom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_robot_status:
                break;
            case R.id.iv_play_center:
                break;
            case R.id.iv_voice_cmd:
                break;
            case R.id.iv_actions:
                break;
            case R.id.iv_blockly:
                break;
            case R.id.iv_community:
                break;
            case R.id.iv_joystick:
                break;
            case R.id.right_bar_top:
                break;
            case R.id.right_bar_center:
                break;
            case R.id.right_bar_bottom:
                break;
            default:
                break;
        }
    }
}
