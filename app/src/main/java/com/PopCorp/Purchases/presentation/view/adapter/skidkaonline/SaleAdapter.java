package com.PopCorp.Purchases.presentation.view.adapter.skidkaonline;

import android.support.percent.PercentFrameLayout;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.FavoriteRecyclerCallback;
import com.PopCorp.Purchases.data.comparator.skidkaonline.SaleDecoratorComparator;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.utils.UIL;
import com.PopCorp.Purchases.presentation.decorator.skidkaonline.SaleDecorator;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.ViewHolder> implements Filterable {

    private final FavoriteRecyclerCallback<Sale> callback;
    private final SaleDecoratorComparator comparator = new SaleDecoratorComparator();

    private ArrayList<Sale> objects;
    private final SortedList<SaleDecorator> publishItems;

    public SaleAdapter(FavoriteRecyclerCallback<Sale> callback, ArrayList<Sale> objects) {
        this.callback = callback;
        this.objects = objects;
        publishItems = new SortedList<>(SaleDecorator.class, new SortedList.Callback<SaleDecorator>() {
            @Override
            public boolean areContentsTheSame(SaleDecorator oneItem, SaleDecorator twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public boolean areItemsTheSame(SaleDecorator oneItem, SaleDecorator twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public int compare(SaleDecorator oneItem, SaleDecorator twoItem) {
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

    public ArrayList<Sale> getSales() {
        ArrayList<Sale> result = new ArrayList<>();
        for (int i = 0; i < publishItems.size(); i++) {
            SaleDecorator decorator = publishItems.get(i);
            if (!decorator.isHeader() && decorator.getSale() != null) {
                result.add(decorator.getSale());
            }
        }
        return result;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View view;
        public final TextView headerName;
        public final PercentFrameLayout layout;
        public final ImageView favorite;
        public final ImageView image;
        private ClickListener clickListener;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            headerName = (TextView) view.findViewById(R.id.header_text);
            layout = (PercentFrameLayout) view.findViewById(R.id.layout);
            favorite = (ImageView) view.findViewById(R.id.favorite);
            image = (ImageView) view.findViewById(R.id.image);
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
        SaleDecorator decorator = publishItems.get(position);

        if (decorator.isHeader()) {
            holder.headerName.setText(decorator.getName());
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        } else {
            Sale sale = decorator.getSale();

            if (sale.isFavorite()) {
                holder.favorite.setImageResource(R.drawable.ic_star_white_24dp);
            } else {
                holder.favorite.setImageResource(R.drawable.ic_star_border_white_24dp);
            }

            holder.favorite.setTag(sale);
            holder.favorite.setOnClickListener(v -> {
                Sale clickedSale = (Sale) v.getTag();
                callback.onFavoriteClicked(clickedSale);
                if (clickedSale.isFavorite()) {
                    ((ImageView) v).setImageResource(R.drawable.ic_star_white_24dp);
                } else {
                    ((ImageView) v).setImageResource(R.drawable.ic_star_border_white_24dp);
                }
            });
            ImageLoader.getInstance().displayImage(sale.getImageSmall(), holder.image, UIL.getImageOptions());
        }

        holder.setClickListener((v, pos) -> {
            SaleDecorator saleDecorator = publishItems.get(pos);
            if (!saleDecorator.isHeader()) {
                callback.onItemClicked(v, saleDecorator.getSale());
            }
        });
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
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skidkaonline_sale, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<Sale> newItems = (ArrayList<Sale>) results.values;
                update(newItems);
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Sale> FilteredArrayNames = new ArrayList<>();

                if (constraint.equals("")) {
                    results.count = objects.size();
                    results.values = objects;
                    return results;
                } else {
                    for (Sale sale : objects) {
                        if (sale.getCatalog().equals(constraint)) {
                            FilteredArrayNames.add(sale);
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

    private void update(ArrayList<Sale> newItems) {
        publishItems.beginBatchedUpdates();
        ArrayList<SaleDecorator> arrayForRemoving = new ArrayList<>();
        if (newItems.size() != 0) {
            for (Sale sale : newItems) {
                boolean finded = false;
                for (int i = 0; i < publishItems.size(); i++) {
                    SaleDecorator decorator = publishItems.get(i);
                    if (decorator.isHeader()) {
                        continue;
                    }
                    if (!newItems.contains(decorator.getSale())) {
                        arrayForRemoving.add(decorator);
                    }
                    if (decorator.getSale().equals(sale)) {
                        finded = true;
                        publishItems.updateItemAt(i, decorator);
                    }
                }
                if (!finded) {
                    publishItems.add(new SaleDecorator("", false, sale, sale.getCatalog()));
                }
            }
        } else {
            for (int i = 0; i < publishItems.size(); i++) {
                SaleDecorator decorator = publishItems.get(i);
                if (decorator.getSale() != null && !newItems.contains(decorator.getSale())) {
                    arrayForRemoving.add(decorator);
                }
            }
        }
        for (SaleDecorator decorator : arrayForRemoving) {
            publishItems.remove(decorator);
        }
        ArrayList<SaleDecorator> headers = new ArrayList<>();
        for (int i = 0; i < publishItems.size(); i++) {
            SaleDecorator decorator = publishItems.get(i);
            if (decorator.isHeader()) {
                continue;
            }
            SaleDecorator header = new SaleDecorator(decorator.getSale().getCatalog(), true, null, decorator.getSale().getCatalog());
            if (!headers.contains(header)) {
                headers.add(header);
            }
        }
        for (SaleDecorator decorator : headers) {
            if (publishItems.indexOf(decorator) == SortedList.INVALID_POSITION) {
                publishItems.add(decorator);
            }
        }
        arrayForRemoving.clear();
        for (int i = 0; i < publishItems.size(); i++) {
            SaleDecorator decorator = publishItems.get(i);
            if (decorator.isHeader() && !headers.contains(decorator)) {
                arrayForRemoving.add(decorator);
            }
        }
        for (SaleDecorator decorator : arrayForRemoving) {
            publishItems.remove(decorator);
        }

        if (publishItems.size() == 0) {
            callback.onEmpty();
        }
        publishItems.endBatchedUpdates();
    }
}
