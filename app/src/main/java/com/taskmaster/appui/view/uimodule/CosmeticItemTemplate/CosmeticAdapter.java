package com.taskmaster.appui.view.uimodule.CosmeticItemTemplate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taskmaster.appui.R;

import java.util.ArrayList;
import java.util.List;

public class CosmeticAdapter extends RecyclerView.Adapter<CosmeticAdapter.ViewHolder> {

    private final List<CosmeticItem> items;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public CosmeticAdapter(List<CosmeticItem> items, OnItemClickListener listener) {
        this.items = new ArrayList<>(items);
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CosmeticItem item = items.get(position);
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText(String.valueOf(item.price));
        holder.itemSubtitle.setText(item.subtitle);
        holder.itemImage.setImageResource(item.imageResId);
    }
    public void updateItems(List<CosmeticItem> newItems) {
        this.items.clear();       // 'items' is the list you're displaying
        this.items.addAll(newItems);
        notifyDataSetChanged();   // tells RecyclerView to redraw the list
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

