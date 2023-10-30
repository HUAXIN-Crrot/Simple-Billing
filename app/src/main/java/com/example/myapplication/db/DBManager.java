package com.example.myapplication.db;
/*
*负责管理数据库的类
* 对表中内容进行增删改查
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static SQLiteDatabase db;
    //初始化数据库
    public static void initDB(Context context){
        DBOopenHelper helper = new DBOopenHelper(context); //得到帮助类对象
        db = helper.getWritableDatabase(); //得到数据库对象
    }
    //读取数据库中数据，写入内存集合
    public static List<TypeBean> getTypeList(int kind){
        List<TypeBean> list = new ArrayList<>();
        //读取typetb表中数据
        String sql = "select * from typetb where kind = " + kind;
        Cursor cursor = db.rawQuery(sql, null);
        //循环读取游标内容，存储到对象中
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndexOrThrow("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind);
            list.add(typeBean);
        }
        return list;
    }

    //向记账表当中插入一条元素
    public static void insertToAccounttb(AccountBean bean){
        ContentValues values = new ContentValues();
        values.put("typename",bean.getTypename());        //typename
        values.put("sImageId",bean.getsImageId());        //sImageId
        values.put("beizhu",bean.getBeizhu());            //beizhu
        values.put("money",bean.getMoney());              //money
        values.put("time",bean.getTime());                //time
        values.put("year",bean.getYear());                //year
        values.put("month",bean.getMonth());              //month
        values.put("day",bean.getDay());                  //day
        values.put("kind",bean.getKind());                //kind
        db.insert("accounttb",null,values);  //accounttb
    }

    //获取记账表中某一天的所有支出或收入情况
    public static List<AccountBean> getAccountListOneDayFromAccounttb(int year, int month,int day){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndexOrThrow("beizhu"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            AccountBean bean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(bean);
        }
        return list;
    }

    //获取记账表中某一月的所有支出或收入情况
    public static List<AccountBean> getAccountListOneMonthFromAccounttb(int year, int month){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndexOrThrow("beizhu"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            AccountBean bean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(bean);
        }
        return list;
    }


    //获取每一天的支出或者收入的总金额 kind: 支出==0 收入==1
    public static float getSumMoneyOneday(int year,int month,int day,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        //遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }

    //获取某一月的支出或者收入的总金额 kind: 支出==0 收入==1
    public static float getSumMoneyOnemonth(int year,int month,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        //遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }

    //获取某一年的支出或者收入的总金额 kind: 支出==0 收入==1
    public static float getSumMoneyOneYear(int year,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        //遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }
    //根据传入Id删除表格中的一条数据
    public static int deletItemFromAccounttbById(int id){
        int i = db.delete("accounttb","id=?",new String[]{id+""});
        return i;
    }
    //根据备注搜索收入或者支出的情况列表
    public static List<AccountBean> getAccountListByRemarrkFromAccounttb(String beizhu){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where beizhu like '%" + beizhu + "%'";
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String bz = cursor.getString(cursor.getColumnIndexOrThrow("beizhu"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            AccountBean bean = new AccountBean(id, typename, sImageId, bz, money, time, year, month, day, kind);
            list.add(bean);
        }
        return list;
    }
}
