package com.abdulrohman.sofraresturant.data.local.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.abdulrohman.sofraresturant.data.model.item.ItemData;

import java.util.List;


@Dao
public interface ItemDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(ItemData itemData);

    @Delete
    public void delete(ItemData item);

    @Query("SELECT * FROM ItemData ")
    public List<ItemData> getAllOrder();

    @Update
    void updateCount(List<ItemData> itemsData);
}
