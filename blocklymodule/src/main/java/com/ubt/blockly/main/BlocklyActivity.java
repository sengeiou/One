package com.ubt.blockly.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.reflect.TypeToken;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.baselib.globalConst.BaseHttpEntity;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.model1E.BaseResponseModel;
import com.ubt.baselib.model1E.BlocklyProjectMode;
import com.ubt.baselib.model1E.UserInfoModel;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.baselib.utils.ByteHexHelper;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.blockly.BlockHttpEntity;
import com.ubt.blockly.R;
import com.ubt.blockly.R2;
import com.ubt.blockly.course.BlocklyUtil;
import com.ubt.blockly.main.bean.BaseRequest;
import com.ubt.blockly.main.bean.BlocklyProjectDelRequest;
import com.ubt.blockly.main.bean.BlocklyProjectRequest;
import com.ubt.blockly.main.bean.BlocklyRespondMode;
import com.ubt.blockly.main.bean.BlocklySaveMode;
import com.ubt.blockly.main.bean.DeviceDirectionEnum;
import com.ubt.blockly.main.bean.DirectionSensorEventListener;
import com.ubt.blockly.main.bean.DragView;
import com.ubt.blockly.main.bean.LedColorEnum;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.vise.log.ViseLog;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.request.PostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */

public class BlocklyActivity extends MVPBaseActivity<BlocklyContract.View, BlocklyPresenter> implements BlocklyContract.View,DirectionSensorEventListener.ActionListener {

    private String TAG = "BlocklyActivity";
    @BindView(R2.id.blockly_webView)
    WebView mWebView;
    @BindView(R2.id.rl_go_video)
    DragView rlGoVideo;
    @BindView(R2.id.iv_shot_album)
    ImageView ivShotAlbum;
    public static String URL = BaseHttpEntity.BLOCKLY_CODEMAO_URL;
    private BlocklyJsInterface mBlocklyJsInterface;

    private DirectionSensorEventListener mListener;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private String mDirection = DeviceDirectionEnum.NONE.getValue();
    private List<String> actionList = new ArrayList<>();

