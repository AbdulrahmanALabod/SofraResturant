package com.abdulrohman.sofraresturant.data.model.item;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * itemData && offerData && catagories
 */
@Entity(tableName = "ItemData")
public class ItemData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("created_at")
    @Expose
    @ColumnInfo(name = "created_at")
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    @ColumnInfo(name = "updated_at")
    private String updatedAt;

    @SerializedName("last_name")
    @Expose
    @ColumnInfo(name = "last_name")
    private String name;

    @SerializedName("starting_at")
    @Expose
    @ColumnInfo(name = "starting_at")
    private String startingAt;

    @SerializedName("ending_at")
    @Expose
    @ColumnInfo(name = "ending_at")
    private String endingAt;

    @SerializedName("preparing_time")
    @Expose
    @ColumnInfo(name = "preparing_time")
    private String preparingTime;

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description")
    private String description;

    @SerializedName("price")
    @Expose
    @ColumnInfo(name = "price")
    private String price;

    @SerializedName("offer_price")
    @Expose
    @ColumnInfo(name = "offer_price")
    private String offerPrice;

    @SerializedName("photo")
    @Expose
    @ColumnInfo(name = "photo")
    private String photo;

    @SerializedName("restaurant_id")
    @Expose
    @ColumnInfo(name = "restaurant_id")
    private String restaurantId;

    @SerializedName("category_id")
    @Expose
    @ColumnInfo(name = "category_id")
    private String categoryId;

    @SerializedName("photo_url")
    @Expose
    @ColumnInfo(name = "photo_url")
    private String photoUrl;

    @SerializedName("has_offer")
    @Expose
    @ColumnInfo(name = "has_offer")
    private Boolean hasOffer;

    @SerializedName("quantity")
    @ColumnInfo(name = "quantity")
    @Expose
    private String quantity;

    @SerializedName("note")
    @ColumnInfo(name = "note")
    @Expose
    private String note;

    public String getStartingAt() {
        return startingAt;
    }

    public void setStartingAt(String startingAt) {
        this.startingAt = startingAt;
    }

    public String getEndingAt() {
        return endingAt;
    }

    public void setEndingAt(String endingAt) {
        this.endingAt = endingAt;
    }

    public String getPreparingTime() {
        return preparingTime;
    }

    public void setPreparingTime(String preparingTime) {
        this.preparingTime = preparingTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Boolean getHasOffer() {
        return hasOffer;
    }

    public void setHasOffer(Boolean hasOffer) {
        this.hasOffer = hasOffer;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
