package com.abdulrohman.sofraresturant.data.api;

import com.abdulrohman.sofraresturant.data.model.category.Categories;
import com.abdulrohman.sofraresturant.data.model.commission.Commission;
import com.abdulrohman.sofraresturant.data.model.connect.Connect;
import com.abdulrohman.sofraresturant.data.model.item.Item;
import com.abdulrohman.sofraresturant.data.model.order.Order;
import com.abdulrohman.sofraresturant.data.model.region.Region;
import com.abdulrohman.sofraresturant.data.model.resturant.Resturants;
import com.abdulrohman.sofraresturant.data.model.resturant.ResturantsInformatiom;
import com.abdulrohman.sofraresturant.data.model.review.Review;
import com.abdulrohman.sofraresturant.data.model.order.NewOrder;
import com.abdulrohman.sofraresturant.data.model.user.UserProfile;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface
ApiServer {
    @GET("restaurants")
    Call<Resturants> getAllResturans(@Query("page") int page);

    @GET("restaurants")
    Call<Resturants> getFilterResturans(@Query("keywork") String keywork,
                                        @Query("region_id") String regionId,
                                        @Query("page") int page);

    @GET("restaurants")
    Call<ResturantsInformatiom> getResturant(@Query("restaurant_id") int restaurantId);

    @GET("items")
    Call<Item> getItemFood(@Query("restaurant_id") int restaurantId,
                           @Query("category_id") int categoryId);


    //current
    //
    @GET("client/my-orders")
    Call<Order> getMyOrders(@Query("api_token") String apiToken,
                            @Query("state") String state,
                            @Query("page") int page);

    @GET("restaurant/my-orders")
    Call<Order> getMyOrdersRest(@Query("api_token") String apiToken,
                                @Query("state") String state,
                                @Query("page") int page);


    @FormUrlEncoded
    @POST("client/new-order")
    Call<NewOrder> createOrder(@Field("restaurant_id") String restaurantId,
                               @Field("note") String note,
                               @Field("address") String address,
                               @Field("payment_method_id") int paymentMethodId,
                               @Field("phone") String phone,
                               @Field("name") String name,
                               @Field("api_token") String api_token,
                               @Field("items[]") ArrayList<Integer> items,
                               @Field("quantities[]") ArrayList<String> quantities,
                               @Field("notes[]") ArrayList<String> notes

    );

    @GET("items")
    Call<Resturants> getFoodsResturant(@Query("restaurant_id") int restaurantId,
                                       @Query("page") int page);

    @GET("client/show-order")
    Call<NewOrder> showMyOrder(@Query("api_token") String apiToken,
                            @Query("order_id") int orderId);


    @GET("cities")
    Call<Region> getCities();

    @GET("regions")
    Call<Region> getRegion(@Query("city_id") int cityId);

    @FormUrlEncoded
    @POST("contact")
    Call<Connect> putConnect(@Field("name") String name,
                             @Field("email") String email,
                             @Field("phone") String phone,
                             @Field("type") String type,
                             @Field("content") String content);

    @GET("offers")
    Call<Item> getOffers();

    @GET("offer")
    Call<Item> getOfferDetial(@Query("offer_id") int offerId);

    @FormUrlEncoded
    @POST("client/login")
    Call<UserProfile> createLoginClient(@Field("email") String email,
                                        @Field("password") String password);


    @FormUrlEncoded
    @POST("restaurant/login")
    Call<UserProfile> createLoginRsturante(@Field("email") String email,
                                           @Field("password") String password);

    @FormUrlEncoded
    @POST("client/reset-password")
    Call<UserProfile> setReset1(@Field("email") String email);

    @FormUrlEncoded
    @POST("client/new-password")
    Call<UserProfile> setReset2(@Field("code") String code,
                                @Field("password") String password,
                                @Field("password_confirmation") String passwordConfirmation);

    @Multipart
    @POST("client/sign-up")
    Call<UserProfile> register(@Part("name") RequestBody name,
                               @Part("email") RequestBody email,
                               @Part("password") RequestBody password,
                               @Part("password_confirmation") RequestBody passwordConfirmation,
                               @Part("phone") RequestBody phone,
                               @Part("region_id") RequestBody regionId,
                               @Part MultipartBody.Part profileImage);

    @Multipart
    @POST("restaurant/sign-up")
    Call<Resturants> registerResturant(@Part("name") RequestBody name,
                                       @Part("email") RequestBody email,
                                       @Part("password") RequestBody password,
                                       @Part("password_confirmation") RequestBody passwordConfirmation,
                                       @Part("phone") RequestBody phone,
                                       @Part("whatsapp") RequestBody whatsapp,
                                       @Part("region_id") RequestBody regionId,
                                       @Part("delivery_cost") RequestBody deliveryCost,
                                       @Part("minimum_charger") RequestBody minimumCharger,
                                       @Part("delivery_time") RequestBody deliveryTime,
                                       @Part MultipartBody.Part profileImage);


    @FormUrlEncoded
    @POST("client/restaurant/review")
    Call<Review> createComment(@Field("rate") String rate,
                               @Field("comment") String comment,
                               @Field("restaurant_id") String restaurant_id,
                               @Field("api_token") String apiToken);

    @GET("restaurant/reviews")
    Call<Review> getCommentResturant(@Query("api_token") String apiToken,
                                     @Query("restaurant_id") String restaurantId,
                                     @Query("page") int page);


    @FormUrlEncoded
    @POST("client/confirm-order")
    Call<Order> approveOrder(@Field("order_id") int orderId,
                             @Field("api_token") String apiToken);

    @FormUrlEncoded
    @POST("client/decline-order")
    Call<Order> declineOrder(@Field("order_id") int orderId,
                             @Field("api_token") String apiToken);

    @FormUrlEncoded
    @POST("restaurant/reject-order")
    Call<Order> rejectOrderRest(@Field("order_id") int orderId,
                                @Field("api_token") String apiToken,
                                @Field("refuse_reason") String refuseReason);

    @FormUrlEncoded
    @POST("restaurant/accept-order")
    Call<Order> acceptOrderRest(@Field("order_id") int orderId,
                                @Field("api_token") String apiToken);

    @FormUrlEncoded
    @POST("restaurant/confirm-order")
    Call<Order> confirmOrderRest(@Field("order_id") int orderId,
                                 @Field("api_token") String apiToken);

    @GET("restaurant/show-order")
    Call<Order> showOrderRest(@Query("order_id") int orderId,
                              @Query("api_token") String apiToken);

    @GET("restaurant/my-categories")
    Call<Resturants> getMyCategories(@Query("api_token") String apiToken);

    @GET("categories")
    Call<Categories> getMyCategoriesHorz(@Query("restaurant_id") String restaurantId);

    @Multipart
    @POST("restaurant/new-category")
    Call<Resturants> createCategry(@Part("name") RequestBody name,
                                   @Part MultipartBody.Part photo,
                                   @Part("api_token") RequestBody apiToken);

    @FormUrlEncoded
    @POST("restaurant/delete-category")
    Call<Resturants> deleteCatg(@Field("category_id") int catgId,
                                @Field("api_token") String apiToken);

    @Multipart
    @POST("restaurant/update-category")
    Call<Resturants> updateCatg(@Part("name") RequestBody name,
                                @Part MultipartBody.Part imagItem,
                                @Part("item_id") RequestBody itemId,
                                @Part("category_id") RequestBody apiToken);

    @GET("restaurant/my-items")
    Call<Item> getFoodsMadeByResturant(@Query("api_token") String apiToken,
                                       @Query("category_id") int categoryId);

    @FormUrlEncoded
    @POST("restaurant/delete-item")
    Call<Item> deleteItem(@Field("item_id") int itemId,
                          @Field("api_token") String apiToken);

    @Multipart
    @POST("restaurant/update-item")
    Call<Item> updateItem(@Part("description") RequestBody description,
                          @Part("price") RequestBody price,
                          @Part("preparing_time") RequestBody preparingTime,
                          @Part("name") RequestBody name,
                          @Part MultipartBody.Part imagItem,
                          @Part("item_id") RequestBody itemId,
                          @Part("api_token") RequestBody apiToken,
                          @Part("offer_price") RequestBody offerPrice);

    @Multipart
    @POST("restaurant/new-item")
    Call<Item> createItem(@Part("description") RequestBody description,
                          @Part("price") RequestBody price,
                          @Part("preparing_time") RequestBody preparingTime,
                          @Part("name") RequestBody name,
                          @Part MultipartBody.Part imagItem,
                          @Part("category_id") RequestBody categoryId,
                          @Part("api_token") RequestBody apiToken,
                          @Part("offer_price") RequestBody offerPrice);

    @Multipart
    @POST("restaurant/new-offer")
    Call<Item> createOfferRest(@Part("description") RequestBody description,
                               @Part("starting_at") RequestBody startingAt,
                               @Part("name") RequestBody name,
                               @Part MultipartBody.Part imagItem,
                               @Part("ending_at") RequestBody endingAt,
                               @Part("api_token") RequestBody apiToken);

    @Multipart
    @POST("restaurant/update-offer")
    Call<Item> updateOfferRest(@Part("description") RequestBody description,
                               @Part("starting_at") RequestBody startingAt,
                               @Part("name") RequestBody name,
                               @Part MultipartBody.Part imagItem,
                               @Part("ending_at") RequestBody endingAt,
                               @Part("api_token") RequestBody apiToken);

    @GET("restaurant/my-offers")
    Call<Item> getOffersRest(@Query("api_token") String apiToken,
                             @Query("page") int page);

    @FormUrlEncoded
    @POST("restaurant/delete-offer")
    Call<Item> deleatOffersRest(@Field("api_token") String apiToken,
                                @Field("offer_id") int offerId);


    @Multipart
    @POST("restaurant/profile")
    Call<UserProfile> editProfileResturant(@Part("api_token") RequestBody apiToken,
                                           @Part("email") RequestBody email,
                                           @Part("name") RequestBody name,
                                           @Part("phone") RequestBody phone,
                                           @Part("region_id") RequestBody regionId,
                                           @Part("delivery_cost") RequestBody deliveryCost,
                                           @Part("minimum_charger") RequestBody minimumCharger,
                                           @Part("availability") RequestBody availability,
                                           @Part MultipartBody.Part photo,
                                           @Part("delivery_time") RequestBody deliveryTime);

    @FormUrlEncoded
    @POST("restaurant/profile")
    Call<UserProfile> getProfileResturant(@Field("api_token") String apiToken);

    @FormUrlEncoded
    @POST("client/profile")
    Call<UserProfile> getProfileClient(@Field("api_token") String apiToken);

    @Multipart
    @POST("client/profile")
    Call<UserProfile> editProfileClient(@Part("api_token") RequestBody apiToken,
                                        @Part("name") RequestBody name,
                                        @Part("email") RequestBody email,
                                        @Part("phone") RequestBody phone,
                                        @Part("region_id") RequestBody regionId,
                                        @Part MultipartBody.Part profileImage);

    @Multipart
    @POST("client/profile")
    Call<UserProfile> editProfileClientWithoutImg(@Part("api_token") RequestBody apiToken,
                                                  @Part("name") RequestBody name,
                                                  @Part("email") RequestBody email,
                                                  @Part("phone") RequestBody phone,
                                                  @Part("region_id") RequestBody regionId);

    @FormUrlEncoded
    @POST("restaurant/change-password")
    Call<UserProfile> changePassRest(@Field("api_token") String apiToken,
                                     @Field("old_password") String oldPassword,
                                     @Field("password") String password,
                                     @Field("password_confirmation") String passwordConfirmation);

    @GET("restaurant/commissions")
    Call<Commission> getCommission(@Query("api_token") String apiToken);


}
