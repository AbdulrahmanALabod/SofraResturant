package com.abdulrohman.sofraresturant.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.api.ApiServer;
import com.abdulrohman.sofraresturant.data.model.region.Region;
import com.abdulrohman.sofraresturant.data.model.region.RegionData;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelperMethodCustom extends HelperMethod {
    private static final String TAG = "HelperMethodCustom";

    public static boolean isClient(String strType) {
        if (Constant.CLIENT_TYPE.equals( strType )) {
            return true;
        }
        return false;
    }

    public static boolean isOwnerRest(String type){
        if (Constant.RESTURANT_TYPE.equals( type )) {
            return true;
        }
        return false;
    }

    public static int getResturanId(Activity activity) {
        int resturantId = 0;
        try {
            SharedPreferences prefs = activity
                    .getSharedPreferences( Constant.SHIRD_API_TOKEN, Context.MODE_PRIVATE );
            String restId = prefs.getString( Constant.SHRED_RESTURANT_GET_BY_ID, null );
            resturantId = Integer.valueOf( restId );
            Log.d( TAG, "getResturanId: resturantId " +resturantId);


        } catch (Exception e) {
            Toast.makeText( activity, e.getMessage(), Toast.LENGTH_SHORT )
                    .show();
        }
        return resturantId;

    }



    public static String getSheardTypeEnter(Activity activity,String type) {
        SharedPreferences preferences = activity.getSharedPreferences( Constant.SHIRD_API_TOKEN, Context.MODE_PRIVATE );
        return preferences.getString(type, "" );

    }

    public static void setImageProfile(final Context context, Fragment fragment,
                                         final ArrayList<AlbumFile> mAlbumFiles,
                                         final ImageView imageView) {

        Album.image( fragment ) // Image selection.
                .multipleChoice()
                .camera( true )
                .columnCount( 3 )
                .selectCount( 1 )
                .checkedList( mAlbumFiles )
                .onResult( new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        Log.d( TAG, "onAction: getPath() " + result.get( 0 ).getPath() );
                        mAlbumFiles.clear();
                        mAlbumFiles.addAll( result );
                        HelperMethodCustom.onLoadImageFromUrl( imageView,
                                result.get( 0 ).getPath(), context, 1 );
                    }
                } )
                .onCancel( new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                    }
                } )
                .start();

    }


}
