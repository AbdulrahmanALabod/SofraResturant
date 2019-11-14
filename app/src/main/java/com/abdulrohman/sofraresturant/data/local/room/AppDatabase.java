package com.abdulrohman.sofraresturant.data.local.room;


import android.app.Activity;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.abdulrohman.sofraresturant.data.model.item.ItemData;
import com.abdulrohman.sofraresturant.helper.Constant;

@Database(entities = {ItemData.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase;

    public static AppDatabase getInstanceRoom(Activity activity) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder( activity, AppDatabase.class, Constant.MY_DB )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
        }
        return appDatabase;
    }

    public abstract ItemDao itemDao();
}
