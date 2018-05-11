package com.ubt.en.alpha1e.voiceCmd;

import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.en.alpha1e.R;
import com.vise.log.ViseLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/5/10 17:01
 * @描述:
 */

@Route(path = ModuleUtils.VolumeCmd_module)
public class VoiceCmdActivity extends AppCompatActivity {
    private static final String HELP_URL = "http://10.10.1.14:8080/alpha1e/voiceCommand.html";

    @BindView(R.id.iv_topbar_back)
    ImageView ivTopbarBack;
    @BindView(R.id.tv_topbar_title)
    TextView tvTopbarTitle;
    @BindView(R.id.tv_topbar_save)
    TextView tvTopbarSave;
    @BindView(R.id.cmd_web_content)
    WebView cmdWebContent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voicecmd);
        ButterKnife.bind(this);

        tvTopbarSave.setText("");

        BaseLoadingDialog.show(this);
        initWebView();
    }

    private void initWebView() {

        WebSettings webSettings = cmdWebContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBlockNetworkImage(false);//解决图片加载不出来的问题

        if (Build.VERSION.SDK_INT >= 19) {//4.4 ,小于4.4没有这个方法
            webSettings.setMediaPlaybackRequiresUserGesture(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                ViseLog.d("url = "+url);
                Uri uri = Uri.parse(url);
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数
                if (uri.getScheme().equals("js")) {
                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.getAuthority().equals("webview")) {

                        //  步骤3：
                        // 执行JS所需要调用的逻辑
                        // 可以在协议上带有参数并传递到Android上
                        String arg = uri.getQueryParameter("arg1");
                        cmdWebContent.loadUrl(HELP_URL);
                    }
                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                ViseLog.d("onReceivedSslError ");
                super.onReceivedSslError(view, handler, error);
                //webview 忽略证书
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                ViseLog.d("onReceivedError ");
                //6.0以下执行
                /*if (!isWebError) {
                    showErrorPage();
                    isWebError = true;
                }*/
            }

            //处理网页加载失败时
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                ViseLog.d("onReceivedError ");
                //6.0以上执行
               /* if (!isWebError) {
                    showErrorPage();
                    isWebError = true;
                }*/
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ViseLog.d("onPageFinished ");
                /*if (!isWebError) {
                    hideErrorPage();
                }
                isWebError = false;
                if (isRefreshing) {
                    isRefreshing = false;
                    imgNetError.clearAnimation();
                }*/
                BaseLoadingDialog.dismiss(VoiceCmdActivity.this);
            }
        };

        cmdWebContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title.contains("404")) {
                    /*if (!isWebError) {
                        showErrorPage();
                        isWebError = true;
                    }*/
                }
            }
        });
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    ViseLog.d("value= "+value);
                }
            });
        }*/
        cmdWebContent.setWebViewClient(webViewClient);
        cmdWebContent.loadUrl(HELP_URL);
    }

    @OnClick(R.id.iv_topbar_back)
    public void onViewClicked() {
        VoiceCmdActivity.this.finish();
    }
}
