package com.abdulrohman.sofraresturant.ui.fragment.client.userCycle.forgetPassword;


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
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;
import com.abdulrohman.sofraresturant.ui.fragment.client.userCycle.LoginFragment;

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
public class NewPasswordFragment extends BaseFragment {
    private static final String TAG = "NewPasswordFragment";
    @BindView(R.id.edt_code_reset2)
    EditText edtCodeReset2;
    @BindView(R.id.edt_new_password_reset2)
    EditText edtNewPasswordReset2;
    @BindView(R.id.edt_confirm_new_password_reset2)
    EditText edtConfirmNewPasswordReset2;
    @BindView(R.id.btn_send_reset2)
    Button btnSendReset2;
    Unbinder unbinder;
    @BindView(R.id.txt_wrong_new_pass)
    TextView txtWrongNewPass;

    public NewPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_new_password, container, false );
        Log.d( TAG, "onCreateView: end" );
        unbinder = ButterKnife.bind( this, view );
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

    @OnClick(R.id.btn_send_reset2)
    public void onViewClicked() {
        String strCode = edtCodeReset2.getText().toString();
        String strConfirmNewPassword = edtConfirmNewPasswordReset2.getText().toString();
        String strNewPassword = edtNewPasswordReset2.getText().toString();

        if (strCode != null && strConfirmNewPassword != null && strNewPassword != null) {
            resetPassword2( strCode, strNewPassword, strConfirmNewPassword );
        } else {
            txtWrongNewPass.setVisibility( View.VISIBLE );
        }
    }


    private void resetPassword2(String code, String pass, String ConfirmPass) {
        apiServices.setReset2( code, pass, ConfirmPass ).enqueue( new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                try {
                    if ((response.body().getStatus() == 1)) {
                        LoginFragment loginFragment = new LoginFragment();
                        HelperMethodCustom.ReplaceFragment( getFragmentManager(), loginFragment,
                                R.id.fram_container_home, null, null );
                    } else {
                        Log.d( TAG, "onResponse: " + response.body().getMsg() );
                        Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                    }
                } catch (Exception e) {
                    Log.d( TAG, "onResponse: e " + e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                try {

                    Log.d( TAG, "onFailure: " + t.getMessage() );
                    Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                } catch (Exception e) {
                    Log.d( TAG, "onResponse: e " + e.getMessage() );
                }
            }
        } );
    }
}
