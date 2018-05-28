package com.ubt.en.alpha1e.action.course;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseBTDisconnectDialog;
import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
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

    @Override
    protected void onResume() {
        super.onResume();
        AppStatusUtils.setBtBussiness(true);
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
        if (BlueClientUtil.getInstance().getConnectionState() == BluetoothState.STATE_CONNECTED) {
            ActionLevelCourseActivity.launchActivity(this, position + 1);
        } else {
            BaseBTDisconnectDialog.getInstance().show(new BaseBTDisconnectDialog.IDialogClick() {
                @Override
                public void onConnect() {
                    ARouter.getInstance().build(ModuleUtils.Bluetooh_BleStatuActivity).navigation();
                    BaseBTDisconnectDialog.getInstance().dismiss();
                }

                @Override
                public void onCancel() {
                    BaseBTDisconnectDialog.getInstance().dismiss();
                }
            });
        }
    }

    private static final int REQUESTCODE = 10000;

    // 为了获取结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (resultCode == 1) {
            if (requestCode == REQUESTCODE) {
                //设置结果显示框的显示数值
                ViseLog.d("课程完成通过");
                int resulttype = data.getIntExtra("resulttype", 0);
                if (resulttype == 0) {
                    int course = data.getIntExtra("course", 1);
                    int leavel = data.getIntExtra("leavel", 1);
                    boolean isComplete = data.getBooleanExtra("isComplete", false);
                    int score = data.getIntExtra("score", 0);
                    if (course < 10) {
                        mPresenter.getActionCourseModels().get(course).setActionLockType(1);
                    }
                    mPresenter.getActionCourseModels().get(course - 1).setActionCourcesScore(1);
                    mActionCoursedapter.notifyDataSetChanged();
                    mPresenter.saveCourseProgress(String.valueOf(course), "1");

                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStatusUtils.setBtBussiness(false);
    }
}
