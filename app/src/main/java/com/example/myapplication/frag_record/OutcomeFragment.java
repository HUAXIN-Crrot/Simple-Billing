package com.example.myapplication.frag_record;

import com.example.myapplication.R;
import com.example.myapplication.db.DBManager;
import com.example.myapplication.db.TypeBean;

import java.util.List;

public class OutcomeFragment extends BaseRecordFragment{


    //重写
    @Override
    public void loadDataToGv() {
        super.loadDataToGv();
        //获取数据库中的数据源
        List<TypeBean>outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(0);
        DBManager.insertToAccounttb(accountBean);
    }
}
