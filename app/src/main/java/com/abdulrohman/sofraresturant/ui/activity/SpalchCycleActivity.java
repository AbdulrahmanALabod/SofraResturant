package com.abdulrohman.sofraresturant.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpalchCycleActivity extends BaseActivity {
    private static final String TAG = "SpalchCycleActivity";

    @BindView(R.id.btn_order_food_splach)
    Button btnOrderFoodSplach;
    @BindView(R.id.btn_buy_food_splach)
    Button btnBuyFoodSplach;
    @BindView(R.id.img_top_splach)
    ImageView img_top_splach;
    @BindView(R.id.img_bottom_splach)
    ImageView img_bottom_splach;
    //var
    private boolean athusicition = false; //connection internet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d( TAG, "onCreate: start" );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splach );
        ButterKnife.bind( this );
        checkConnection();

        Log.d( TAG, "onCreate: end" );

    }

    private void checkConnection() {
        ConnectivityManager cManager = (ConnectivityManager) getApplicationContext()
                .getSystemService( getApplicationContext().CONNECTIVITY_SERVICE );
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if (nInfo != null && nInfo.isConnected()) {
            athusicition = true;
        } else {
            Toast.makeText( this, "بعد إذنك افحص شبكة الأنترنت", Toast.LENGTH_SHORT )
                    .show();
        }
    }

    @OnClick({R.id.btn_order_food_splach, R.id.btn_buy_food_splach})
    public void onViewClicked(View view) {
        removeValue( Constant.TYPE_ENTER_CLIENT );
        removeValue( Constant.TYPE_ENTER_REST );
        if (athusicition) {
            Intent intent = null;
            switch (view.getId()) {
                case R.id.btn_order_food_splach:
                    HelperMethodCustom.createShared( this, Constant.TYPE_ENTER_CLIENT, Constant.CLIENT_TYPE );
                    intent = new Intent( SpalchCycleActivity.this, HomeCycleActivity.class );
                    break;
                case R.id.btn_buy_food_splach:
                    String sheardTypeEnter = HelperMethodCustom
                            .getApiToken( this, Constant.RESTAURANT_API_TOKEN );

                    if (sheardTypeEnter != null) {
                        intent = new Intent( SpalchCycleActivity.this, HomeCycleActivity.class );
                    } else {
                        HelperMethodCustom.createShared( this, Constant.TYPE_ENTER_REST, Constant.RESTURANT_TYPE );
                        intent = new Intent( SpalchCycleActivity.this, UserCycleActivity.class );
                    }
                    break;
            }
            if (intent != null) {
                startActivity( intent );
            }
        } else {
            checkConnection();
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void removeValue(String type) {
        SharedPreferences sharedPreferences = getSharedPreferences( Constant.SHIRD_API_TOKEN, MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove( type );
        editor.apply();
    }
}
