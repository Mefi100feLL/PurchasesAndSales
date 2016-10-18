package com.PopCorp.Purchases.presentation.view.adapter.skidkaonline;

import android.support.v7.util.SortedList;
import android.support.v7.widget.GridLayoutManager;
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
import com.PopCorp.Purchases.data.comparator.skidkaonline.ShopDecoratorComparator;
import com.PopCorp.Purchases.data.model.skidkaonline.Shop;
import com.PopCorp.Purchases.data.utils.UIL;
import com.PopCorp.Purchases.presentation.decorator.skidkaonline.ShopDecorator;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.ReplaySubject;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> implements Filterable {

    public static final String FILTER_ALL = "";
    public static final String FILTER_FAVORITE = "favorite";

    private final FavoriteRecyclerCallback<Shop> callback;

    private final ShopDecoratorComparator comparator = new ShopDecoratorComparator();

    private ArrayList<Shop> objects;
    private final SortedList<ShopDecorator> publishItems;

    public ShopAdapter(FavoriteRecyclerCallback<Shop> callback, ArrayList<Shop> objects) {
        this.callback = callback;
        this.objects = objects;
        publishItems = new SortedList<>(ShopDecorator.class, new SortedList.Callback<ShopDecorator>() {
            @Override
            public boolean areContentsTheSame(ShopDecorator oneItem, ShopDecorator twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public boolean areItemsTheSame(ShopDecorator oneItem, ShopDecorator twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public int compare(ShopDecorator oneItem, ShopDecorator twoItem) {
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

    public void setLayoutManager(GridLayoutManager layoutManager, final int countColumns) {
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (publishItems.get(position).isHeader()) {
                    return countColumns;
                }
                return 1;
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View view;
        public final TextView headerName;
        public final ImageView image;
        public final TextView name;
        public final TextView count;
        public final ImageView favorite;
        private ClickListener clickListener;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            headerName = (TextView) view.findViewById(R.id.header_text);
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
        ShopDecorator decorator = publishItems.get(position);

        if (decorator.isHeader()) {
            holder.headerName.setText(decorator.getName());
        } else {
            Shop shop = decorator.getShop();

            ImageLoader.getInstance().displayImage(shop.getImage(), holder.image, UIL.getImageOptions());
            if (position == 1) {
                if (!publishSubject.hasCompleted()) {
                    publishSubject.onNext(holder.favorite);
                    publishSubject.onCompleted();
                }
            }

            holder.name.setText(shop.getName());
            holder.count.setVisibility(View.GONE);

            if (shop.isFavorite()) {
                holder.favorite.setImageResource(R.drawable.ic_star_amber_24dp);
            } else {
                holder.favorite.setImageResource(R.drawable.ic_star_border_amber_24dp);
            }

            holder.favorite.setTag(decorator);
            holder.favorite.setOnClickListener(v -> {
                Shop clickedShop = ((ShopDecorator) v.getTag()).getShop();
                callback.onFavoriteClicked(clickedShop);
                if (clickedShop.isFavorite()) {
                    ((ImageView) v).setImageResource(R.drawable.ic_star_amber_24dp);
                } else {
                    ((ImageView) v).setImageResource(R.drawable.ic_star_border_amber_24dp);
                }
            });
        }

        holder.setClickListener((v, pos) -> {
            ShopDecorator saleDecorator = publishItems.get(pos);
            if (!saleDecorator.isHeader()) {
                callback.onItemClicked(v, saleDecorator.getShop());
            }
        });
    }

    private ReplaySubject<View> publishSubject = ReplaySubject.create();

    public Observable<View> getFavoriteViews(){
        return publishSubject;
    }

    @Override
    public int getItemViewType(int position) {
        if (publishItems.get(position).isHeader()) {
            return 1;
        }
        return 2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v;
        if (position == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        }

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
        ArrayList<ShopDecorator> arrayForRemoving = new ArrayList<>();
        if (newItems.size() != 0) {
            for (Shop shop : newItems) {
                boolean finded = false;
                for (int i = 0; i < publishItems.size(); i++) {
                    ShopDecorator decorator = publishItems.get(i);
                    if (decorator.isHeader()) {
                        continue;
                    }
                    if (!newItems.contains(decorator.getShop())) {
                        arrayForRemoving.add(decorator);
                    }
                    if (decorator.getShop().equals(shop)) {
                        finded = true;
                        //publishItems.updateItemAt(i, decorator);
                    }
                }
                if (!finded) {
                    publishItems.add(new ShopDecorator(shop.getName(), false, shop, shop.getCategory()));
                }
            }
        } else {
            for (int i = 0; i < publishItems.size(); i++) {
                ShopDecorator decorator = publishItems.get(i);
                if (decorator.getShop() != null && !newItems.contains(decorator.getShop())) {
                    arrayForRemoving.add(decorator);
                }
            }
        }
        for (ShopDecorator decorator : arrayForRemoving) {
            publishItems.remove(decorator);
        }
        ArrayList<ShopDecorator> headers = new ArrayList<>();
        for (int i = 0; i < publishItems.size(); i++) {
            ShopDecorator decorator = publishItems.get(i);
            if (decorator.isHeader()) {
                continue;
            }
            ShopDecorator header = new ShopDecorator(decorator.getShop().getCategory().getName(), true, null, decorator.getShop().getCategory());
            if (!headers.contains(header)) {
                headers.add(header);
            }
        }
        for (ShopDecorator decorator : headers) {
            if (publishItems.indexOf(decorator) == SortedList.INVALID_POSITION) {
                publishItems.add(decorator);
            }
        }
        arrayForRemoving.clear();
        for (int i = 0; i < publishItems.size(); i++) {
            ShopDecorator decorator = publishItems.get(i);
            if (decorator.isHeader() && !headers.contains(decorator)) {
                arrayForRemoving.add(decorator);
            }
        }
        for (ShopDecorator decorator : arrayForRemoving) {
            publishItems.remove(decorator);
        }

        if (publishItems.size() == 0) {
            callback.onEmpty();
        }
        publishItems.endBatchedUpdates();
    }
}
