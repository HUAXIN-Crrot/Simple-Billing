package com.example.myapplication.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.myapplication.R;
import com.example.myapplication.db.AccountBean;
import com.example.myapplication.db.DBManager;
import com.example.myapplication.db.TypeBean;
import com.example.myapplication.utils.BeiZhuDialog;
import com.example.myapplication.utils.KeyBoardUtils;
import com.example.myapplication.utils.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 记录页面当中的支出模块
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener {

    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv, beizhuTv, timeTv;
    GridView typeGv;
    List<TypeBean>typeList;
    TypeBaseAdapter adapter;
    AccountBean accountBean;  //将需要插入到记账本当中的数据保存成对象形式


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean(); //创建对象
        accountBean.setTypename("其他");
        accountBean.setsImageId(R.mipmap.ic_qita_fs);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        setInitTime();
        //给GridView填充数据方法
        loadDataToGv();
        setGvListener(); //设置GridView每一项的点击事件
        return view;
    }
/*获取当前时间，显示在timeTv上*/
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = sdf.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }

    private void setGvListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.selectPos = i;
                adapter.notifyDataSetChanged(); //提示绘制发生变化
                TypeBean typeBean = typeList.get(i);
                String typename = typeBean.getTypename();
                typeTv.setText(typename);
                accountBean.setTypename(typename);
                int simageId = typeBean.getSimageId();
                typeIv.setImageResource(simageId);
                accountBean.setsImageId(simageId);
            }
        });
    }

    public void loadDataToGv() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);
    }

    private void initView(View view){
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        beizhuTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);
        //让自定义软键盘显示
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        boardUtils.showKeyboard();
        //设置接口，监听按钮是否被点击
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //获取输入钱数
                String moneyStr = moneyEt.getText().toString();
                if(TextUtils.isEmpty(moneyStr) || moneyStr.equals("0")){
                    getActivity().finish();
                    return ;
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);
                //获取记录的信息，保存在数据库中
                saveAccountToDB();
                //返回上级页面
                getActivity().finish();
            }
        });
    }
    public abstract void  saveAccountToDB();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
            case R.id.frag_record_tv_beizhu:
                showBZDialog();
                break;
        }
    }

//弹出显示时间的对话框
    private void showTimeDialog() {
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();
        //设定确定按钮被点击的监听器
        dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }

    /*弹出备注对话框 */
    public void showBZDialog(){
        BeiZhuDialog dialog = new BeiZhuDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BeiZhuDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                if(!TextUtils.isEmpty(msg)){
                    beizhuTv.setText(msg);
                    accountBean.setBeizhu(msg);
                }
                dialog.cancel();
            }
        });

    }
}