package com.alcidae.smarthome.ir.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/27 17:48 1.0
 * @time 2018/3/27 17:48
 * @project ir_demo com.alcidae.smarthome.ir.data.db
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/27 17:48
 */

public class DbUtil extends SQLiteOpenHelper {
    private static final String DB = "IR.db";
    private static final int VERSION = 1;
    private static DbUtil INSTANCE = null;

    public static DbUtil getInstance(Context c) {
        if (INSTANCE == null) {
            synchronized (DbUtil.class) {
                if (INSTANCE == null)
                    INSTANCE = new DbUtil(c);
            }
        }
        return INSTANCE;
    }

    private DbUtil(Context context) {
        super(context, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
