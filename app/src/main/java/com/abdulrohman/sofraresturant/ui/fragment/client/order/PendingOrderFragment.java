package com.abdulrohman.sofraresturant.ui.fragment.client.order;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.adapter.OrderAdapter;
import com.abdulrohman.sofraresturant.data.model.order.Order;
import com.abdulrohman.sofraresturant.data.model.order.OrderData;
import com.abdulrohman.sofraresturant.helper.AppDialog;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.helper.OnEndLess;
import com.abdulrohman.sofraresturant.ui.activity.BaseActivity;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class PendingOrderFragment extends BaseFragment implements BaseActivity.OnPositionItem,
        BaseActivity.OnPressButton, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "PendingOrderFragment";
    public OrderFragment orderFragment;
    Unbinder unbinder;
    @BindView(R.id.recycle_pending_order)
    RecyclerView recyclePendingOrder;
    @BindView(R.id.swipe_container_pending)
    SwipeRefreshLayout swipeContainerPending;
    //var
    private List<OrderData> lstItemData = new ArrayList<>();
    private OnEndLess onEndLess;
    private int previousPage;
    private int maxPage;
    private OrderAdapter adapt;
    private int refreshId;
    private String strReasonCancel = "";

    public PendingOrderFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d( TAG, "onCreateView: start" );
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_pending_order, container, false );
        unbinder = ButterKnife.bind( this, view );

        swipeContainerPending.setOnRefreshListener( this );
        swipeContainerPending.setColorSchemeResources( R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark );

        refresLayout();
        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    private void refresLayout() {
        if (getActivity() != null) {
            swipeContainerPending.setRefreshing( true );
        }
        swipeContainerPending.post( new Runnable() {
            @Override
            public void run() {
                setRecycle( lstItemData );
                if (lstItemData.size() == 0) {
                    Log.d( TAG, "onCreateView: size()==0" );
                    getPendingOrder( 1 );
                } else {
                    swipeContainerPending.setRefreshing( false );
                }
            }
        } );
    }

    private void getPendingOrder(final int page) {
        String token = "";
        if (Constant.CLIENT_TYPE.equals( HelperMethodCustom
                .getSheardTypeEnter( getActivity(), Constant.TYPE_ENTER_CLIENT ) )) {
            token = HelperMethodCustom.getApiToken( getActivity(), Constant.CLIENT_API_TOKEN );
            getMyOrder( apiServices.getMyOrders( token, Constant.PENDING_STATE_ORDER, page ), page );

        } else {
            token = HelperMethodCustom.getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN );
            getMyOrder( apiServices.getMyOrdersRest( token, Constant.PENDING_STATE_ORDER, page ), page );
        }

    }

    private void getMyOrder(Call<Order> myOrdersRest, final int page) {
        swipeContainerPending.setRefreshing( true );
        myOrdersRest.enqueue( new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        if (page == 1) {
                            previousPage = 1;
                            onEndLess.currentPage = 1;
                            onEndLess.preiviosTotal = 0;
                        }
                        maxPage = response.body().getData().getLastPage();
                        lstItemData.clear();
                        lstItemData.addAll( response.body().getData().getData() );

                        setRecycle( lstItemData );
                        Log.d( TAG, "onResponse: getStatus() == 1" );


                    } else {
                        Log.d( TAG, "onResponse: " + response.body().getMsg() );
                        Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                    }
                    if (swipeContainerPending != null) {
                        swipeContainerPending.setRefreshing( false );
                    }
                } catch (NullPointerException e) {
                    Log.d( TAG, "onResponse: NullPointerException " + e.getMessage() );
                } catch (Exception e) {
                    Log.d( TAG, "onResponse: Exception " + e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable throwable) {
                try {
                    Log.d( TAG, "onFailure: " + throwable.getMessage() );
                    if (getActivity() != null)
                        swipeContainerPending.setRefreshing( false );
                } catch (NullPointerException e) {
                    Log.d( TAG, "onFailure: NullPointerException " + e.getMessage() );
                } catch (Exception e) {
                    Log.d( TAG, "onFailure: Exception " + e.getMessage() );
                }
            }

        } );
    }

    private void setRecycle(List<OrderData> lstData) {
        adapt = new OrderAdapter( getContext(), PendingOrderFragment.this,
                lstData, this, this );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        recyclePendingOrder.setLayoutManager( linearLayoutManager );
        onEndLess = new OnEndLess( 1, linearLayoutManager ) {
            @Override
            public void onLoadMore(int page) {

                if (currentPage <= maxPage) {
                    if (maxPage != 0 && currentPage != 1) {
                        setPositionRefresh( currentPage );
                        previousPage = currentPage;
                        getPendingOrder( currentPage );
                        showToast( getActivity(), String.valueOf( currentPage ) );
                    } else {
                        onEndLess.currentPage = previousPage;
                    }
                } else {
                    onEndLess.currentPage = previousPage;
                }
            }
        };
        recyclePendingOrder.setAdapter( adapt );
        adapt.notifyDataSetChanged();

    }

    private void setPositionRefresh(int currentPage) {
        refreshId = currentPage;
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    // onSucssess call in client only
    @Override
    public void onSucssess(final int position) {
        final OrderData orderData = lstItemData.get( position );

        apiServices.declineOrder( orderData.getId(),
                HelperMethodCustom.getApiToken( getActivity(), Constant.CLIENT_API_TOKEN ) )
                .enqueue( new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        try {
                            if (response.body().getStatus() == 1) {

                                orderData.setState( Constant.REJECTED_STATE_ORDER );
                                orderFragment.currentOrderFragment.lstItemData.add( orderData );
                                lstItemData.remove( position );
                                adapt.notifyItemRemoved( position );
                                adapt.notifyDataSetChanged();
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

    @Override
    public void onPositive(final int position) {
        if (!Constant.TYPE_ENTER_CLIENT.equals( HelperMethodCustom
                .getSheardTypeEnter( getActivity(), Constant.TYPE_ENTER_CLIENT ) )) {
            final OrderData orderData = lstItemData.get( position );


            apiServices.acceptOrderRest( orderData.getId(), HelperMethodCustom
                    .getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ) )
                    .enqueue( new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            try {
                                if (response.body().getStatus() == 1) {
                                    orderData.setState( Constant.ACCESSPTED_STATE_ORDER );
                                    orderFragment.currentOrderFragment.lstItemData.add( orderData );
                                    lstItemData.remove( position );
                                    adapt.notifyItemRemoved( position );
                                } else {
                                    Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                                }

                            } catch (Exception e) {
                                Log.d( TAG, "onPositive: e " + e.getMessage() );
                            }
                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            try {

                                Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                            } catch (Exception e) {
                                Log.d( TAG, "onPositive: e " + e.getMessage() );
                            }
                        }
                    } );


        }
    }

    @Override
    public void onNegtive(final int position) {
        AppDialog appDialog = new AppDialog();
        Dialog dialog = appDialog.showDialog( getActivity(),
                R.layout.dialog_reason_cancel );
        dialog.show();
        final EditText edtReasonCancel = dialog.findViewById( R.id.edt_reson_cancel_dialog );
        Button btnCancel = dialog.findViewById( R.id.btn_send_reson_cancel_dialog );

        btnCancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strReasonCancel = edtReasonCancel.getText().toString();
                if (!Constant.TYPE_ENTER_CLIENT.equals( HelperMethodCustom
                        .getSheardTypeEnter( getActivity(), Constant.TYPE_ENTER_CLIENT ) )) {
                    rejectOrderRest( strReasonCancel, position );
                }
            }
        } );


    }

    private void rejectOrderRest(final String strReasonCancel, final int position) {
        final OrderData orderData = lstItemData.get( position );

            apiServices.rejectOrderRest( orderData.getId(), HelperMethodCustom
                    .getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ), strReasonCancel )
                    .enqueue( new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            try {
                                if (response.body().getStatus() == 1) {
                                    orderData.setState( Constant.REJECTED_STATE_ORDER );
                                    orderFragment.previousOrderFragment.lstOrderData.add( orderData );
                                    lstItemData.remove( position );
                                    adapt.notifyItemRemoved( position );

                                } else {
                                    Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                                    Log.d( TAG, "onResponse: getStatus() == 0 " + response.body().getMsg() );
                                }
                            } catch (Exception e) {
                                Log.d( TAG, "onNegtive: e" + e.getMessage() );
                            }
                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            try {
                                Log.d( TAG, "onFailure: " + t.getMessage() );
                                Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                            }catch (Exception e) {
                                Log.d( TAG, "onNegtive: e" + e.getMessage() );
                            }
                        }
                    } );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        getPendingOrder( refreshId );
    }
}
