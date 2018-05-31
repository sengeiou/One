package com.ubt.en.alpha1e.remote.presenster;

import android.content.Context;

import com.ubt.baselib.btCmd1E.cmd.BTCmdActionDoDefault;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.remote.R;
import com.ubt.en.alpha1e.remote.contract.RemoteMainContact;
import com.ubt.en.alpha1e.remote.model.RemoteRoleInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class RemoteMainPrenster extends BasePresenterImpl<RemoteMainContact.View> implements RemoteMainContact.Presenter {

    private List<RemoteRoleInfo> mRoleInfos = new ArrayList<>();

    private BlueClientUtil mBlueClientUtil;

    @Override
    public void init(Context context) {
        mBlueClientUtil = BlueClientUtil.getInstance();
        initData();
        byte[] param = new byte[1];
        param[0] = 1;
        mBlueClientUtil.sendData(new BTCmdActionDoDefault(param).toByteArray());//进入遥控器
    }


    private void initData() {
        mRoleInfos.clear();
        RemoteRoleInfo info = new RemoteRoleInfo(1, SkinManager.getInstance().getTextById(R.string.joystick_football),
                "", R.drawable.img_football_player, 0);
        RemoteRoleInfo info2 = new RemoteRoleInfo(2, SkinManager.getInstance().getTextById(R.string.joystick_fighter),
                "", R.drawable.img_fighter, 0);
        RemoteRoleInfo info3 = new RemoteRoleInfo(3, SkinManager.getInstance().getTextById(R.string.joystick_dancer),
                "", R.drawable.img_dancer, 1);
        mRoleInfos.add(info);
        mRoleInfos.add(info2);
        mRoleInfos.add(info3);
    }

    /**
     * 获取列表角色
     *
     * @return
     */
    @Override
    public List<RemoteRoleInfo> getRoleInfos() {
        return mRoleInfos;
    }



    @Override
    public void unRegister(){
        byte[] param = new byte[1];
        param[0] = 0;
        mBlueClientUtil.sendData(new BTCmdActionDoDefault(param).toByteArray());//退出遥控器
    }
}
