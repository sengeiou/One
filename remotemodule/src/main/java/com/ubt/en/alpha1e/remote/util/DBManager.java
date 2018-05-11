package com.ubt.en.alpha1e.remote.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.ubt.baselib.utils.FileUtils;
import com.ubt.en.alpha1e.remote.model.RemoteInfo;
import com.ubt.en.alpha1e.remote.model.RemoteItem;
import com.vise.log.ViseLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author：liuhai
 * @date：2018/5/4 15:49
 * @modifier：ubt
 * @modify_date：2018/5/4 15:49
 * [A brief description]
 * version
 */

public class DBManager {
    Context context;
    private SQLiteDatabase db;

    public static enum ModelType {
        FOOTBALL_PLAYER, DANCER, FIGHTER, CUSTOM
    }

    //数据库的名称
    private String DB_NAME = "UbtLogs_20160506001";

    private static String SDCardPath = Environment
            .getExternalStorageDirectory().getPath();

    public String DB_PATH = "";

    public DBManager(Context context) {
        this.context = context;

        String catchPath = FileUtils.getCacheDirectory(context, "");
        DB_PATH = catchPath + "/db/";
        initFile();
        db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME,
                null, SQLiteDatabase.OPEN_READWRITE);
        //   DataSupport.saveAll(dbHelper.getPDQuestion());//把查到的数据保存到LitePal中，方便使用查询
        getRemoteInfoByModel(ModelType.FOOTBALL_PLAYER, false, "1");
    }


    private void initFile() {
        //判断数据库是否拷贝到相应的目录下
        if (new File(DB_PATH + DB_NAME).exists() == false) {
            File dir = new File(DB_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }
            //复制文件
            try {
                InputStream is = context.getAssets().open(DB_NAME);
                OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);
                byte[] buffer = new byte[1024];//用来复制文件
                int length;//保存已经复制的长度
                //开始复制
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                //刷新
                os.flush();
                //关闭
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public RemoteInfo getRemoteInfoByModel(ModelType modelType, boolean isCN, String model) {

        String str_get = "select log_btn_index,action_name,#,action_image_name from remote_info_logs left join remote_action_list on remote_info_logs.log_action_index = remote_action_list.action_index where log_model_index = $;";
        String _country = isCN ? "action_name_ch" : "action_name_en";
        String _model = null;
        if (model == null) {
            _model = getModelIndex(modelType) + "";
        } else {
            _model = model;
        }

        str_get = str_get.replace("#", _country);
        str_get = str_get.replace("$", _model);
        RemoteInfo info = new RemoteInfo();
        Cursor cursor = db.rawQuery(str_get, null);
        try {
            while (cursor.moveToNext()) {
                int btn_index = cursor.getInt(cursor.getColumnIndex("log_btn_index"));
                RemoteItem item = getItemByIndex(btn_index, info);
                item.hts_name = cursor.getString(cursor.getColumnIndex("action_name"));
                item.image_name = cursor.getString(cursor.getColumnIndex("action_image_name"));
                item.show_name = cursor.getString(cursor.getColumnIndex(_country));
                ViseLog.d(item.toString());
                //UbtLog.d(TAG,"更新遥控动作item.hts_name="+item.hts_name+"   item.image_name="+item.image_name+"  show_name="+item.show_name+"   btn_index="+btn_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    private int getModelIndex(ModelType type) {
        switch (type) {
            case FOOTBALL_PLAYER:
                return 1;
            case FIGHTER:
                return 2;
            case DANCER:
                return 3;
            case CUSTOM:
                return 4;
        }
        return -1;
    }

    public static RemoteItem getItemByIndex(int index, RemoteInfo info) {
        switch (index) {
            case 1:
                return info.do_up;
            case 2:
                return info.do_left;
            case 3:
                return info.do_right;
            case 4:
                return info.do_down;
            case 5:
                return info.do_to_left;
            case 6:
                return info.do_to_right;
            case 7:
                return info.do_1;
            case 8:
                return info.do_2;
            case 9:
                return info.do_3;
            case 10:
                return info.do_4;
            case 11:
                return info.do_5;
            case 12:
                return info.do_6;
        }
        return null;
    }

//    public List<RemoteRoleInfo> getAllRemoteRole() {
//
//        List<RemoteRoleInfo> result = new ArrayList<>();
//
//        String str_get = "select * from remote_gamepad_role";
//        ViseLog.d("查询所有的遥控器角色：" + str_get);
//        Cursor cursor = db.rawQuery(str_get, null);
//
//        try {
//            while (cursor.moveToNext()) {
//                RemoteRoleInfo info = ConvertToModel(cursor);
//                result.add(info);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//
//    private ContentValues ConvertToValue(RemoteRoleInfo model) {
//        ContentValues values = new ContentValues();
//        //values.put("roleid", model.roleid);
//        values.put("roleName", model.roleName);
//        values.put("roleIntroduction", model.roleIntroduction);
//        values.put("roleIcon", model.roleIcon);
//        values.put("bz", model.bz);
//
//        return values;
//    }
//
//    private RemoteRoleInfo ConvertToModel(Cursor cursor) {
//
//        RemoteRoleInfo model = new RemoteRoleInfo();
//
//        model.roleid = cursor.getInt(cursor.getColumnIndex("roleid"));
//        model.roleName = cursor.getString(cursor.getColumnIndex("roleName"));
//        model.roleIntroduction = cursor.getString(cursor.getColumnIndex("roleIntroduction"));
//        model.roleIcon = cursor.getString(cursor.getColumnIndex("roleIcon"));
//        model.bz = cursor.getString(cursor.getColumnIndex("bz"));
//        ViseLog.d(model.toString());
//        return model;
//    }
}
