package com.ubt.en.alpha1e.ble.Contact;

import android.content.Context;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;

/**
 * @author：liuhai
 * @date：2018/4/11 11:08
 * @modifier：ubt
 * @modify_date：2018/4/11 11:08
 * [A brief description]
 * version
 */

public class BleGuideContact {
    public interface View extends BaseView {
        void goBleSraechActivity();
    }

    public interface  Presenter extends BasePresenter<View> {
        void init(Context context);

        void unRegister();



        void checkBlestatu();
    }
}
