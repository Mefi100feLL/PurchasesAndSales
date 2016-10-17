package com.PopCorp.Purchases.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.PopCorp.Purchases.R;
import com.PopCorp.Purchases.data.dao.ListItemCategoryDAO;
import com.PopCorp.Purchases.data.dao.ProductDAO;
import com.PopCorp.Purchases.data.db.strategy.VersionFiveUpdateStrategy;
import com.PopCorp.Purchases.data.db.strategy.VersionFourUpdateStrategy;
import com.PopCorp.Purchases.data.db.strategy.VersionSevenUpdateStrategy;
import com.PopCorp.Purchases.data.db.strategy.VersionSixUpdateStrategy;
import com.PopCorp.Purchases.data.db.strategy.VersionThreeUpdateStrategy;
import com.PopCorp.Purchases.data.db.strategy.VersionTwoUpdateStrategy;
import com.PopCorp.Purchases.data.model.ListItemCategory;
import com.PopCorp.Purchases.data.model.Product;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class UpdaterDB {

    public void update(Context context, SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            new VersionTwoUpdateStrategy().update(context, db);
        }
        if (oldVersion < 3) {
            new VersionThreeUpdateStrategy().update(context, db);
        }
        if (oldVersion < 4) {
            new VersionFourUpdateStrategy().update(context, db);
        }
        if (oldVersion < 5) {
            new VersionFiveUpdateStrategy().update(context, db);
        }
        if (oldVersion < 6) {
            new VersionSixUpdateStrategy().update(context, db);
        }
        if (oldVersion < 7){
            new VersionSevenUpdateStrategy().update(context, db);
        }
    }

    public static void addCategs(Context context, SQLiteDatabase db) {
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(context.getResources().getColor(R.color.md_brown_500));
        colors.add(context.getResources().getColor(R.color.md_orange_500));
        colors.add(context.getResources().getColor(R.color.md_blue_500));
        colors.add(context.getResources().getColor(R.color.md_teal_500));
        colors.add(context.getResources().getColor(R.color.md_deep_purple_500));
        colors.add(context.getResources().getColor(R.color.md_red_500));
        colors.add(context.getResources().getColor(R.color.md_green_500));

        categories.add(context.getResources().getString(R.string.default_categ_one));
        categories.add(context.getResources().getString(R.string.default_categ_two));
        categories.add(context.getResources().getString(R.string.default_categ_three));
        categories.add(context.getResources().getString(R.string.default_categ_four));
        categories.add(context.getResources().getString(R.string.default_categ_five));
        categories.add(context.getResources().getString(R.string.default_categ_six));
        categories.add(context.getResources().getString(R.string.default_categ_seven));

        ListItemCategoryDAO categoryDAO = new ListItemCategoryDAO(db);
        for (int i = 0; i < categories.size(); i++) {
            if (categoryDAO.getWithName(categories.get(i)) == null) {
                ListItemCategory category = new ListItemCategory(-1, categories.get(i), colors.get(i));
                categoryDAO.updateOrAddToDB(category);
            }
        }
    }


    public static void addAllProducts(Context context, SQLiteDatabase db) {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<String> counts = new ArrayList<>();
        ArrayList<String> coasts = new ArrayList<>();
        ArrayList<String> edizms = new ArrayList<>();
        try {
            XmlPullParser xpp = context.getResources().getXml(R.xml.all);
            String tag = "";
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_TAG:
                        if (xpp.getName().equals("name")) {
                            tag = "name";
                        } else if (xpp.getName().equals("category")) {
                            tag = "category";
                        } else if (xpp.getName().equals("count")) {
                            tag = "count";
                        } else if (xpp.getName().equals("coast")) {
                            tag = "coast";
                        } else if (xpp.getName().equals("edizm")) {
                            tag = "edizm";
                        } else {
                            tag = "";
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (tag.equals("name")) {
                            names.add(xpp.getText());
                        }
                        if (tag.equals("category")) {
                            categories.add(xpp.getText());
                        }
                        if (tag.equals("coast")) {
                            coasts.add(xpp.getText());
                        }
                        if (tag.equals("count")) {
                            counts.add(xpp.getText());
                        }
                        if (tag.equals("edizm")) {
                            edizms.add(xpp.getText());
                        }
                        break;
                    default:
                        break;
                }
                xpp.next();
            }
        } catch (XmlPullParserException | IOException ignored) {
        }

        ProductDAO dao = new ProductDAO(db);
        ListItemCategoryDAO categoryDAO = new ListItemCategoryDAO(db);
        for (int i = 0; i < names.size(); i++) {
            Product product = new Product(-1, names.get(i), counts.get(i), edizms.get(i), coasts.get(i), categoryDAO.getWithName(categories.get(i)), "", "", false);
            dao.updateOrAddToDB(product);
        }
    }

}
