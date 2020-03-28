package com.hongtao.live.home.watch.gift;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hongtao.live.R;
import com.hongtao.live.module.Content;
import com.hongtao.live.module.Gift;
import com.hongtao.live.module.NormalResponse;
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
public class SendGiftDialog extends Dialog implements View.OnClickListener, GiftAdapter.Callback {
    private static final String TAG = "SendGiftDialog";
    private String toUserId;
    private int roomId;


    public SendGiftDialog(@NonNull Context context) {
        super(context);
    }

    public SendGiftDialog(@NonNull Context context, int themeResId, String userId, int roomId) {
        super(context, themeResId);
        this.toUserId = userId;
        this.roomId = roomId;
    }

    protected SendGiftDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private RecyclerView mRvGift;
    private GiftApi mGiftApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_send_gift);
        mGiftApi = ServiceGenerator.createService(GiftApi.class);
        initView();
        initViewData();
    }

    private void initView() {
        mRvGift = findViewById(R.id.send_gift_rv_gift);
        ImageView ivClose = findViewById(R.id.send_gift_iv_close);
        ivClose.setOnClickListener(this);
    }

    private void initViewData() {
        mGiftApi.getGifts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Gift>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(List<Gift> gifts) {
                        GiftAdapter giftAdapter = new GiftAdapter(gifts, SendGiftDialog.this);
                        mRvGift.setAdapter(giftAdapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(mRvGift.getContext());
                        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        mRvGift.setLayoutManager(layoutManager);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_gift_iv_close:
                dismiss();
                break;
        }
    }

    @Override
    public void onClick(Gift gift) {
        mGiftApi.sendGift(gift.getGiftId(), toUserId, roomId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<NormalResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(NormalResponse normalResponse) {
                        if (normalResponse.getCode() == NormalResponse.CODE_SUCCESS) {
                            Toast.makeText(getContext(), Content.Message.MSG_GIFT_SEND_SUCCESS, Toast.LENGTH_SHORT).show();
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), Content.Message.MSG_GIFT_SEND_FAIL, Toast.LENGTH_SHORT).show();
                        }
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
