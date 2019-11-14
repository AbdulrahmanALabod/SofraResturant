package com.abdulrohman.sofraresturant.ui.fragment.resturant.offer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.adapter.OfferRestAdapter;
import com.abdulrohman.sofraresturant.data.model.item.Item;
import com.abdulrohman.sofraresturant.data.model.item.ItemData;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.helper.OnEndLess;
import com.abdulrohman.sofraresturant.ui.activity.BaseActivity;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

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
public class OfferRestFragment extends BaseFragment implements BaseActivity.OnPressButton {
    private static final String TAG = OfferRestFragment.class.getSimpleName();
    //var
    OfferRestAdapter offerRestAdapter;
    List<ItemData> lstOferList = new ArrayList<>();
    @BindView(R.id.btn_add_ofr_rest)
    Button btnAddOfrRest;
    Unbinder unbinder;
    @BindView(R.id.recycl_ofr_rest)
    RecyclerView recyclOfrRest;
    private int maxPage;
    private int previos = 1;
    private OnEndLess onEndLess;
    private AddNewOfferFragment addNewOfferFragment;

    public OfferRestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_offer_rest, container, false );
        unbinder = ButterKnife.bind( this, view );
        initRecycle();
        if (lstOferList.size() == 0) {
            getOfferRest( 1 );
        }
        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    private void initRecycle() {
        LinearLayoutManager linearLayout = new LinearLayoutManager( getActivity(),
                LinearLayoutManager.VERTICAL, false );
        recyclOfrRest.setLayoutManager( linearLayout );

        onEndLess = new OnEndLess( 1, linearLayout ) {
            @Override
            public void onLoadMore(int page) {
                if (currentPage <= maxPage) {
                    if (maxPage != 0 && currentPage != 1) {
                        getOfferRest( currentPage );
                    } else {
                        currentPage = previos;
                    }
                } else {
                    currentPage = previos;
                }
            }
        };
        recyclOfrRest.addOnScrollListener( onEndLess );
        offerRestAdapter = new OfferRestAdapter( getActivity(), getActivity(), lstOferList, this );
        recyclOfrRest.setAdapter( offerRestAdapter );
        offerRestAdapter.notifyDataSetChanged();

    }

    private void getOfferRest(final int currentPage) {
        apiServices.getOffersRest( HelperMethodCustom.getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ),
                currentPage ).enqueue( new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.body().getStatus() == 1) {
                    if (currentPage == 1) {
                        lstOferList = new ArrayList<>();
                        offerRestAdapter = new OfferRestAdapter( getActivity(), getActivity(),
                                lstOferList, OfferRestFragment.this );
                        previos = 1;
                        onEndLess.currentPage = 1;
                        onEndLess.preiviosTotal = 0;
                    }
                    maxPage = response.body().getData().getLastPage();
                    lstOferList.addAll( response.body().getData().getData() );
                    initRecycle();
                } else {
                    Log.d( TAG, "onResponse: getStatus()==0 " + response.body().getMsg() );
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.d( TAG, "onFailure: t " + t.getMessage() );
                Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        if (offerRestAdapter != null) {
            offerRestAdapter.saveStates( outState );
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        if (offerRestAdapter != null) {
            offerRestAdapter.restoreStates( savedInstanceState );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPositive(int position) {
        ItemData itemData = lstOferList.get( position );
        addNewOfferFragment = new AddNewOfferFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable( Constant.OFFER_REST_ID, itemData );
        addNewOfferFragment.setArguments( bundle );
        HelperMethodCustom.ReplaceFragment( getFragmentManager(), addNewOfferFragment, R.id.fram_container_home,
                null, null );
    }


    @Override
    public void onNegtive(final int position) {
        ItemData itemData;

            itemData = lstOferList.get( position );
            apiServices.deleatOffersRest( HelperMethodCustom.getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ),
                    itemData.getId() ).enqueue( new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {
                    try {
                        if (response.body().getStatus() == 1) {
                            lstOferList.remove( position );
                            offerRestAdapter.notifyItemRemoved( position );
                        } else {
                            Log.d( TAG, "onResponse: getStatus()==0 " + response.body().getMsg() );
                        }
                    } catch (Exception e) {
                        Log.d( TAG, "onNegtive: e " + e.getMessage() );
                        Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) {
                    try {

                        Log.d( TAG, "onFailure: t " + t.getMessage() );
                    }catch (Exception e) {
                        Log.d( TAG, "onFailure: e " + e.getMessage() );
                        Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                }
            } );

    }

    @OnClick(R.id.btn_add_ofr_rest)
    public void onViewClicked() {
        addNewOfferFragment = new AddNewOfferFragment();
        HelperMethodCustom.ReplaceFragment( getFragmentManager(), addNewOfferFragment, R.id.fram_container_home,
                null, null );
    }
}
