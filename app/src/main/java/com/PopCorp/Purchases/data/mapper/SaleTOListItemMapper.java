package com.PopCorp.Purchases.data.mapper;

import com.PopCorp.Purchases.data.dao.ListItemCategoryDAO;
import com.PopCorp.Purchases.data.dao.skidkaonline.ShopDAO;
import com.PopCorp.Purchases.data.model.ListItem;
import com.PopCorp.Purchases.data.model.ListItemCategory;
import com.PopCorp.Purchases.data.model.ListItemSale;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.skidkaonline.Shop;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SaleToListItemMapper {

    public static ListItem getListItem(Sale sale){
        ListItem result;
        SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy", new Locale("ru"));
        List<ListItemCategory> categories = new ListItemCategoryDAO().getAllCategories();
        ListItemCategory listItemCategory = null;
        if (categories != null && categories.size() > 0) {
            listItemCategory = categories.get(0);
            for (ListItemCategory category : categories) {
                if (category.getName().contains("Акци")) {
                    listItemCategory = category;
                }
            }
        }
        String count = "1";
        String edizm = "шт ";
        if (sale.getQuantity() != null && !sale.getQuantity().isEmpty()) {
            String[] countSplit = sale.getQuantity().split(" ");
            count = countSplit[0];
            edizm = countSplit[1];
            switch (edizm) {
                case "г":
                    edizm = "кг";
                    count = new BigDecimal(count).divide(new BigDecimal("1000")).toString();
                    break;
                case "мл":
                    edizm = "литр";
                    count = new BigDecimal(count).divide(new BigDecimal("1000")).toString();
                    break;
            }
        }
        String coast = getCoast(sale, count);
        String comment = sale.getSubTitle();
        if (!comment.isEmpty()) {
            comment += "\n";
        }
        comment += format.format(new Date(sale.getPeriodStart())) + " - " + format.format(new Date(sale.getPeriodEnd()));

        result = new ListItem(
                -1,
                -1,
                sale.getTitle(),
                count,
                edizm,
                coast,
                listItemCategory,
                sale.getShop().getName(),
                comment,
                false,
                false,
                new ListItemSale(-1, sale.getImage(), sale.getId(), format.format(new Date(sale.getPeriodStart())), format.format(new Date(sale.getPeriodEnd())))
        );
        return result;
    }

    private static String getCoast(Sale sale, String count) {
        String result = "0";
        if (sale.getCoast() != null && !sale.getCoast().isEmpty()) {
            result = sale.getCoast().split(" ")[0];
        }
        if (sale.getCoastForQuantity() != null && !sale.getCoastForQuantity().isEmpty()) {
            String[] split = sale.getCoastForQuantity().split(" ");
            result = split[split.length - 2];
        } else {
            BigDecimal countDecimal = new BigDecimal(count);
            BigDecimal coastDecimal = new BigDecimal(result);
            result = countDecimal.multiply(coastDecimal).toString();
        }
        return result;
    }

    public static ListItem getListItem(com.PopCorp.Purchases.data.model.skidkaonline.Sale sale) {
        SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy", new Locale("ru"));
        List<ListItemCategory> categories = new ListItemCategoryDAO().getAllCategories();
        ListItemCategory listItemCategory = null;
        if (categories != null && categories.size() > 0) {
            listItemCategory = categories.get(0);
            for (ListItemCategory category : categories) {
                if (category.getName().contains("Акци")) {
                    listItemCategory = category;
                }
            }
        }
        String count = "1";
        String edizm = "шт ";
        String coast = "0";
        String shopName = "";
        Shop shop = new ShopDAO().getWithUrl(sale.getShopUrl(), sale.getCityId());
        if (shop != null) {
            shopName = shop.getName();
        }
        String comment = format.format(new Date(sale.getPeriodStart())) + " - " + format.format(new Date(sale.getPeriodEnd()));

        ListItem item = new ListItem(
                -1,
                -1,
                String.valueOf(sale.getId()),
                count,
                edizm,
                coast,
                listItemCategory,
                shopName,
                comment,
                false,
                false,
                new ListItemSale(-1, sale.getImageBig(), sale.getId(), format.format(new Date(sale.getPeriodStart())), format.format(new Date(sale.getPeriodEnd())))
        );
        return item;
    }
}
