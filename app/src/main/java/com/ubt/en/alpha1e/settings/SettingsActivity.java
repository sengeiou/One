package com.ubt.en.alpha1e.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ubt.en.alpha1e.R;


/**
 * @author ubt
 *         <p>
 *         设置多语言
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //configFragment();
    }

    private void configFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .commitAllowingStateLoss();
    }


//    public void night1(View view) {
//        SkinManager.getInstance().loadSkin("zh_cn",null);
//
//    }
//
//    public void night2(View view) {
//        SkinManager.getInstance().loadSkin("zh_tw");
//    }
//
//    public void restore(View view) {
//        SkinManager.getInstance().restoreDefaultTheme();
//    }
//
//    public void night3(View view) {
//        SkinManager.getInstance().loadSkin("ja");
//
//    }
}
