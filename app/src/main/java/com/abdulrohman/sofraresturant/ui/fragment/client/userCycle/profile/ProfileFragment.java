package com.abdulrohman.sofraresturant.ui.fragment.client.userCycle.profile;


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
import android.widget.Spinner;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.region.Region;
import com.abdulrohman.sofraresturant.data.model.region.RegionData;
import com.abdulrohman.sofraresturant.data.model.user.UserData;
import com.abdulrohman.sofraresturant.data.model.user.UserProfile;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.helper.MediaLoader;
import com.abdulrohman.sofraresturant.helper.SendData;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;
import com.abdulrohman.sofraresturant.ui.fragment.resturant.resturantCycle.profile.ContinoueProfileFragment;
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

import static com.abdulrohman.sofraresturant.helper.HelperMethodCustom.convertFileToMultipart;
import static com.abdulrohman.sofraresturant.helper.HelperMethodCustom.convertToRequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    @BindView(R.id.img_profile_profile)
    CircleImageView imgProfileProfile;
    @BindView(R.id.edt_name_profile)
    EditText edtNameProfile;
    @BindView(R.id.edt_email_profile)
    EditText edtEmailProfile;
    @BindView(R.id.edt_phone_profile)
    EditText edtPhoneProfile;
    @BindView(R.id.spnr_city_proile_client)
    Spinner spnrCityProileClient;
    @BindView(R.id.spnr_nighboure_proile_client)
    Spinner spnrNighboureProileClient;
    @BindView(R.id.edt_price_profile)
    EditText edtPriceProfile;
    @BindView(R.id.btn_modify_proile_client)
    Button btnModifyProileClient;


    //var
    private UserData userData;
    private boolean clientMode = false;
    private Unbinder unbinder;
    private String strPathImag = "";
    private String strName;
    private String strPhone;
    private String strEmail;
    private String strMinemumPrice;
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();
    private List<RegionData> regionData = new ArrayList<>();


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_profile, container, false );
        unbinder = ButterKnife.bind( this, view );

        if (getActivity() != null) {
            Album.initialize( AlbumConfig.newBuilder( getActivity() )
                    .setAlbumLoader( new MediaLoader() ).build() );
        }

        if (HelperMethodCustom.getSheardTypeEnter( getActivity(), Constant.TYPE_ENTER_CLIENT )
                .equals( Constant.CLIENT_TYPE )) {
            clientMode = true;
            onCallClient( apiServices.getProfileClient( HelperMethodCustom
                    .getApiToken( getActivity(), Constant.CLIENT_API_TOKEN ) ) );
            edtPriceProfile.setVisibility( View.GONE );
            edtPhoneProfile.setVisibility( View.VISIBLE );
        } else {
            onCallRest( apiServices.getProfileResturant( HelperMethodCustom
                    .getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ) ) );
            edtPhoneProfile.setVisibility( View.GONE );
            edtPriceProfile.setVisibility( View.VISIBLE );
        }
        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    private void onCallClient(Call<UserProfile> profileClient) {
        profileClient.enqueue( new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                try {

                    if (response.body().getStatus() == 1) {
                        userData = response.body().getData().getUser();
                        initProfileWedgit( userData );
                    } else {
                        Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                        Log.d( TAG, "onResponse: getStatus() == 0 " + response.body().getMsg() );
                    }
                } catch (Exception e) {
                    Log.d( TAG, "onResponse: e " + e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                try {

                    Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                    Log.d( TAG, "onFailure: " + t.getMessage() );
                } catch (Exception e) {
                    Log.d( TAG, "onCallClient: e " + e.getMessage() );
                }
            }
        } );
    }


    private void onCallRest(Call<UserProfile> resturantsCall) {
        resturantsCall.enqueue( new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        userData = response.body().getData().getUser();
                        initProfileWedgit( userData );

                    } else {
                        Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                        Log.d( TAG, "onResponse: getStatus() == 0 " + response.body().getMsg() );
                    }
                } catch (Exception e) {
                    Log.d( TAG, "onCallClient: e " + e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                Log.d( TAG, "onFailure: " + t.getMessage() );
            }
        } );

    }

    private void initProfileWedgit(UserData userData) {
        initSpinerCity( userData.getRegion().getCity().getId() );
        initSpinnerRegion( userData.getRegion().getId() );
        edtNameProfile.setText( userData.getName() );
        edtEmailProfile.setText( userData.getEmail() );
        spnrCityProileClient.setSelection( userData.getRegion().getCity().getId() );
        int regionId = userData.getRegion().getId();
        Log.d( TAG, "initProfileWedgit:regionId " + regionId );
        spnrNighboureProileClient.setSelection( regionId );
        strPathImag = userData.getPhotoUrl();
        HelperMethodCustom.onLoadImageFromUrl( imgProfileProfile, strPathImag, getActivity(),
                1 );
        if (clientMode) {
            edtPhoneProfile.setText( userData.getPhone() );
        } else {
            edtPriceProfile.setText( userData.getDeliveryCost() );
        }

    }

    public void initSpinerCity(final int cityId) {
        apiServices.getCities().enqueue( new Callback<Region>() {
            @Override
            public void onResponse(Call<Region> call, Response<Region> response) {
                try {

                    List<String> names = new ArrayList<>();
                    List<Integer> ids = new ArrayList<>();
                    names.add( getActivity().getString( R.string.city ) );
                    ids.add( 0 );
                    if (response.body().getStatus() == 1) {

                        regionData = response.body().getData().getData();
                        for (int i = 0; i < regionData.size(); i++) {
                            names.add( regionData.get( i ).getName() );
                            ids.add( regionData.get( i ).getId() );
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( getActivity(),
                                android.R.layout.simple_list_item_1, names );
                        spnrCityProileClient.setAdapter( arrayAdapter );
                        spnrCityProileClient.setSelection( cityId );
                        spnrCityProileClient.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                initSpinnerRegion( position );
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        } );
                    }
                } catch (Exception e) {
                    Log.d( TAG, "onResponse: e " + e.getMessage() );
                }

            }

            @Override
            public void onFailure(Call<Region> call, Throwable t) {
                try {
                    Log.d( TAG, "onFailure: " + t.getMessage() );
                } catch (Exception e) {
                    Log.d( TAG, "onFailure: e " + e.getMessage() );
                }

            }
        } );
    }

    public void initSpinnerRegion(final int regionId) {

        apiServices.getRegion( regionId ).enqueue( new Callback<Region>() {
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
                        spnrNighboureProileClient.setAdapter( arrayAdapter );
                        spnrNighboureProileClient.setSelection( regionId );
                        spnrNighboureProileClient.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                sendIdsSelect( position );
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        } );
                    }
                } catch (NullPointerException e) {
                    Log.d( TAG, "onResponse: e " + e.getMessage() );
                }
            }


            @Override
            public void onFailure(Call<Region> call, Throwable t) {
                try {

                    Log.d( TAG, "onFailure: " + t.getMessage() );
                } catch (Exception e) {
                    Log.d( TAG, "onResponse: e " + e.getMessage() );
                }
            }
        } );

    }

    private void sendIdsSelect(int position) {

    }

    @OnClick({R.id.img_profile_profile, R.id.btn_modify_proile_client})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_profile_profile:
                HelperMethodCustom.setImageProfile( getActivity(), ProfileFragment.this,
                        mAlbumFiles, imgProfileProfile );
                break;
            case R.id.btn_modify_proile_client:
                if (mAlbumFiles.size() != 0) {
                    String album = mAlbumFiles.get( 0 ).getPath();
                    if (!album.isEmpty()) {
                        strPathImag = album;
                    }
                }
                if (clientMode) {
                    editProfileClient();
                } else {
                    strMinemumPrice = edtPriceProfile.getText().toString();
                    SendData sendData = new SendData( strName, strEmail,
                            String.valueOf( spnrNighboureProileClient.getSelectedItemPosition() ),
                            strMinemumPrice,
                            strPathImag );
                    ContinoueProfileFragment fragment = new ContinoueProfileFragment();
                    Bundle bund = new Bundle();
                    bund.putSerializable( Constant.CONTIOUE_PROFILE, sendData );
                    fragment.setArguments( bund );
                    HelperMethodCustom.ReplaceFragment( getFragmentManager(), fragment, R.id.fram_container_home,
                            null, null );

                }
                break;
        }
    }

    private void editProfileClient() {

        strName = edtNameProfile.getText().toString();
        strPhone = edtPhoneProfile.getText().toString();
        strEmail = edtEmailProfile.getText().toString();
        Log.d( TAG, "editProfileClient: path " + strPathImag );
        if (mAlbumFiles != null && mAlbumFiles.size() != 0) {
            OnCallEditClient( apiServices.editProfileClient( convertToRequestBody( HelperMethodCustom
                            .getApiToken( getActivity(), Constant.CLIENT_API_TOKEN ) ),
                    convertToRequestBody( strName ),
                    convertToRequestBody( strEmail ),
                    convertToRequestBody( strPhone ),
                    convertToRequestBody( String.valueOf( spnrNighboureProileClient.getSelectedItemPosition() ) ),
                    convertFileToMultipart( strPathImag, "profile_image" ) ) );
        } else {
            OnCallEditClient( apiServices.editProfileClientWithoutImg( convertToRequestBody( HelperMethodCustom
                            .getApiToken( getActivity(), Constant.CLIENT_API_TOKEN ) ),
                    convertToRequestBody( strName ),
                    convertToRequestBody( strEmail ),
                    convertToRequestBody( strPhone ),
                    convertToRequestBody( String.valueOf( spnrNighboureProileClient.getSelectedItemPosition() ) ) ) );
        }


    }

    private void OnCallEditClient(Call<UserProfile> userEditProfileCall) {
        userEditProfileCall.enqueue( new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        Toast.makeText( getActivity(), response.body().getMsg(),
                                Toast.LENGTH_LONG ).show();
                    } else {
                        Toast.makeText( getActivity(), response.body().getMsg(),
                                Toast.LENGTH_LONG ).show();
                    }

                } catch (Exception e) {
                    Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_SHORT ).show();
                    Log.d( TAG, "editProfileClient: e.getMessage() " + e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                try {
                    Log.d( TAG, "onFailure: " + t.getMessage() );
                    Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_LONG ).show();
                } catch (Exception e) {
                    Log.d( TAG, "onFailure: t " + t.getMessage() );
                }
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
