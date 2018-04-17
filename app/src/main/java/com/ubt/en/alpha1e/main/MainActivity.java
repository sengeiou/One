package com.ubt.en.alpha1e.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.RightBar;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.en.alpha1e.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ModuleUtils.Main_MainActivity)
public class MainActivity extends MVPBaseActivity<MainContract.View, MainPresenter> implements MainContract.View {

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
    @BindView(R.id.right_bar)
    RightBar rightBar;

    private int prePower = -1;
    private Handler mHandler;

    @Override
    public int getContentViewId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        rightBar.setRightBarStatus(RightBar.TOP_ON);
        mPresenter.init();
        initHandler();
        initRightBarClick(); //初始化右边栏点击事件

        if(mPresenter.isRobotConnected()){ //蓝牙连接成功，隐藏错误状态
            ivRobotStatusError.setVisibility(View.INVISIBLE);
            tvChargingBattary.setVisibility(View.VISIBLE);
        }else{
            tvChargingBattary.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.queryBattery(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.queryBattery(false);
    }


    /**
     * 初始化handler
     */
    private void initHandler() {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case MainContract.HCMD_REFRESH_BATTERY:
                        refreshBatteryStatus(msg.arg1);
                        break;
                    case MainContract.HCMD_LOST_BT:
                        ivRobotStatusError.setVisibility(View.VISIBLE);
                        tvChargingBattary.setVisibility(View.INVISIBLE);
                        break;
                    case MainContract.HCMD_BT_CONNETED:
                        ivRobotStatusError.setVisibility(View.INVISIBLE);
                        tvChargingBattary.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }

            }
        };
    }


    /**
     * 右边栏为自定义VIEW点击事件使用传统的方式实现
     */
    private void initRightBarClick() {
        rightBar.setCenterClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        rightBar.setBottomClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    /**
     * 刷新机器人电池状态
     */
    public void refreshBatteryStatus(int power) {
        if (prePower == power) {
            return;
        }
        //纠正参数
        if (power < 0) {
            power = 0;
        } else if (power > 100) {
            power = 100;
        }

        //判断电量选择电量图标
        Drawable img = null;
        if (power <= 20 && (prePower > 20 || prePower < 0)) {
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
        //更新电量显示
        tvChargingBattary.setText(power + "%");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.release();
    }

    @OnClick({R.id.iv_robot_status, R.id.iv_play_center, R.id.iv_voice_cmd, R.id.iv_actions,
            R.id.iv_blockly, R.id.iv_community, R.id.iv_joystick,
            })
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
            default:
                break;
        }
    }

    @Override
    public Handler getViewHandler() {
        return mHandler;
    }
}
