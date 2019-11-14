package com.abdulrohman.sofraresturant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.ui.fragment.client.order.CurrentOrderFragment;
import com.abdulrohman.sofraresturant.ui.fragment.client.order.PendingOrderFragment;
import com.abdulrohman.sofraresturant.ui.fragment.client.order.PreviousOrderFragment;

public class OrderPager extends FragmentPagerAdapter {
//   / Fragment[] fragments= {new CurrentOrderFragment(),new PreviousOrderFragment()};
    Context mContext;
    public OrderPager(FragmentManager fm ,Context context) {
        super( fm );
        this.mContext=context;
    }

    @Override
    public Fragment getItem(int i) {
        if (i==0) {
            return new PendingOrderFragment();
        }else if (i==1){
            return  new CurrentOrderFragment();
        }else if ((i==2)){
            return new PreviousOrderFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0) {
            return mContext.getString( R.string.new_order);
        }else if(position==1) {
            return mContext.getString( R.string.current);
        }else {
            return mContext.getString(R.string.completed);
        }
    }
}
