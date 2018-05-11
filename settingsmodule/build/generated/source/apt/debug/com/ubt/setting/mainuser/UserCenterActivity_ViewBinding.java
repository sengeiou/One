// Generated code from Butter Knife. Do not modify!
package com.ubt.setting.mainuser;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.ubt.setting.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UserCenterActivity_ViewBinding implements Unbinder {
  private UserCenterActivity target;

  private View view2131492972;

  @UiThread
  public UserCenterActivity_ViewBinding(UserCenterActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public UserCenterActivity_ViewBinding(final UserCenterActivity target, View source) {
    this.target = target;

    View view;
    target.mTvTitle = Utils.findRequiredViewAsType(source, R.id.tv_main_title, "field 'mTvTitle'", TextView.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.rl_leftmenu, "field 'mRecyclerView'", RecyclerView.class);
    target.mViewPager = Utils.findRequiredViewAsType(source, R.id.fl_main_content, "field 'mViewPager'", FrameLayout.class);
    view = Utils.findRequiredView(source, R.id.iv_main_back, "field 'mIvMainBack' and method 'onBack'");
    target.mIvMainBack = Utils.castView(view, R.id.iv_main_back, "field 'mIvMainBack'", ImageView.class);
    view2131492972 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBack(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    UserCenterActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTvTitle = null;
    target.mRecyclerView = null;
    target.mViewPager = null;
    target.mIvMainBack = null;

    view2131492972.setOnClickListener(null);
    view2131492972 = null;
  }
}
