package com.PopCorp.Purchases.presentation.view.adapter;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.comparator.ListItemCategoryComparator;
import com.PopCorp.Purchases.data.model.ListItemCategory;

import java.util.List;

public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.ViewHolder> {

    private final RecyclerCallback<ListItemCategory> callback;
    private final ListItemCategoryComparator comparator = new ListItemCategoryComparator();

    private List<ListItemCategory> objects;
    private final SortedList<ListItemCategory> publishItems;

    public CategoriesRecyclerViewAdapter(RecyclerCallback<ListItemCategory> callback, List<ListItemCategory> objects) {
        this.callback = callback;
        this.objects = objects;
        publishItems = new SortedList<>(ListItemCategory.class, new SortedList.Callback<ListItemCategory>() {
            @Override
            public boolean areContentsTheSame(ListItemCategory oneItem, ListItemCategory twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public boolean areItemsTheSame(ListItemCategory oneItem, ListItemCategory twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public int compare(ListItemCategory oneItem, ListItemCategory twoItem) {
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
        for (ListItemCategory category : objects){
            publishItems.add(category);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View view;
        public final ImageView image;
        public final TextView name;
        private CategoriesAdapter.ViewHolder.ClickListener clickListener;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            image = (ImageView) view.findViewById(R.id.category_image);
            name = (TextView) view.findViewById(R.id.category_name);
            view.setOnClickListener(this);
        }

        public interface ClickListener {
            void onClick(View v, int position);
        }

        public void setClickListener(CategoriesAdapter.ViewHolder.ClickListener clickListener) {
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
        ListItemCategory category = publishItems.get(position);

        ShapeDrawable coloredCircle = new ShapeDrawable(new OvalShape());
        coloredCircle.getPaint().setColor(category.getColor());
        holder.image.setBackgroundDrawable(coloredCircle);
        holder.name.setText(category.getName());

        holder.setClickListener((v, pos) -> callback.onItemClicked(v, publishItems.get(pos)));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listitem_category_for_dialog, parent, false);
        return new ViewHolder(v);
    }
}
