package com.ubt.factorytest.utils;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.ubt.factorytest.MainActivity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/12/22 14:11
 * @描述:
 */
@RunWith(AndroidJUnit4.class)
public class ToastUtilsTest {

    private Context context;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        if(context == null){
            context = mActivityTestRule.getActivity().getApplicationContext();
            ContextUtils.init(context);
        }
        Log.i("zz","setUp");
    }

    @Test
    public void showShortToast001(){
        Log.i("zz","showShortToast001");
        ToastUtils.showShort("hello");
        Assert.assertTrue(true);
    }
}