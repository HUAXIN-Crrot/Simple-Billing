package com.example.myapplication;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myapplication.adapter.AccountAdapter;
import com.example.myapplication.db.AccountBean;
import com.example.myapplication.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    ListView historyLv;
    TextView timeTv;
    List<AccountBean> mDatas;
    AccountAdapter adapter;
    int year, month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyLv = findViewById(R.id.history_lv);
        timeTv = findViewById(R.id.history_tv_time);
        mDatas = new ArrayList<>();
        //设置适配器
        adapter = new AccountAdapter(this,mDatas);
        historyLv.setAdapter(adapter);
        initTime();
        timeTv.setText(year + "年" + month + "月");
        loadData();


    }
//获取指定年份份收支情况的列表
    private void loadData() {
        List<AccountBean> list = DBManager.getAccountListOneMonthFromAccounttb(year, month);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;

    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.history_iv_back:
                finish();
                break;
            case R.id.history_iv_rili:

                break;
        }

    }
}