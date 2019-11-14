package com.abdulrohman.sofraresturant.ui.fragment.resturant.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.commission.Commission;
import com.abdulrohman.sofraresturant.data.model.commission.CommissionData;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommissionsFragment extends BaseFragment {
    private static final String TAG = CommissionsFragment.class.getSimpleName();
    @BindView(R.id.txt_restaurant_commission)
    TextView txtRestaurantCommission;
    @BindView(R.id.txt_app_commission)
    TextView txtAppCommission;
    @BindView(R.id.txt_have_paying_commission)
    TextView txtHavePayingCommission;
    @BindView(R.id.txt_remind_commission)
    TextView txtRemindCommission;
    Unbinder unbinder;

    public CommissionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_commissions, container, false );
        unbinder = ButterKnife.bind( this, view );
        getCommission();
        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    private void getCommission() {

        apiServices.getCommission( HelperMethodCustom.getApiToken( getActivity()
                , Constant.RESTAURANT_API_TOKEN ) )
                .enqueue( new Callback<Commission>() {
                    @Override
                    public void onResponse(Call<Commission> call, Response<Commission> response) {
                        try {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 1) {
                                    CommissionData commissionData = response.body().getData();
                                    txtRestaurantCommission.setText( commissionData.getTotal() );
                                    txtAppCommission.setText( commissionData.getCommissions() );
                                    txtHavePayingCommission.setText( commissionData.getPayments() );
                                    txtRemindCommission.setText( String.valueOf( commissionData.getNetCommissions() ) );
                                }
                            }
                        } catch (Exception e) {
                            Log.d( TAG, "getCommission: e " + e.getMessage() );
                        }
                    }

                    @Override
                    public void onFailure(Call<Commission> call, Throwable t) {

                    }
                } );
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
