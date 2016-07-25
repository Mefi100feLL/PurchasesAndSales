package com.PopCorp.Purchases.data.utils;

import com.PopCorp.Purchases.data.dao.ListItemSaleDAO;
import com.PopCorp.Purchases.data.dao.skidkaonline.CityDAO;
import com.PopCorp.Purchases.data.dao.skidkaonline.SaleCommentDAO;
import com.PopCorp.Purchases.data.dao.skidkaonline.SaleDAO;
import com.PopCorp.Purchases.data.dao.skidkaonline.ShopDAO;
import com.PopCorp.Purchases.data.model.skidkaonline.City;
import com.PopCorp.Purchases.data.model.skidkaonline.Sale;
import com.PopCorp.Purchases.data.model.skidkaonline.Shop;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Calendar;
import java.util.List;

public class SkidkaonlineSalesCleaner {

    private CityDAO cityDAO = new CityDAO();
    private SaleDAO saleDAO = new SaleDAO();
    private ShopDAO shopDAO = new ShopDAO();
    private SaleCommentDAO saleCommentDAO = new SaleCommentDAO();
    private ListItemSaleDAO listItemSaleDAO = new ListItemSaleDAO();

    public void start(){
        List<City> regions = cityDAO.getAllCities();
        for (City city : regions){
            List<Shop> shops = shopDAO.getShopsForCity(city.getId());
            for (Shop shop : shops){
                Calendar today = Calendar.getInstance();
                List<Sale> sales = saleDAO.getSales(shop.getCityId(), shop.getUrl());
                for (Sale sale : sales){
                    Calendar saleDate = Calendar.getInstance();
                    saleDate.setTimeInMillis(sale.getPeriodEnd());
                    saleDate.add(Calendar.DAY_OF_YEAR, 1);
                    saleDate.set(Calendar.HOUR_OF_DAY, 0);
                    saleDate.set(Calendar.MINUTE, 30);
                    if (today.getTimeInMillis() > saleDate.getTimeInMillis()) {
                        saleDAO.remove(sale);
                        saleCommentDAO.removeForSale(sale.getId());
                        if (listItemSaleDAO.countWithImage(sale.getImageSmall()) == 0){
                            ImageLoader.getInstance().getDiskCache().remove(sale.getImageSmall());
                        }
                        if (listItemSaleDAO.countWithImage(sale.getImageBig()) == 0){
                            ImageLoader.getInstance().getDiskCache().remove(sale.getImageBig());
                        }
                    }
                }
            }
        }
    }
}
