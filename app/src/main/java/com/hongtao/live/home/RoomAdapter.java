package com.hongtao.live.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongtao.live.R;
import com.hongtao.live.module.Room;
import com.hongtao.live.util.GreyPicTransform;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created 2020/3/19.
 *
 * @author HongTao
 */
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.VH> {
    private static final String TAG = "RoomAdapter";
    private List<Room> mRooms;

    private OnClickListener mOnClickListener;


    public RoomAdapter(List<Room> rooms) {
        mRooms = rooms;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_rv_room, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Room room = mRooms.get(position);
        Log.d(TAG, "onBindViewHolder: " + room.toString());
        holder.mTvNum.setText(String.valueOf(room.getNum()));
        holder.mTvNick.setText(room.getNick());
        holder.mTvRoomIntroduction.setText(room.getRoomIntroduction());
        Glide.with(holder.mIvAvatar.getContext())
                .load(room.getAvatar())
                .centerCrop()
                .transform(new GreyPicTransform())
                .into(holder.mIvAvatar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnClickListener) {
                    mOnClickListener.onClickItem(room);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRooms == null ? 0 : mRooms.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView mIvAvatar;
        TextView mTvNick;
        TextView mTvNum;
        TextView mTvRoomIntroduction;

        public VH(@NonNull View itemView) {
            super(itemView);
            mIvAvatar = itemView.findViewById(R.id.room_iv_avatar);
            mTvNick = itemView.findViewById(R.id.room_tv_nick);
            mTvNum = itemView.findViewById(R.id.room_tv_num);
            mTvRoomIntroduction = itemView.findViewById(R.id.room_tv_room_introduction);
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClickItem(Room room);
    }
}
