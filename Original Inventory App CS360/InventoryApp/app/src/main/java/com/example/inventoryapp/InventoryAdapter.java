package com.example.inventoryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {
    private ArrayList<InventoryItem> inventoryList;
    private OnItemActionListener listener;

    public interface OnItemActionListener {
        void onDeleteClick(InventoryItem item);
        void onItemClick(InventoryItem item);
        void onPlusClick(InventoryItem item);
        void onMinusClick(InventoryItem item);
    }

    public InventoryAdapter(ArrayList<InventoryItem> inventoryList, OnItemActionListener listener) {
        this.inventoryList = inventoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item, parent, false);

        return new InventoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        InventoryItem currentItem = inventoryList.get(position);

        holder.textItemName.setText(currentItem.getItemName());
        holder.editQuantityRow.setText(String.valueOf(currentItem.getQuantity()));

        holder.editQuantityRow.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentItem);
            }
        });
        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(currentItem);
            }
        });

        holder.buttonPlus.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlusClick(currentItem);
            }
        });

        holder.buttonMinus.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMinusClick(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    public static class InventoryViewHolder extends RecyclerView.ViewHolder {
        TextView textItemName;
        EditText editQuantityRow;
        Button buttonDelete;
        Button buttonMinus;
        Button buttonPlus;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);

            textItemName = itemView.findViewById(R.id.textItemName);
            editQuantityRow = itemView.findViewById(R.id.editQuantityRow);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonMinus = itemView.findViewById(R.id.buttonMinus);
            buttonPlus = itemView.findViewById(R.id.buttonPlus);

        }
    }
}
