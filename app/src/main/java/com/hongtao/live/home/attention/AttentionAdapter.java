package com.hongtao.live.home.attention;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hongtao.live.R;
import com.hongtao.live.module.Attention;

import java.sql.Timestamp;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created 2020/3/27.
 *
 * @author HongTao
 */
public class AttentionAdapter extends RecyclerView.Adapter<AttentionAdapter.VH> {
    private List<Attention> mAttentions;
    private Callback mCallback;

    public AttentionAdapter(List<Attention> attentions, Callback callback) {
        mAttentions = attentions;
        mCallback = callback;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attention_rv_room, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Attention attention = mAttentions.get(position);
        Glide.with(holder.mIvAvatar.getContext())
                .load(attention.getAvatar())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))//圆形
                .into(holder.mIvAvatar);
        Glide.with(holder.mTvIsLiving)
                .load(attention.isLiving() ? R.drawable.ic_living_online : R.drawable.ic_living_offline)
                .into(holder.mTvIsLiving);

        holder.mTvNick.setText(attention.getNick());
        holder.mTvRoomName.setText("直播间：" + attention.getRoomName());
        holder.mTvRoomIntroduction.setText("直播简介：" + attention.getRoomIntroduction());
        holder.mTvTime.setText("关注时间：" + new Timestamp(attention.getTime()).toString());

        if (mCallback != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onClick(attention);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mAttentions == null ? 0 : mAttentions.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView mIvAvatar, mTvIsLiving;
        TextView mTvNick, mTvRoomName, mTvRoomIntroduction, mTvTime;

        public VH(@NonNull View itemView) {
            super(itemView);
            mIvAvatar = itemView.findViewById(R.id.attention_iv_avatar);
            mTvIsLiving = itemView.findViewById(R.id.attention_iv_is_living);
            mTvNick = itemView.findViewById(R.id.attention_tv_nick);
            mTvRoomName = itemView.findViewById(R.id.attention_tv_room_name);
            mTvRoomIntroduction = itemView.findViewById(R.id.attention_tv_room_introduction);
            mTvTime = itemView.findViewById(R.id.attention_tv_time);
        }
    }

    public interface Callback {
        void onClick(Attention attention);
    }
}
