package com.taskmaster.appui.Services.CosmeticItemTemplate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.taskmaster.appui.R;

import java.util.List;

public class CosmeticAdapter extends RecyclerView.Adapter<CosmeticAdapter.ViewHolder> {

    private List<CosmeticItem> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public CosmeticAdapter(List<CosmeticItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemPrice, itemSubtitle;
        View clickOverlay;
        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemSubtitle = itemView.findViewById(R.id.itemSubtitle);
            clickOverlay = itemView.findViewById(R.id.itemClickOverlay);
            clickOverlay.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CosmeticItem item = items.get(position);
        holder.itemName.setText(item.name);
        holder.itemPrice.setText(String.valueOf(item.price));
        holder.itemSubtitle.setText(item.subtitle);
        holder.itemImage.setImageResource(item.imageResId);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

