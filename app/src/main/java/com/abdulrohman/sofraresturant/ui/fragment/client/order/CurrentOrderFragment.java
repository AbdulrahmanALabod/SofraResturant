package com.abdulrohman.sofraresturant.ui.fragment.client.order;


import android.annotation.SuppressLint;
import android.os.Bundle;
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

import static com.abdulrohman.sofraresturant.R.layout.fragment_order;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentOrderFragment extends BaseFragment implements BaseActivity.OnPositionItem,
        BaseActivity.OnPressButton, SwipeRefreshLayout.OnRefreshListener, OrderViewPager {
    private static final String TAG = "CurrentOrderFragment";
    //var
    public List<OrderData> lstItemData = new ArrayList<>();
    public OrderFragment orderFragment;
    Unbinder unbinder;
    @BindView(R.id.recycle_current_order)
    RecyclerView recycleCurrentOrder;
    @BindView(R.id.swipe_container_current)
    SwipeRefreshLayout swipeContainerCurrent;
    OrderViewPager orderViewPager;
    private OnEndLess onEndLess;
    private int previousPage;
    private int maxPage;
    private OrderAdapter adapt;
    private int refreshPosition;//for onRefresh()

    public CurrentOrderFragment() {
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

        View view = inflater.inflate( R.layout.fragment_current_order, container, false );
        unbinder = ButterKnife.bind( this, view );

        orderFragment = (OrderFragment) getActivity().getSupportFragmentManager()
                .findFragmentById( fragment_order );

        swipeContainerCurrent.setOnRefreshListener( this );
        swipeContainerCurrent.setColorSchemeResources( R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark );

        refreshLayout();

        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    private void refreshLayout() {
        swipeContainerCurrent.setRefreshing( true );
        swipeContainerCurrent.post( new Runnable() {
            @Override
            public void run() {
                setRecycle( lstItemData );
                if (lstItemData.size() == 0) {
                    Log.d( TAG, "onCreateView: size()==0" );
                    getCurrentOrder( 1 );
                } else {
                    swipeContainerCurrent.setRefreshing( false );
                }
            }
        } );
    }

    private void getCurrentOrder(final int page) {
        String token = "";
        if (Constant.CLIENT_TYPE.equals( HelperMethodCustom
                .getSheardTypeEnter( getActivity(), Constant.TYPE_ENTER_CLIENT ) )) {
            token = Constant.apiToken;
            getMyOrderCurrent( apiServices.getMyOrders( token, Constant.CURRENT_STATE_ORDER, page ), page );
        } else {
            token = HelperMethodCustom.getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN );
            getMyOrderCurrent( apiServices.getMyOrdersRest( token, Constant.CURRENT_STATE_ORDER, page ), page );
        }

    }

    private void getMyOrderCurrent(Call<Order> myOrders, final int page) {
        refreshPosition = page;
        swipeContainerCurrent.setRefreshing( true );
        myOrders.enqueue( new Callback<Order>() {
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
                        Log.d( TAG, "onResponse: " + response.message() );
                        Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                    }
                    if (swipeContainerCurrent != null) {
                        swipeContainerCurrent.setRefreshing( false );
                    }
                } catch (NullPointerException e) {
                    Log.d( TAG, "onResponse: NullPointerException " + e.getMessage() );
                } catch (Exception e) {
                    Log.d( TAG, "onResponse: Exception " + e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable throwable) {
                Toast.makeText( getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT ).show();
                Log.d( TAG, "onFailure: " + throwable.getMessage() );
                if (getActivity() != null) {
                    swipeContainerCurrent.setRefreshing( false );
                }
            }
        } );
    }

    private void setRecycle(List<OrderData> lstData) {
        adapt = new OrderAdapter( getContext(), CurrentOrderFragment.this,
                lstData, this, this );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        recycleCurrentOrder.setLayoutManager( linearLayoutManager );
        onEndLess = new OnEndLess( 1, linearLayoutManager ) {
            @Override
            public void onLoadMore(int page) {

                if (currentPage <= maxPage) {
                    if (maxPage != 0 && currentPage != 1) {
                        previousPage = currentPage;
                        getCurrentOrder( currentPage );
                        showToast( getActivity(), String.valueOf( currentPage ) );
                    } else {
                        onEndLess.currentPage = previousPage;
                    }
                } else {
                    onEndLess.currentPage = previousPage;
                }
            }
        };
        recycleCurrentOrder.setAdapter( adapt );
        adapt.notifyDataSetChanged();

    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @Override
    public void onSucssess(final int position) {
        final OrderData orderData = lstItemData.get( position );
        apiServices.approveOrder( orderData.getId(),
                HelperMethodCustom.getApiToken( getActivity(), Constant.CLIENT_API_TOKEN ) )
                .enqueue( new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        if (response.body().getStatus() == 1) {
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_LONG ).show();
                            orderData.setState( Constant.DELIVERED_STATE_ORDER );
                            orderFragment.previousOrderFragment.lstOrderData.add( orderData );
                            lstItemData.remove( position );
                            adapt.notifyItemRemoved( position );
                            adapt.notifyDataSetChanged();

                        } else {
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_LONG ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                } );

    }

    @Override
    public void onPositive(final int position) {
        final OrderData orderData = lstItemData.get( position );

        apiServices.confirmOrderRest( orderData.getId(), HelperMethodCustom
                .getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ) )
                .enqueue( new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        try {
                            if (response.body().getStatus() == 1) {
                                orderData.setState( Constant.DELIVERED_STATE_ORDER );
                                orderFragment.previousOrderFragment.lstOrderData.add( orderData );
                                lstItemData.remove( position );
                                adapt.notifyItemRemoved( position );
                            } else {
                                Log.d( TAG, "onResponse: getStatus()==0 " + response.body().getMsg() );
                            }
                        } catch (NullPointerException e) {
                            Log.d( TAG, "onPositive: e " + e.getMessage() );
                        } catch (Exception e) {
                            Log.d( TAG, "onPositive: e " + e.getMessage() );
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        try {
                            Log.d( TAG, "onFailure: t " + t.getMessage() );
                        } catch (NullPointerException e) {
                            Log.d( TAG, "onPositive: e " + e.getMessage() );
                        } catch (Exception e) {
                            Log.d( TAG, "onPositive: e " + e.getMessage() );
                        }
                    }
                } );

    }

    @Override
    public void onNegtive(int position) {
//don't any thing
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        getCurrentOrder( refreshPosition );
    }

    @Override
    public void OnOrder(OrderData order) {
        Log.d( TAG, "OnOrder: " );
        lstItemData.add( order );
    }
}
