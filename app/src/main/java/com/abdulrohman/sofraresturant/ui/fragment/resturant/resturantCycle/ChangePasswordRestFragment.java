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
import com.abdulrohman.sofraresturant.data.model.user.UserProfile;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;
import com.abdulrohman.sofraresturant.ui.fragment.general.MoreFragment;

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
public class ChangePasswordRestFragment extends BaseFragment {
    private static final String TAG = ChangePasswordRestFragment.class.getSimpleName();
    @BindView(R.id.edt_old_chng_pass)
    EditText edtOldChngPass;
    @BindView(R.id.edt_new_pass_chng_pass)
    EditText edtNewPassChngPass;
    @BindView(R.id.edt_confirm_new_chng_pass)
    EditText edtConfirmNewChngPass;
    @BindView(R.id.btn_change_chng_pass)
    Button btnChangeChngPass;
    Unbinder unbinder;

    public ChangePasswordRestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_change_pass_rest, container, false );
        unbinder = ButterKnife.bind( this, view );
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

    @OnClick(R.id.btn_change_chng_pass)
    public void onViewClicked() {
        apiServices.changePassRest( HelperMethodCustom.getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ),
                edtOldChngPass.getText().toString(), edtNewPassChngPass.getText().toString(),
                edtConfirmNewChngPass.getText().toString() ).enqueue( new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.body().getStatus() == 1) {
                    MoreFragment fragment = new MoreFragment();
                    HelperMethodCustom.ReplaceFragment( getFragmentManager(), fragment, R.id.fram_container_home,
                            null, null );
                } else {
                    Log.d( TAG, "onResponse: getStatus()==0 " + response.body().getMsg() );
                    Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                Log.d( TAG, "onFailure: t " + t.getMessage() );
            }
        } );
    }
}
