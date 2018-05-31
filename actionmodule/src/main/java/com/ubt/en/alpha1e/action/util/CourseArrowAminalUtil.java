package com.ubt.en.alpha1e.action.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

import com.ubt.baselib.utils.BitmapUtil;
import com.ubt.en.alpha1e.action.R;


/**
 * @author：liuhai
 * @date：2018/1/2 16:14
 * @modifier：ubt
 * @modify_date：2018/1/2 16:14
 * [A brief description]
 * version
 */

public class CourseArrowAminalUtil {

    /**
     * 执行指示动画
     *
     * @param flag
     * @param imageView
     * @param arrow
     */
    public static void startViewAnimal(boolean flag, ImageView imageView, int arrow) {
        AnimationDrawable animationDrawable = null;
        if (flag) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(arrow == 1 ? R.drawable.animal_right_arrow : R.drawable.animal_left_arrow);
            animationDrawable = (AnimationDrawable) imageView.getDrawable();
            animationDrawable.start();
        } else {
            imageView.setVisibility(View.GONE);
            animationDrawable = null;
            if (null != animationDrawable) {
                animationDrawable.stop();
            }
        }
    }

    /**
     * 执行指示动画
     *
     * @param flag
     * @param imageView
     * @param arrow
     */
    public static void startLegViewAnimal(boolean flag, ImageView imageView, int arrow) {
        AnimationDrawable animationDrawable = null;
        if (flag) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(arrow == 1 ? R.drawable.animal_baitui : R.drawable.animal_baidongzuo);
            animationDrawable = (AnimationDrawable) imageView.getDrawable();
            animationDrawable.start();
        } else {
            imageView.setVisibility(View.GONE);
            animationDrawable = null;
            if (null != animationDrawable) {
                animationDrawable.stop();
            }
        }
    }

    /**
     * 执行指示动画
     *
     * @param flag
     * @param mImageView
     * @param arrow
     */
    public static void startTwoLegViewAnimal(Context context, boolean flag, final ImageView mImageView, int arrow) {
        myAnimation animation = null;
        if (flag) {
            mImageView.setVisibility(View.VISIBLE);
            int[] images = new int[4];
            images[0] = R.drawable.baishuangshou_1;     //动画开始时的动画
            images[1] = R.drawable.baishuangshou_2;
            images[2] = R.drawable.baishuangshou_3;
            images[3] = R.drawable.baishuangshou_4;
            //动画结束时的画面

            int[] durations = new int[4];
            durations[0] = 100;   //事件触发后多长时间开始动画
            durations[1] = 100;
            durations[2] = 200;
            durations[3] = 300;

            animation = new myAnimation(mImageView, images, durations, context);
        } else {
            mImageView.setVisibility(View.GONE);
            animation = null;
        }
//        AnimationDrawable animationDrawable = null;
//        if (flag) {
//            imageView.setVisibility(View.VISIBLE);
//            // 通过逐帧动画的资源文件获得AnimationDrawable示例
//            animationDrawable = (AnimationDrawable) context.getResources().getDrawable(R.drawable.animal_baidongleg);
//            imageView.setBackgroundDrawable(animationDrawable);
//
//            animationDrawable = (AnimationDrawable) imageView.getDrawable();
//            if (animationDrawable.isRunning()) {    //当前AnimationDrawable是否正在播放
//                animationDrawable.stop();    //停止播放逐帧动画。
//            }
//            animationDrawable.start();
//        } else {
//            imageView.setVisibility(View.GONE);
//            if (null != animationDrawable) {
//                animationDrawable.stop();
//            }
//            animationDrawable = null;
//        }
    }

    public static class myAnimation {
        private ImageView mImageView;   //播方动画的相应布局
        private int[] mImageRes;
        private int[] durations;
        private Context mContext;

        public myAnimation(ImageView pImageView, int[] pImageRes,
                           int[] durations, Context context) {
            this.mImageView = pImageView;
            this.durations = durations;
            this.mImageRes = pImageRes;
            this.mContext = context;
            mImageView.setImageResource(mImageRes[1]);
            play(0);
        }

        private void play(final int pImageNo) {

            mImageView.postDelayed(new Runnable() {     //采用延迟启动子线程的方式
                @Override
                public void run() {
                    Bitmap bitmap = BitmapUtil.compressImage(mContext.getResources(), mImageRes[pImageNo], 2);
                    mImageView.setImageBitmap(bitmap);
                    if (pImageNo == mImageRes.length - 1) {
                        play(0);
                    } else {
                        play(pImageNo + 1);
                    }
                }
            }, durations[pImageNo]);

        }
    }
}
