package com.abdulrohman.sofraresturant.ui.fragment.general.resturant;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.adapter.ResturantDetailesPager;
import com.abdulrohman.sofraresturant.data.model.resturant.ResturantData;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.activity.BaseActivity;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResturantDetailesFragment extends BaseFragment {
    private static final String TAG = "ResturantDetilsFragment";
    @BindView(R.id.tab_Layout_detailes)
    TabLayout tabLayoutDetailes;
    @BindView(R.id.pager_detailes)
    ViewPager pagerDetailes;
    Unbinder unbinder;

    //var
    BaseActivity.OnPositionItem onPositionItem;
    private ResturantData data;


    public ResturantDetailesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d( TAG, "onCreateView: start" );
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_resturant_detailes, container, false );
        unbinder = ButterKnife.bind( this, view );

        // onPositionItem = (BaseActivity.OnPositionItem) getActivity();

        ResturantDetailesPager pager = new ResturantDetailesPager( getChildFragmentManager(), getContext() );
        tabLayoutDetailes.setupWithViewPager( pagerDetailes );
        pagerDetailes.setAdapter( pager );

        Bundle bundle = getArguments();
        if (bundle != null) {
            data = (ResturantData) bundle.getSerializable( Constant.RESTURANT_ID );
            Log.d( TAG, "onCreateView: data.getId() " + data.getId() );
            HelperMethodCustom.createShared( getActivity(), Constant.SHRED_RESTURANT_GET_BY_ID,
                    String.valueOf( data.getId() ) );
            HelperMethodCustom.createShared( getActivity(), Constant.RESTURANT_NAME,
                    String.valueOf( data.getName() ) );
//            setResturantDetail( data.getId() );

        }
        Log.d( TAG, "onCreateView: end" );
        return view;
    }

//    private void setResturantDetail(final int id) {
//        txtNameRestDetailes.setText( data.getName() );
//        HelperMethodCustom.onLoadImageFromUrl( imgResturantDetailes, data.getPhotoUrl(),
//                getActivity(), 1 );
//    }


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
