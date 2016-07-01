package com.PopCorp.Purchases.presentation.view.adapter;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.comparator.SameSaleComparator;
import com.PopCorp.Purchases.data.model.SameSale;

import java.util.ArrayList;
import java.util.List;

public class SameSaleAdapter extends RecyclerView.Adapter<SameSaleAdapter.ViewHolder>{

    private final Context context;
    private final List<SameSale> objects;
    private final SortedList<SameSale> publishItems;
    private final RecyclerCallback<SameSale> callback;

    private SameSaleComparator comparator = new SameSaleComparator();

    public SameSaleAdapter(Context context, RecyclerCallback<SameSale> callback, List<SameSale> objects) {
        this.context = context;
        this.callback = callback;
        this.objects = objects;
        publishItems = new SortedList<>(SameSale.class, new SortedList.Callback<SameSale>() {
            @Override
            public boolean areContentsTheSame(SameSale oneItem, SameSale twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public boolean areItemsTheSame(SameSale oneItem, SameSale twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public int compare(SameSale oneItem, SameSale twoItem) {
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
        update();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View view;
        public final TextView period;
        public final TextView coastAndShop;
        public final TextView text;
        private ClickListener clickListener;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            period = (TextView) view.findViewById(R.id.period);
            coastAndShop = (TextView) view.findViewById(R.id.coast_and_shop);
            text = (TextView) view.findViewById(R.id.text);
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
        SameSale sameSale = publishItems.get(position);

        holder.text.setText(sameSale.getText());

        holder.coastAndShop.setText(context.getString(R.string.coast_in_shop).replace("coast", sameSale.getCoast()).replace("shop", sameSale.getShopName()));

        String period = sameSale.getPeriodStart();
        if (!sameSale.getPeriodStart().equals(sameSale.getPeriodEnd())){
            period += " - " + sameSale.getPeriodEnd();
        }
        holder.period.setText(period);

        holder.setClickListener((v, pos) -> callback.onItemClicked(v, publishItems.get(pos)));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_same_sale, parent, false);
        return new ViewHolder(v);
    }

    public void update() {
        publishItems.beginBatchedUpdates();
        for (SameSale sameSale : objects) {
            int index = publishItems.indexOf(sameSale);
            if (index == SortedList.INVALID_POSITION) {
                publishItems.add(sameSale);
            } else {
                publishItems.updateItemAt(index, sameSale);
            }
        }

        ArrayList<SameSale> arrayForRemoving = new ArrayList<>();
        for (int i = 0; i < publishItems.size(); i++) {
            SameSale sameSale = publishItems.get(i);
            if (!objects.contains(sameSale)) {
                arrayForRemoving.add(sameSale);
            }
        }
        for (SameSale sameSale : arrayForRemoving) {
            publishItems.remove(sameSale);
        }
        publishItems.endBatchedUpdates();
    }
}
