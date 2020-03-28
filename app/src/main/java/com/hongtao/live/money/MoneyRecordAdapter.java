package com.hongtao.live.money;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hongtao.live.R;
import com.hongtao.live.module.MoneyRecord;

import java.sql.Timestamp;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created 2020/3/28.
 *
 * @author HongTao
 */
public class MoneyRecordAdapter extends RecyclerView.Adapter<MoneyRecordAdapter.VH> {
    private List<MoneyRecord> mMoneyRecords;

    public MoneyRecordAdapter(List<MoneyRecord> moneyRecords) {
        mMoneyRecords = moneyRecords;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_money_dialog_rv_record, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        MoneyRecord moneyRecord = mMoneyRecords.get(position);
        holder.mTvTime.setText(new Timestamp(moneyRecord.getTime()).toString());
        switch (moneyRecord.getType()) {
            case MoneyRecord.TYPE_SEND_GIFT:
                holder.mTvType.setText(holder.mTvType.getResources().getText(R.string.money_dialog_tv_send_gift));
                holder.mTvAmount.setText("-" + moneyRecord.getMoney());
                holder.mTvType.setTextColor(holder.mTvType.getResources().getColor(R.color.red));
                holder.mTvAmount.setTextColor(holder.mTvAmount.getResources().getColor(R.color.red));
                break;
            case MoneyRecord.TYPE_RECEIVE_GIFT:
                holder.mTvType.setText(holder.mTvType.getResources().getText(R.string.money_dialog_tv_receive_gift));
                holder.mTvAmount.setText("+" + moneyRecord.getMoney());
                holder.mTvType.setTextColor(holder.mTvType.getResources().getColor(R.color.colorPrimaryDark));
                holder.mTvAmount.setTextColor(holder.mTvAmount.getResources().getColor(R.color.colorPrimaryDark));
                break;
            case MoneyRecord.TYPE_RECHARGE:
                holder.mTvType.setText(holder.mTvType.getResources().getText(R.string.money_dialog_tv_recharge));
                holder.mTvAmount.setText("+" + moneyRecord.getMoney());
                holder.mTvType.setTextColor(holder.mTvType.getResources().getColor(R.color.colorPrimaryDark));
                holder.mTvAmount.setTextColor(holder.mTvAmount.getResources().getColor(R.color.colorPrimaryDark));
                break;
            case MoneyRecord.TYPE_WITHDRAW:
                holder.mTvType.setText(holder.mTvType.getResources().getText(R.string.money_dialog_tv_withdraw));
                holder.mTvAmount.setText("-" + moneyRecord.getMoney());
                holder.mTvType.setTextColor(holder.mTvType.getResources().getColor(R.color.red));
                holder.mTvAmount.setTextColor(holder.mTvAmount.getResources().getColor(R.color.red));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMoneyRecords == null ? 0 : mMoneyRecords.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        private TextView mTvType, mTvAmount, mTvTime;

        public VH(@NonNull View itemView) {
            super(itemView);
            mTvType = itemView.findViewById(R.id.money_record_dialog_tv_type);
            mTvAmount = itemView.findViewById(R.id.money_record_dialog_tv_amount);
            mTvTime = itemView.findViewById(R.id.money_record_dialog_tv_time);
        }
    }
}
