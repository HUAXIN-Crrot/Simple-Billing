package com.example.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.example.myapplication.R;



public class DBOopenHelper  extends SQLiteOpenHelper {
    public DBOopenHelper(@Nullable Context context) {
        super(context, "tally.db", null, 1);
    }

    // 创建数据库的方法，只有项目第一次运行时会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表示类型的表
        String sql = "create table typetb(id integer primary key autoincrement,typename varchar(10),imageId integer,sImageId integer,kind integer)";
        db.execSQL(sql);
        insertType(db);
        //创建记账表
        sql = "create table accounttb(id integer primary key autoincrement,typename varchar(10),sImageId integer,beizhu varchar(80),money float," +
                "time varchar(60),year integer,month integer,day integer,kind integer)";
        db.execSQL(sql);
    }

    private void insertType(SQLiteDatabase db) {
        // 向typetb表中插入元素
        String sql = "insert into typetb (typename,imageId,sImageId,kind) values (?,?,?,?)";
        db.execSQL(sql,new Object[]{"其他", R.mipmap.ic_qita, R.mipmap.ic_qita_fs,0});
        db.execSQL(sql,new Object[]{"餐饮", R.mipmap.ic_canyin,R.mipmap.ic_canyin_fs,0});
        db.execSQL(sql,new Object[]{"交通", R.mipmap.ic_bus,R.mipmap.ic_bus_fs,0});
        db.execSQL(sql,new Object[]{"购物", R.mipmap.ic_shopping,R.mipmap.ic_shopping_fs,0});
        db.execSQL(sql,new Object[]{"服饰", R.mipmap.ic_cloth,R.mipmap.ic_cloth_fs,0});
        db.execSQL(sql,new Object[]{"日用品", R.mipmap.ic_day,R.mipmap.ic_day_fs,0});
        db.execSQL(sql,new Object[]{"娱乐", R.mipmap.ic_amuse,R.mipmap.ic_amuse_fs,0});
        db.execSQL(sql,new Object[]{"零食", R.mipmap.ic_snack,R.mipmap.ic_snack_fs,0});
        db.execSQL(sql,new Object[]{"饮料", R.mipmap.ic_drink,R.mipmap.ic_drink_fs,0});
        db.execSQL(sql,new Object[]{"学习", R.mipmap.ic_study,R.mipmap.ic_study_fs,0});
        db.execSQL(sql,new Object[]{"医疗", R.mipmap.ic_hospital,R.mipmap.ic_hospital_fs,0});
        db.execSQL(sql,new Object[]{"住宅", R.mipmap.ic_live,R.mipmap.ic_live_fs,0});
        db.execSQL(sql,new Object[]{"水电煤", R.mipmap.ic_water,R.mipmap.ic_water_fs,0});
        db.execSQL(sql,new Object[]{"通讯", R.mipmap.ic_phone,R.mipmap.ic_phone_fs,0});
        db.execSQL(sql,new Object[]{"社交", R.mipmap.ic_hongbao,R.mipmap.ic_hongbao_fs,0});

        db.execSQL(sql,new Object[]{"其他", R.mipmap.in_qita,R.mipmap.in_qita_fs,1});
        db.execSQL(sql,new Object[]{"薪资", R.mipmap.in_gongzi,R.mipmap.in_gongzi_fs,1});
        db.execSQL(sql,new Object[]{"奖金", R.mipmap.in_jiangjin,R.mipmap.in_jiangjin_fs,1});
        db.execSQL(sql,new Object[]{"借入", R.mipmap.in_jieru,R.mipmap.in_jieru_fs,1});
        db.execSQL(sql,new Object[]{"收债", R.mipmap.in_shouzhai,R.mipmap.in_shouzhai_fs,1});
        db.execSQL(sql,new Object[]{"利息收入", R.mipmap.in_lixi,R.mipmap.in_lixi_fs,1});
        db.execSQL(sql,new Object[]{"投资回报", R.mipmap.in_touzi,R.mipmap.in_touzi_fs,1});
        db.execSQL(sql,new Object[]{"二手交易", R.mipmap.in_ershoujiaoyi,R.mipmap.in_ershoujiaoyi_fs,1});
        db.execSQL(sql,new Object[]{"意外所得", R.mipmap.in_yiwai,R.mipmap.in_yiwai_fs,1});
    }

    // 数据库版本在更新时发生改变会调用此方法

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
