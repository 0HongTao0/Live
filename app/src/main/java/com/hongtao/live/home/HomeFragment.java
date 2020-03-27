package com.hongtao.live.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hongtao.live.R;
import com.hongtao.live.home.attention.AttentionActivity;
import com.hongtao.live.module.Room;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created 2020/3/4.
 *
 * @author HongTao
 */
public class HomeFragment extends Fragment implements HomeContract.View, RoomAdapter.OnClickListener, View.OnClickListener {
    private static final String TAG = "HomeFragment";

    private HomePresenter mHomePresenter;

    private RecyclerView mRvRoom;

    private RoomAdapter mRoomAdapter;
    private SmartRefreshLayout mSrlRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mRvRoom = rootView.findViewById(R.id.home_rv_rooms);
        rootView.findViewById(R.id.home_iv_attention).setOnClickListener(this);
        mSrlRefresh = rootView.findViewById(R.id.home_srl_refresh);
        mSrlRefresh.setEnableLoadMore(false);
        mSrlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mHomePresenter.getRoomList();
            }
        });
        mHomePresenter = new HomePresenter(this);
        mHomePresenter.getRoomList();
        return rootView;
    }

    @Override
    public void showRoomList(List<Room> rooms) {
        mRoomAdapter = new RoomAdapter(rooms);
        mRoomAdapter.setOnClickListener(this);
        mRvRoom.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvRoom.setAdapter(mRoomAdapter);
        if (mSrlRefresh != null) {
            mSrlRefresh.finishRefresh();
        }
    }

    @Override
    public void onClickItem(Room room) {
        mHomePresenter.startWatchActivity(room, getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_iv_attention:
                AttentionActivity.start(getContext());
                break;
        }
    }
}
