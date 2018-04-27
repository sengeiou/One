package com.ubt.en.alpha1e.action.contact;

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

public class SaveActionContact {
    public interface View extends BaseView {
        void notifyDataSetChanged();
    }

    public interface Presenter extends BasePresenter<View> {

        void initActionTypeData();

        public void selectActionMode(int position);
    }
}
