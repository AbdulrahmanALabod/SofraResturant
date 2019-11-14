package com.abdulrohman.sofraresturant.ui.fragment.client.userCycle;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.user.UserProfile;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.helper.Validation;
import com.abdulrohman.sofraresturant.helper.InternetState;
import com.abdulrohman.sofraresturant.ui.activity.HomeCycleActivity;
import com.abdulrohman.sofraresturant.ui.activity.UserCycleActivity;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;
import com.abdulrohman.sofraresturant.ui.fragment.client.userCycle.forgetPassword.RestPasswordFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {
    private static final String TAG = LoginFragment.class.getSimpleName();
    @BindView(R.id.edt_email_login)
    EditText edtEmailLogin;
    @BindView(R.id.edt_password_login)
    EditText edtPasswordLogin;
    @BindView(R.id.txt_forget_pass_login)
    TextView txtForgetPassLogin;
    @BindView(R.id.btn_login_login)
    Button btnLoginLogin;
    @BindView(R.id.txt_signup_login)
    TextView txtSignupLogin;
    Unbinder unbinder;
    @BindView(R.id.txt_wrong_enter_login)
    TextView txtWrongEnterLogin;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_login, container, false );
        unbinder = ButterKnife.bind( this, view );

        HelperMethodCustom.setCloroEditText( getContext(), edtEmailLogin, R.color.white );
        HelperMethodCustom.setCloroEditText( getContext(), edtPasswordLogin, R.color.white );


        Log.d( TAG, "onCreateView: end" );
        return view;
    }


    @Override
    public void onBack() {
//        if (!HelperMethodCustom.isClient( Constant.CLIENT_TYPE )) {
//            baseActivity.SuperBackPressed();
//        } else {
//            super.onBack();
//        }
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.txt_forget_pass_login, R.id.btn_login_login, R.id.txt_signup_login})
    public void onViewClicked(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.txt_forget_pass_login:
                fragment = new RestPasswordFragment();
                break;
            case R.id.btn_login_login:
                checkEnterType();
                break;
            case R.id.txt_signup_login:
                fragment = new RegisterFragment();
                break;

        }
        if (fragment != null) {
            HelperMethodCustom.ReplaceFragment( getFragmentManager(), fragment, R.id.fram_container_user,
                    null, null );
        }
    }

    private boolean checkEnterType() {
        String strEnterClient = HelperMethodCustom.getSheardTypeEnter( getActivity(), Constant.TYPE_ENTER_CLIENT );
        String strEmial = edtEmailLogin.getText().toString().trim();
        String strPass = edtPasswordLogin.getText().toString().trim();
        if (Validation.setEmailValidation( getActivity(), strEmial ) &&
                Validation.setPasswordValidation( getActivity(), strPass )) {
            if (HelperMethodCustom.isClient( strEnterClient )) {
                createLoginClient( strEmial, strPass );
                return true;
            } else {
                createLoginResturan( strEmial, strPass );
                return true;
            }
        }

        return false;
    }

    private void createLoginClient(String email, String pass) {
        HelperMethodCustom.disappearKeypad( getActivity(), btnLoginLogin );
        HelperMethodCustom.showProgressDialog( getActivity(), getString( R.string.please_wite ) );
        if (InternetState.isConnected( getActivity() )){
            apiServices.createLoginClient( email, pass ).enqueue( new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                    if (response.body().getStatus() == 1) {
                        HelperMethodCustom.createShared( getActivity(), Constant.CLIENT_API_TOKEN,
                                response.body().getData().getApiToken() );
                        Intent intent = new Intent( (UserCycleActivity) getActivity(), HomeCycleActivity.class );
                        intent.putExtra( Constant.AFTER_LOGIN_CLIENT, true );
                        startActivity( intent );
                        HelperMethodCustom.createShared( getActivity(), Constant.CLIENT_API_TOKEN,
                                response.body().getData().getApiToken() );
                        HelperMethodCustom.createShared( getActivity(), Constant.RESTURANT_ADDRESS,
                                response.body().getData().getUser().getRegion().getName() );
                    } else {
                        Log.d( TAG, "onResponse: response " + response.body().getMsg() );
                        Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                    }
                    HelperMethodCustom.dismissProgressDialog();

                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    Log.d( TAG, "onFailure: Throwable " + t.getMessage() );
                    Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                    HelperMethodCustom.dismissProgressDialog();
                }
            } );
        }
    }

    private void createLoginResturan(String email, String pass) {
        HelperMethodCustom.disappearKeypad( getActivity(), btnLoginLogin );
        apiServices.createLoginRsturante( email, pass ).enqueue( new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.body().getStatus() == 1) {
                    HelperMethodCustom.createShared( getActivity(), Constant.RESTAURANT_API_TOKEN,
                            response.body().getData().getApiToken() );
                    Intent intent = new Intent( (UserCycleActivity) getActivity(), HomeCycleActivity.class );
                    startActivity( intent );
                } else {
                    Log.d( TAG, "onResponse: response " + response.body().getMsg() );
                    Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                }
                HelperMethodCustom.dismissProgressDialog();

            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                try {
                    Log.d( TAG, "onFailure: Throwable " + t.getMessage() );
                    Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                    HelperMethodCustom.dismissProgressDialog();
                } catch (Exception e) {
                    Log.d( TAG, "onFailure:  e.getMessage() " + e.getMessage() );
                }
            }
        } );
    }
}
