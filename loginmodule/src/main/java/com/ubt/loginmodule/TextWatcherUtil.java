package com.ubt.loginmodule;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class TextWatcherUtil implements TextWatcher {

    private EditText editText;
    private TextWatcherListener textWatcherListener;

    public TextWatcherUtil(EditText editText, TextWatcherListener textWatcherListener){
        this.editText = editText;
        this.textWatcherListener = textWatcherListener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        int textLength = editable.length();
        if(textLength >0) {
            textWatcherListener.hasText();
        }else{
            textWatcherListener.noText();
        }

    }


    public interface TextWatcherListener {

        void hasText();
        void noText();


    }
}
