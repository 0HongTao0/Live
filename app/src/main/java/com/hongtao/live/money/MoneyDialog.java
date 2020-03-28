package com.hongtao.live.money;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hongtao.live.R;
import com.hongtao.live.util.MoneyValueFilter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created 2020/3/28.
 *
 * @author HongTao
 */
public class MoneyDialog extends Dialog implements View.OnClickListener {
    public static final int TYPE_RECHARGE = 1;
    public static final int TYPE_WITHDRAW = 2;

    private int mType;
    private TextView mTvTitle;
    private EditText mEtAmount;
    private Callback mCallback;

    public MoneyDialog(@NonNull Context context) {
        super(context);
    }

    public MoneyDialog(@NonNull Context context, int themeResId, int type, Callback callback) {
        super(context, themeResId);
        mType = type;
        this.mCallback = callback;
    }

    protected MoneyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_money);
        initView();
        if (mType == TYPE_WITHDRAW) {
            mTvTitle.setText(getContext().getResources().getText(R.string.money_dialog_tv_title_withdraw));
        }
    }

    private void initView() {
        mTvTitle = findViewById(R.id.money_dialog_tv_title);
        mEtAmount = findViewById(R.id.money_dialog_et_amount);
        mEtAmount.setFilters(new InputFilter[]{new MoneyValueFilter()});
        Button btnCancel = findViewById(R.id.money_dialog_btn_cancel);
        btnCancel.setOnClickListener(this);
        Button btnConfirm = findViewById(R.id.money_dialog_btn_confirm);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.money_dialog_btn_confirm:
                mCallback.confirm(mType, Float.parseFloat(mEtAmount.getText().toString()));
                dismiss();
                break;
            case R.id.money_dialog_btn_cancel:
                dismiss();
                break;
        }
    }

    public interface Callback {
        void confirm(int type, float money);
    }
}
