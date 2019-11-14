package com.abdulrohman.sofraresturant.ui.fragment.client.order;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.local.room.AppDatabase;
import com.abdulrohman.sofraresturant.data.model.item.ItemData;
import com.abdulrohman.sofraresturant.data.model.order.NewOrder;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;

import java.util.ArrayList;

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
public class CheckFinalFragment extends BaseFragment {
    private static final String TAG = CheckFinalFragment.class.getSimpleName();
    @BindView(R.id.edt_add_note_final_check)
    EditText edtAddNoteFinalCheck;
    @BindView(R.id.txt_adress_check_final)
    TextView txtAdressCheckFinal;
    @BindView(R.id.img_check_check_final)
    ImageView imgCheckCheckFinal;
    @BindView(R.id.txt_onlin_check_final)
    TextView txtOnlinCheckFinal;
    @BindView(R.id.img_online_final_check)
    ImageView imgOnlineFinalCheck;
    @BindView(R.id.txt_price_final_check)
    TextView txtPriceFinalCheck;
    @BindView(R.id.txt_charger_final_check)
    TextView txtChargerFinalCheck;
    @BindView(R.id.txt_all_price_final_check)
    TextView txtAllPriceFinalCheck;
    @BindView(R.id.btn_confirm_order_final_check)
    Button btnConfirmOrderFinalCheck;
    Unbinder unbinder;
    //vars
    private String strNote = "";
    private String strAddress = "";
    private String phone = "";
    private double priceOrder;
    private double priceCharger;
    private double allPrice;
    private int payMethod;
    private int resturantId;
    private ArrayList<ItemData> itemData;
    private ArrayList<Integer> items;
    private ArrayList<String> quantites;
    private ArrayList<String> notes;
    private AppDatabase appDatabase;
    private SharedPreferences prefs;


    public CheckFinalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_check_final, container, false );
        unbinder = ButterKnife.bind( this, view );
        appDatabase = AppDatabase.getInstanceRoom( getActivity() );
        imgCheckCheckFinal.setImageResource( R.drawable.ic_check );
        payMethod = 1;//when recive

        resturantId = HelperMethodCustom.getResturanId( getActivity() );
        itemData = new ArrayList<>();
        quantites = new ArrayList<>();
        notes = new ArrayList<>();
        items = new ArrayList<>();

        itemData = (ArrayList<ItemData>) appDatabase.itemDao().getAllOrder();
        for (int i = 0; i < itemData.size(); i++) {
            Log.d( TAG, "onCreateView: " + itemData.get( i ).getPrice() );
        }
        for (int i = 0; i < itemData.size(); i++) {
            notes.add( itemData.get( i ).getNote() );
            quantites.add( itemData.get( i ).getQuantity() );
            items.add( itemData.get( i ).getId() );
        }

        initText();
        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    private void initText() {
        prefs = getActivity().getSharedPreferences( Constant.SHIRD_API_TOKEN, Context.MODE_PRIVATE );

        strAddress = prefs.getString( Constant.RESTURANT_ADDRESS, null );
        txtAdressCheckFinal.setText( strAddress );

        priceOrder = Double.parseDouble( prefs.getString( Constant.ALL_PRAICE, null ) );
        priceCharger = 10.0;
        allPrice = priceOrder + priceCharger;
        txtPriceFinalCheck.setText( "sumition " + prefs.getString( Constant.ALL_PRAICE, null ) );
        txtChargerFinalCheck.setText( "charger price " + String.valueOf( priceCharger ) );
        txtAllPriceFinalCheck.setText( "all price " + String.valueOf( allPrice ) );
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

    @OnClick({R.id.img_check_check_final, R.id.img_online_final_check, R.id.btn_confirm_order_final_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_check_check_final:
                imgOnlineFinalCheck.setImageResource( R.color.white );
                imgCheckCheckFinal.setImageResource( R.drawable.ic_check );
                payMethod = 1;
                break;
            case R.id.img_online_final_check:
                imgOnlineFinalCheck.setImageResource( R.drawable.ic_check );
                imgCheckCheckFinal.setImageResource( R.color.white );
                payMethod = 2;
                break;
            case R.id.btn_confirm_order_final_check:
                createOrder();
                break;
        }
    }

    private void createOrder() {
        strNote = edtAddNoteFinalCheck.getText().toString();
        String strName = prefs.getString( Constant.RESTURANT_NAME, null );

            apiServices.createOrder( String.valueOf( resturantId ), strNote, strAddress, payMethod, phone, strName,
                    HelperMethodCustom.getApiToken( getActivity(), Constant.CLIENT_API_TOKEN ),
                    items, quantites, notes ).enqueue( new Callback<NewOrder>() {
                @Override
                public void onResponse(Call<NewOrder> call, Response<NewOrder> response) {
                    try {
                        if (response.body().getStatus() == 1) {
                            OrderInformationFragment orderInformationFragment = new OrderInformationFragment();
                            Bundle args = new Bundle();
                            args.putSerializable( Constant.ORDER_ID_CHICK,
                                    response.body().getData().getId() );
                            orderInformationFragment.setArguments( args );
                            HelperMethodCustom.ReplaceFragment( getFragmentManager(), orderInformationFragment,
                                    R.id.fram_container_home, null, null );
                        } else {
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                        }

                    } catch (Exception e) {
                        Log.d( TAG, "createOrder: e " + e.getMessage() );
                    }
                }

                @Override
                public void onFailure(Call<NewOrder> call, Throwable t) {
                        try {
                    Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                        } catch (Exception e) {
                            Log.d( TAG, "createOrder: e " + e.getMessage() );
                        }
                }
            } );

    }

}
