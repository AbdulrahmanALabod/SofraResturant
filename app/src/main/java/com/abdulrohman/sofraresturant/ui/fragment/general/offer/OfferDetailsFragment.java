package com.abdulrohman.sofraresturant.ui.fragment.general.offer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.item.Item;
import com.abdulrohman.sofraresturant.data.model.item.ItemData;
import com.abdulrohman.sofraresturant.helper.Constant;
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
public class OfferDetailsFragment extends BaseFragment {
    private static final String TAG = OfferDetailsFragment.class.getSimpleName();
    @BindView(R.id.txt_name_offer_detail)
    TextView txtNameOfferDetail;
    @BindView(R.id.txt_consist_offer_detail)
    TextView txtConsistOfferDetail;
    @BindView(R.id.txt_from_offer_detail)
    TextView txtFromOfferDetail;
    @BindView(R.id.txt_to_offer_detail)
    TextView txtToOfferDetail;
    @BindView(R.id.button)
    Button button;
    Unbinder unbinder;
    private ItemData offerData = null;

    public OfferDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_offer_detail, container, false );

        Bundle bundle = getArguments();
        if (bundle != null) {
            offerData = (ItemData) bundle.getSerializable( Constant.OFFER_ID );
        }
        getOffer();
        Log.d( TAG, "onCreateView: end" );
        unbinder = ButterKnife.bind( this, view );
        return view;
    }

    private void getOffer() {

        if (offerData != null) {
            apiServices.getOfferDetial( offerData.getId() ).enqueue( new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {
                    try {
                        if (response.body().getStatus() == 1) {
                            txtNameOfferDetail.setText( offerData.getName() );
                            txtConsistOfferDetail.setText( offerData.getDescription() );
                            txtFromOfferDetail.setText( offerData.getStartingAt() );
                            txtToOfferDetail.setText( offerData.getEndingAt() );
                        } else {
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                        }

                    } catch (Exception e) {
                        Log.d( TAG, "getOffer: e " + e.getMessage() );
                    }
                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) {
                    try {
                        Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                    } catch (Exception e) {
                        Log.d( TAG, "getOffer: e " + e.getMessage() );
                    }
                }
            } );
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
