package com.ubt.blockly;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.blockly.main.BlocklyActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */

@Route(path = ModuleUtils.Blockly_BlocklyProgram)
public class BlocklyMainActivity extends MVPBaseActivity<BlocklyMainContract.View, BlocklyMainPresenter> implements BlocklyMainContract.View{

    Unbinder unbinder;
    @BindView(R2.id.block_iv_back)
    ImageView ivBack;
    @BindView(R2.id.rl_block_create)
    RelativeLayout rlBlockCreate;
    @BindView(R2.id.rl_block_work)
    RelativeLayout rlBlockWork;
    @BindView(R2.id.rl_block_download)
    RelativeLayout rlBlockDownload;
    @BindView(R2.id.rl_block_make)
    RelativeLayout rlBlockMake;

    @Override
    public int getContentViewId() {
        return R.layout.block_activity_main;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick({R2.id.block_iv_back, R2.id.rl_block_create, R2.id.rl_block_work, R2.id.rl_block_download, R2.id.rl_block_make})
    public void onClickView(View view){
        int i = view.getId();
        if (i == R.id.block_iv_back) {
            finish();

        } else if (i == R.id.rl_block_create) {
            startActivity(new Intent(this, BlocklyActivity.class));

        } else if (i == R.id.rl_block_work) {
        } else if (i == R.id.rl_block_download) {
        } else if (i == R.id.rl_block_make) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
