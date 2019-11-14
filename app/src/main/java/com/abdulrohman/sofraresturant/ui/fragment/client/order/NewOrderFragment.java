package com.abdulrohman.sofraresturant.ui.fragment.client.order;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrderFragment extends BaseFragment {
    private static final String TAG = Class.class.getSimpleName();

    public NewOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater,container,savedInstanceState );
        View view= inflater.inflate( R.layout.fragment_empty, container, false );
        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}
