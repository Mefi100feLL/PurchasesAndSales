package com.PopCorp.Purchases.data.utils;

import com.PopCorp.Purchases.data.dao.ListItemSaleDAO;
import com.PopCorp.Purchases.data.dao.RegionDAO;
import com.PopCorp.Purchases.data.dao.SaleCommentDAO;
import com.PopCorp.Purchases.data.dao.SaleDAO;
import com.PopCorp.Purchases.data.dao.SameSaleDAO;
import com.PopCorp.Purchases.data.dao.ShopDAO;
import com.PopCorp.Purchases.data.model.Region;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.Shop;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public class SalesCleaner {

    private RegionDAO regionDAO = new RegionDAO();
    private SaleDAO saleDAO = new SaleDAO();
    private ShopDAO shopDAO = new ShopDAO();
    private SameSaleDAO sameSaleDAO = new SameSaleDAO();
    private SaleCommentDAO saleCommentDAO = new SaleCommentDAO();
    private ListItemSaleDAO listItemSaleDAO = new ListItemSaleDAO();

    public void start(){
        List<Region> regions = regionDAO.getAllRegions();
        for (Region region : regions){
            List<Shop> shops = shopDAO.getShopsForRegion(region.getId());
            for (Shop shop : shops){
                Calendar today = Calendar.getInstance();
                List<Sale> sales = saleDAO.getForShop(shop.getId());
                for (Sale sale : sales){
                    Calendar saleDate = Calendar.getInstance();
                    saleDate.setTimeInMillis(sale.getPeriodEnd());
                    saleDate.add(Calendar.DAY_OF_YEAR, 1);
                    saleDate.set(Calendar.HOUR_OF_DAY, 0);
                    saleDate.set(Calendar.MINUTE, 30);
                    if (today.getTimeInMillis() > saleDate.getTimeInMillis()) {
                        saleDAO.remove(sale);
                        saleCommentDAO.removeForSale(sale.getId());
                        sameSaleDAO.removeForSale(sale.getId());
                        if (listItemSaleDAO.countWithImage(sale.getImage()) == 0){
                            ImageLoader.getInstance().getDiskCache().remove(sale.getImage());
                        }
                    }
                }
            }
        }
    }
}
