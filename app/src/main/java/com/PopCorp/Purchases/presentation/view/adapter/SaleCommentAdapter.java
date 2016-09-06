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
import com.PopCorp.Purchases.data.comparator.SaleCommentComparator;
import com.PopCorp.Purchases.data.model.SaleComment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class SaleCommentAdapter extends RecyclerView.Adapter<SaleCommentAdapter.ViewHolder>{

    private final Context context;
    private final List<SaleComment> objects;
    private final SortedList<SaleComment> publishItems;

    private RecyclerCallback<SaleComment> callback;

    private SaleCommentComparator comparator = new SaleCommentComparator();

    public SaleCommentAdapter(Context context, RecyclerCallback<SaleComment> callback, List<SaleComment> objects) {
        this.context = context;
        this.callback = callback;
        this.objects = objects;
        publishItems = new SortedList<>(SaleComment.class, new SortedList.Callback<SaleComment>() {
            @Override
            public boolean areContentsTheSame(SaleComment oneItem, SaleComment twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public boolean areItemsTheSame(SaleComment oneItem, SaleComment twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public int compare(SaleComment oneItem, SaleComment twoItem) {
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
        public final TextView author;
        public final TextView whom;
        public final TextView text;
        public final TextView dateTime;
        public final TextView tmpText;
        private ClickListener clickListener;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            author = (TextView) view.findViewById(R.id.author);
            whom = (TextView) view.findViewById(R.id.whom);
            text = (TextView) view.findViewById(R.id.text);
            dateTime = (TextView) view.findViewById(R.id.date_time);
            tmpText = (TextView) view.findViewById(R.id.tmp_text);
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
        SaleComment saleComment = publishItems.get(position);

        holder.author.setText(saleComment.getAuthor());
        /*if (!saleComment.getWhom().isEmpty()){
            holder.whom.setVisibility(View.VISIBLE);
            holder.whom.setText(context.getString(R.string.whom).replace("name", saleComment.getWhom()));
        } else{
            holder.whom.setVisibility(View.GONE);
        }*/

        holder.whom.setVisibility(View.GONE);

        if (saleComment.getErrorText() != null || saleComment.getError() != 0 || saleComment.getTmpText() != 0){
            String tmpText = "";
            if (saleComment.getErrorText() != null){
                tmpText = saleComment.getErrorText() + context.getString(R.string.error_touch_for_retry);
                holder.tmpText.setTextColor(context.getResources().getColor(R.color.md_red_500));
            } else if (saleComment.getError() != 0){
                tmpText = context.getString(saleComment.getError()) + context.getString(R.string.error_touch_for_retry);
                holder.tmpText.setTextColor(context.getResources().getColor(R.color.md_red_500));
            } else if (saleComment.getTmpText() != 0){
                tmpText = context.getString(saleComment.getTmpText());
                holder.tmpText.setTextColor(context.getResources().getColor(R.color.secondary_text));
            }
            holder.tmpText.setText(tmpText);
            holder.dateTime.setVisibility(View.GONE);
            holder.tmpText.setVisibility(View.VISIBLE);
        } else{
            String dateTimeText;
            Calendar today = Calendar.getInstance();
            Calendar dateTime = Calendar.getInstance();
            dateTime.setTime(new Date(saleComment.getDateTime()));
            if (today.get(Calendar.DAY_OF_YEAR) != dateTime.get(Calendar.DAY_OF_YEAR)) {
                SimpleDateFormat format = new SimpleDateFormat("dd MMM, HH:mm", new Locale("ru"));
                dateTimeText = format.format(dateTime.getTime());
            } else {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm", new Locale("ru"));
                dateTimeText = format.format(dateTime.getTime());
            }
            holder.dateTime.setText(dateTimeText);
            holder.dateTime.setVisibility(View.VISIBLE);
            holder.tmpText.setVisibility(View.GONE);
        }

        holder.text.setText(saleComment.getText());

        holder.setClickListener((v, pos) -> callback.onItemClicked(v, publishItems.get(pos)));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(v);
    }

    public void update() {
        publishItems.beginBatchedUpdates();
        for (SaleComment comment : objects) {
            int index = publishItems.indexOf(comment);
            if (index == SortedList.INVALID_POSITION) {
                publishItems.add(comment);
            } else {
                publishItems.updateItemAt(index, comment);
            }
        }

        ArrayList<SaleComment> arrayForRemoving = new ArrayList<>();
        for (int i = 0; i < publishItems.size(); i++) {
            SaleComment comment = publishItems.get(i);
            if (!objects.contains(comment)) {
                arrayForRemoving.add(comment);
            }
        }
        for (SaleComment comment : arrayForRemoving) {
            publishItems.remove(comment);
        }
        publishItems.endBatchedUpdates();
    }
}
