package com.ubt.en.alpha1e.ble.Contact;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;
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

    }

    public interface Presenter extends BasePresenter<RobotLanguageContact.View> {

        void getRobotLanguageList();

    }
}
