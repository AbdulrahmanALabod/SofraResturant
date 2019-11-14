package com.abdulrohman.sofraresturant.ui.fragment.resturant.foodItem;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.adapter.ItemsCatgAdapter;
import com.abdulrohman.sofraresturant.data.model.item.Item;
import com.abdulrohman.sofraresturant.data.model.item.ItemData;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.helper.MediaLoader;
import com.abdulrohman.sofraresturant.helper.OnEndLess;
import com.abdulrohman.sofraresturant.ui.activity.BaseActivity;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

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
public class MyItemsFragment extends BaseFragment implements BaseActivity.OnPressButton,
        SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = MyItemsFragment.class.getSimpleName();

    //var
    Unbinder unbinder;
    @BindView(R.id.swip_container_my_item)
    SwipeRefreshLayout swipContainerMyItem;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.fab_item_cat)
    FloatingActionButton fabItemCat;
    @BindView(R.id.txt_name_item_item_cat)
    TextView txtNameItemItemCat;
    private ArrayList<ItemData> lstItemData = new ArrayList<>();
    private ItemsCatgAdapter itemsCatgAdapter;
    private OnEndLess onEndLess;
    private int maxPage;
    private int privious = 1;
    private int currentPageRefresh = 1;
    private int catgId;

    public MyItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_my_items, container, false );
        unbinder = ButterKnife.bind( this, view );
        swipContainerMyItem.setOnRefreshListener( this );

        Bundle bundle = getArguments();
        if (bundle != null) {
            catgId = bundle.getInt( Constant.ITEMS_BY_CATG );
            txtNameItemItemCat.setText( bundle.getString( Constant.ITEM_NAME_CATG ) );
            Log.d( TAG, "onCreateView: catgId =" + catgId );
        }
        if (getActivity() != null) {
            Album.initialize( AlbumConfig.newBuilder( getActivity() )
                    .setAlbumLoader( new MediaLoader() ).build() );
        }

        swipContainerMyItem.setColorSchemeResources( R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark );
        refreshLayout();

        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    private void refreshLayout() {
        swipContainerMyItem.post( new Runnable() {
            @Override
            public void run() {
                Log.d( TAG, "run: " );
                if (swipContainerMyItem != null) {
                    swipContainerMyItem.setRefreshing( true );
                }
                initRecycle();
                if (lstItemData.size() == 0) {
                    getItemsCatg( 1 );
                } else {
                    swipContainerMyItem.setRefreshing( false );
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
                Toast.makeText( getActivity(), String.valueOf( currentPage ), Toast.LENGTH_SHORT ).show();
                if (currentPage <= maxPage) {
                    if (maxPage != 0 && currentPage == 1) {
                        currentPageRefresh = currentPage;
                        privious = currentPage;
                        getItemsCatg( currentPage );
                    } else {
                        onEndLess.currentPage = privious;
                    }
                } else {
                    onEndLess.currentPage = privious;
                }
            }
        };
        recycleView.addOnScrollListener( onEndLess );
        itemsCatgAdapter = new ItemsCatgAdapter( getActivity(), getActivity(), lstItemData, this );
        recycleView.setAdapter( itemsCatgAdapter );
        itemsCatgAdapter.notifyDataSetChanged();
    }

    private void getItemsCatg(final int page) {

        swipContainerMyItem.setRefreshing( true );
        apiServices.getFoodsMadeByResturant( HelperMethodCustom
                .getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ), catgId )
                .enqueue( new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        try {
                            if (response.body().getStatus() == 1) {
                                if (page == 1) {
                                    lstItemData = new ArrayList<>();
                                    itemsCatgAdapter = new ItemsCatgAdapter( getActivity(), getActivity(),
                                            lstItemData, MyItemsFragment.this );
                                    privious = 1;
                                    onEndLess.currentPage = 1;
                                    onEndLess.preiviosTotal = 0;
                                }
                                maxPage = response.body().getData().getLastPage();
                                lstItemData.addAll( response.body().getData().getData() );
                                initRecycle();
                                itemsCatgAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                                Log.d( TAG, "onResponse: " + response.body().getMsg() );
                            }
                            swipContainerMyItem.setRefreshing( false );
                        } catch (Exception e) {
                            Log.d( TAG, "getItemsCatg: e " + e.getMessage() );
                        }
                    }

                    @Override
                    public void onFailure(Call<Item> call, Throwable t) {
                        try {

                            Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                            Log.d( TAG, "onFailure: " + t.getMessage() );
                        } catch (Exception e) {
                            Log.d( TAG, "onFailure: e " + e.getMessage() );
                        }
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
    public void onPositive(int position) {
        Fragment fragment = new UpdateItemCatgFragment();
        ItemData itemData = lstItemData.get( position );
        Bundle bundle = new Bundle();
        bundle.putSerializable( Constant.ITEM_FOOD_ID_REST, itemData );
        fragment.setArguments( bundle );
        HelperMethodCustom.ReplaceFragment( getFragmentManager(), fragment, R.id.fram_container_home,
                null, null );
    }


    @Override
    public void onNegtive(final int position) {
        ItemData itemData = lstItemData.get( position );
        apiServices.deleteItem( itemData.getId(),
                HelperMethodCustom.getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ) )
                .enqueue( new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        if (response.body().getStatus() == 1) {
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_LONG ).show();
                            itemsCatgAdapter.notifyItemRemoved( position );
                        } else {
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_LONG ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Item> call, Throwable t) {
                        Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_LONG ).show();
                    }
                } );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        if (itemsCatgAdapter != null) {
            itemsCatgAdapter.saveStates( outState );
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        if (itemsCatgAdapter != null) {
            itemsCatgAdapter.restoreStates( savedInstanceState );
        }
    }

    @OnClick(R.id.fab_item_cat)
    public void onViewClicked() {
        UpdateItemCatgFragment fragment = new UpdateItemCatgFragment();
        Bundle bundle = new Bundle();
        bundle.putInt( Constant.ITEM_FOOD_ID_REST, catgId );
        fragment.setArguments( bundle );
        HelperMethodCustom.ReplaceFragment( getFragmentManager(), fragment, R.id.fram_container_home,
                null, null );
    }

    @Override
    public void onRefresh() {
        getItemsCatg( currentPageRefresh );
    }
}
