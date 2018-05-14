package com.ubt.baselib.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ubt.baselib.R;
import com.vise.log.ViseLog;

/**
 * @author：liuhai
 * @date：2018/5/5 15:12
 * @modifier：ubt
 * @modify_date：2018/5/5 15:12
 * [A brief description]
 * version
 */

@SuppressLint("AppCompatCustomView")
public class PressImageView extends ImageView {


    public PressImageView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

        init(context, attrs);

    }


    public PressImageView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);

    }


    private void init(Context context, AttributeSet attrs) {

        //获取到自定义属性

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PressImageView);

        Drawable src_n = a.getDrawable(R.styleable.PressImageView_src_normal);

        Drawable src_s = a.getDrawable(R.styleable.PressImageView_src_press);

        a.recycle();//注意回收

        ViseLog.d("srcn==="+src_n+"   src_s=="+src_s);

        setImageDrawable(createDrawable(src_s, src_n));

    }


    public PressImageView(Context context) {

        this(context, null);

    }

    //创建drawable

    public Drawable createDrawable(Drawable press, Drawable normal) {

        StateListDrawable stateList = new StateListDrawable();

        int statePressed = android.R.attr.state_pressed;

        stateList.addState(new int[]{statePressed}, press);

        stateList.addState(new int[]{}, normal);

        return stateList;

    }

}

