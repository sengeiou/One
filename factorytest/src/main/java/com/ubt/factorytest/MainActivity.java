package com.ubt.factorytest;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.util.Log;

import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.factorytest.bluetooth.BluetoothFragment;
import com.ubt.factorytest.bluetooth.BluetoothPresenter;
import com.ubt.factorytest.utils.ContextUtils;
import com.ubt.factorytest.utils.EspressoIdlingResource;
import com.ubt.factorytest.utils.SpUtils;

import me.yokeyword.fragmentation.SupportActivity;

public class MainActivity extends SupportActivity {

    private final String BT_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    private BluetoothPresenter mPresenter;
//    private BluetoothController mBluetoothController;
    private BlueClientUtil mBlueClient;
    private BluetoothFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContextUtils.init(this);
        SpUtils.getInstance().init(this);
        initBluetooth();
        fragment = findFragment(BluetoothFragment.class);
        if (fragment == null) {
            fragment = BluetoothFragment.newInstance();
            loadRootFragment(R.id.frame_content, fragment);
        }
        mPresenter = new BluetoothPresenter(fragment);
    }

    private void initBluetooth() {
        mBlueClient = BlueClientUtil.getInstance();
        mBlueClient.init(MainActivity.this);
//        mBluetoothController = BluetoothController.getInstance().build(this);
//        mBluetoothController.setAppUuid(UUID.fromString(BT_UUID));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if(mBluetoothController != null) {
            mBluetoothController.release();
        }*/
        if(mBlueClient != null){
            mBlueClient.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mBlueClient.getConnectionState() != BluetoothState.STATE_CONNECTED
                && !getTopFragment().equals(fragment)){
            popTo(BluetoothFragment.class, false);
            Log.i("MainActivity","蓝牙端开，重新加载主页面!");
        }
    }

    @VisibleForTesting
    public IdlingResource getIdlingresource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
