package com.abdulrohman.sofraresturant.ui.fragment.general;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.adapter.ResturantsAdapter;
import com.abdulrohman.sofraresturant.data.model.region.Region;
import com.abdulrohman.sofraresturant.data.model.resturant.ResturantData;
import com.abdulrohman.sofraresturant.data.model.resturant.Resturants;
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
public class SearchClientFragment extends BaseFragment implements BaseActivity.OnPositionItem {
    private static final String TAG = "SearchClientFragment";

    @BindView(R.id.edt_search_rest_client)
    EditText edtSearchRestClient;
    @BindView(R.id.spin_city_search_rest_client)
    Spinner spinCitySearchRestClient;
    @BindView(R.id.recycl_search_rest_client)
    RecyclerView recyclSearchRestClient;
    @BindView(R.id.img_search_search_rest_client)
    ImageView imgSearchSearchRestClient;
    OnEndLess onEndLess;
    @BindView(R.id.progress_bar_search)
    ProgressBar progressBarSearch;

    //var
    private ResturantsAdapter resturantsAdapter;
    private Unbinder unbinder;
    private ArrayAdapter<String> cityAdapter;
    private List<Integer> ids = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private int maxPage;
    private int previousPage = 1;
    private boolean Filter = false;
    private List<ResturantData> resturantDataList = new ArrayList<>();

    public SearchClientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_search_cliet, container, false );
        unbinder = ButterKnife.bind( this, view );

        recyclSearchRestClient.setVisibility( View.INVISIBLE );
        progressBarSearch.setVisibility( View.VISIBLE );

        edtSearchRestClient.setTextSize( HelperMethodCustom.reSizeFont( getResources(), 24,
                18, 12 ) );

        if (ids.size() == 0) {
            getCity();
        } else {
            setAdapteCity( cityAdapter );
        }


        initRecycle();
        if (resturantDataList.size() == 0) {
            getAllResturant( 1 );
        } else {
            recyclSearchRestClient.setVisibility( View.VISIBLE );
            progressBarSearch.setVisibility( View.GONE );
        }

        return view;
    }

    private void getCity() {

        apiServices.getCities().enqueue( new Callback<Region>() {
            @Override
            public void onResponse(Call<Region> call, Response<Region> response) {
                try {
                    if (getActivity() != null) {
                        if (response.body().getStatus() == 1) {

                            ids.add( 0 );
                            if (getActivity() != null) {
                                names.add( getString( R.string.chose_city ) );
                            }
                            for (int i = 0; i < response.body().getData().getData().size(); i++) {
                                names.add( response.body().getData().getData().get( i ).getName() );
                                ids.add( i );
                            }
                            cityAdapter = new ArrayAdapter<String>( getActivity(), R.layout.item_spin_city, names );
                            setAdapteCity( cityAdapter );

                        } else {
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d( TAG, "getCity: e " + e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<Region> call, Throwable t) {
                Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );

    }

    private void setAdapteCity(ArrayAdapter<String> arrayAdapter) {
        spinCitySearchRestClient.setAdapter( arrayAdapter );
    }

    private void getAllResturant(final int page) {
        onCall( apiServices.getAllResturans( page ), page );
    }

    private void initRecycle() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity() );
        recyclSearchRestClient.setLayoutManager( linearLayoutManager );
        onEndLess = new OnEndLess( 1, linearLayoutManager ) {
            @Override
            public void onLoadMore(int page) {
                Toast.makeText( getActivity(), String.valueOf( currentPage ), Toast.LENGTH_SHORT ).show();
                if (currentPage <= maxPage) {
                    if (maxPage != 0 && currentPage != 1) {
                        previousPage = currentPage;
                        if (Filter) {
                            filterResturant( currentPage );
                        } else {
                            getAllResturant( currentPage );
                        }
                    } else {
                        onEndLess.currentPage = previousPage;
                    }
                } else {
                    onEndLess.currentPage = previousPage;
                }
            }
        };
        recyclSearchRestClient.addOnScrollListener( onEndLess );

        resturantsAdapter = new ResturantsAdapter( getActivity(), resturantDataList, this,
                getFragmentManager() );
        recyclSearchRestClient.setAdapter( resturantsAdapter );
        resturantsAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.img_search_search_rest_client)
    public void onViewClicked() {
        HelperMethodCustom.disappearKeypad( getActivity(), imgSearchSearchRestClient );
        if (Filter) {
            Filter = false;
            getAllResturant( 1 );
        } else {
            Filter = true;
            filterResturant( 1 );
        }
    }

    private void filterResturant(int page) {
        onCall( apiServices.getFilterResturans( edtSearchRestClient.getText().toString(),
                String.valueOf( spinCitySearchRestClient.getSelectedItemPosition() ), 1 ), page );
    }

    private void onCall(Call<Resturants> allResturans, final int page) {
        allResturans.enqueue( new Callback<Resturants>() {
            @Override
            public void onResponse(Call<Resturants> call, Response<Resturants> response) {
                try {
                    if (response.body().getStatus() == 1) {

                        if (page == 1) {
                            resturantDataList = new ArrayList<>();
                            resturantsAdapter = new ResturantsAdapter( getActivity(), resturantDataList,
                                    SearchClientFragment.this, getFragmentManager() );
                            previousPage = 1;
                            onEndLess.currentPage = 1;
                            onEndLess.preiviosTotal = 0;
                        }

                        recyclSearchRestClient.setVisibility( View.VISIBLE );
                        progressBarSearch.setVisibility( View.GONE );

                        maxPage = response.body().getData().getLastPage();
                        resturantDataList.addAll( response.body().getData().getData() );
                        initRecycle();
                        resturantsAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                        Log.d( TAG, "onResponse: " + response.body().getMsg() );
                    }
                } catch (Exception e) {
                    Log.d( TAG, "filterResturant: e " + e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<Resturants> call, Throwable t) {
                try {
                    Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                    Log.d( TAG, "onFailure: " + t.getMessage() );
                } catch (Exception e) {
                    Log.d( TAG, "filterResturant: e " + e.getMessage() );
                }
            }
        } );
    }

    @Override
    public void onSucssess(int position) {
    }

    @Override
    public void onBack() {
        getActivity().finish();
    }
}
