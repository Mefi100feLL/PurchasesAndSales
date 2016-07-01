package com.PopCorp.Purchases.presentation.view.adapter;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.model.ListItemCategory;

import java.util.List;

public class ListItemCategoriesAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<ListItemCategory> categories;

    public ListItemCategoriesAdapter(Context context, List<ListItemCategory> categories) {
        super(context, R.layout.item_listitem_category);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_listitem_category, parent, false);
        }
        ShapeDrawable coloredCircle = new ShapeDrawable(new OvalShape());
        coloredCircle.getPaint().setColor(categories.get(position).getColor());
        view.findViewById(R.id.category_image).setBackgroundDrawable(coloredCircle);
        ((TextView) view.findViewById(R.id.category_name)).setText(categories.get(position).getName());
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_listitem_category, parent, false);
        }ShapeDrawable coloredCircle = new ShapeDrawable(new OvalShape());
        coloredCircle.getPaint().setColor(categories.get(position).getColor());
        view.findViewById(R.id.category_image).setBackgroundDrawable(coloredCircle);
        ((TextView) view.findViewById(R.id.category_name)).setText(categories.get(position).getName());
        return view;
    }

    @Override
    public int getCount()
    {
        return categories.size();
    }

    @Override
    public String getItem(int position)
    {
        return categories.get(position).getName();
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public int getItemPosition(Object obj){
        return categories.indexOf(obj);
    }
}