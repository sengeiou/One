package com.ubt.blockly.main;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class BlocklyContract {

    interface View extends BaseView {

    }

    interface  Presenter extends BasePresenter<View> {

    }
}
