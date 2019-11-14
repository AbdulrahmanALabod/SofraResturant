package com.abdulrohman.sofraresturant.ui.fragment.resturant.resturantCycle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.resturant.Resturants;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.helper.MediaLoader;
import com.abdulrohman.sofraresturant.helper.SendData;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;
import com.abdulrohman.sofraresturant.ui.fragment.client.userCycle.LoginFragment;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

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
public class ContinouRegisterFragment extends BaseFragment {
    private static final String TAG = ContinouRegisterFragment.class.getSimpleName();
    @BindView(R.id.img_profile_continoue_signup)
    CircleImageView imgProfileContinoueSignup;
    @BindView(R.id.edt_phone_continoue_signup)
    EditText edtPhoneContinoueSignup;
    @BindView(R.id.edt_watsapp_continoue_signup)
    EditText edtWatsappContinoueSignup;
    @BindView(R.id.btn_register_continoue_signup)
    Button btnRegisterContinoueSignup;
    Unbinder unbinder;

    //var
    private SendData sendData;
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();



    public ContinouRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_continou_register, container, false );
        unbinder = ButterKnife.bind( this, view );

        Bundle bundle = getArguments();
        if (bundle != null) {
            sendData = (SendData) bundle.getSerializable( Constant.CONTIOUE_REGISTER );
            Log.d( TAG, "onCreateView: sendData " + sendData.getName() );
        } else {
            Log.d( TAG, "onCreateView: bundle == null" );
        }

        Album.initialize( AlbumConfig.newBuilder( getActivity())
                .setAlbumLoader( new MediaLoader() ).build() );

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

    @OnClick({R.id.img_profile_continoue_signup, R.id.btn_register_continoue_signup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_profile_continoue_signup:
                HelperMethodCustom.setImageProfile( getActivity(), this, mAlbumFiles,
                        imgProfileContinoueSignup );
                break;
            case R.id.btn_register_continoue_signup:
                Log.d( TAG, "onViewClicked: strPathImg" + mAlbumFiles.get( 0 ).getPath() );
                if (!mAlbumFiles.get( 0 ).getPath().isEmpty()) {
                    registerResturante( mAlbumFiles.get( 0 ).getPath() );
                } else {
                    Toast.makeText( getActivity(), "Upload image", Toast.LENGTH_SHORT ).show();
                }

                break;
        }
    }

    private void registerResturante(String strPathImg) {
        String strPhone = edtPhoneContinoueSignup.getText().toString();
        String strWhatsApp = edtWatsappContinoueSignup.getText().toString();
        apiServices.registerResturant( convertToRequestBody( sendData.getName() ),
                convertToRequestBody( sendData.getE_mail() ),
                convertToRequestBody( sendData.getPass() ),
                convertToRequestBody( sendData.getConfirmPass() ),
                convertToRequestBody( strPhone ),
                convertToRequestBody( strWhatsApp ),
                convertToRequestBody( sendData.getRegion() ),
                convertToRequestBody( sendData.getMinimumPrice() ),
                convertToRequestBody( sendData.getPriceCharger() ),
                convertToRequestBody( sendData.getPeriodReach() ),
                convertFileToMultipart( strPathImg, "photo" ) )
                .enqueue( new Callback<Resturants>() {
                    @Override
                    public void onResponse(Call<Resturants> call, Response<Resturants> response) {
                        if (response.body().getStatus() == 1) {
                            Log.d( TAG, "onResponse: " + response.body().getMsg() );
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                            LoginFragment frgment = new LoginFragment();
                            HelperMethodCustom.ReplaceFragment( getFragmentManager(), frgment,
                                    R.id.fram_container_user, null, null );

                        } else {
                            Log.d( TAG, "onResponse: " + response.body().getMsg() );
                        }
                    }

                    @Override
                    public void onFailure(Call<Resturants> call, Throwable t) {
                        Log.d( TAG, "onFailure: " + t.getMessage() );
                    }
                } );
    }
}
