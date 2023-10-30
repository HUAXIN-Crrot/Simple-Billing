package com.example.myapplication.frag_record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.db.DBManager;
import com.example.myapplication.db.TypeBean;

import java.util.List;


public class IncomeFragment extends BaseRecordFragment {

    @Override
    public void loadDataToGv() {
        super.loadDataToGv();
        //获取数据库中的数据源
        List<TypeBean> inlist = DBManager.getTypeList(1);
        typeList.addAll(inlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.in_qita_fs);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(1);
        DBManager.insertToAccounttb(accountBean);
    }
}