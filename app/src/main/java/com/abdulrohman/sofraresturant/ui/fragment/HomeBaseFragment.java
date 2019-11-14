package com.abdulrohman.sofraresturant.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.fragment.general.SearchClientFragment;
import com.abdulrohman.sofraresturant.ui.fragment.resturant.categories.MyCategoriesFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeBaseFragment extends BaseFragment {
    private static final String TAG = "HomeBaseFragment";

    private Fragment fragment;

    public HomeBaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view = inflater.inflate( R.layout.fragment_home_base, container, false );

            if (HelperMethodCustom.isClient( HelperMethodCustom.getSheardTypeEnter( getActivity(),Constant.TYPE_ENTER_CLIENT )  )) {
                fragment = new SearchClientFragment();
            } else {
                fragment = new MyCategoriesFragment();
            }

        HelperMethodCustom.ReplaceFragment( getFragmentManager(), fragment
                , R.id.fram_container_home
                , null
                , null );
        return view;
    }

    @Override
    public void onBack() {
        getActivity().finish();
    }


}
