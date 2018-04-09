package com.ubt.factorytest.test.recycleview;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ubt.factorytest.R;

import java.util.List;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/11/16 18:23
 * @描述:
 */

public class TestItemClickAdapter extends BaseMultiItemQuickAdapter<TestClickEntity, BaseViewHolder>{

    public TestItemClickAdapter(List<TestClickEntity> data) {
        super(data);
        addItemType(TestClickEntity.CLICK_ITEM_VIEW, R.layout.item_click_view);
        addItemType(TestClickEntity.CLICK_ITEM_SPEAKER, R.layout.item_click_speaker);
        addItemType(TestClickEntity.CLICK_ITEM_MIC, R.layout.item_click_mictest);
    }

    @Override
    protected void convert(BaseViewHolder helper, TestClickEntity item) {
        if(item.getTestID() == TestClickEntity.TEST_ITEM_ROBOTRESET ||
                item.getTestID() == TestClickEntity.TEST_ITEM_SAVETESTPROFILE ){
            helper.setVisible(R.id.btn_ok, false);
            helper.setVisible(R.id.btn_fail, false);
            helper.setVisible(R.id.tv_suc, false);

        }else {
            helper.setVisible(R.id.btn_ok, true);
            helper.setVisible(R.id.btn_fail, true);
            helper.setVisible(R.id.tv_suc, true);
            if (!item.isTested()) {
                helper.addOnClickListener(R.id.btn_ok)
                        .addOnClickListener(R.id.btn_fail);
            } else {
                if (item.isPass()) { //通过之后 隐藏失败按钮，通过按钮禁止点击
                    helper.setVisible(R.id.btn_fail, false);
                    helper.getView(R.id.btn_ok).setClickable(false);
                } else {
                    helper.setVisible(R.id.btn_ok, false);
                    helper.getView(R.id.btn_fail).setClickable(false);
                }
            }
        }


        switch (helper.getItemViewType()) {
            case TestClickEntity.CLICK_ITEM_VIEW:
                helper.setImageResource(R.id.imageView,item.getImgID())
                .setText(R.id.tv_item, item.getTestItem())
                .setText(R.id.tv_result, item.getTestResult());
                break;
            case TestClickEntity.CLICK_ITEM_SPEAKER:
                helper  .addOnClickListener(R.id.btn_vol_sub)
                        .addOnClickListener(R.id.btn_vol_add)
                        .setImageResource(R.id.imageView,item.getImgID())
                        .setText(R.id.tv_item, item.getTestItem())
                        .setText(R.id.tv_result, item.getTestResult());
                break;
            case TestClickEntity.CLICK_ITEM_MIC:

                helper  .addOnClickListener(R.id.btn_mic_stop)
                        .setImageResource(R.id.imageView,item.getImgID())
                        .setText(R.id.tv_item, item.getTestItem())
                        .setText(R.id.tv_result, item.getTestResult());
                break;
        }
    }

}
