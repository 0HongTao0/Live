package com.hongtao.live.money;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hongtao.live.R;
import com.hongtao.live.module.MoneyRecord;
import com.hongtao.live.net.ServiceGenerator;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created 2020/3/28.
 *
 * @author HongTao
 */
public class MoneyRecordDialog extends Dialog {
    private static final String TAG = "MoneyRecordDialog";

    public MoneyRecordDialog(@NonNull Context context) {
        super(context);
    }

    public MoneyRecordDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MoneyRecordDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private RecyclerView mRvMoneyRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_money_record);
        mRvMoneyRecord = findViewById(R.id.money_record_dialog_rv_record);
        findViewById(R.id.money_record_dialog_iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mRvMoneyRecord.setLayoutManager(new LinearLayoutManager(getContext()));
        MoneyApi moneyApi = ServiceGenerator.createService(MoneyApi.class);
        moneyApi.getMoneyRecord()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<MoneyRecord>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(List<MoneyRecord> moneyRecords) {
                        MoneyRecordAdapter moneyRecordAdapter = new MoneyRecordAdapter(moneyRecords);
                        mRvMoneyRecord.setAdapter(moneyRecordAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }
}
