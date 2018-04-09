package com.ubt.factorytest.test.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/12/28 15:31
 * @描述:
 */
public class DataServerTest {
    private DataServer dataServer;
    Context appContext;
    @Before
    public void setUp() throws Exception {
        if(appContext == null) {
            appContext = InstrumentationRegistry.getTargetContext();
        }
        if(dataServer == null) {
            dataServer = new DataServer(appContext);
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getTestInitData() throws Exception {
        Assert.assertNotNull(dataServer.getTestInitData());
    }

    @Test
    public void setDataResult() throws Exception {
        dataServer.setDataResult(0, "hello");
        dataServer.setDataResult(1, "");
        dataServer.setDataResult(2, null);
        dataServer.setDataResult(200, null);
        Assert.assertThat(dataServer.getDataCache().size(), equalTo(0));
        Assert.assertNotNull(dataServer.getTestInitData());
        Assert.assertThat(dataServer.getDataCache().size(), greaterThan(5));
        dataServer.setDataResult(0, "hello");
        dataServer.setDataResult(1, "");
        dataServer.setDataResult(2, null);
        dataServer.setDataResult(100, "hello");
    }

    @Test
    public void setDataisPass() throws Exception {
        dataServer.setDataisPass(0, true);
        dataServer.setDataisPass(1, false);
        dataServer.setDataisPass(100, false);
        Assert.assertNotNull(dataServer.getTestInitData());
        dataServer.setDataisPass(0, true);
        dataServer.setDataisPass(1, false);
        dataServer.setDataisPass(100, true);
    }

    @Test
    public void setHasTested() throws Exception {
        dataServer.setHasTested(0);
        dataServer.setHasTested(100);
        Assert.assertNotNull(dataServer.getTestInitData());
        dataServer.setHasTested(1);
        dataServer.setHasTested(100);
    }


    @Test
    public void clearDataCache() throws Exception {
        dataServer.clearDataCache();
        Assert.assertNotNull(dataServer.getTestInitData());
        Assert.assertThat(dataServer.getDataCache().size(), greaterThan(5));
        dataServer.clearDataCache();
        Assert.assertThat(dataServer.getDataCache().size(), equalTo(0));
    }

    @Test
    public void isTestComplete() throws Exception {
        Assert.assertNotNull(dataServer.getTestInitData());
        dataServer.setHasTested(0);
        Assert.assertFalse(dataServer.isTestComplete());
        for(int i = 0; i < dataServer.getDataCache().size() - 2; i++){
            dataServer.setHasTested(i);
        }
        Assert.assertTrue(dataServer.isTestComplete());

    }

}