    private boolean fromVideo = false;
    public static final String FROM_VIDEO = "fromVideo";
    public static final String SHOTCUT_NAME = "shotVideo";
    private boolean isLoadFinish = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        if(BlueClientUtil.getInstance().getConnectionState() == 3){
            mPresenter.register(this);
            mPresenter.getActionList();
        }
        fromVideo =  getIntent().getBooleanExtra(FROM_VIDEO, false);
        init();
    }

    private void init() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER );
        BaseLoadingDialog.show(this);
        initWebView();
        if(mListener == null){
            mListener = new DirectionSensorEventListener(this);
            mSensorManager.registerListener(mListener,mSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
        listUserProgram();

        rlGoVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!rlGoVideo.isDrag()){
                    finish();
                }

            }
        });

    }

    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        //开发稳定后需去掉该行代码
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setUseWideViewPort(true);  //将图片调整到适合webview的大小
        mWebView.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        new HeightVisibleChangeListener(mWebView);

        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                BaseLoadingDialog.dismiss(BlocklyActivity.this);
                isLoadFinish = true;

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isLoadFinish = true;
                BaseLoadingDialog.dismiss(BlocklyActivity.this);
                if(BlueClientUtil.getInstance().getConnectionState() == 3){

                    mPresenter.startOrStopRun((byte)0x01);
                    mPresenter.doReadInfraredSensor((byte)0x01);  //进入block如果连接蓝牙立马开始读取红外传感器数据
                    mPresenter.doRead6Dstate();
                    mPresenter.doReadTemperature((byte)0x01);

                    if(mWebView != null ){
                        //由于读取电量间隔时间1s中太长，有可能导致用户使用时还没将电量值上报给前端，导致低电量无法运行
                        mWebView.loadUrl("javascript:uploadLowPowerData(" + AppStatusUtils.getCurrentPower() + ")");
                    }

                }

                if(fromVideo){
                    rlGoVideo.setVisibility(View.VISIBLE);
                    Bitmap bitmap = BitmapFactory.decodeFile(BlocklyUtil.getPath() + SHOTCUT_NAME+ ".jpg");
                    if(bitmap != null){
                        ivShotAlbum.setImageBitmap(bitmap);
                    }

                }else{
                    rlGoVideo.setVisibility(View.GONE);
                }

            }

        };

        mWebView.setWebViewClient(webViewClient);

        mBlocklyJsInterface = new BlocklyJsInterface(BlocklyActivity.this);
        mWebView.addJavascriptInterface(mBlocklyJsInterface, "blocklyObj");
        try {
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = mWebView.getSettings().getClass();
                Method method = clazz.getMethod(
                        "setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(mWebView.getSettings(), true);
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        mWebView.loadUrl(URL);
    }

    @Override
    public int getContentViewId() {
        return R.layout.block_activity_blockly;
    }


    @Override
    public void getActionList(List<String> actionList) {
        ViseLog.d("actionList:" + actionList.toString());
        this.actionList = actionList;
        if(mWebView != null){
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    ViseLog.d("checkBlueConnectState getActionList");
                    mWebView.loadUrl("javascript:checkBlueConnectState()");
                }
            });
        }

    }

    @Override
    public void notePlayFinish(String actionName) {
        ViseLog.d("notePlayFinish:" + actionName);
        if(actionName.equals("default") || actionName.equals("初始化")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:continueSteps()");
                }
            });
        }else{
            mPresenter.playAction("初始化");
        }
    }

    @Override
    public void playEmojiFinish() {
        showEmoji = false;
        if(mWebView != null){
            ViseLog.d( "play sound or emoji finish!");
            mWebView.loadUrl("javascript:continueSteps()");
//                        mWebView.loadUrl("javascript:playSoundEffectFinish()");
        }
    }

    @Override
    public void playSoundFinish() {
        playSoundAudio = false;
        if(mWebView != null){
            ViseLog.d( "play sound or emoji finish!");
            mWebView.loadUrl("javascript:continueSteps()");
//                        mWebView.loadUrl("javascript:playSoundEffectFinish()");
        }
    }

    @Override
    public void doWalkFinish() {
        playRobotAction("初始化");
    }

    @Override
    public void infraredSensor(final int state) {
        if(mWebView != null ){
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    ViseLog.d(TAG, "uploadInfraredSensorDistance:" + state);
                    mWebView.loadUrl("javascript:uploadInfraredSensorDistance(" + state + ")");
                }
            });
        }
    }

    @Override
    public void read6DState(final int state) {
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                ViseLog.d(TAG, "gesture:" + state);
                mWebView.loadUrl("javascript:robotPostture(" + state + ")");
            }
        });
    }

    @Override
    public void tempState(final String state) {
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                ViseLog.d(TAG, "humidity:" + state);
                mWebView.loadUrl("javascript:humidity(" + state + ")");
            }
        });
    }

    @Override
    public void updatePower(int power) {
        ViseLog.d("updatePower:" + power);
        if(mWebView != null && isLoadFinish){
            mWebView.loadUrl("javascript:uploadLowPowerData(" + power + ")");
        }
    }

    @Override
    public void lostBT() {

        if(mPresenter != null){
            mPresenter.unRegister();
        }

        if(mWebView != null){
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    ViseLog.d(" checkBlueConnectState Bluetooth disconnect");
                    mWebView.loadUrl("javascript:checkBlueConnectState()");
                }
            });
        }

    }

    @Override
    public void noteTapHead() {
        if(isLoadFinish && mWebView != null){
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    ViseLog.d("tapped robot head and stop block run!");
                    mWebView.loadUrl("javascript:robotTouched()");
                }
            });

        }
    }

    @Override
    public void noteRobotFallDown(int state) {
        ViseLog.d("noteRobotFallDown:" + state);
        if(state == 3){
            callJavascript(true);
        }else{
            callJavascript(false);
        }
    }

    public void callJavascript( boolean status){

        final String js = "javascript:eventJudgmentForApp(" + status + ")" ;
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                ViseLog.d("callJavascript:" + js);
                mWebView.loadUrl(js);
            }
        });
    }

    public void playRobotAction(String name) {
        ViseLog.d("playRobotAction:" + name);
        mPresenter.playAction(name);
    }

    public void stopPlay(){
        mPresenter.stopAction();
    }

    public List<String> getActionList(){
        return actionList;
    }

    public void connectBluetooth(){
        ARouter.getInstance().build(ModuleUtils.Bluetooh_BleStatuActivity).withInt(Constant1E.ENTER_BLESTATU_ACTIVITY, 2).navigation(this, 1);
    }

    public boolean isBlueToothConnected(){
        if(BlueClientUtil.getInstance().getConnectionState() == 3){
            return true;
        }else{
            return  false;
        }
    }


    public void checkRobotFall() {
        if(mPresenter != null) {
            mPresenter.doReadRobotFallState();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unRegister();
        if(BlueClientUtil.getInstance().getConnectionState() == 3){
            mPresenter.doReadInfraredSensor((byte)0x00);
            mPresenter.startOrStopRun((byte)0x02);
            mPresenter.doReadTemperature((byte)0x00);
        }
        destroyWebView();
    }

    public void destroyWebView() {

        if(mWebView != null) {
            mWebView.removeAllViews();
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.freeMemory();
            mWebView.destroy();
            mWebView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing
        }

    }

    @Override
    public void onShake(long timeStamp, float[] value) {
        mDirection = DeviceDirectionEnum.SWING.getValue();
    }

    @Override
    public void onLeftUp() {
        mDirection = DeviceDirectionEnum.LEFT.getValue();
    }

    @Override
    public void onRightUp() {
        mDirection = DeviceDirectionEnum.RIGHT.getValue();
    }

    @Override
    public void onTopUp() {
        mDirection = DeviceDirectionEnum.UP.getValue();
    }

    @Override
    public void onBottomUp() {
        mDirection = DeviceDirectionEnum.DOWN.getValue();
    }

    @Override
    public void onDirectionNone() {
        mDirection = DeviceDirectionEnum.NONE.getValue();
    }

    public String getmDirection(){
        return mDirection;
    }

    /**
     * 显示表情
     * @param params
     */
    private boolean showEmoji = false;
    public void showEmoji(String params) {
        if(!showEmoji){
            showEmoji = true;
            mPresenter.playEmoji(params);
        }

    }

    private boolean playSoundAudio = false;
    public void playSoundAudio(String params){
        if(!playSoundAudio){
            playSoundAudio = true;
            mPresenter.playSound(params);
        }

    }

    public void doWalk(byte direct, byte speed, byte[] step) {
        if(mPresenter != null){
            mPresenter.doWalk(direct, speed, step);
        }
    }

    public void stopPlayAudio(){
        mPresenter.stopPlayAudio();
        mPresenter.stopPlayEmoji();
        mPresenter.stopLedLight();
    }

    public static final String LED_NORMAL = "normal";
    public static final String LED_FAST = "fast";
    public static final String LED_SLOW = "slow";
    public static final String LED_OFF = "off";
    public static final String RED = "red";
    public static final String ORANGE = "orange";
    public static final String YELLOW = "yellow";
    public static final String GREEN = "green";
    public static final String CYAN = "cyan";
    public static final String BLUE = "blue";
    public static final String PURPLE = "purple";
    public static final String WHITE = "white";

    LedColorEnum colorEnum = LedColorEnum.WHITE;
    public void setLedLight(String status){
        try {
            if(status != null){
                JSONObject jsonObject = new JSONObject(status);
                String state = jsonObject.getString("state");
                String color = jsonObject.getString("EyeColor");
                String time = jsonObject.getString("time");
                byte [] params = new byte[5];
                if(state.equalsIgnoreCase(LED_NORMAL)){
                    params[0] = (byte)0x01;
                }else if(state.equalsIgnoreCase(LED_FAST)){
                    params[0] = (byte)0x02;
                }else if(state.equalsIgnoreCase(LED_SLOW)){
                    params[0] = (byte)0x03;
                }else if(state.equalsIgnoreCase(LED_OFF)){
                    params[0] = (byte)0x04;
                }

                if(color.equalsIgnoreCase(RED)) {
                    colorEnum = LedColorEnum.RED;
                }else if(color.equalsIgnoreCase(ORANGE)) {
                    colorEnum = LedColorEnum.ORANGE;
                }else if(color.equalsIgnoreCase(YELLOW)) {
                    colorEnum = LedColorEnum.YELLOW;
                }else if(color.equalsIgnoreCase(GREEN)) {
                    colorEnum = LedColorEnum.GREEN;
                }else if(color.equalsIgnoreCase(CYAN)) {
                    colorEnum = LedColorEnum.CYAN;
                }else if(color.equalsIgnoreCase(BLUE)) {
                    colorEnum = LedColorEnum.BLUE;
                }else if(color.equalsIgnoreCase(PURPLE)) {
                    colorEnum = LedColorEnum.PURPLE;
                }else if(color.equalsIgnoreCase(WHITE)) {
                    colorEnum = LedColorEnum.WHITE;
                }

                params[1] = ByteHexHelper.intToHexByte(colorEnum.getR());
                params[2] = ByteHexHelper.intToHexByte(colorEnum.getG());
                params[3] = ByteHexHelper.intToHexByte(colorEnum.getB());

                params[4] = ByteHexHelper.intToHexByte(Integer.valueOf(time));
                mPresenter.playLedLight(params);

            }
        }catch (JSONException e){
            ViseLog.d(e.getMessage());
        }

    }



    public void saveUserProgram(final String pid, final String programName, final String programData){

        BlocklySaveMode saveMode = new BlocklySaveMode();
        saveMode.setProgramName(programName);
        saveMode.setProgramData(programData);
        List<BlocklySaveMode> list = new ArrayList<BlocklySaveMode>();
        list.add(saveMode);
        BlocklyProjectRequest request = new BlocklyProjectRequest();
        UserInfoModel userInfoModel = (UserInfoModel) SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
        request.setUserId(userInfoModel.getUserId());
        request.setToken(SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN));
        request.setList(list);

        ViseHttp.BASE(new PostRequest(BlockHttpEntity.SAVE_USER_PROGRAM)
                .setJson(GsonImpl.get().toJson(request)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ViseLog.d(TAG, "saveUserProgram onResponse:" + response);
                        BaseResponseModel<BlocklyRespondMode> baseResponseModel = GsonImpl.get().toObject(response,
                                new TypeToken<BaseResponseModel<List<BlocklyRespondMode>>>() {
                                }.getType());

                        List<BlocklyRespondMode> blocklyRespondModeList = new ArrayList<BlocklyRespondMode>();

                        if (baseResponseModel.status) {

                            blocklyRespondModeList = (List<BlocklyRespondMode>) baseResponseModel.models;

                            ViseLog.d(TAG, "blocklyRespondMode:" + blocklyRespondModeList.toString());

                            for(int i= 0; i < blocklyRespondModeList.size(); i++ ){
                                BlocklyProjectMode blocklyProjectMode = new BlocklyProjectMode();
                                blocklyProjectMode.setPid(blocklyRespondModeList.get(i).getId());
                                blocklyProjectMode.setUserId(blocklyRespondModeList.get(i).getUserId());
                                blocklyProjectMode.setProgramName(blocklyRespondModeList.get(i).getProgramName());
                                blocklyProjectMode.setProgramData(blocklyRespondModeList.get(i).getProgramData());
                                blocklyProjectMode.setDelState(false);
                                blocklyProjectMode.setServerState(true);

                                blocklyProjectMode.save();
                            }



                        }
                    }

                    @Override
                    public void onFail(int i, String s) {
                        ViseLog.e( "saveUserProgram onError e:" + s);
                        BlocklyProjectMode blocklyProjectMode = new BlocklyProjectMode();
                        blocklyProjectMode.setPid(pid);
                        UserInfoModel userInfoModel = (UserInfoModel)SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
                        blocklyProjectMode.setUserId(userInfoModel.getUserId());
                        blocklyProjectMode.setProgramName(programName);
                        blocklyProjectMode.setProgramData(programData);
                        blocklyProjectMode.setDelState(false);
                        blocklyProjectMode.setServerState(false);

                        blocklyProjectMode.saveOrUpdate("pid = ?", pid);
                    }
                });

    }


    public void listUserProgram() {
        BaseRequest baseRequest = new BaseRequest();
        UserInfoModel userInfoModel = (UserInfoModel)SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
        baseRequest.setUserId(userInfoModel.getUserId());
        baseRequest.setToken(SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN));

        ViseHttp.BASE(new PostRequest(BlockHttpEntity.GET_USER_PROGRAM)
                .setJson(GsonImpl.get().toJson(baseRequest)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ViseLog.d(TAG, "listUserProgram onResponse:" + response);
                        BaseResponseModel<BlocklyRespondMode> baseResponseModel = GsonImpl.get().toObject(response,
                                new TypeToken<BaseResponseModel<List<BlocklyRespondMode>>>() {
                                }.getType());

                        List<BlocklyRespondMode> blocklyRespondModeList = new ArrayList<BlocklyRespondMode>();

                        if (baseResponseModel.status) {

                            blocklyRespondModeList = (List<BlocklyRespondMode>) baseResponseModel.models;

                            ViseLog.d(TAG, "listUserProgram blocklyRespondMode:" + blocklyRespondModeList.toString());

                            for(int i= 0; i < blocklyRespondModeList.size(); i++ ){
                                BlocklyProjectMode blocklyProjectMode = new BlocklyProjectMode();
                                blocklyProjectMode.setPid(blocklyRespondModeList.get(i).getId());
                                blocklyProjectMode.setUserId(blocklyRespondModeList.get(i).getUserId());
                                blocklyProjectMode.setProgramName(blocklyRespondModeList.get(i).getProgramName());
                                blocklyProjectMode.setProgramData(blocklyRespondModeList.get(i).getProgramData());
                                blocklyProjectMode.setDelState(false);
                                blocklyProjectMode.setServerState(true);

                                blocklyProjectMode.saveOrUpdate("pid = ?", blocklyRespondModeList.get(i).getId());
                            }



                        }
                    }

                    @Override
                    public void onFail(int i, String s) {
                        ViseLog.e( "listUserProgram onError:" + s);
                    }
                });


    }


    public void deleteUserProgram(final String[] programIds) {


        List<String> ids = new ArrayList<String>();
        for(int i =0; i < programIds.length; i++){
            ids.add(i, programIds[i]);
        }

        BlocklyProjectDelRequest blocklyProjectDelRequest = new BlocklyProjectDelRequest();
        blocklyProjectDelRequest.setProgramIds(ids);
        blocklyProjectDelRequest.setToken(SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN));
        UserInfoModel userInfoModel = (UserInfoModel)SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
        blocklyProjectDelRequest.setUserId(userInfoModel.getUserId());
        ViseHttp.BASE(new PostRequest(BlockHttpEntity.DEL_USER_PROGRAM)
                .setJson(GsonImpl.get().toJson(blocklyProjectDelRequest)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ViseLog.d(TAG, "deleteUserProgram onResponse:" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if((boolean)jsonObject.get("status")){
                                JSONArray jsonArray = jsonObject.getJSONArray("models");
                                for(int i = 0; i<jsonArray.length(); i++){
                                    String pid = jsonArray.get(i).toString();
                                    DataSupport.deleteAll(BlocklyProjectMode.class, "pid = ?", pid);
                                }
                                if(mWebView != null){
                                    mWebView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            ViseLog.d(TAG, "projectIsOver");
                                            mWebView.loadUrl("javascript:projectIsOver()");
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(int e, String s) {
                        ViseLog.d(TAG, "deleteUserProgram onError:" + s);
                        for(int i = 0; i<programIds.length; i++){

                            DataSupport.deleteAll(BlocklyProjectMode.class, "pid = ?", programIds[i]);
                        }
                        if(mWebView != null){
                            mWebView.post(new Runnable() {
                                @Override
                                public void run() {
                                    ViseLog.d(TAG, "projectIsOver");
                                    mWebView.loadUrl("javascript:projectIsOver()");
                                }
                            });
                        }
                    }
                });




    }


    public void updateUserProgram(final String pid, final String programName, final String programData) {
        BlocklySaveMode saveMode = new BlocklySaveMode();
        saveMode.setProgramId(pid);
        saveMode.setProgramName(programName);
        saveMode.setProgramData(programData);
        List<BlocklySaveMode> list = new ArrayList<BlocklySaveMode>();
        list.add(saveMode);

        BlocklyProjectRequest request = new BlocklyProjectRequest();
        request.setList(list);
        UserInfoModel userInfoModel = (UserInfoModel)SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
        request.setUserId(userInfoModel.getUserId());
        request.setToken(SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN));
        ViseHttp.BASE(new PostRequest(BlockHttpEntity.UPDATE_USER_PROGRAM)
                .setJson(GsonImpl.get().toJson(request)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ViseLog.d(TAG, "updateUserProgram onResponse:" + response);
                        BaseResponseModel<BlocklyRespondMode> baseResponseModel = GsonImpl.get().toObject(response,
                                new TypeToken<BaseResponseModel<List<BlocklyRespondMode>>>() {
                                }.getType());

                        List<BlocklyRespondMode> blocklyRespondModeList = new ArrayList<BlocklyRespondMode>();

                        if (baseResponseModel.status) {

                            blocklyRespondModeList = (List<BlocklyRespondMode>) baseResponseModel.models;

                            ViseLog.d(TAG, "blocklyRespondMode:" + blocklyRespondModeList.toString());

                            for(int i= 0; i < blocklyRespondModeList.size(); i++ ){
                                BlocklyProjectMode blocklyProjectMode = new BlocklyProjectMode();
                                blocklyProjectMode.setPid(blocklyRespondModeList.get(i).getId());
                                blocklyProjectMode.setUserId(blocklyRespondModeList.get(i).getUserId());
                                blocklyProjectMode.setProgramName(blocklyRespondModeList.get(i).getProgramName());
                                blocklyProjectMode.setProgramData(blocklyRespondModeList.get(i).getProgramData());
                                blocklyProjectMode.setDelState(false);
                                blocklyProjectMode.setServerState(true);

                                blocklyProjectMode.saveOrUpdate("pid = ?", blocklyRespondModeList.get(i).getId());
                            }



                        }
                    }

                    @Override
                    public void onFail(int i, String s) {
                        ViseLog.d(TAG, "updateUserProgram onError e:" +s);

                        BlocklyProjectMode blocklyProjectMode = new BlocklyProjectMode();
                        blocklyProjectMode.setPid(pid);
                        UserInfoModel userInfoModel = (UserInfoModel) SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
                        blocklyProjectMode.setUserId(userInfoModel.getUserId());
                        blocklyProjectMode.setProgramName(programName);
                        blocklyProjectMode.setProgramData(programData);
                        blocklyProjectMode.setDelState(false);
                        blocklyProjectMode.setServerState(false);

                        blocklyProjectMode.saveOrUpdate("pid = ?", pid);
                    }
                });

    }



    public static class HeightVisibleChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {

        private WebView webview;
        public HeightVisibleChangeListener(WebView webView){
            this.webview = webView;
            webview.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        int lastHeight;
        int lastVisibleHeight;

        @Override
        public void onGlobalLayout() {
            Rect visible = new Rect();
            Rect size = new Rect();
            webview.getWindowVisibleDisplayFrame(visible);
            webview.getHitRect(size);

            int height = size.bottom-size.top;
            int visibleHeight = visible.bottom - visible.top;

            if(height == lastHeight && lastVisibleHeight == visibleHeight){
                return;
            }

            lastHeight = height;
            lastVisibleHeight = visibleHeight;
            int moveHeight = height - visibleHeight;
            ViseLog.d("height:" + height + "__visibleHeight:" + visibleHeight  + "_moveHeight:" + moveHeight
                    + "屏幕占比:" + moveHeight/height);
            String ratio = cop(moveHeight, height);
            ViseLog.d("ratio:" +ratio);
            webview.loadUrl("javascript:phoneKeyborad(" + ratio + ")");
        }

        private String cop(int num1, int num2){
            NumberFormat numberFormat = NumberFormat.getInstance();

            // 设置精确到小数点后2位

            numberFormat.setMaximumFractionDigits(2);

            String result = numberFormat.format((float) num1 / (float) num2 );
            return  result;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ViseLog.d("onActivityResult:" + requestCode + "resultCode:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode == 1){
                if(BlueClientUtil.getInstance().getConnectionState() == BluetoothState.STATE_CONNECTED){
                    if(mPresenter != null ){
                        ViseLog.d("onActivityResult send cmd");
                        mPresenter.register(this);
                        mPresenter.getActionList();
                        mPresenter.doReadInfraredSensor((byte)0x01);
                        mPresenter.startOrStopRun((byte)0x01);
                        mPresenter.doRead6Dstate();
                        mPresenter.doReadTemperature((byte)0x01);
                    }
                }


                if(mWebView != null){
                    mWebView.post(new Runnable() {
                        @Override
                        public void run() {
                            ViseLog.d(" checkBlueConnectState onActivityResult Bluetooth disconnect");
                            mWebView.loadUrl("javascript:checkBlueConnectState()");
                        }
                    });
                }

            }
        }

    }




}
