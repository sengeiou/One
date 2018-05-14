package com.ubt.en.alpha1e.remote.contract;

import android.content.Context;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;
import com.ubt.en.alpha1e.remote.model.RemoteItem;

import java.util.List;

/**
 * @author：liuhai
 * @date：2018/4/11 11:08
 * @modifier：ubt
 * @modify_date：2018/4/11 11:08
 * [A brief description]
 * version
 */

public class RemoteContact {
    public interface View extends BaseView {

        void playFinish();

    }

    public interface  Presenter extends BasePresenter<View> {

        void init(Context context,int remoteType);
        void unRegister();
        List<RemoteItem> getRemoteItems();

    }
}
