package com.hongtao.live.home.watch.gift;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongtao.live.R;
import com.hongtao.live.module.Gift;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created 2020/3/28.
 *
 * @author HongTao
 */
public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.VH> {
    private List<Gift> mGifts;
    private Callback mCallback;

    public GiftAdapter(List<Gift> gifts, Callback callback) {
        mGifts = gifts;
        mCallback = callback;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_send_gift_rv_gift, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Gift gift = mGifts.get(position);
        Glide.with(holder.mIvPic.getContext()).load(gift.getPic()).into(holder.mIvPic);
        holder.mTvName.setText(gift.getName());
        holder.mTvPrice.setText(String.valueOf(gift.getPrice()));
        if (mCallback != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onClick(gift);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mGifts == null ? 0 : mGifts.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        private ImageView mIvPic;
        private TextView mTvPrice, mTvName;

        public VH(@NonNull View itemView) {
            super(itemView);
            mIvPic = itemView.findViewById(R.id.send_gift_iv_pic);
            mTvPrice = itemView.findViewById(R.id.send_gift_tv_price);
            mTvName = itemView.findViewById(R.id.send_gift_tv_name);
        }
    }

    public interface Callback {
        void onClick(Gift gift);
    }
}
