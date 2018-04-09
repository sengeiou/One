package com.ubt.factorytest.bluetooth;

import android.support.test.rule.ActivityTestRule;

import com.ubt.factorytest.MainActivity;

import org.junit.Before;
import org.junit.Rule;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/12/22 14:34
 * @描述:
 */
public class BluetoothPresenterTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {

    }

}