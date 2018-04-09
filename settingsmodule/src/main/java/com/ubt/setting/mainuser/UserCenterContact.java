package com.ubt.setting.mainuser;


import android.content.Context;
import android.support.v4.app.Fragment;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;

import java.util.List;

/**
 * @author：liuhai
 * @date：2017/10/27 11:41
 * @modifier：ubt
 * @modify_date：2017/10/27 11:41
 * [A brief description]
 * version
 */

public interface UserCenterContact {

    interface UserCenterView extends BaseView {
        void loadData(List<LeftMenuModel> list, List<Fragment> fragmentList);

        void getUnReadMessage(boolean isSuccess, int count);
    }

    interface UserCenterPresenter extends BasePresenter<UserCenterView> {
        void initData(Context contex);

        void getUnReadMessage();
    }

}
