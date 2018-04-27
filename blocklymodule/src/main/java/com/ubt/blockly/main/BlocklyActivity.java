package com.ubt.blockly.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ubt.baselib.globalConst.BaseHttpEntity;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.blockly.R;
import com.ubt.blockly.R2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */

public class BlocklyActivity extends MVPBaseActivity<BlocklyContract.View, BlocklyPresenter> implements BlocklyContract.View {

    @BindView(R2.id.blockly_webView)
    WebView mWebView;
    public static String URL = BaseHttpEntity.BLOCKLY_CODEMAO_URL;
    private BlocklyJsInterface mBlocklyJsInterface;

    private DirectionSensorEventListener mListener;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private String mDirection = DeviceDirectionEnum.NONE.getValue();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER );

        initWebView();

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
//        new HeightVisibleChangeListener(mWebView);

        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
          /*      if(HttpAddress.WebServiceAdderss.contains(HttpAddress.WebAddressDevelop)){
                    //webview 忽略证书
                    handler.proceed();
                }else {
                    super.onReceivedSslError(view, handler, error);
                }*/
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
       /*         isLoadFinish = true;
                loadError = true;
                if(!checkWalk){
                    dismissLoading();
                }

                if(rlBlank != null){
                    rlBlank.setVisibility(View.VISIBLE);
                }
                mWebView.setVisibility(View.INVISIBLE);

                if(mHelper != null && mHelper.isStartHibitsProcess()){
                    mHelper.showStartHibitsProcess(new IDismissCallbackListener() {
                        @Override
                        public void onDismissCallback(Object obj) {
                            UbtLog.d(TAG,"onDismissCallback = obj == " + obj);
                            if((boolean)obj){
                                //行为习惯流程未结束，退出当前流程
                                finish();
                            }
                        }
                    });
                }*/

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
      /*          isLoadFinish = true;
                if(!loadError){
                    if(rlBlank != null){
                        rlBlank.setVisibility(View.GONE);
                    }
                    mWebView.setVisibility(View.VISIBLE);
                }
                dismissLoading();
                if(isBulueToothConnected()){
                    UbtLog.d(TAG, "do start infrared!");
                    mSensorHelper.doReadInfraredSensor((byte)0x01);  //进入block如果连接蓝牙立马开始读取红外传感器数据
//                    mSensorHelper.doReadGyroData((byte)0x01);
//                    mSensorHelper.doReadAcceleration((byte)0x01);
                    mSensorHelper.doRead6DState();
                    mSensorHelper.doReadTemperature((byte)0x01);
                }

                if(isBulueToothConnected()){
                    mainHelper.doReadState();
                }

                UbtLog.d(TAG,"onPageFinished isLoadActionFinish = " + isLoadActionFinish);
                if(isLoadActionFinish){
                    if(isFromCourse){
                        mHandler.sendEmptyMessage(DO_SHOW_LESSON_TASK);
                    }
                    dismissLoading();
                }

                if(fromVideo && !loadError){
                    rlGoVideo.setVisibility(View.VISIBLE);
                    Bitmap bitmap = BitmapFactory.decodeFile(BlocklyUtil.getPath() + SHOTCUT_NAME+ ".jpg");
                    if(bitmap != null){
                        ivShotAlbum.setImageBitmap(bitmap);
                    }

                }else{
                    rlGoVideo.setVisibility(View.GONE);
                }
                if(mHelper != null && mHelper.isStartHibitsProcess()){
                    mHelper.showStartHibitsProcess(new IDismissCallbackListener() {
                        @Override
                        public void onDismissCallback(Object obj) {
                            UbtLog.d(TAG,"onDismissCallback = obj == " + obj);
                            if((boolean)obj){
                                //行为习惯流程未结束，退出当前流程
                                finish();
                            }
                        }
                    });
                }*/
            }

        };

        mWebView.setWebViewClient(webViewClient);

        mBlocklyJsInterface = new BlocklyJsInterface(BlocklyActivity.this);
        mWebView.addJavascriptInterface(mBlocklyJsInterface, "blocklyObj");
//        mWebView.loadUrl(URL);
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

//        mWebView.loadUrl("file:///"+getBlocklyPath(this));
        mWebView.loadUrl(URL);
    }

    @Override
    public int getContentViewId() {
        return R.layout.block_activity_blockly;
    }
}
