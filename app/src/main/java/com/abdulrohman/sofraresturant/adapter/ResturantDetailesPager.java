package com.abdulrohman.sofraresturant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.ui.fragment.general.resturant.CommentRateFragment;
import com.abdulrohman.sofraresturant.ui.fragment.general.resturant.InformationResturantFragment;
import com.abdulrohman.sofraresturant.ui.fragment.general.resturant.MenuClientFragment;

public class ResturantDetailesPager extends FragmentPagerAdapter {

    private Context context;
    private String titel;

    public ResturantDetailesPager(FragmentManager fm, Context context) {
        super( fm );
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new MenuClientFragment();
        } else if (i == 1) {
            return new CommentRateFragment();
        } else {
            return new InformationResturantFragment();
        }
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                titel = context.getString( R.string.menu );
                break;
            case 1:
                titel = context.getString( R.string.comment_recommendtion );
                break;
            case 2:
                titel = context.getString( R.string.details );
                break;
        }
        return titel;
    }
}
