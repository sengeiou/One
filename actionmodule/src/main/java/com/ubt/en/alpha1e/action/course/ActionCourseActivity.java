package com.ubt.en.alpha1e.action.course;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.adapter.ActionCoursedapter;
import com.ubt.en.alpha1e.action.contact.ActionCourseContact;
import com.ubt.en.alpha1e.action.presenter.ActionCoursePrenster;
import com.vise.log.ViseLog;

/**
 * 课程列表页面
 */
public class ActionCourseActivity extends MVPBaseActivity<ActionCourseContact.View, ActionCoursePrenster> implements ActionCourseContact.View, BaseQuickAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private ImageView ivBack;
    ActionCoursedapter mActionCoursedapter;

    @Override
    public int getContentViewId() {
        ViseLog.d("=====getContentViewId======");
        int type = getIntent().getIntExtra("position", 1);
        return R.layout.action_activity_course;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ViseLog.d("=====onCreate======");
        super.onCreate(savedInstanceState);
        mPresenter.init(this);
        BaseLoadingDialog.show(this, "Loading...");
        initView();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyleview_content);
        mActionCoursedapter = new ActionCoursedapter(R.layout.action_main_course_item, mPresenter.getActionCourseModels());
        mActionCoursedapter.setOnItemClickListener(this);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = 30;
                outRect.left = 30;
                outRect.top = 50;
            }
        });

        mRecyclerView.setAdapter(mActionCoursedapter);

        ivBack = findViewById(R.id.iv_main_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void notifyDataChanged() {
        BaseLoadingDialog.dismiss(this);
        mActionCoursedapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(this, ActionLevelOneActivity.class));
    }
}
