package com.example.myapplication;

import android.app.Application;
import com.example.myapplication.db.DBManager;
/* 全局应用类 */

public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        DBManager.initDB(getApplicationContext());
    }
}
