package com.hongtao.live.me;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hongtao.live.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created 2020/3/30.
 *
 * @author HongTao
 */
public class AlterTextDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "AlterTextDialog";
    public static final int TYPE_NICK = 1;
    public static final int TYPE_JOB = 2;
    public static final int TYPE_INTRODUCE = 3;
    public static final int TYPE_LIVE_INTRODUCE = 4;

    private int mType;
    private String oldData;
    private Callback mCallback;

    public AlterTextDialog(@NonNull Context context, int type, String old, Callback callback) {
        super(context);
        mType = type;
        oldData = old;
        mCallback = callback;
    }

    public AlterTextDialog(@NonNull Context context, int themeResId, int type, String old, Callback callback) {
        super(context, themeResId);
        mType = type;
        oldData = old;
        mCallback = callback;
    }

    protected AlterTextDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, int type, String old, Callback callback) {
        super(context, cancelable, cancelListener);
        mType = type;
        oldData = old;
        mCallback = callback;
    }

    private EditText mEtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alter_text);
        TextView tvTitle = findViewById(R.id.alter_text_dialog_tv_title);
        switch (mType) {
            case TYPE_NICK:
                tvTitle.setText(tvTitle.getResources().getText(R.string.alter_text_dialog_tv_nick));
                break;
            case TYPE_JOB:
                tvTitle.setText(tvTitle.getResources().getText(R.string.alter_text_dialog_tv_job));
                break;
            case TYPE_INTRODUCE:
                tvTitle.setText(tvTitle.getResources().getText(R.string.alter_text_dialog_tv_introduce));
                break;
            case TYPE_LIVE_INTRODUCE:
                tvTitle.setText(tvTitle.getResources().getText(R.string.alter_text_dialog_tv_live_introduce));
                break;
        }
        TextView tvConfirm = findViewById(R.id.alter_text_dialog_btn_confirm);
        tvConfirm.setOnClickListener(this);
        TextView tvCancel = findViewById(R.id.alter_text_dialog_btn_cancel);
        tvCancel.setOnClickListener(this);
        mEtData = findViewById(R.id.alter_text_dialog_et_data);
        mEtData.setText(oldData);
        mEtData.setSelection(oldData.length());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alter_text_dialog_btn_cancel:
                dismiss();
                break;
            case R.id.alter_text_dialog_btn_confirm:
                if (null != mCallback) mCallback.confirm(mEtData.getText().toString(), mType);
                dismiss();
                break;
        }
    }

    public interface Callback {
        void confirm(String data, int type);
    }
}
