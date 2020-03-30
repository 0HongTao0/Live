package com.hongtao.live.me;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hongtao.live.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created 2020/3/30.
 *
 * @author HongTao
 */
public class AlterGenderDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "AlterGenderDialog";

    private int mOldGender;
    private Callback mCallback;
    private RadioButton mRbMale, mRbFemale;

    public AlterGenderDialog(@NonNull Context context, int oldGender, Callback callback) {
        super(context);
        mOldGender = oldGender;
        mCallback = callback;
    }

    public AlterGenderDialog(@NonNull Context context, int themeResId, int oldGender, Callback callback) {
        super(context, themeResId);
        mOldGender = oldGender;
        mCallback = callback;
    }

    protected AlterGenderDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, int oldGender, Callback callback) {
        super(context, cancelable, cancelListener);
        mOldGender = oldGender;
        mCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alter_gender);
        mRbMale = findViewById(R.id.alter_gender_dialog_rb_male);
        mRbFemale = findViewById(R.id.alter_gender_dialog_rb_female);
        TextView tvConfirm = findViewById(R.id.alter_gender_dialog_btn_confirm);
        tvConfirm.setOnClickListener(this);
        TextView tvCancel = findViewById(R.id.alter_gender_dialog_btn_cancel);
        tvCancel.setOnClickListener(this);
        if (mOldGender == 1) {
            mRbMale.setChecked(true);
        } else {
            mRbFemale.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alter_gender_dialog_btn_cancel:
                dismiss();
                break;
            case R.id.alter_gender_dialog_btn_confirm:
                mCallback.confirm(mRbMale.isChecked() ? 1 : 0);
                dismiss();
                break;
        }
    }

    public interface Callback {
        void confirm(int gender);
    }
}
