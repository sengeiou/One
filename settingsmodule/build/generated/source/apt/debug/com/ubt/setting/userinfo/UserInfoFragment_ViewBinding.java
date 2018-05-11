// Generated code from Butter Knife. Do not modify!
package com.ubt.setting.userinfo;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.ubt.baselib.customView.ShapedImageView;
import com.ubt.setting.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UserInfoFragment_ViewBinding implements Unbinder {
  private UserInfoFragment target;

  private View view2131492967;

  private View view2131492995;

  private View view2131492952;

  private View view2131493114;

  private View view2131493115;

  @UiThread
  public UserInfoFragment_ViewBinding(final UserInfoFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.img_head, "field 'mImgHead' and method 'onClickView'");
    target.mImgHead = Utils.castView(view, R.id.img_head, "field 'mImgHead'", ShapedImageView.class);
    view2131492967 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickView(p0);
      }
    });
    target.mTvUserName = Utils.findRequiredViewAsType(source, R.id.edit_user_name, "field 'mTvUserName'", EditText.class);
    view = Utils.findRequiredView(source, R.id.male, "field 'mMale' and method 'onRadioCheck'");
    target.mMale = Utils.castView(view, R.id.male, "field 'mMale'", RadioButton.class);
    view2131492995 = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.onRadioCheck(p0, p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.female, "field 'mFemale' and method 'onRadioCheck'");
    target.mFemale = Utils.castView(view, R.id.female, "field 'mFemale'", RadioButton.class);
    view2131492952 = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.onRadioCheck(p0, p1);
      }
    });
    target.mRadioGroupSex = Utils.findRequiredViewAsType(source, R.id.radio_group_sex, "field 'mRadioGroupSex'", RadioGroup.class);
    view = Utils.findRequiredView(source, R.id.tv_user_age, "field 'mTvUserAge' and method 'onClickView'");
    target.mTvUserAge = Utils.castView(view, R.id.tv_user_age, "field 'mTvUserAge'", TextView.class);
    view2131493114 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickView(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_user_grade, "field 'mTvUserGrade' and method 'onClickView'");
    target.mTvUserGrade = Utils.castView(view, R.id.tv_user_grade, "field 'mTvUserGrade'", TextView.class);
    view2131493115 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickView(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    UserInfoFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mImgHead = null;
    target.mTvUserName = null;
    target.mMale = null;
    target.mFemale = null;
    target.mRadioGroupSex = null;
    target.mTvUserAge = null;
    target.mTvUserGrade = null;

    view2131492967.setOnClickListener(null);
    view2131492967 = null;
    ((CompoundButton) view2131492995).setOnCheckedChangeListener(null);
    view2131492995 = null;
    ((CompoundButton) view2131492952).setOnCheckedChangeListener(null);
    view2131492952 = null;
    view2131493114.setOnClickListener(null);
    view2131493114 = null;
    view2131493115.setOnClickListener(null);
    view2131493115 = null;
  }
}
