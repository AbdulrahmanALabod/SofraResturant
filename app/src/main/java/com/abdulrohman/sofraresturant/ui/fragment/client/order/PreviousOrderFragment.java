package com.abdulrohman.sofraresturant.ui.fragment.client.order;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.adapter.OrderAdapter;
import com.abdulrohman.sofraresturant.data.model.order.Order;
import com.abdulrohman.sofraresturant.data.model.order.OrderData;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.helper.OnEndLess;
import com.abdulrohman.sofraresturant.helper.OrderViewPager;
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
public class PreviousOrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        OrderViewPager {
    private static final String TAG = "PreviousOrderFragment";
    public OrderFragment orderFragment;
    //var
    public List<OrderData> lstOrderData = new ArrayList<>();
    Unbinder unbinder;
    @BindView(R.id.recycle_previos_order)
    RecyclerView recyclePreviosOrder;
    @BindView(R.id.swipe_container_previous)
    SwipeRefreshLayout swipeContainerPrevious;
    private OnEndLess onEndLess;
    private int maxPage;
    private int previousPage = 1;
    private OrderAdapter adapt;
    private int refreshPostion;
    private LinearLayoutManager linearLayoutManager;

    public PreviousOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        linearLayoutManager = new LinearLayoutManager( getContext() );
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );

        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_previous_order, container, false );
        unbinder = ButterKnife.bind( this, view );
        if(recyclePreviosOrder!= null){
            recyclePreviosOrder.setLayoutManager( linearLayoutManager );
        }
        swipeContainerPrevious.setOnRefreshListener( this );
        swipeContainerPrevious.setColorSchemeResources( R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark );


        refreshLyout();

        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    private void refreshLyout() {
        swipeContainerPrevious.setRefreshing( true );
        swipeContainerPrevious.post( new Runnable() {
            @Override
            public void run() {
                setRecycle( lstOrderData );
                if (lstOrderData.size() == 0) {
                    Log.d( TAG, "onCreateView: size()==0" );
                    getPreviousOrder( 1 );
                } else {
                    swipeContainerPrevious.setRefreshing( false );
                }
            }
        } );
    }

    private void getPreviousOrder(int page) {
        refreshPostion = page;
        swipeContainerPrevious.setRefreshing( true );
        String token = "";
        if (Constant.CLIENT_TYPE.equals( HelperMethodCustom
                .getSheardTypeEnter( getActivity(), Constant.TYPE_ENTER_CLIENT ) )) {
            token = HelperMethodCustom.getApiToken( getActivity(), Constant.CLIENT_API_TOKEN );

            getMyOrderPrevious( apiServices.getMyOrders( token, Constant.COMMPLET_STATE_ORDER, page ) );
        } else {
            token = HelperMethodCustom.getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN );
            getMyOrderPrevious( apiServices.getMyOrders( token, Constant.COMMPLET_STATE_ORDER, page ) );
        }


    }

    private void getMyOrderPrevious(Call<Order> myOrders) {
        myOrders.enqueue( new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.body().getStatus() == 1) {

                    List<OrderData> orderData = response.body().getData().getData();
                    maxPage = response.body().getData().getLastPage();
                    setRecycle( orderData );
                } else {
                    Log.d( TAG, "onResponse:response.body().getMsg() " + response.body().getMsg() );
                    Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                }
                if (swipeContainerPrevious != null) {
                    swipeContainerPrevious.setRefreshing( false );
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable throwable) {
                Log.d( TAG, "onFailure: throwable " + throwable );
                Toast.makeText( getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT ).show();
                if (getActivity() != null) {
                    swipeContainerPrevious.setRefreshing( false );
                }
            }
        } );
    }

    private void setRecycle(List<OrderData> lstData) {

        adapt = new OrderAdapter( getContext(), PreviousOrderFragment.this,
                lstData, null, null );

        onEndLess = new OnEndLess( 1, linearLayoutManager ) {
            @Override
            public void onLoadMore(int page) {


                if (currentPage <= maxPage) {
                    if (maxPage != 0 && currentPage != 1) {
                        previousPage = currentPage;
                        getPreviousOrder( currentPage );
                        showToast( getActivity(), String.valueOf( currentPage ) );
                    } else {
                        onEndLess.currentPage = previousPage;
                    }
                } else {
                    onEndLess.currentPage = previousPage;
                }
            }
        };
        recyclePreviosOrder.setAdapter( adapt );
        adapt.notifyDataSetChanged();

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


    @Override
    public void onRefresh() {
        getPreviousOrder( refreshPostion );
    }

    @Override
    public void OnOrder(OrderData order) {
        Log.d( TAG, "OnOrder: called" );
        Log.d( TAG, "OnOrder: order " + order.getState() );
        lstOrderData.add( order );
    }
}
