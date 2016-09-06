package com.PopCorp.Purchases.data.net;

import com.PopCorp.Purchases.data.dto.CommentResult;
import com.PopCorp.Purchases.data.dto.UniversalDTO;
import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.model.Region;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.SaleComment;
import com.PopCorp.Purchases.data.model.Shop;
import com.PopCorp.Purchases.data.model.skidkaonline.City;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface API {

    //  Mestoskidki

    @GET("/mestoskidki/cities")
    Observable<UniversalDTO<List<Region>>> getRegions();

    @GET("/mestoskidki/categories")
    Observable<UniversalDTO<List<Category>>> getCategories();

    @FormUrlEncoded
    @POST("/mestoskidki/shops")
    Observable<UniversalDTO<List<Shop>>> getShops(@Field("city") int regionId);

    @FormUrlEncoded
    @POST("/mestoskidki/sales")
    Observable<UniversalDTO<List<Sale>>> getSales(@Field("city") int regionId,
                                                  @Field("shops") String shops,
                                                  @Field("categs") String categories,
                                                  @Field("categs_types") String categoriesTypes);

    @FormUrlEncoded
    @POST("/mestoskidki/sale")
    Observable<UniversalDTO<Sale>> getSale(@Field("city") int regionId,
                                           @Field("id") int saleId);

    @FormUrlEncoded
    @POST("/mestoskidki/comments")
    Observable<UniversalDTO<List<SaleComment>>> getComments(@Field("sale") int saleId, @Field("city") int cityId);

    @FormUrlEncoded
    @POST("/mestoskidki//comments/new")
    Observable<UniversalDTO<SaleComment>> sendComment(@Field("author") String author,
                                          @Field("whom") String whom,
                                          @Field("text") String text,
                                          @Field("city") int regionId,
                                          @Field("id") int saleId);

    //  Skidkaonline
    @GET("/skidkaonline/cities")
    Observable<UniversalDTO<List<City>>> getSkidkaonlineCities();

    @FormUrlEncoded
    @POST("/skidkaonline/shops")
    Observable<UniversalDTO<List<com.PopCorp.Purchases.data.model.skidkaonline.Shop>>> getSkidkaonlineShops(@Field("city") int cityId);

    @FormUrlEncoded
    @POST("/skidkaonline/sales")
    Observable<UniversalDTO<List<com.PopCorp.Purchases.data.model.skidkaonline.Sale>>> getSkidkaonlineSales(@Field("city") int cityId,
                                                                                                            @Field("shop") String shopUrl);


    @FormUrlEncoded
    @POST("/skidkaonline/sale")
    Observable<UniversalDTO<com.PopCorp.Purchases.data.model.skidkaonline.Sale>> getSkidkaonlineSale(@Field("city") int cityId,
                                                                                                     @Field("id") int saleId);

    @FormUrlEncoded
    @POST("/skidkaonline/comments")
    Observable<UniversalDTO<List<com.PopCorp.Purchases.data.model.skidkaonline.SaleComment>>> getSkidkaonlineSaleComments(@Field("sale_id") int saleId);

    @FormUrlEncoded
    @POST("/skidkaonline/comments/new")
    Observable<UniversalDTO<com.PopCorp.Purchases.data.model.skidkaonline.SaleComment>> sendSkidkaonlineSaleComment(
            @Field("author") String author,
            @Field("text") String text,
            @Field("city") int cityId,
            @Field("sale_id") int saleId);
}
