package com.abdulrohman.sofraresturant.ui.fragment.general;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.connect.Connect;
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
public class ConnectFragment extends BaseFragment {
    private static final String TAG = "ConnectFragment";
    @BindView(R.id.edt_name_connect_us)
    EditText edtNameConnectUs;
    @BindView(R.id.edt_email_connect_us)
    EditText edtEmailConnectUs;
    @BindView(R.id.edt_phone_connect_us)
    EditText edtPhoneConnectUs;
    @BindView(R.id.edt_message_connect_us)
    EditText edtMessageConnectUs;
    @BindView(R.id.rdio_complaint_connect_us)
    RadioButton rdioComplaintConnectUs;
    @BindView(R.id.rdio_gess_connect_us)
    RadioButton rdioGessConnectUs;
    @BindView(R.id.rdio_query_connect_us)
    RadioButton rdioQueryConnectUs;
    @BindView(R.id.btn_send_connect_us)
    Button btnSendConnectUs;
    Unbinder unbinder;
    // var
    TypeSuggess typeSuggess;

    public ConnectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_connect, container, false );

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

    @OnClick({R.id.rdio_complaint_connect_us, R.id.rdio_gess_connect_us, R.id.rdio_query_connect_us,
            R.id.btn_send_connect_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rdio_complaint_connect_us:
                typeSuggess = TypeSuggess.complaint;
                break;
            case R.id.rdio_gess_connect_us:
                typeSuggess = TypeSuggess.suggestion;
                break;
            case R.id.rdio_query_connect_us:
                typeSuggess = TypeSuggess.inquiry;
                break;
            case R.id.btn_send_connect_us:
                creatConnect();
                break;
        }
        Log.d( TAG, "onViewClicked: typeSuggess " + typeSuggess.toString() );
    }

    private void creatConnect() {
        String strName = edtNameConnectUs.getText().toString();
        String strEmail = edtEmailConnectUs.getText().toString();
        String strPhone = edtPhoneConnectUs.getText().toString();
        String strMsg = edtMessageConnectUs.getText().toString();
        apiServices.putConnect( strName, strEmail, strPhone, typeSuggess.toString(), strMsg )
                .enqueue( new Callback<Connect>() {
                    @Override
                    public void onResponse(Call<Connect> call, Response<Connect> response) {
                        if (response.body().getStatus() == 1) {
                            Log.d( TAG, "onResponse:getStatus()==1 " );
                            onBack();
                        } else {
                            showToast( getActivity(), response.message() );
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                            Log.d( TAG, "onResponse: getStatus()==0" );
                        }
                    }

                    @Override
                    public void onFailure(Call<Connect> call, Throwable t) {
                        showToast( getActivity(), t.getMessage() );
                        Log.d( TAG, "onFailure: " + t.getMessage() );
                    }
                } );
    }

    public enum TypeSuggess {
        complaint, suggestion, inquiry
    }
}
