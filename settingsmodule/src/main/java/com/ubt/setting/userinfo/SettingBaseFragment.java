package com.ubt.setting.userinfo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.mvp.BaseView;
import com.ubt.baselib.mvp.MVPBaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author：liuhai
 * @date：2018/1/25 16:54
 * @modifier：ubt
 * @modify_date：2018/1/25 16:54
 * [A brief description]
 * version
 */

public abstract class SettingBaseFragment<V extends BaseView, T extends BasePresenterImpl<V>> extends MVPBaseFragment {
    protected View mRootView;
    private Unbinder mUnbinder;
    public Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);
        mUnbinder = ButterKnife.bind(this, mRootView);//绑定framgent
        initUI();
        return mRootView;
    }

    protected abstract void initUI();


    public abstract int getContentViewId();

}
