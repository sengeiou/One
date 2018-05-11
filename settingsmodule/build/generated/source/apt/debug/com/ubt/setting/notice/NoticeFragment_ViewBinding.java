// Generated code from Butter Knife. Do not modify!
package com.ubt.setting.notice;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ubt.setting.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NoticeFragment_ViewBinding implements Unbinder {
  private NoticeFragment target;

  @UiThread
  public NoticeFragment_ViewBinding(NoticeFragment target, View source) {
    this.target = target;

    target.mRecyclerviewNotice = Utils.findRequiredViewAsType(source, R.id.recyclerview_notice, "field 'mRecyclerviewNotice'", RecyclerView.class);
    target.mRefreshLayout = Utils.findRequiredViewAsType(source, R.id.refreshLayout, "field 'mRefreshLayout'", SmartRefreshLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    NoticeFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mRecyclerviewNotice = null;
    target.mRefreshLayout = null;
  }
}
