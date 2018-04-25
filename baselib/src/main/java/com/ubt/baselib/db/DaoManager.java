package com.ubt.baselib.db;

import android.app.Application;
import android.content.Context;

/**
 * @author：liuhai
 * @date：2018/4/19 10:38
 * @modifier：ubt
 * @modify_date：2018/4/19 10:38
 * [A brief description]
 * version
 */

public class DaoManager {
    private static DaoManager Instance;

    private Context mContext;


    public static DaoManager getInstance() {
        if (Instance == null) {
            synchronized (DaoManager.class) {
                if (Instance == null) {
                    Instance = new DaoManager();
                }
            }
        }
        return Instance;
    }

    private DaoManager() {

    }

    public void init(Application application) {
        mContext = application.getApplicationContext();

    }

}
