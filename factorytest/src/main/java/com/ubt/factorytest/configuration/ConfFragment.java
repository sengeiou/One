package com.ubt.factorytest.configuration;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.ubt.factorytest.R;
import com.ubt.factorytest.test.data.btcmd.FactoryTool;
import com.ubt.factorytest.utils.SpUtils;
import com.ubt.factorytest.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/3 10:32
 * @描述:
 */

public class ConfFragment extends SupportFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ed_soft)
    EditText edSoft;
    @BindView(R.id.ed_firmware)
    EditText edFirmware;
    @BindView(R.id.ed_min_power)
    EditText edMinPower;
    @BindView(R.id.ed_max_power)
    EditText edMaxPower;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    Unbinder unbinder;

    private Handler mHandler;
    private FactoryParamConf paramConf;

    public static ConfFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ConfFragment fragment = new ConfFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conf, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbarNav(toolbar);
        toolbar.setTitle("测试参数配置");
        mHandler = new Handler(Looper.getMainLooper());
        String parm = SpUtils.getInstance().getFactoryConf();
        if(!TextUtils.isEmpty(parm)){
            paramConf = new FactoryParamConf();
            paramConf = paramConf.fromJson(parm);
            edSoft.setText(paramConf.getSoftVer());
            edFirmware.setText(paramConf.getFirmwareVer());
            edMinPower.setText(paramConf.getMinPower()+"");
            edMaxPower.setText(paramConf.getMaxPower()+"");

        }
        return view;
    }

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    hideKeyboard();
                    pop();
                } else {
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideKeyboard();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        checkParamValidate();
    }

    private boolean checkParamValidate(){
        boolean isOk = true;
        String softVer = edSoft.getText().toString();
        String firmVer = edFirmware.getText().toString();
        String minPower = edMinPower.getText().toString();
        String maxPower = edMaxPower.getText().toString();


        if(TextUtils.isEmpty(softVer)){
            ToastUtils.showShort("软件版本号不能为空!!!");
            return false;
        }
        if(TextUtils.isEmpty(firmVer)){
            ToastUtils.showShort("硬件版本号不能为空!!!");
            return false;
        }

        if(TextUtils.isEmpty(minPower) ||
                TextUtils.isEmpty(maxPower)){
            ToastUtils.showShort("电量值不能为空!!!");
            return false;
        }
        int minVol = Integer.valueOf(minPower);
        int maxVol = Integer.valueOf(maxPower);

        if(minVol > maxVol){
            ToastUtils.showShort("最小音量需要比最大音量小!!!");
            return false;
        }

        FactoryParamConf conf = new FactoryParamConf();
        conf.setSoftVer(softVer);
        conf.setFirmwareVer(firmVer);
        conf.setMinPower(minVol);
        conf.setMaxPower(maxVol);

        SpUtils.getInstance().saveFactoryConf(conf.toJson());

        ToastUtils.showShort("保存配置参数成功!");

        FactoryTool.getInstance().init();

        hideKeyboard();
        pop();
        return isOk;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && this.getActivity().getCurrentFocus() != null) {
            if (this.getActivity().getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
