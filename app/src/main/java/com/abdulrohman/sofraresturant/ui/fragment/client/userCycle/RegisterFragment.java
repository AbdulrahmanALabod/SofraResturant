package com.abdulrohman.sofraresturant.ui.fragment.client.userCycle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.region.Region;
import com.abdulrohman.sofraresturant.data.model.region.RegionData;
import com.abdulrohman.sofraresturant.data.model.user.UserProfile;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.helper.MediaLoader;
import com.abdulrohman.sofraresturant.helper.SendData;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;
import com.abdulrohman.sofraresturant.ui.fragment.resturant.resturantCycle.ContinouRegisterFragment;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.abdulrohman.sofraresturant.helper.HelperMethod.convertFileToMultipart;
import static com.abdulrohman.sofraresturant.helper.HelperMethod.convertToRequestBody;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment {
    private static final String TAG = "RegisterFragment";
    @BindView(R.id.img_profile_signup)
    CircleImageView imgProfileSignup;
    @BindView(R.id.edt_name_connect_us_signup)
    EditText edtNameConnectUsSignup;
    @BindView(R.id.edt_email_signup)
    EditText edtEmailSignup;
    @BindView(R.id.edt_priod_signup)
    EditText edtPriodSignup;
    @BindView(R.id.spin_city_signup)
    Spinner spinCitySignup;
    @BindView(R.id.spin_nighbour_signup)
    Spinner spinNighbourSignup;
    @BindView(R.id.edt_password_signup)
    EditText edtPasswordSignup;
    @BindView(R.id.edt_confirm_new_password_signup)
    EditText edtConfirmNewPasswordSignup;
    @BindView(R.id.btn_register_signup)
    Button btnRegisterSignup;
    Unbinder unbinder;
    @BindView(R.id.edt_minimum_price_signup)
    EditText edtMinimumPriceSignup;
    @BindView(R.id.edt_charger_price_signup)
    EditText edtChargerPriceSignup;
    @BindView(R.id.liner_spin_nighbour_signup)
    LinearLayout linerSpinNighbourSignup;
    @BindView(R.id.img_spin_nighbour_signup)
    ImageView imgSpinNighbourSignup;
    //var
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();
    private String strEnterClient;
    private String edtName;
    private String edtEmail;
    private String edtPriod;
    private String edtPassword;
    private String edtConfirmNewPassword;
    private String edtChargerPrice;
    private String edtMinimumPrice;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_register, container, false );
        unbinder = ButterKnife.bind( this, view );
        linerSpinNighbourSignup.setVisibility( View.GONE );
        imgSpinNighbourSignup.setVisibility( View.GONE );

        strEnterClient = HelperMethodCustom.getSheardTypeEnter( getActivity(), Constant.TYPE_ENTER_CLIENT );
        Log.d( TAG, "onCreateView: strEnterClient " + strEnterClient );
        if (HelperMethodCustom.isClient( strEnterClient )) {
            edtMinimumPriceSignup.setVisibility( View.GONE );
            edtChargerPriceSignup.setVisibility( View.GONE );
            imgProfileSignup.setVisibility( View.VISIBLE );
            btnRegisterSignup.setText( getActivity().getString( R.string.login ) );

        } else {
            imgProfileSignup.setVisibility( View.GONE );
            edtMinimumPriceSignup.setVisibility( View.VISIBLE );
            edtChargerPriceSignup.setVisibility( View.VISIBLE );
            btnRegisterSignup.setText( getActivity().getString( R.string.continoue ) );

        }

        Album.initialize( AlbumConfig.newBuilder( getContext() )
                .setAlbumLoader( new MediaLoader() ).build() );

        if (getActivity() != null) {//not attached to a context
            initSpinerCity();
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

    @OnClick({R.id.img_profile_signup, R.id.btn_register_signup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_profile_signup:
                HelperMethodCustom.setImageProfile( getActivity(), this, mAlbumFiles, imgProfileSignup );
                break;
            case R.id.btn_register_signup:
                checkEnter();
                break;
        }
    }

    private void checkEnter() {
        if (HelperMethodCustom.isClient( strEnterClient )) {
            if (!mAlbumFiles.get( 0 ).getPath().isEmpty()) {
                registerClient( mAlbumFiles.get( 0 ).getPath() );
            } else {
                Toast.makeText( getActivity(), getString( R.string.please_upload_image ),
                        Toast.LENGTH_SHORT ).show();
            }
        } else {
            edtMinimumPrice = edtMinimumPriceSignup.getText().toString();
            edtChargerPrice = edtChargerPriceSignup.getText().toString();

            ContinouRegisterFragment fragment = new ContinouRegisterFragment();
            Bundle bundle = new Bundle();
            setValue();
            SendData sendData = new SendData( edtName, edtEmail, edtPriod, edtPriod,
                    String.valueOf( spinNighbourSignup.getSelectedItemId() ), edtPassword,
                    edtConfirmNewPassword, edtMinimumPrice, edtChargerPrice );
            bundle.putSerializable( Constant.CONTIOUE_REGISTER, sendData );
            fragment.setArguments( bundle );

            HelperMethodCustom.ReplaceFragment( getFragmentManager(), fragment, R.id.fram_container_user,
                    null, null );
        }
    }

    private void registerClient(String strPath) {
        setValue();
        apiServices.register( convertToRequestBody( edtName ), convertToRequestBody( edtEmail ),
                convertToRequestBody( edtPassword ), convertToRequestBody( edtConfirmNewPassword ),
                convertToRequestBody( edtPriod ), convertToRequestBody( "1" ),
                convertFileToMultipart( strPath, "profile_image" ) )
                .enqueue( new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                        if (response.body().getStatus() == 1) {
                            Log.d( TAG, "onResponse: " + response.body().getMsg() );
                            LoginFragment fragment = new LoginFragment();
                            HelperMethodCustom.ReplaceFragment( getFragmentManager(), fragment,
                                    R.id.fram_container_user, null, null );
                        } else {
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                            Log.d( TAG, "onResponse: " + response.body().getMsg() );
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                        Log.d( TAG, "onResponse: " + t.getMessage() );
                        Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                } );
    }

    private void setValue() {
        edtName = edtNameConnectUsSignup.getText().toString();
        edtEmail = edtEmailSignup.getText().toString();
        edtPriod = edtPriodSignup.getText().toString();
        edtPassword = edtPasswordSignup.getText().toString();
        edtConfirmNewPassword = edtConfirmNewPasswordSignup.getText().toString();
    }

    public void initSpinerCity() {

        apiServices.getCities().enqueue( new Callback<Region>() {
            @Override
            public void onResponse(Call<Region> call, Response<Region> response) {
                try {
                    List<String> names = new ArrayList<>();
                    List<Integer> ids = new ArrayList<>();
                    names.add( getActivity().getString( R.string.city ) );
                    ids.add( 0 );
                    if (response.body().getStatus() == 1) {

                        final List<RegionData> regionData = response.body().getData().getData();
                        for (int i = 0; i < regionData.size(); i++) {
                            names.add( regionData.get( i ).getName() );
                            ids.add( regionData.get( i ).getId() );
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( getActivity(),
                                android.R.layout.simple_list_item_1, names );
                        spinCitySignup.setAdapter( arrayAdapter );
                        spinCitySignup.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                linerSpinNighbourSignup.setVisibility( View.VISIBLE );
                                imgSpinNighbourSignup.setVisibility( View.VISIBLE );
                                initSpinnerRegion( position );
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        } );
                    }
                } catch (Exception e) {
                    Log.d( TAG, "initSpinerCity: e " + e.getMessage() );
                }
            }


            @Override
            public void onFailure(Call<Region> call, Throwable t) {
                try {

                    Log.d( TAG, "onFailure: " + t.getMessage() );
                } catch (Exception e) {
                    Log.d( TAG, "initSpinerCity: e " + e.getMessage() );
                }
            }
        } );

    }


    public void initSpinnerRegion(int position) {

        apiServices.getRegion( position ).enqueue( new Callback<Region>() {
            @Override
            public void onResponse(Call<Region> call, Response<Region> response) {
                try {
                    List<String> names = new ArrayList<>();
                    List<Integer> ids = new ArrayList<>();
                    names.add( getActivity().getString( R.string.nighbour_name ) );
                    ids.add( 0 );
                    if (response.body().getStatus() == 1) {

                        List<RegionData> regionData = response.body().getData().getData();
                        for (int i = 0; i < regionData.size(); i++) {
                            names.add( regionData.get( i ).getName() );
                            ids.add( regionData.get( i ).getId() );
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( getActivity(),
                                android.R.layout.simple_list_item_1, names );
                        spinNighbourSignup.setAdapter( arrayAdapter );

                    } else {
                        Log.d( TAG, "onResponse: " + response.body().getMsg() );
                        Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                    }
                } catch (NullPointerException e) {
                    Log.d( TAG, "onResponse: e " + e.getMessage() );
                    Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onFailure(Call<Region> call, Throwable t) {
                Log.d( TAG, "onFailure: t " + t.getMessage() );
                Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();

            }
        } );
    }
}

