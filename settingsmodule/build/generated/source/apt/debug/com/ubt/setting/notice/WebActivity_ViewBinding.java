// Generated code from Butter Knife. Do not modify!
package com.ubt.setting.notice;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ubt.setting.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WebActivity_ViewBinding implements Unbinder {
  private WebActivity target;

  @UiThread
  public WebActivity_ViewBinding(WebActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WebActivity_ViewBinding(WebActivity target, View source) {
    this.target = target;

    target.mTvTitle = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'mTvTitle'", TextView.class);
    target.mIbReturn = Utils.findRequiredViewAsType(source, R.id.ib_return, "field 'mIbReturn'", ImageButton.class);
    target.mRlTile = Utils.findRequiredViewAsType(source, R.id.rl_tile, "field 'mRlTile'", RelativeLayout.class);
    target.mWebContent = Utils.findRequiredViewAsType(source, R.id.web_content, "field 'mWebContent'", WebView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WebActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTvTitle = null;
    target.mIbReturn = null;
    target.mRlTile = null;
    target.mWebContent = null;
  }
}
