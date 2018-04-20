package com.ubt.mainmodule.user.profile.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.mainmodule.R;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/4/20 10:48
 * @描述:
 */

public class UserInfoEditActivity extends MVPBaseActivity<UserInfoEditContract.View, UserInfoEditPresenter> implements UserInfoEditContract.View{


    public static void startActivity(Context context){
        Intent intent = new Intent(context, UserInfoEditActivity.class);
        /*Bundle args = new Bundle();

        intent.setData(args);*/
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_userinfoedit);

    }

    @Override
    public Handler getViewHandler() {
        return null;
    }

    @Override
    public int getContentViewId() {
        return 0;
    }
}
