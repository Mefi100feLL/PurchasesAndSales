package com.PopCorp.Purchases.presentation.view.adapter;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.comparator.CategoryComparator;
import com.PopCorp.Purchases.data.dao.CategoryDAO;
import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.net.APIFactory;
import com.PopCorp.Purchases.data.utils.UIL;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> implements Filterable {

    public static final String FILTER_ALL = "";
    public static final String FILTER_FAVORITE = "favorite";

    private final RecyclerCallback<Category> callback;
    private final CategoryComparator comparator = new CategoryComparator();

    private CategoryDAO categoryDAO = new CategoryDAO();

    private ArrayList<Category> objects;
    private final SortedList<Category> publishItems;

    private String currentFilter;

    public CategoriesAdapter(RecyclerCallback<Category> callback, ArrayList<Category> objects) {
        this.callback = callback;
        this.objects = objects;
        publishItems = new SortedList<>(Category.class, new SortedList.Callback<Category>() {
            @Override
            public boolean areContentsTheSame(Category oneItem, Category twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public boolean areItemsTheSame(Category oneItem, Category twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public int compare(Category oneItem, Category twoItem) {
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
        public final ImageView image;
        public final TextView name;
        public final ImageView favorite;
        private ClickListener clickListener;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            favorite = (ImageView) view.findViewById(R.id.favorite);
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
        Category category = publishItems.get(position);

        ImageLoader.getInstance().displayImage(APIFactory.MESTOSKIDKI_URL + "/" + category.getImageUrl(), holder.image, UIL.getImageOptions());

        holder.name.setText(category.getName());

        if (category.isFavorite()) {
            holder.favorite.setImageResource(R.drawable.ic_star_amber_24dp);
        } else {
            holder.favorite.setImageResource(R.drawable.ic_star_border_amber_24dp);
        }

        holder.favorite.setTag(category);
        holder.favorite.setOnClickListener(v -> {
            Category clickedCategory = (Category) v.getTag();
            clickedCategory.setFavorite(!clickedCategory.isFavorite());
            notifyItemChanged(publishItems.indexOf(clickedCategory));
            categoryDAO.updateOrAddToDB(clickedCategory);
            if (currentFilter.equals(FILTER_FAVORITE)) {
                publishItems.remove(clickedCategory);
                if (publishItems.size() == 0){
                    callback.onEmpty();
                }
            }
        });

        holder.setClickListener((v, pos) -> callback.onItemClicked(v, publishItems.get(pos)));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<Category> newItems = (ArrayList<Category>) results.values;
                update(newItems);
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Category> FilteredArrayNames = new ArrayList<>();

                currentFilter = (String) constraint;
                if (constraint.equals(FILTER_ALL)) {
                    results.count = objects.size();
                    results.values = objects;
                    return results;
                } else {
                    for (Category category : objects) {
                        if (category.isFavorite()) {
                            FilteredArrayNames.add(category);
                        }
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                return results;
            }
        };

        return filter;
    }

    private void update(ArrayList<Category> newItems) {
        publishItems.beginBatchedUpdates();
        for (Category category : newItems) {
            int index = publishItems.indexOf(category);
            if (index == SortedList.INVALID_POSITION) {
                publishItems.add(category);
            } else {
                publishItems.updateItemAt(index, category);
            }
        }

        ArrayList<Category> arrayForRemoving = new ArrayList<>();
        for (int i = 0; i < publishItems.size(); i++) {
            Category category = publishItems.get(i);
            if (!newItems.contains(category)) {
                arrayForRemoving.add(category);
            }
        }
        for (Category category : arrayForRemoving) {
            publishItems.remove(category);
        }

        if (publishItems.size() == 0) {
            callback.onEmpty();
        }
        publishItems.endBatchedUpdates();
    }
}