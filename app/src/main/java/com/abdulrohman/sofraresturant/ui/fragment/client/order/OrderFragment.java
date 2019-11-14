package com.abdulrohman.sofraresturant.ui.fragment.client.order;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.adapter.ViewPagerWithFragmentAdapter;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends BaseFragment {
    private static final String TAG = "OrderFragment";
    public PendingOrderFragment pendingOrderFragment;
    public CurrentOrderFragment currentOrderFragment;
    public PreviousOrderFragment previousOrderFragment;
    @BindView(R.id.pager_order)
    ViewPager pagerOrder;
    Unbinder unbinder;
    @BindView(R.id.tab_order)
    TabLayout tabOrder;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d( TAG, "onCreateView: start" );
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_order, container, false );
        unbinder = ButterKnife.bind( this, view );

        pendingOrderFragment = new PendingOrderFragment();
        currentOrderFragment = new CurrentOrderFragment();
        previousOrderFragment = new PreviousOrderFragment();

        pendingOrderFragment.orderFragment = OrderFragment.this;
        currentOrderFragment.orderFragment = OrderFragment.this;
        previousOrderFragment.orderFragment = OrderFragment.this;

        ViewPagerWithFragmentAdapter orderPager = new ViewPagerWithFragmentAdapter( getChildFragmentManager() );
        orderPager.addPager( pendingOrderFragment, getString( R.string.new_order ) );
        orderPager.addPager( currentOrderFragment, getString( R.string.current ) );
        orderPager.addPager( previousOrderFragment, getString( R.string.completed ) );
        tabOrder.setupWithViewPager( pagerOrder );
        pagerOrder.setAdapter( orderPager );
        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
