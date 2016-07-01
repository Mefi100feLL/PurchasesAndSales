package com.PopCorp.Purchases.data.net;

import com.PopCorp.Purchases.data.dto.CommentResult;
import com.PopCorp.Purchases.data.model.Category;
import com.PopCorp.Purchases.data.model.Region;
import com.PopCorp.Purchases.data.model.Sale;
import com.PopCorp.Purchases.data.model.Shop;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface API {

    //  Mestoskidki

    @GET("/mestoskidki/cities")
    Observable<List<Region>> getRegions();

    @GET("/mestoskidki/categories")
    Observable<List<Category>> getCategories();

    @FormUrlEncoded
    @POST("/mestoskidki/shops")
    Observable<List<Shop>> getShops(@Field("city") int regionId);

    @FormUrlEncoded
    @POST("/mestoskidki/sales")
    Observable<List<Sale>> getSales(@Field("city") int regionId,
                                    @Field("shops") String shops,
                                    @Field("categs") String categories,
                                    @Field("categs_types") String categoriesTypes);

    @FormUrlEncoded
    @POST("/mestoskidki/sale")
    Observable<Sale> getSale(@Field("city") int regionId,
                             @Field("id") int saleId);

    @FormUrlEncoded
    @POST("/mestoskidki//comments/new")
    Observable<CommentResult> sendComment(@Field("author") String author,
                                          @Field("whom") String whom,
                                          @Field("text") String text,
                                          @Field("city") int regionId,
                                          @Field("id") int saleId);

    //  Skidkaonline
}
