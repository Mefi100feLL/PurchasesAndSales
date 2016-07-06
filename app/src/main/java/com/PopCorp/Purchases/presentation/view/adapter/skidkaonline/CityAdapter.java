package com.PopCorp.Purchases.presentation.view.adapter.skidkaonline;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.callback.RecyclerCallback;
import com.PopCorp.Purchases.data.comparator.skidkaonline.CityComparator;
import com.PopCorp.Purchases.data.model.skidkaonline.City;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> implements Filterable, SectionIndexer {

    private final RecyclerCallback<City> callback;
    private final CityComparator comparator = new CityComparator();

    private ArrayList<City> objects;
    private final SortedList<City> publishItems;
    private final ArrayList<Section> sections = new ArrayList<>();

    private City selectedCity;


    public CityAdapter(RecyclerCallback<City> callback, ArrayList<City> objects) {
        this.callback = callback;
        this.objects = objects;
        publishItems = new SortedList<>(City.class, new SortedList.Callback<City>() {
            @Override
            public boolean areContentsTheSame(City oneItem, City twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public boolean areItemsTheSame(City oneItem, City twoItem) {
                return oneItem.equals(twoItem);
            }

            @Override
            public int compare(City oneItem, City twoItem) {
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

    public void setSelectedCity(City selectedCity) {
        this.selectedCity = selectedCity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View view;
        public final TextView name;
        public final TextView region;
        public final View mainLayout;
        private ClickListener clickListener;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = (TextView) view.findViewById(R.id.name);
            region = (TextView) view.findViewById(R.id.region);
            mainLayout = view.findViewById(R.id.main_layout);
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
        City city = publishItems.get(position);

        holder.name.setText(city.getName());

        if (city.getRegion().isEmpty()){
            holder.region.setVisibility(View.GONE);
        } else{
            holder.region.setVisibility(View.VISIBLE);
            holder.region.setText(city.getRegion());
        }

        if (city.equals(selectedCity)){
            holder.mainLayout.setBackgroundResource(R.color.md_btn_selected);
        } else {
            holder.mainLayout.setBackgroundResource(R.drawable.list_selector);
        }

        holder.setClickListener((v, pos) -> callback.onItemClicked(v, publishItems.get(pos)));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<City> newItems = (ArrayList<City>) results.values;
                update(newItems);
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<City> FilteredArrayNames = new ArrayList<>();

                if (constraint.equals("")) {
                    results.count = objects.size();
                    results.values = objects;
                    return results;
                } else {
                    for (City city : objects) {
                        if (city.getName().contains(constraint) || city.getRegion().contains(constraint)) {
                            FilteredArrayNames.add(city);
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

    private void update(ArrayList<City> newItems) {
        sections.clear();
        publishItems.beginBatchedUpdates();
        for (City city : newItems) {
            Section section = new Section(city.getName().substring(0, 1), objects.indexOf(city));
            if (!sections.contains(section)) {
                sections.add(section);
            }
            int index = publishItems.indexOf(city);
            if (index == SortedList.INVALID_POSITION) {
                publishItems.add(city);
            } else {
                publishItems.updateItemAt(index, city);
            }
        }

        ArrayList<City> arrayForRemoving = new ArrayList<>();
        for (int i = 0; i < publishItems.size(); i++) {
            City city = publishItems.get(i);
            if (!newItems.contains(city)) {
                arrayForRemoving.add(city);
            }
        }
        for (City city : arrayForRemoving) {
            publishItems.remove(city);
        }

        if (publishItems.size() == 0) {
            callback.onEmpty();
        }
        publishItems.endBatchedUpdates();
    }

    @Override
    public Object[] getSections() {
        return sections.toArray(new Section[sections.size()]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return sections.get(sectionIndex).getPosition();
    }

    @Override
    public int getSectionForPosition(int position) {
        String word = objects.get(position).getName().substring(0, 1);
        for (Section section : sections) {
            if (section.getSection().equals(word)) {
                return sections.indexOf(section);
            }
        }
        return 0;
    }

    private class Section {

        private String section;
        private int position;

        public Section(String section, int position) {
            setSection(section);
            setPosition(position);
        }

        @Override
        public boolean equals(Object object) {
            Section section = (Section) object;
            return section.getSection().equals(getSection());
        }

        @Override
        public String toString() {
            return getSection();
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}