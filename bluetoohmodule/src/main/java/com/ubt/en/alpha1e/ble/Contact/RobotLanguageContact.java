package com.ubt.en.alpha1e.ble.Contact;

import android.content.Context;

import com.ubt.baselib.model1E.BleNetWork;
import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;
import com.ubt.en.alpha1e.ble.model.BleDownloadLanguageRsp;
import com.ubt.en.alpha1e.ble.model.BleUpgradeProgressRsp;
import com.ubt.en.alpha1e.ble.model.RobotLanguage;

import java.util.List;

/**
 * @author：liuhai
 * @date：2018/4/19 19:00
 * @modifier：ubt
 * @modify_date：2018/4/19 19:00
 * [A brief description]
 * version
 */

public class RobotLanguageContact {
    public interface View extends BaseView {
       void setRobotLanguageList(boolean status, List<RobotLanguage> list);

       void setRobotLanguageListExist(List<RobotLanguage> list);

        void setRobotLanguageResult(int status);

        void setDownloadLanguage(BleDownloadLanguageRsp downloadLanguageRsp);

        void setUpgradeProgress(BleUpgradeProgressRsp upgradeProgressRsp);

        void setRobotNetWork(BleNetWork bleNetWork);
    }

    public interface Presenter extends BasePresenter<RobotLanguageContact.View> {

        void init(Context context);

        void unRegister();

        void getRobotLanguageListFromWeb();

        void getRobotLanguageListFromRobot();

        void setRobotLanguage(String language);

        void getRobotWifiStatus();
    }
}
