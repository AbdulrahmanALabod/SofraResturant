package com.abdulrohman.sofraresturant.ui.fragment.general.offer;


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
import com.abdulrohman.sofraresturant.adapter.OfferAdapter;
import com.abdulrohman.sofraresturant.data.model.item.Item;
import com.abdulrohman.sofraresturant.data.model.item.ItemData;
import com.abdulrohman.sofraresturant.helper.OnEndLess;
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
public class OfferFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = OfferFragment.class.getSimpleName();
    //var
    List<ItemData> lstOffers = new ArrayList<>();
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    Unbinder unbinder;
    @BindView(R.id.rfrsh_offer)
    SwipeRefreshLayout rfrshOffer;
    private OnEndLess onEndLess;
    private OfferAdapter offerAdapter;
    private int previousPage = 1;
    private int maxPage;
    private int posRefrish;


    public OfferFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_offer, container, false );
        unbinder = ButterKnife.bind( this, view );

        rfrshOffer.setOnRefreshListener( this );
        rfrshOffer.setColorSchemeResources( R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark );

        refreshLyout();
        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    private void refreshLyout() {
        rfrshOffer.setRefreshing( true );
        rfrshOffer.post( new Runnable() {
            @Override
            public void run() {
                initRecycle();
                if (lstOffers.size() == 0) {
                    getOffers( 1 );
                } else {
                    rfrshOffer.setRefreshing( false );
                }
            }
        } );
    }

    private void initRecycle() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity() );
        recycleView.setLayoutManager( linearLayoutManager );
        onEndLess = new OnEndLess( 1, linearLayoutManager ) {
            @Override
            public void onLoadMore(int page) {

                showToast( getActivity(), String.valueOf( currentPage ) );
                if (currentPage <= maxPage) {
                    if (maxPage != 0 && currentPage != 1) {
                        previousPage = currentPage;
                        getOffers( currentPage );
                    } else {
                        onEndLess.currentPage = previousPage;
                    }
                } else {
                    onEndLess.currentPage = previousPage;
                }
            }
        };
        recycleView.addOnScrollListener( onEndLess );

        offerAdapter = new OfferAdapter( getActivity(), lstOffers, getFragmentManager() );
        recycleView.setAdapter( offerAdapter );
        offerAdapter.notifyDataSetChanged();
    }

    private void getOffers(final int page) {
        posRefrish = page;
        apiServices.getOffers().enqueue( new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.body().getStatus() == 1) {
                    if (page == 1) {
                        offerAdapter = new OfferAdapter( getActivity(), lstOffers, getFragmentManager() );
                        lstOffers = new ArrayList<>();
                        previousPage = 1;
                        onEndLess.currentPage = 1;
                        onEndLess.preiviosTotal = 0;
                    }
                    maxPage = response.body().getData().getLastPage();
                    lstOffers.clear();
                    lstOffers.addAll( response.body().getData().getData() );
                    initRecycle();
                    offerAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText( getContext(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                }
                if (getActivity() != null) {
                    rfrshOffer.setRefreshing( false );
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Toast.makeText( getContext(), t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );

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
        getOffers( posRefrish );
    }
}
