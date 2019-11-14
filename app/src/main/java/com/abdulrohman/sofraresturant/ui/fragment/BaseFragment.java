package com.abdulrohman.sofraresturant.ui.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.data.api.ApiClient;
import com.abdulrohman.sofraresturant.data.api.ApiServer;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.ui.activity.BaseActivity;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    public BaseActivity baseActivity;
    public ApiServer apiServices;

    public void initFragment() {
        baseActivity = (BaseActivity) getActivity();
        baseActivity.baseFragment = this;
    }

    public void onBack() {
        baseActivity.SuperBackPressed();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Log.d( TAG, "onCreateView: start" );
        View view = super.onCreateView( inflater, container, savedInstanceState );
        apiServices = ApiClient.getClient().create( ApiServer.class );
        // Log.d( TAG, "onCreateView: end" );
        return view;
    }

    public void showToast(Activity activity,String msg) {
        if (activity.isFinishing()) {
            Toast.makeText(activity, msg, Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    public void onDestroy() {
        ButterKnife.bind( getActivity() );
        super.onDestroy();
    }
}
