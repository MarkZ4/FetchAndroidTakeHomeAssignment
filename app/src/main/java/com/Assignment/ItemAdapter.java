package com.Assignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Map<Integer, List<Item>> groupedItems;
    private final List<Object> displayList;
    private final Map<Integer, Boolean> expandedState;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public ItemAdapter(Map<Integer, List<Item>> groupedItems) {
        this.groupedItems = groupedItems;
        this.expandedState = new HashMap<>();
        this.displayList = new ArrayList<>();
        initDisplayList();
    }

    private void initDisplayList() {
        displayList.clear();
        for (Map.Entry<Integer, List<Item>> entry : groupedItems.entrySet()) {
            int listId = entry.getKey();
            displayList.add(listId);
            if (expandedState.getOrDefault(listId, false)) {
                displayList.addAll(entry.getValue());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return displayList.get(position) instanceof Integer ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_HEADER) {
            Integer listId = (Integer) displayList.get(position);
            ((HeaderViewHolder) holder).bind(listId, expandedState.getOrDefault(listId, false));
        } else {
            Item item = (Item) displayList.get(position);
            ((ItemViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView listIdTextView;
        private final ImageView chevronImageView;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            listIdTextView = itemView.findViewById(R.id.listIdTextView);
            chevronImageView = itemView.findViewById(R.id.chevronImageView);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                Integer listId = (Integer) displayList.get(position);
                boolean isExpanded = expandedState.getOrDefault(listId, false);
                expandedState.put(listId, !isExpanded);
                initDisplayList();
                notifyDataSetChanged();
            });
        }

        public void bind(int listId, boolean isExpanded) {
            listIdTextView.setText("List Id: " + listId);
            chevronImageView.setRotation(isExpanded ? 90 : 0);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView idTextView;
        private final TextView nameTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }

        public void bind(Item item) {
            idTextView.setText("ID: " + item.getId());
            nameTextView.setText("Name: " + item.getName());
        }
    }
}



