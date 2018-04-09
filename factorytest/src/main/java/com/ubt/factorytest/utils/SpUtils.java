package com.ubt.factorytest.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ubt on 2017/12/15.
 */

public class SpUtils{
        private static SpUtils instance;
        private Context mContext;
        private static final String saveWifiPwd = "saveWifiPwd";
        private static final String factoryConf = "factoryConf";


        private SpUtils(){

        }

        public void init(Context context){
            this.mContext = context.getApplicationContext();
        }

        public static SpUtils getInstance(){
            if(instance == null){
                synchronized (SpUtils.class){
                    if(instance == null){
                        instance = new SpUtils();
                    }
                }
            }

            return instance;
        }

        public String getWifiPwd(String wifiName)
        {
            SharedPreferences sp = mContext.getSharedPreferences(saveWifiPwd, Context.MODE_PRIVATE);
            return sp.getString(wifiName, "");
        }

        public boolean clear()
        {
            SharedPreferences sp = mContext.getSharedPreferences(saveWifiPwd, Context.MODE_PRIVATE);
            return sp.edit().clear().commit();
        }



        public void putWifiPwd(String wifiName,String wifiPwd)
        {
            SharedPreferences sp = mContext.getSharedPreferences(saveWifiPwd, Context.MODE_PRIVATE);
            sp.edit().putString(wifiName, wifiPwd).apply();
        }

        public void saveFactoryConf(String conf){
            SharedPreferences sp = mContext.getSharedPreferences(factoryConf, Context.MODE_PRIVATE);
            sp.edit().putString(factoryConf, conf).apply();

        }

        public String getFactoryConf(){
            SharedPreferences sp = mContext.getSharedPreferences(factoryConf, Context.MODE_PRIVATE);
            return sp.getString(factoryConf, "");
        }

}
