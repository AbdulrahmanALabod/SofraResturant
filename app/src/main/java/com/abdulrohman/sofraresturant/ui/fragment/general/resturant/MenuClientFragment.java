package com.abdulrohman.sofraresturant.ui.fragment.general.resturant;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.adapter.CatgHorzAdapter;
import com.abdulrohman.sofraresturant.adapter.FoodsResturantAdapter;
import com.abdulrohman.sofraresturant.data.model.category.Categories;
import com.abdulrohman.sofraresturant.data.model.category.CategoryData;
import com.abdulrohman.sofraresturant.data.model.item.Item;
import com.abdulrohman.sofraresturant.data.model.item.ItemData;
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
public class MenuClientFragment extends BaseFragment implements BaseActivity.OnPositionItem {
    private static final String TAG = MenuClientFragment.class.getSimpleName();

    //var
    @BindView(R.id.recycl_catg_horz)
    RecyclerView recyclCatgHorz;
    @BindView(R.id.recycl_items_menu)
    RecyclerView recyclItemsMenu;
    Unbinder unbinder;
    @BindView(R.id.txt_hint_menu)
    TextView txtHintMenu;

    private OnEndLess onEndLess;
    private FoodsResturantAdapter foodsResturantAdapter;
    private List<ItemData> lstItemFood = new ArrayList<>();
    private List<CategoryData> lstCatgHorz = new ArrayList<>();
    private int maxPage;
    private int previousPage = 1;
    private int resturantId;
    private LinearLayoutManager linearLayoutManager;
    private CatgHorzAdapter catgHorzAdapter;
    private CategoryData catg;
    private LinearLayoutManager linearLayoutManagerHorz;

    public MenuClientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_menu_client, container, false );
        unbinder = ButterKnife.bind( this, view );
        txtHintMenu.setVisibility( View.VISIBLE );
        linearLayoutManager = new LinearLayoutManager( getActivity(), LinearLayoutManager.VERTICAL, false );
        linearLayoutManagerHorz = new LinearLayoutManager( getActivity(), LinearLayoutManager.HORIZONTAL, false );
        if (recyclCatgHorz != null) {
            recyclCatgHorz.setLayoutManager( linearLayoutManagerHorz );
            recyclItemsMenu.setLayoutManager( linearLayoutManager );
        }


        resturantId = HelperMethodCustom.getResturanId( getActivity() );


        initRecycleHorz();
        if (lstCatgHorz.size() == 0) {
            getCatgHorz();
        }

        return view;
    }

    private void initRecycleHorz() {
        catgHorzAdapter = new CatgHorzAdapter( getActivity(),
                lstCatgHorz,
                MenuClientFragment.this );
        recyclCatgHorz.setAdapter( catgHorzAdapter );
        catgHorzAdapter.notifyDataSetChanged();
    }

    private void getCatgHorz() {

        apiServices.getMyCategoriesHorz( String.valueOf( resturantId ) ).enqueue( new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        lstCatgHorz.addAll( response.body().getData() );
                        initRecycleHorz();
                    } else {
                        Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                        Log.d( TAG, "onResponse: " + response.body().getMsg() );
                    }
                } catch (Exception e) {
                    Log.d( TAG, "onCreateView: e " + e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                try {

                    Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                    Log.d( TAG, "onFailure: " + t.getMessage() );
                } catch (Exception e) {
                    Log.d( TAG, "onCreateView: e " + e.getMessage() );
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
    public void onSucssess(int position) {
        Log.d( TAG, "onSucssess: position " + position );
        txtHintMenu.setVisibility( View.GONE );
        catg = lstCatgHorz.get( position );
        initRecycle();
        getItemsFood( 1, catg.getId() );
    }

    private void initRecycle() {
        if (getActivity() != null) {
            foodsResturantAdapter = new FoodsResturantAdapter( getActivity(), lstItemFood, resturantId );


            onEndLess = new OnEndLess( 1, linearLayoutManager ) {
                @Override
                public void onLoadMore(int page) {

                    Toast.makeText( getActivity(), String.valueOf( currentPage ), Toast.LENGTH_SHORT ).show();
                    if (currentPage <= maxPage) {
                        if (maxPage != 0 && currentPage != 1) {
                            previousPage = currentPage;
                            getItemsFood( currentPage, catg.getId() );
                        } else {
                            onEndLess.currentPage = previousPage;
                        }
                    } else {
                        onEndLess.currentPage = previousPage;
                    }
                }
            };

            recyclItemsMenu.addOnScrollListener( onEndLess );
            recyclItemsMenu.setAdapter( foodsResturantAdapter );
            foodsResturantAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );

    }

    private void getItemsFood(final int page, int catgId) {

        apiServices.getItemFood( resturantId, catgId ).enqueue( new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        if (page == 1) {
                            lstItemFood = new ArrayList<>();
                            foodsResturantAdapter = new FoodsResturantAdapter( getActivity(), lstItemFood, resturantId );
                            previousPage = 1;
                            onEndLess.currentPage = 1;
                            onEndLess.preiviosTotal = 0;
                        }
                        maxPage = response.body().getData().getLastPage();
                        lstItemFood.addAll( response.body().getData().getData() );
                        if (lstItemFood.size() == 0) {
                            txtHintMenu.setVisibility( View.VISIBLE );
                            txtHintMenu.setText( getString( R.string.there_are_not_item_to_show ) );
                        }
                        initRecycle();
                    } else {
                        Log.d( TAG, "onResponse: " + response.body().getMsg() );
                    }
                } catch (NullPointerException e) {
                    Log.d( TAG, "onResponse: e.getMessage() " + e.getMessage() );
                    Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_SHORT ).show();
                } catch (IllegalThreadStateException e) {
                    Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_SHORT ).show();
                } catch (Exception e) {
                    Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_SHORT ).show();
                    Log.d( TAG, "getItemsFood: " + e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                try {
                    Log.d( TAG, "onFailure: " + t.getMessage() );
                } catch (Exception e) {
                    Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_SHORT ).show();
                    Log.d( TAG, "getItemsFood: " + e.getMessage() );
                }
            }
        } );

    }
}
