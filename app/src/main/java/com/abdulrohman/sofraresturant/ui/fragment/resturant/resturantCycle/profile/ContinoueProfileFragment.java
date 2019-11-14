package com.abdulrohman.sofraresturant.ui.fragment.resturant.resturantCycle.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.user.UserData;
import com.abdulrohman.sofraresturant.data.model.user.UserProfile;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.abdulrohman.sofraresturant.helper.HelperMethodCustom.convertFileToMultipart;
import static com.abdulrohman.sofraresturant.helper.HelperMethodCustom.convertToRequestBody;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContinoueProfileFragment extends BaseFragment {
    private static final String TAG = Class.class.getSimpleName();
    @BindView(R.id.edt_price_charger_profile)
    EditText edtPriceChargerProfile;
    @BindView(R.id.edt_period_prepare_profile)
    EditText edtPeriodPrepareProfile;
    @BindView(R.id.swtch_state_profilre)
    Switch swtchStateProfilre;
    @BindView(R.id.txt_information_call_profile)
    TextView txtInformationCallProfile;
    @BindView(R.id.edt_phone_profile)
    EditText edtPhoneProfile;
    @BindView(R.id.edt_whatss_app_profile)
    EditText edtWhatssAppProfile;
    @BindView(R.id.btn_modify_proile_client)
    Button btnModifyProileClient;
    Unbinder unbinder;
    private boolean openMode;
    private UserData userData;
    private String strMinimumCharger;
    private String strPeriodPrepare;
    private String strPhone;
    private String strWhatsapp;
    private String strAviblity;

    public ContinoueProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_continoue_profile, container, false );
        unbinder = ButterKnife.bind( this, view );
        Bundle args = getArguments();
        if (args != null) {
            userData = (UserData) args.getSerializable( Constant.CONTIOUE_PROFILE );
        }
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

    @OnClick({R.id.swtch_state_profilre, R.id.btn_modify_proile_client})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.swtch_state_profilre:
                if (swtchStateProfilre.isChecked()) {
                    strAviblity = getString( R.string.open );
                } else {
                    strAviblity = getString( R.string.close );
                }
                break;
            case R.id.btn_modify_proile_client:
                editProfileRest();
                break;
        }
    }

    private void editProfileRest() {
        strPeriodPrepare = edtPeriodPrepareProfile.getText().toString();
        strMinimumCharger = edtPriceChargerProfile.getText().toString();
        strPhone = edtPhoneProfile.getText().toString();
        strWhatsapp = edtWhatssAppProfile.getText().toString();
        apiServices.editProfileResturant( convertToRequestBody( HelperMethodCustom.getApiToken( getActivity(),
                Constant.RESTAURANT_API_TOKEN ) ), convertToRequestBody( userData.getEmail() ),
                convertToRequestBody( userData.getName() ),
                convertToRequestBody( strPhone ),
                convertToRequestBody( userData.getRegionId() ),
                convertToRequestBody( userData.getDeliveryCost() ),
                convertToRequestBody( strMinimumCharger ),
                convertToRequestBody( strAviblity ),
                convertFileToMultipart( userData.getPhotoUrl(), "photo" ),
                convertToRequestBody( userData.getDeliveryCost() )
        ).enqueue( new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.body().getStatus() == 1) {
                    Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                } else {
                    Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }
}
