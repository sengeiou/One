package com.ubt.baselib.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ubt.baselib.utils.ActivityTool;

import java.lang.reflect.ParameterizedType;

import me.yokeyword.fragmentation.SupportActivity;


/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public abstract class MVPBaseActivity<V extends BaseView,T extends BasePresenterImpl<V>> extends SupportActivity implements BaseView{
    public T mPresenter;

    public abstract int getContentViewId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getContentViewId() != 0) {
            setContentView(getContentViewId());
        }
        //presenter 绑定View
        mPresenter= getInstance(this,1);
        if(mPresenter != null) {
            mPresenter.attachView((V) this);
        }
        ActivityTool.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter!=null) {
            mPresenter.detachView();
        }
        ActivityTool.removeActivity(this);
    }

    @Override
    public Context getContext(){
        return this;
    }

    public  <T> T getInstance(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }
}
