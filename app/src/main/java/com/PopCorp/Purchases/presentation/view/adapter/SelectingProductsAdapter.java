package com.PopCorp.Purchases.presentation.view.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.comparator.ProductAlphabetComparator;
import com.PopCorp.Purchases.data.comparator.ProductCategoryComparator;
import com.PopCorp.Purchases.data.comparator.ProductDecoratorComparator;
import com.PopCorp.Purchases.data.model.Product;
import com.PopCorp.Purchases.presentation.decorator.ProductDecorator;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;

public class SelectingProductsAdapter extends RecyclerView.Adapter<SelectingProductsAdapter.ViewHolder> implements Filterable, FastScrollRecyclerView.SectionedAdapter {

    public static final String FILTER_ALL = "";
    public static final String FILTER_FAVORITE = "favorite";

    private final Context context;

    private RecyclerCallback<Product> callback;

    private ProductDecoratorComparator comparator = new ProductDecoratorComparator();

    private ArrayList<Product> objects;
    private ArrayList<Product> selected;
    private final SortedList<ProductDecorator> publishItems;

    public SelectingProductsAdapter(Context context, RecyclerCallback<Product> callback, ArrayList<Product> objects, ArrayList<Product> selected, Comparator<Product> productComparator) {
        this.context = context;
        this.callback = callback;
        this.objects = objects;
        this.selected = selected;
        this.comparator.setComparator(productComparator);
        publishItems = new SortedList<>(ProductDecorator.class, new SortedList.Callback<ProductDecorator>() {
            @Override
            public boolean areContentsTheSame(ProductDecorator oneItem, ProductDecorator twoItem) {
                return oneItem.equalsContent(twoItem);
            }

            @Override
            public boolean areItemsTheSame(ProductDecorator oneItem, ProductDecorator twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public int compare(ProductDecorator oneItem, ProductDecorator twoItem) {
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

    public void setComparator(Comparator<Product> comparator) {
        this.comparator.setComparator(comparator);
        update();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        String result = "";
        if (comparator.getComparator() instanceof ProductAlphabetComparator) {
            result = publishItems.get(position).getItem().getName().substring(0, 1);
        } else if (comparator.getComparator() instanceof ProductCategoryComparator) {
            result = publishItems.get(position).getCategory().getName();
        }
        return result;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View view;
        public final TextView headerName;
        public final ImageView minus;
        public final ImageView plus;
        public final TextView count;
        public final CheckBox checkBox;
        public final View countLayout;
        private ClickListener clickListener;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            headerName = (TextView) view.findViewById(R.id.header_text);
            checkBox = (CheckBox) view.findViewById(R.id.item_product_checkbox);
            minus = (ImageView) view.findViewById(R.id.item_product_image_minus);
            plus = (ImageView) view.findViewById(R.id.item_product_image_plus);
            count = (TextView) view.findViewById(R.id.item_product_edit_count);
            countLayout = view.findViewById(R.id.item_product_layout_count);
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
        ProductDecorator decorator = publishItems.get(position);

        if (decorator.isHeader()) {
            holder.headerName.setText(decorator.getName());
            if (decorator.getCategory() == null) {
                holder.headerName.setTextColor(context.getResources().getColor(getPrimaryColorForTheme()));
            } else {
                holder.headerName.setTextColor(decorator.getCategory().getColor());
            }
        } else {
            Product product = decorator.getItem();
            holder.checkBox.setText(product.getName());
            holder.checkBox.setTag(product);
            holder.checkBox.setOnCheckedChangeListener(null);
            if (selected.contains(product)) {
                holder.checkBox.setChecked(true);
                holder.countLayout.setVisibility(View.VISIBLE);
            } else {
                holder.checkBox.setChecked(false);
                holder.countLayout.setVisibility(View.GONE);
            }
            holder.checkBox.setOnCheckedChangeListener((compoundButton, checked) -> {
                Product item = (Product) compoundButton.getTag();
                if (checked) {
                    selected.add(item);
                    holder.countLayout.setVisibility(View.VISIBLE);
                } else {
                    selected.remove(item);
                    holder.countLayout.setVisibility(View.GONE);
                }
            });

            holder.count.setTag(product);
            holder.minus.setOnClickListener(v -> {
                if (holder.count.getText().toString().isEmpty()) {
                    holder.count.setText("1");
                }
                BigDecimal value = new BigDecimal(holder.count.getText().toString());
                if (value.doubleValue() >= 1) {
                    value = value.subtract(new BigDecimal("1"));
                    holder.count.setText(value.toString());
                }
                Product tagProduct = (Product) holder.count.getTag();
                tagProduct.setCount(value);
            });
            holder.plus.setOnClickListener(v -> {
                if (holder.count.getText().toString().isEmpty()) {
                    holder.count.setText("1");
                }
                BigDecimal value = new BigDecimal(holder.count.getText().toString());
                value = value.add(new BigDecimal("1"));
                holder.count.setText(value.toString());
                Product tagProduct = (Product) holder.count.getTag();
                tagProduct.setCount(value);
            });
            holder.count.setText(product.getCountString());
            Drawable drawableCheck = context.getResources().getDrawable(R.drawable.abc_btn_check_material);
            if (drawableCheck != null) {
                if (product.getCategory() != null) {
                    drawableCheck.setColorFilter(product.getCategory().getColor(), PorterDuff.Mode.SRC_IN);
                } else {
                    drawableCheck.setColorFilter(context.getResources().getColor(R.color.md_blue_grey_500), PorterDuff.Mode.SRC_IN);
                }
                holder.checkBox.setButtonDrawable(drawableCheck);
            }
        }

        holder.setClickListener((v, position1) -> {
            ProductDecorator decor = publishItems.get(position1);
            if (!decor.isHeader() && decor.getItem() != null) {
                Product item = decor.getItem();
                if (selected.contains(item)) {
                    selected.remove(item);
                    //holder.checkBox.setChecked(false);
                } else {
                    selected.add(item);
                    //holder.checkBox.setChecked(true);
                }
                publishItems.updateItemAt(position1, decor);
            }
        });
    }

    private int getPrimaryColorForTheme() {
        int[] attrs = new int[]{android.R.attr.textColorPrimary};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        typedArray.recycle();
        return backgroundResource;
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
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_selecting, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<Product> newItems = (ArrayList<Product>) results.values;
                update(newItems);
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Product> FilteredArrayNames = new ArrayList<>();

                if (constraint.equals(FILTER_ALL)) {
                    results.count = objects.size();
                    results.values = objects;
                    return results;
                } else if (constraint.equals(FILTER_FAVORITE)) {
                    for (Product item : objects) {
                        if (item.isFavorite() || selected.contains(item)) {
                            FilteredArrayNames.add(item);
                        }
                    }
                } else {
                    String filter = constraint.toString().toLowerCase();
                    for (Product product : objects) {
                        if (product.getName().toLowerCase().contains(filter)) {
                            FilteredArrayNames.add(product);
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

    protected void update(ArrayList<Product> products) {
        publishItems.beginBatchedUpdates();
        for (Product product : products) {
            ProductDecorator decorator = new ProductDecorator("", false, product, product.getCategory());
            int index = publishItems.indexOf(decorator);
            if (index == SortedList.INVALID_POSITION) {
                publishItems.add(decorator);
            } else {
                publishItems.updateItemAt(index, decorator);
            }
        }

        ArrayList<ProductDecorator> arrayForRemoving = new ArrayList<>();
        for (int i = 0; i < publishItems.size(); i++) {
            ProductDecorator decorator = publishItems.get(i);
            if (!decorator.isHeader() && !products.contains(decorator.getItem())) {
                arrayForRemoving.add(decorator);
            }
        }
        for (ProductDecorator decorator : arrayForRemoving) {
            publishItems.remove(decorator);
        }

        ArrayList<ProductDecorator> headers = new ArrayList<>();
        if (comparator.getComparator() instanceof ProductCategoryComparator) {
            for (Product product : products) {
                ProductDecorator header;
                if (product.getCategory() != null) {
                    header = new ProductDecorator(product.getCategory().getName(), true, null, product.getCategory());
                } else {
                    header = new ProductDecorator(context.getString(R.string.header_no_category), true, null, null);
                }
                if (!headers.contains(header)) {
                    headers.add(header);
                }
            }
            for (ProductDecorator header : headers) {
                if (publishItems.indexOf(header) == SortedList.INVALID_POSITION) {
                    publishItems.add(header);
                }
            }
        }

        arrayForRemoving.clear();
        for (int i = 0; i < publishItems.size(); i++) {
            ProductDecorator decorator = publishItems.get(i);
            if (decorator.isHeader() && !headers.contains(decorator)) {
                arrayForRemoving.add(decorator);
            }
        }
        for (ProductDecorator header : arrayForRemoving) {
            publishItems.remove(header);
        }

        if (publishItems.size() == 0){
            callback.onEmpty();
        }
        publishItems.endBatchedUpdates();
    }

    private void update(){
        publishItems.beginBatchedUpdates();
        ArrayList<ProductDecorator> headers = new ArrayList<>();
        if (comparator.getComparator() instanceof ProductCategoryComparator) {
            for (int i = 0; i < publishItems.size(); i++) {
                ProductDecorator decorator = publishItems.get(i);
                if (!decorator.isHeader() && decorator.getItem()!= null){
                    ProductDecorator header = new ProductDecorator(decorator.getCategory().getName(), true, null, decorator.getCategory());
                    if (!headers.contains(header)) {
                        headers.add(header);
                    }
                }
            }
            for (ProductDecorator header : headers) {
                if (publishItems.indexOf(header) == SortedList.INVALID_POSITION) {
                    publishItems.add(header);
                }
            }
        }

        ArrayList<ProductDecorator> arrayForRemoving = new ArrayList<>();
        for (int i = 0; i < publishItems.size(); i++) {
            ProductDecorator decorator = publishItems.get(i);
            if (decorator.isHeader() && !headers.contains(decorator)) {
                arrayForRemoving.add(decorator);
            }
        }
        for (ProductDecorator header : arrayForRemoving) {
            publishItems.remove(header);
        }


        arrayForRemoving.clear();
        for (int i = 0; i < publishItems.size(); i++) {
            ProductDecorator decorator = publishItems.get(i);
            arrayForRemoving.add(decorator);
        }
        publishItems.clear();
        for (ProductDecorator decorator : arrayForRemoving) {
            publishItems.add(decorator);
        }
        publishItems.endBatchedUpdates();
    }
}
