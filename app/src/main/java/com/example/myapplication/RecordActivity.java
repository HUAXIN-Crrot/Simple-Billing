package com.example.myapplication;

import com.example.myapplication.adapter.RecordPagerAdapter;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.myapplication.frag_record.OutcomeFragment;
import com.google.android.material.tabs.TabLayout;
import com.example.myapplication.frag_record.IncomeFragment;
import com.example.myapplication.frag_record.BaseRecordFragment;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //1.查找控件
        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);
        //2.设置加载页面
        initPager();
    }
    private void initPager(){
        //初始化ViewPager页面的集合
        List<Fragment>fragmentList = new ArrayList<>();
        //创建收入和支出页面
        OutcomeFragment outFrag = new OutcomeFragment();
        IncomeFragment inFrag = new IncomeFragment();
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);

        //创建适配器
        RecordPagerAdapter recordPagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(),fragmentList);
        //设置适配器
        viewPager.setAdapter(recordPagerAdapter);
        //将TabLayout和ViewPager进行关联
        tabLayout.setupWithViewPager(viewPager);
    }
    //点击事件
    public void onClick(View view){
        switch (view.getId()){
            case R.id.record_iv_back:
                finish();
                break;
        }
    }
}