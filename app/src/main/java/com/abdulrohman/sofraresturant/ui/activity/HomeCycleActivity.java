package com.abdulrohman.sofraresturant.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethod;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.fragment.HomeBaseFragment;
import com.abdulrohman.sofraresturant.ui.fragment.client.order.CheckFinalFragment;
import com.abdulrohman.sofraresturant.ui.fragment.client.order.FinalItemsOrderFragment;
import com.abdulrohman.sofraresturant.ui.fragment.client.order.OrderFragment;
import com.abdulrohman.sofraresturant.ui.fragment.client.userCycle.profile.ProfileFragment;
import com.abdulrohman.sofraresturant.ui.fragment.general.MoreFragment;
import com.abdulrohman.sofraresturant.ui.fragment.general.SearchClientFragment;
import com.abdulrohman.sofraresturant.ui.fragment.resturant.categories.MyCategoriesFragment;
import com.abdulrohman.sofraresturant.ui.fragment.resturant.profile.CommissionsFragment;

public class HomeCycleActivity extends BaseActivity {

    //var
    boolean afterRegister = false;
    private String strEnterClient;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        Fragment fragment;

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (HelperMethodCustom.isClient( strEnterClient )) {
                        fragment = new SearchClientFragment();
                    } else {
                        fragment = new MyCategoriesFragment();
                    }
                    break;
                case R.id.navigation_dashboard:
                    fragment = new OrderFragment();
                    break;
                case R.id.navigation_personal:
                    fragment = new ProfileFragment();
                    break;
                case R.id.navigation_more:
                    fragment = new MoreFragment();
                    break;
            }

            HelperMethod.ReplaceFragment( getSupportFragmentManager(),
                    fragment,
                    R.id.fram_container_home,
                    null,
                    null );
            return true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home_cycle );
        appBar( true );
        getSupportActionBar().setTitle( "" );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelperMethodCustom.isClient( strEnterClient )) {
                    FinalItemsOrderFragment fragment= new FinalItemsOrderFragment();
                    HelperMethodCustom.ReplaceFragment( getSupportFragmentManager(), fragment,
                            R.id.fram_container_home, null, null );
                } else {
                    CommissionsFragment commissionsFragment = new CommissionsFragment();
                    HelperMethodCustom.ReplaceFragment( getSupportFragmentManager(), commissionsFragment,
                            R.id.fram_container_home, null, null );
                }
            }
        } );


        strEnterClient = HelperMethodCustom.getSheardTypeEnter( this, Constant.TYPE_ENTER_CLIENT );
        if (HelperMethodCustom.isClient( strEnterClient )) {
            getSupportActionBar().setLogo( R.drawable.cart );
        } else {
            getSupportActionBar().setLogo( R.drawable.ic_calculator_solid );
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled( false );

        navigation = (BottomNavigationView) findViewById( R.id.navigation );
        navigation.setOnNavigationItemSelectedListener( mOnNavigationItemSelectedListener );
        //NEW 10/7/2019
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            afterRegister = bundle.getBoolean( Constant.AFTER_LOGIN_CLIENT );
            if (afterRegister == true) {
                CheckFinalFragment checkFinalFragment = new CheckFinalFragment();
                HelperMethodCustom.ReplaceFragment( getSupportFragmentManager(), checkFinalFragment,
                        R.id.fram_container_home, null, null );
            }
        } else {
            replaceWithoutStack();
        }

    }

    private void replaceWithoutStack() {
        HomeBaseFragment fragment = new HomeBaseFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace( R.id.fram_container_home, fragment );
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_home, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected( item );
    }

    @Override
    public void SuperBackPressed() {

        if (R.id.navigation_home == navigation.getSelectedItemId()) {
            super.SuperBackPressed();
        } else {
            navigation.setSelectedItemId( R.id.navigation_home );
        }
    }
}

