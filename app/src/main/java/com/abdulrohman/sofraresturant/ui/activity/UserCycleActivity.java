package com.abdulrohman.sofraresturant.ui.activity;

import android.os.Bundle;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.fragment.client.userCycle.LoginFragment;

public class UserCycleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user );
        LoginFragment loginFragment = new LoginFragment();
        HelperMethodCustom.ReplaceFragment( getSupportFragmentManager(), loginFragment,
                R.id.fram_container_user, null, null );
    }

}
