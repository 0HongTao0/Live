package com.hongtao.live.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hongtao.live.R;
import com.hongtao.live.module.Message;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created 2020/3/25.
 *
 * @author HongTao
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.VH> {
    private static final String TAG = "MessageAdapter";

    private List<Message> mMessages;

    public MessageAdapter(List<Message> messages) {
        mMessages = messages;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_watch_rv_message, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Message message = mMessages.get(position);
        Glide.with(holder.mIvAvatar.getContext())
                .load(message.getAvatar())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))//圆形
                .into(holder.mIvAvatar);
        holder.mTvMessage.setText(message.getNick() + ":" + message.getMessage());
        if (message.getType() == 2) {
            holder.mTvMessage.setTextColor(holder.mIvAvatar.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public int getItemCount() {
        return mMessages == null ? 0 : mMessages.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView mIvAvatar;
        TextView mTvMessage;

        public VH(@NonNull View itemView) {
            super(itemView);
            mIvAvatar = itemView.findViewById(R.id.watch_message_iv_avatar);
            mTvMessage = itemView.findViewById(R.id.watch_message_tv_message);
        }
    }
}
