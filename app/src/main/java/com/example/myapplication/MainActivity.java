package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.example.myapplication.adapter.AccountAdapter;
import com.example.myapplication.db.AccountBean;
import com.example.myapplication.db.DBManager;
import com.example.myapplication.utils.BudgetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView todayLv; //展示今日收支情况
    ImageView searchIv;
    Button editBtn;
    ImageButton moreBtn;
    //声明数据源
    List<AccountBean> mDatas;
    AccountAdapter adapter;
    int year,month,day;
    //头布局相关控件
    View headerView;
    TextView topOutTv, topInTv, topbudgeTv, topConTv;
    ImageView topShowIv;
    SharedPreferences preferences;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
        //添加ListView的头布局
        addLVHeaderView();
        mDatas = new ArrayList<>();
        //设置适配器,加载每一行数据到列表中
       adapter = new AccountAdapter(this,mDatas);
       todayLv.setAdapter(adapter);
    }
//初始化自带View的方法
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_more);
        searchIv = findViewById(R.id.main_iv_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        setLVLongClickListener();
    }
//设置ListView的长按事件
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {  //点击头布局
                    return false;
                }
                int pos = i - 1;
                AccountBean clickBean = mDatas.get(pos); //获取正在被点击的信息

                //弹出提示用户是否删除对话框
                showDeleItemDialog(clickBean);
                return false;
            }
        });
    }
    //弹出是否删除某条记录对话框
    private void showDeleItemDialog(final AccountBean clickBean){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("您确定要删除这条记录吗？")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int click_id = clickBean.getId();
                        //执行删除的操作
                        DBManager.deletItemFromAccounttbById(click_id);
                        mDatas.remove(clickBean);  //实时刷新，移除集合中对象
                        adapter.notifyDataSetChanged();  //提示数据更新
                        setTopTvShow();   //改变头布局显示内容
                    }
                });
        builder.create().show();  //显示对话框

    }

    //给ListView添加头布局
    private void addLVHeaderView() {
        //布局转换为View对象
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);
        //查找头布局可用控件
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgeTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_iv_hide);

        topbudgeTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);
    }
    //获取今日的具体时间
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    //当activity获取焦点时，调用方法
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }
//设置头布局当中文本内容的显示
    private void setTopTvShow() {
        //获取今日支出和收入总金额
        float incomeOneday = DBManager.getSumMoneyOneday(year, month, day, 1);
        float outcomeOneday = DBManager.getSumMoneyOneday(year, month, day, 0);
        String infoOneDay = "今日支出 ￥" + outcomeOneday + "收入 ￥" + incomeOneday;
        topConTv.setText(infoOneDay);
        //获取本月支出和收入总金额
        float incomeOnemonth = DBManager.getSumMoneyOnemonth(year, month, 1);
        float outcomeOnemonth = DBManager.getSumMoneyOnemonth(year, month, 0);
        topInTv.setText("￥"+incomeOnemonth);
        topOutTv.setText("￥"+outcomeOnemonth);

        //设置显示预算剩余
        float bmoney = preferences.getFloat("bmoney", 0);//获取预算
        if(bmoney == 0){
            topbudgeTv.setText("￥ 0");
        }else{
            float syMoney = bmoney - outcomeOnemonth;
            topbudgeTv.setText("￥" + syMoney);
        }


    }

    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_iv_search:
                Intent it = new Intent(this,SearchActivity.class); //跳转界面
                startActivity(it);

                break;
            case R.id.main_btn_edit:
                Intent intent = new Intent(this,RecordActivity.class); //跳转界面
                startActivity(intent);
                break;
            case R.id.main_btn_more:
                Intent intent1 = new Intent(this,HistoryActivity.class); //跳转界面
                startActivity(intent1);
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;
            case R.id.item_mainlv_iv_hide:
                //切换TextView明文和密文
                toggleShow();
                break;
        }
        if(view == headerView){
            //头布局被点击
        }
    }
//显示预算设置对话框
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //预算金额写入共享参数中存储
                SharedPreferences.Editor edit = preferences.edit();
                edit.putFloat("bmoney",money);
                edit.commit();

                //计算剩余金额
                float outcomeOnemonth = DBManager.getSumMoneyOnemonth(year, month, 0);

                float syMoney = money - outcomeOnemonth; //用预算剩余 = 预算 - 支出
                topbudgeTv.setText("￥" + syMoney);
            }
        });
    }

    boolean inShow = true;
//点击头布局眼睛时加密
    private void toggleShow() {
        if (inShow) {
            //明文->密文
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(passwordMethod); //设置隐藏
            topOutTv.setTransformationMethod(passwordMethod);
            topbudgeTv.setTransformationMethod(passwordMethod);
            topShowIv.setImageResource(R.mipmap.hide);
            inShow = false;                               //设置标志位为隐藏
        }
        else{
            //密文->明文
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(hideMethod); //设置隐藏
            topOutTv.setTransformationMethod(hideMethod);
            topbudgeTv.setTransformationMethod(hideMethod);
            topShowIv.setImageResource(R.mipmap.eye2);
            inShow = true;
        }
    }
}