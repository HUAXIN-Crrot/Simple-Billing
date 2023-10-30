package com.example.myapplication.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.myapplication.R;

public class BudgetDialog extends Dialog implements View.OnClickListener {
    ImageView cancelIv;
    Button ensureBtn;
    EditText moneyEt;
    public interface OnEnsureListener{
        public void onEnsure(float money);
    }
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    @SuppressLint("MissingInflatedId")
    public BudgetDialog(@NonNull Context context) {
        super(context);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_budget);
        cancelIv = findViewById(R.id.dialog_budget_iv_error);
        ensureBtn = findViewById(R.id.dialog_budget_btn_ensure);
        moneyEt = findViewById(R.id.dialog_budget_et);
        cancelIv.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_budget_iv_error:
                cancel();                  //取消对话框
                break;
            case R.id.dialog_budget_btn_ensure:
                //获取输入数据数值
                String data = moneyEt.getText().toString();
                if (TextUtils.isEmpty(data)) {
                    Toast.makeText(getContext(),"输入数据不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                float money = Float.parseFloat(data);
                if(money <= 0){
                    Toast.makeText(getContext(),"预算金额必须大于0！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onEnsureListener != null) {
                    onEnsureListener.onEnsure(money);
                }
                cancel();
                break;
        }

    }
}
