package com.ubt.en.alpha1e.ble.Contact;

import android.content.Context;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;
import com.ubt.en.alpha1e.ble.model.UpgradeProgressInfo;

/**
 * @author：liuhai
 * @date：2018/4/11 11:08
 * @modifier：ubt
 * @modify_date：2018/4/11 11:08
 * [A brief description]
 * version
 */

public class RobotStatuContact {
    public interface View extends BaseView {


        void setRobotSoftVersion(String softVersion);


        void setAutoUpgradeStatus(int status);

        void updateUpgradeProgress(UpgradeProgressInfo progressInfo);
        void updateFirmProgress(UpgradeProgressInfo progressInfo);

        void setRobotHardVersion(String hardVersion);
    }

    public interface Presenter extends BasePresenter<View> {

        void init(Context context);

        void unRegister();

        void getRobotAutoState();


        /** 改变 1E 自动升级状态
         * @param is0pen false 为未开启， true为已开启
         */
        void doChangeAutoUpgrade(boolean is0pen);
    }
}
