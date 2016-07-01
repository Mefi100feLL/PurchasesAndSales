package com.PopCorp.Purchases.presentation.view.adapter;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.ShoppingListCallback;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ShoppingList;
import com.PopCorp.Purchases.data.utils.EllipsizeLineSpan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ShoppingListsAdapter extends RecyclerView.Adapter<ShoppingListsAdapter.ViewHolder> {

    private final Context context;
    private final ShoppingListCallback callback;

    private ArrayList<ShoppingList> objects;
    private final SortedList<ShoppingList> publishItems;

    public ShoppingListsAdapter(Context context, ShoppingListCallback callback, ArrayList<ShoppingList> objects, Comparator<ShoppingList> comparator) {
        this.context = context;
        this.callback = callback;
        this.objects = objects;
        publishItems = new SortedList<>(ShoppingList.class, new SortedList.Callback<ShoppingList>() {
            @Override
            public boolean areContentsTheSame(ShoppingList oneItem, ShoppingList twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public boolean areItemsTheSame(ShoppingList oneItem, ShoppingList twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public int compare(ShoppingList oneItem, ShoppingList twoItem) {
                return comparator.compare(oneItem, twoItem);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View view;
        public final ImageView overflow;
        public final TextView name;
        public final TextView items;
        public final View itemsLayout;
        public final TextView count;
        private ClickListener clickListener;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            overflow = (ImageView) view.findViewById(R.id.overflow);
            name = (TextView) view.findViewById(R.id.name);
            items = (TextView) view.findViewById(R.id.items);
            itemsLayout = view.findViewById(R.id.items_layout);
            count = (TextView) view.findViewById(R.id.count);
            view.setOnClickListener(this);
        }

        public interface ClickListener {
            void onClick(View v, int position);
        }

        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return publishItems.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ShoppingList list = publishItems.get(position);

        holder.name.setText(list.getName());
        holder.count.setText(String.valueOf(list.getItems().size()));
        if (list.getItems().size() == 0){
            holder.itemsLayout.setVisibility(View.GONE);
        } else{
            holder.itemsLayout.setVisibility(View.VISIBLE);
            holder.items.setText(getSpannableStringBuilder(list.getItems()));
        }

        holder.overflow.setTag(list);
        holder.overflow.setOnClickListener(v -> {
            ShoppingList clickedList = (ShoppingList) v.getTag();
            callback.onOverflowClicked(v, clickedList);
        });

        holder.setClickListener((v, position1) -> callback.onItemClicked(v, publishItems.get(position1)));
    }

    public SpannableStringBuilder getSpannableStringBuilder(List<ListItem> items) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder("");
        for (ListItem item : items) {
            spannableString.append(item.getName());
            if (item.isBuyed()) {
                spannableString.setSpan(new EllipsizeLineSpan(true), spannableString.length() - item.getName().length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                spannableString.setSpan(new EllipsizeLineSpan(false), spannableString.length() - item.getName().length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (items.indexOf(item) != items.size() - 1) {
                spannableString.append("\n");
            }
        }
        return spannableString;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list, parent, false);
        return new ViewHolder(v);
    }

    public void update() {
        publishItems.beginBatchedUpdates();
        for (ShoppingList list : objects) {
            int index = publishItems.indexOf(list);
            if (index == SortedList.INVALID_POSITION) {
                publishItems.add(list);
            } else {
                publishItems.updateItemAt(index, list);
            }
        }

        ArrayList<ShoppingList> arrayForRemoving = new ArrayList<>();
        for (int i = 0; i < publishItems.size(); i++) {
            ShoppingList list = publishItems.get(i);
            if (!objects.contains(list)) {
                arrayForRemoving.add(list);
            }
        }
        for (ShoppingList list : arrayForRemoving) {
            publishItems.remove(list);
        }

        if (publishItems.size() == 0) {
            callback.onEmpty();
        }
        publishItems.endBatchedUpdates();
    }
}