package com.example.restudy.adt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restudy.R;
import com.example.restudy.model.Packages;

import java.util.List;

/**
 * Adapter hiển thị danh sách gói đăng ký.
 * <p>
 * Cách dùng (trong Activity):
 *
 * <pre>
 * adapter = new PackageAdapter(
 *         list,
 *         pkg -> showEditDialog(pkg),   // listener Sửa
 *         pkg -> confirmDelete(pkg)     // listener Xoá
 * );
 * </pre>
 */
public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.PackageViewHolder> {

    /** interface dùng cho cả nút Sửa và Xoá */
    public interface OnPackageClick {
        void onClick(Packages pkg);
    }

    private List<Packages> packageList;
    private final OnPackageClick editListener;
    private final OnPackageClick deleteListener;

    public PackagesAdapter(List<Packages> packageList,
                           OnPackageClick editListener,
                           OnPackageClick deleteListener) {
        this.packageList = packageList;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    /* -------- RecyclerView boilerplate -------- */

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_packages_layout, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        holder.bind(packageList.get(position));
    }

    @Override
    public int getItemCount() {
        return packageList == null ? 0 : packageList.size();
    }

    /** Gọi khi bạn muốn làm mới toàn bộ danh sách */
    public void setData(List<Packages> newList) {
        this.packageList = newList;
        notifyDataSetChanged();
    }

    /* -------- ViewHolder -------- */

    class PackageViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName, tvDescription, tvPrice, tvDuration;
        private final ImageButton btnEdit, btnDelete;

        PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName        = itemView.findViewById(R.id.tvPackageName);
            tvDescription = itemView.findViewById(R.id.tvPackageDescription);
            tvPrice       = itemView.findViewById(R.id.tvPackagePrice);
            tvDuration    = itemView.findViewById(R.id.tvPackageDuration);
            btnEdit       = itemView.findViewById(R.id.btnEditPackage);
            btnDelete     = itemView.findViewById(R.id.btnDeletePackage);
        }

        void bind(Packages pkg) {
            tvName.setText(pkg.getName());

            // Mô tả tuỳ ý – bạn có thể thay đổi tuỳ nhu cầu
            String desc = "Tối đa " + pkg.getMaxPosts() + " bài • "
                    + (pkg.isCanPin() ? "Có ghim" : "Không ghim");
            tvDescription.setText(desc);

            tvPrice.setText(String.format("Giá: %,dđ", pkg.getPrice()));
            tvDuration.setText(String.format("Thời hạn: %d ngày", pkg.getDuration()));

            /* Sự kiện nút Sửa */
            btnEdit.setOnClickListener(v -> {
                if (editListener != null) editListener.onClick(pkg);
            });

            /* Sự kiện nút Xoá */
            btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) deleteListener.onClick(pkg);
            });
        }
    }
}
