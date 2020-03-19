package com.hongtao.live.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hongtao.live.R;
import com.hongtao.live.module.Room;

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
public class HomeFragment extends Fragment implements HomeContract.View, RoomAdapter.OnClickListener {
    private static final String TAG = "HomeFragment";

    private HomePresenter mHomePresenter;

    private RecyclerView mRvRoom;

    private RoomAdapter mRoomAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mRvRoom = rootView.findViewById(R.id.home_rv_rooms);
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
    }

    @Override
    public void onClickItem(Room room) {
        mHomePresenter.startWatchActivity(room, getContext());
    }
}
