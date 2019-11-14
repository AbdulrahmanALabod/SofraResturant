package com.abdulrohman.sofraresturant.ui.fragment.general.resturant;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.resturant.ResturantData;
import com.abdulrohman.sofraresturant.data.model.resturant.ResturantsInformatiom;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformationResturantFragment extends BaseFragment {
    private static final String TAG = InformationResturantFragment.class.getSimpleName();

    @BindView(R.id.txt_open_information)
    TextView txtOpenInformation;
    @BindView(R.id.txt_city_name_information)
    TextView txtCityNameInformation;
    @BindView(R.id.txt_nighbour_name_information)
    TextView txtNighbourNameInformation;
    @BindView(R.id.txt_mini_price_information)
    TextView txtMiniPriceInformation;
    @BindView(R.id.txt_charger_price_information)
    TextView txtChargerPriceInformation;
    Unbinder unbinder;
    //var
    private int resturantId;
    private ResturantData restData;

    public InformationResturantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d( TAG, "onCreateView: start" );
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_information_resturant,
                container,
                false );
        unbinder = ButterKnife.bind( this, view );

        resturantId = HelperMethodCustom.getResturanId( getActivity() );

        getDetailRest();
        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    private void getDetailRest() {

        apiServices.getResturant( resturantId ).enqueue( new Callback<ResturantsInformatiom>() {
            @Override
            public void onResponse(Call<ResturantsInformatiom> call, Response<ResturantsInformatiom> response) {
                try {
                    if (response.body().getStatus() == 1) {
                       ResturantData restData= response.body().getData();
                        if (restData!=null){
                            setValueText( restData );
                        }
                    } else {
                        Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                    }
                } catch (Exception e) {
                    Log.d( TAG, "getDetailRest: e.getMessage() " + e.getMessage() );
                }
            }


            @Override
            public void onFailure(Call<ResturantsInformatiom> call, Throwable t) {
                try {
                    Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                } catch (Exception e) {
                    Log.d( TAG, "onFailure: e " + e.getMessage() );
                }
            }
        } );

    }

    private void setValueText(ResturantData resturantData) {
        try {
            txtOpenInformation.setText( resturantData.getAvailability() );
            txtChargerPriceInformation.setText( resturantData.getDeliveryCost() );
            txtCityNameInformation.setText( getActivity().getString( R.string.mans ) );
//            txtCityNameInformation.setText( resturantData.getRegion().getCity().getName() );
            txtNighbourNameInformation.setText( getActivity().getString( R.string.jala ) );
//            txtNighbourNameInformation.setText(  resturantData.getRegion().getName() );
            txtMiniPriceInformation.setText( resturantData.getMinimumCharger() );
        } catch (Exception e) {
            Log.d( TAG, "setValueText:  e" + e.getMessage() );
        }

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
