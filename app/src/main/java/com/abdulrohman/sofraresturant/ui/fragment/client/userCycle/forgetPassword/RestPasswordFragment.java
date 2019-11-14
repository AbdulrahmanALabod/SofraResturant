package com.abdulrohman.sofraresturant.ui.fragment.client.userCycle.forgetPassword;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.abdulrohman.sofraresturant.R;

import com.abdulrohman.sofraresturant.data.model.resturant.Resturants;
import com.abdulrohman.sofraresturant.data.model.user.UserProfile;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;

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
public class RestPasswordFragment extends BaseFragment {
    private static final String TAG = "RestPasswordFragment";
    @BindView(R.id.edt_email_reset1)
    EditText edtEmailReset1;
    @BindView(R.id.btn_send_reset1)
    Button btnSendReset1;
    Unbinder unbinder;

    public RestPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );

        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_rest_password, container, false );
        unbinder = ButterKnife.bind( this, view );

        HelperMethodCustom.setCloroEditText( getContext(), edtEmailReset1, R.color.white );

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

    @OnClick(R.id.btn_send_reset1)
    public void onViewClicked() {
        String email = edtEmailReset1.getText().toString();
        if (email.isEmpty()) {
            showToast(getActivity(), getString( R.string.enter_code ) );
        } else {
            setReset( email );
        }
        Fragment fragment = new NewPasswordFragment();
        HelperMethodCustom.ReplaceFragment( getFragmentManager(), fragment, R.id.fram_container_user,
                null, null );
    }

    private void setReset(String email) {
        apiServices.setReset1( email ).enqueue( new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.body().getStatus() == 1) {
                    Log.d( TAG, "onResponse: getStatus()==1" );
                } else {
                    Log.d( TAG, "onResponse: getStatus()==0 " + response.message() );
                    showToast( getActivity(),response.message() );
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.d( TAG, "onFailure: " + t.getMessage() );
                showToast( getActivity(),t.getMessage() );
            }
        } );
    }
}
