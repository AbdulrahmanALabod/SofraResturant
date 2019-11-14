package com.abdulrohman.sofraresturant.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;

public class BaseActivity extends AppCompatActivity {
    //var
    public BaseFragment baseFragment;
    Toolbar toolbar;

    public void appBar(boolean exsist) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            toolbar = findViewById( R.id.toolbar );
            if (toolbar != null) {
                setSupportActionBar( toolbar );
                actionBar = getSupportActionBar();
            }
        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( exsist );
        }

    }

    public void SuperBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        baseFragment.onBack();
    }

    public interface OnPositionItem {
        void onSucssess(int position);
    }

    public  interface  OnPressButton{
        void onPositive(int position);
        void onNegtive(int position);
    }

}
