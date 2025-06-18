package com.example.restudy.adt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restudy.R;
import com.example.restudy.model.Transactions;

import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    // Interface để xử lý sự kiện khi click vào item
    public interface OnTransactionClickListener {
        void onTransactionClick(Transactions transaction);
    }

    private Context context;
    private List<Transactions> transactionList;
    private OnTransactionClickListener listener;

    // Constructor truyền thêm listener
    public TransactionsAdapter(Context context, List<Transactions> transactionList, OnTransactionClickListener listener) {
        this.context = context;
        this.transactionList = transactionList;
        this.listener = listener;
    }

    // Cập nhật danh sách và refresh RecyclerView
    public void setTransactionList(List<Transactions> list) {
        this.transactionList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transactions_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TransactionsAdapter.ViewHolder holder, int position) {
        Transactions t = transactionList.get(position);
        holder.tvTransactionId.setText("Mã giao dịch: #" + t.getId());
        holder.tvPackageId.setText("Gói: " + t.getPackageId());
        holder.tvAmount.setText("Số tiền: " + t.getAmount() + "đ");
        holder.tvStatus.setText("Trạng thái: " + t.getStatus());
        holder.tvCreatedAt.setText("Thời gian: " + t.getCreatedAt());

        // Gán sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTransactionClick(t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionList != null ? transactionList.size() : 0;
    }

    // ViewHolder ánh xạ view trong item layout
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTransactionId, tvPackageId, tvAmount, tvStatus, tvCreatedAt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTransactionId = itemView.findViewById(R.id.tvTransactionId);
            tvPackageId = itemView.findViewById(R.id.tvPackageId);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }
    }
}
