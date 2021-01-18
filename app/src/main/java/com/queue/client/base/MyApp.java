package com.queue.client.base;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.queue.client.greendao.DaoMaster;
import com.queue.client.greendao.DaoSession;

public class MyApp extends Application {
    private static DaoSession sDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
//        Bugly.init(this, "2f39fdd248", false);
        initDb();
    }

    private void initDb() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "smartAdver.db", null);
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        sDaoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }

}
