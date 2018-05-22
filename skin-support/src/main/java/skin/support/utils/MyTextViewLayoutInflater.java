package skin.support.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import skin.support.app.SkinLayoutInflater;
import skin.support.widget.SkinCompatButton;
import skin.support.widget.SkinCompatTextView;

/**
 * @author：liuhai
 * @date：2018/4/3 16:16
 * @modifier：ubt
 * @modify_date：2018/4/3 16:16
 * [A brief description]
 * version
 */

public class MyTextViewLayoutInflater implements SkinLayoutInflater {
    @Override
    public View createView(@NonNull Context context, String name, @NonNull AttributeSet attrs) {
        View view = null;
        switch (name) {

            case "TextView":
                view = new SkinCompatTextView(context, attrs);
                break;
            case "Button":
                view = new SkinCompatButton(context, attrs);

        }
        return view;
    }
}