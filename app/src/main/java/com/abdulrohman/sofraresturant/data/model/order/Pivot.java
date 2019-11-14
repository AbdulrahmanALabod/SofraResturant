package com.abdulrohman.sofraresturant.data.model.order;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Pivot {

    @SerializedName("order_id")
    @Expose
    @ColumnInfo(name = "order_id")
    private String orderId;
    @SerializedName("item_id")
    @ColumnInfo(name = "item_id")
    @Expose
    private String itemId;
    @SerializedName("price_pivot")
    @ColumnInfo(name = "price")
    @Expose
    private String price;
    @SerializedName("quantity")
    @ColumnInfo(name = "quantity")
    @Expose
    private String quantity;
    @SerializedName("note")
    @ColumnInfo(name = "note")
    @Expose
    private String note;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
