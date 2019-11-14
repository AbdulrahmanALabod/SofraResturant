package com.abdulrohman.sofraresturant.ui.fragment.client.order;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.adapter.OrderShowAdapter;
import com.abdulrohman.sofraresturant.data.model.item.ItemData;
import com.abdulrohman.sofraresturant.data.model.order.NewOrder;
import com.abdulrohman.sofraresturant.data.model.order.Order;
import com.abdulrohman.sofraresturant.data.model.order.OrderData;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderInformationFragment extends BaseFragment {
    private static final String TAG = OrderInformationFragment.class.getSimpleName();

    @BindView(R.id.img_order_information)
    CircleImageView imgOrderInformation;
    @BindView(R.id.txt_name_rest_order_information)
    TextView txtNameRestOrderInformation;
    @BindView(R.id.txt_date_order_information)
    TextView txtDateOrderInformation;
    @BindView(R.id.txt_address_order_information)
    TextView txtAddressOrderInformation;
    @BindView(R.id.recl_all_order_infromation)
    RecyclerView reclAllOrderInfromation;
    @BindView(R.id.txt_net_price_order_information)
    TextView txtNetPriceOrderInformation;
    @BindView(R.id.txt_price_charger_order_information)
    TextView txtPriceChargerOrderInformation;
    @BindView(R.id.txt_all_price_order_information)
    TextView txtAllPriceOrderInformation;
    @BindView(R.id.txt_pay_way_price_order_information)
    TextView txtPayWayPriceOrderInformation;
    @BindView(R.id.btn_call_order_information)
    Button btnCallOrderInformation;
    @BindView(R.id.btn_accept_order_information)
    Button btnAcceptOrderInformation;
    @BindView(R.id.btn_cancel_order_information)
    Button btnCancelOrderInformation;
    //vars
    Unbinder unbinder;
    private int orderId;
    private OrderShowAdapter orderShowAdapter;
    private LinearLayoutManager linearLayoutManager;

    public OrderInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_order_information, container, false );
        unbinder = ButterKnife.bind( this, view );
        linearLayoutManager = new LinearLayoutManager( getActivity() );
        if (reclAllOrderInfromation != null) {
            reclAllOrderInfromation.setLayoutManager( linearLayoutManager );
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            orderId = bundle.getInt( Constant.ORDER_ID_CHICK );
            Log.d( TAG, "onCreateView: orderId " + orderId );
            showOrder();
        }

        Log.d( TAG, "onCreateView: end" );

        return view;
    }

    private void showOrder() {
        apiServices.showMyOrder( HelperMethodCustom
                .getApiToken( getActivity(), Constant.CLIENT_API_TOKEN ), orderId )
                .enqueue( new Callback<NewOrder>() {
                    @Override
                    public void onResponse(Call<NewOrder> call, Response<NewOrder> response) {
                        try {
                            if (response.body().getStatus() == 1) {
                                initWedgit( response.body().getData() );

                            } else {
                                Log.d( TAG, "onResponse: " + response.body().getMsg() );
                            }
                        } catch (Exception e) {
                            Log.d( TAG, "onResponse: e " + e.getMessage() );
                        }
                    }

                    @Override
                    public void onFailure(Call<NewOrder> call, Throwable t) {
                        Log.d( TAG, "onFailure: t " + t.getMessage() );
                    }
                } );
    }


    private void initWedgit(OrderData data) {
        HelperMethodCustom.onLoadImageFromUrl( imgOrderInformation, data.getRestaurant().getPhotoUrl(),
                getActivity(), 1 );
        txtNameRestOrderInformation.setText( data.getRestaurant().getName() );
        txtDateOrderInformation.setText( getString( R.string.date ) + " " + data.getCreatedAt() );
        txtAddressOrderInformation.setText( getString( R.string.address ) + " " + data.getAddress() );
        txtNetPriceOrderInformation.setText( getString( R.string.price ) + " " + data.getCost() + getString( R.string.dollar ) );
        txtPriceChargerOrderInformation.setText( getString( R.string.delivery_Cost ) + " " + data.getDeliveryCost() + getString( R.string.dollar ) );
        txtAllPriceOrderInformation.setText( getString( R.string.all_price ) + " " + data.getTotal() + getString( R.string.dollar ) );
        HelperMethodCustom.onLoadImageFromUrl( imgOrderInformation, data.getRestaurant().getPhotoUrl(), getActivity(), 1 );
        if ("1".equals( data.getPaymentMethodId() )) {
            txtPayWayPriceOrderInformation.setText( getString( R.string.pay ) + " " + getString( R.string.cash ) );
        } else if ("2".equals( data.getPaymentMethodId() )) {
            txtPayWayPriceOrderInformation.setText( getString( R.string.pay ) + " " + getString( R.string.online ) );
        }
        initRecycle( data.getItems() );

    }

    private void initRecycle(List<ItemData> lstItemData) {
        orderShowAdapter = new OrderShowAdapter( getActivity(), lstItemData );
        reclAllOrderInfromation.setAdapter( orderShowAdapter );
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

    @OnClick({R.id.btn_call_order_information, R.id.btn_accept_order_information, R.id.btn_cancel_order_information})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_call_order_information:
                break;
            case R.id.btn_accept_order_information:
                approveOrder();
                break;
            case R.id.btn_cancel_order_information:
                declineOrder();
                break;
        }
    }

    private void approveOrder() {
        apiServices.approveOrder( orderId,
                HelperMethodCustom.getApiToken( getActivity(), Constant.CLIENT_API_TOKEN ) )
                .enqueue( new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        try {
                            if (response.body().getStatus() == 1) {
                                Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_LONG ).show();
                            } else {
                                Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_LONG ).show();
                            }
                        } catch (Exception e) {
                            Log.d( TAG, "onResponse: e " + e.getMessage() );
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        try {
                            Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                        } catch (Exception e) {
                            Log.d( TAG, "onFailure: e " + e.getMessage() );
                        }
                    }
                } );
    }

    private void declineOrder() {
        apiServices.declineOrder( orderId,
                HelperMethodCustom.getApiToken( getActivity(), Constant.CLIENT_API_TOKEN ) )
                .enqueue( new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        try {
                            if (response.body().getStatus() == 1) {
                                Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_LONG ).show();
                            } else {
                                Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_LONG ).show();
                            }
                        } catch (Exception e) {
                            Log.d( TAG, "onSucssess: e " + e.getMessage() );
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        showToast( getActivity(), t.getMessage() );
                    }
                } );
    }
}
