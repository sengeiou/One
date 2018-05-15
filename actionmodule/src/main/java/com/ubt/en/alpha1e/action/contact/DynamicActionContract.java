package com.ubt.en.alpha1e.action.contact;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;
import com.ubt.en.alpha1e.action.model.DynamicActionModel;

import java.util.List;

/**
 * @author：liuhai
 * @date：2018/5/2 17:05
 * @modifier：ubt
 * @modify_date：2018/5/2 17:05
 * [A brief description]
 * version
 */

public class DynamicActionContract {
    public interface View extends BaseView {
        void setDynamicData(boolean status,int type,List<DynamicActionModel> list);
        void deleteActionResult(boolean isSuccess);
    }

    public interface  Presenter extends BasePresenter<View> {
        void getDynamicData(int type);

        void getDynamicData(int type,int page,int offset);

        void deleteActionById(int actionId);
    }
}
