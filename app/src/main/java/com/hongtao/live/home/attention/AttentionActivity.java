package com.hongtao.live.home.attention;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hongtao.live.R;
import com.hongtao.live.base.BaseActivity;
import com.hongtao.live.home.watch.WatchActivity;
import com.hongtao.live.module.Attention;
import com.hongtao.live.module.Room;
import com.hongtao.live.net.ServiceGenerator;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created 2020/3/27.
 *
 * @author HongTao
 */
public class AttentionActivity extends BaseActivity implements AttentionAdapter.Callback, OnRefreshListener {
    private static final String TAG = "AttentionActivity";

    public static void start(Context context) {
        Intent intent = new Intent(context, AttentionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private SmartRefreshLayout mSrlRefresh;
    private AttentionAdapter mAttentionAdapter;
    private List<Attention> mAttentions = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_attention;
    }

    @Override
    public void initView() {
        RecyclerView rvAttention = findViewById(R.id.attention_rv_room);
        rvAttention.setLayoutManager(new LinearLayoutManager(this));
        mAttentionAdapter = new AttentionAdapter(mAttentions, AttentionActivity.this);
        rvAttention.setAdapter(mAttentionAdapter);

        mSrlRefresh = findViewById(R.id.attention_srl_refresh);
        mSrlRefresh.setEnableLoadMore(false);
        initViewData();
    }

    private void initViewData() {
        AttentionApi attentionApi = ServiceGenerator.createService(AttentionApi.class);
        attentionApi.getAttentionRoom()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Attention>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(List<Attention> attentions) {
                        mAttentions.clear();
                        mAttentions.addAll(attentions);
                        mAttentionAdapter.notifyDataSetChanged();
                        if (mSrlRefresh != null) {
                            mSrlRefresh.finishRefresh();
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

    @Override
    public void onClick(Attention attention) {
        WatchActivity.start(this, Room.create(attention));
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initViewData();
    }
}
