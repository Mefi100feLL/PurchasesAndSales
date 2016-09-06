package com.PopCorp.Purchases.presentation.view.adapter;

import android.content.Context;
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
import com.PopCorp.Purchases.data.callback.FavoriteRecyclerCallback;
import com.PopCorp.Purchases.data.comparator.ShopComparator;
import com.PopCorp.Purchases.data.model.Shop;
import com.PopCorp.Purchases.data.net.APIFactory;
import com.PopCorp.Purchases.data.utils.UIL;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ViewHolder> implements Filterable {

    public static final String FILTER_ALL = "";
    public static final String FILTER_FAVORITE = "favorite";

    private final Context context;
    private final FavoriteRecyclerCallback<Shop> callback;
    private final ShopComparator comparator = new ShopComparator();

    private ArrayList<Shop> objects;
    private final SortedList<Shop> publishItems;


    public ShopsAdapter(Context context, FavoriteRecyclerCallback<Shop> callback, ArrayList<Shop> objects) {
        this.context = context;
        this.callback = callback;
        this.objects = objects;
        publishItems = new SortedList<>(Shop.class, new SortedList.Callback<Shop>() {
            @Override
            public boolean areContentsTheSame(Shop oneItem, Shop twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public boolean areItemsTheSame(Shop oneItem, Shop twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public int compare(Shop oneItem, Shop twoItem) {
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
        public final TextView count;
        public final ImageView favorite;
        private ClickListener clickListener;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            count = (TextView) view.findViewById(R.id.sales_count);
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
        Shop shop = publishItems.get(position);

        ImageLoader.getInstance().displayImage(APIFactory.MESTOSKIDKI_URL + "/" + shop.getImageUrl(), holder.image, UIL.getImageOptions());

        holder.name.setText(shop.getName());
        holder.count.setText(context.getString(R.string.count_of_sales).replace("count", String.valueOf(shop.getCountSales())));

        if (shop.isFavorite()) {
            holder.favorite.setImageResource(R.drawable.ic_star_amber_24dp);
        } else {
            holder.favorite.setImageResource(R.drawable.ic_star_border_amber_24dp);
        }

        holder.favorite.setTag(shop);
        holder.favorite.setOnClickListener(v -> {
            Shop clickedShop = (Shop) v.getTag();
            callback.onFavoriteClicked(clickedShop);
        });

        holder.setClickListener((v, position1) -> callback.onItemClicked(v, publishItems.get(position1)));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<Shop> newItems = (ArrayList<Shop>) results.values;
                update(newItems);
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Shop> FilteredArrayNames = new ArrayList<>();

                if (constraint.equals(FILTER_ALL)) {
                    results.count = objects.size();
                    results.values = objects;
                    return results;
                } else {
                    for (Shop shop : objects) {
                        if (shop.isFavorite()) {
                            FilteredArrayNames.add(shop);
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

    private void update(ArrayList<Shop> newItems) {
        publishItems.beginBatchedUpdates();
        for (Shop shop : newItems) {
            int index = publishItems.indexOf(shop);
            if (index == SortedList.INVALID_POSITION) {
                publishItems.add(shop);
            } else {
                publishItems.updateItemAt(index, shop);
            }
        }

        ArrayList<Shop> arrayForRemoving = new ArrayList<>();
        for (int i = 0; i < publishItems.size(); i++) {
            Shop shop = publishItems.get(i);
            if (!newItems.contains(shop)) {
                arrayForRemoving.add(shop);
            }
        }
        for (Shop shop : arrayForRemoving) {
            publishItems.remove(shop);
        }

        if (publishItems.size() == 0) {
            callback.onEmpty();
        }
        publishItems.endBatchedUpdates();
    }
}