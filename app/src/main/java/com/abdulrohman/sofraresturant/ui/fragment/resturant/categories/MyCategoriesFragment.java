package com.abdulrohman.sofraresturant.ui.fragment.resturant.categories;


import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.adapter.CatagoriesAdapter;
import com.abdulrohman.sofraresturant.data.model.resturant.ResturantData;
import com.abdulrohman.sofraresturant.data.model.resturant.Resturants;
import com.abdulrohman.sofraresturant.helper.AppDialog;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethod;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.helper.MediaLoader;
import com.abdulrohman.sofraresturant.helper.OnEndLess;
import com.abdulrohman.sofraresturant.ui.activity.BaseActivity;
import com.abdulrohman.sofraresturant.ui.activity.HomeCycleActivity;
import com.abdulrohman.sofraresturant.ui.activity.SpalchCycleActivity;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;
import com.abdulrohman.sofraresturant.ui.fragment.resturant.foodItem.MyItemsFragment;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.abdulrohman.sofraresturant.helper.HelperMethod.convertToRequestBody;
import static com.abdulrohman.sofraresturant.helper.HelperMethodCustom.convertFileToMultipart;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCategoriesFragment extends BaseFragment implements BaseActivity.OnPressButton
        , SwipeRefreshLayout.OnRefreshListener, BaseActivity.OnPositionItem {
    private static final String TAG = MyCategoriesFragment.class.getSimpleName();
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.fab_mycatgory)
    FloatingActionButton fabMycatgory;
    Unbinder unbinder;
    @BindView(R.id.expirment)
    TextView expirment;
    @BindView(R.id.swipe_container_my_catg)
    SwipeRefreshLayout swipeContainerMyCatg;
    //VAR
    private CatagoriesAdapter catagoriesAdapter;
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();
    private Dialog dialog;
    private String strPathImg = "";
    private boolean updateMode = false;
    private ArrayList<ResturantData> lstCatagories = new ArrayList<>();
    private OnEndLess onEndLess;
    private int maxPage;
    private int privious = 1;
    private int currentPageRegresh = 1;
    private LinearLayoutManager linearLayoutManager;
    private boolean updateImgMode = false;

    public MyCategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_my_categories, container, false );
        unbinder = ButterKnife.bind( this, view );

        swipeContainerMyCatg.setOnRefreshListener( this );
        swipeContainerMyCatg.setColorSchemeResources( R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark );

        if (getActivity() != null) {
            Album.initialize( AlbumConfig.newBuilder( getActivity() )
                    .setAlbumLoader( new MediaLoader() ).build() );
        }

        linearLayoutManager = new LinearLayoutManager( getActivity(),
                LinearLayoutManager.VERTICAL,
                false );
        recycleView.setLayoutManager( linearLayoutManager );

        refrech();//swiper layout refresh

        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    private void refrech() {
        swipeContainerMyCatg.post( new Runnable() {
            @Override
            public void run() {
                swipeContainerMyCatg.setRefreshing( true );

                initRecycle();
                if (lstCatagories.size() == 0) {
                    if (getActivity() != null)
                        getItemsCatg( 1 );
                } else {
                    // you should set false because setRefreshing still on when back from another fragment
                    swipeContainerMyCatg.setRefreshing( false );
                }
            }
        } );

    }

    private void initRecycle() {
        try {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity()
                    , LinearLayoutManager.VERTICAL, false );
            recycleView.setLayoutManager( linearLayoutManager );

            onEndLess = new OnEndLess( 1, linearLayoutManager ) {
                @Override
                public void onLoadMore(int page) {

                    if (currentPage <= maxPage) {
                        if (maxPage != 0 && currentPage != 1) {
                            privious = currentPage;
                            currentPageRegresh = currentPage;
                            if (getActivity() != null) {
                                getItemsCatg( currentPage );
                            }
                        } else {
                            onEndLess.currentPage = privious;
                        }
                    } else {
                        onEndLess.currentPage = privious;
                    }
                }
            };
        } catch (Exception e) {
            Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_SHORT ).show();
        }

        recycleView.addOnScrollListener( onEndLess );
        catagoriesAdapter = new CatagoriesAdapter( getActivity(), lstCatagories,
                this, this );
        recycleView.setAdapter( catagoriesAdapter );
        catagoriesAdapter.notifyDataSetChanged();
    }

    private void getItemsCatg(final int page) {
        // Showing refresh animation before making http call
        swipeContainerMyCatg.setRefreshing( true );
        apiServices.getMyCategories( HelperMethodCustom.getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ) )
                .enqueue( new Callback<Resturants>() {
                    @Override
                    public void onResponse(Call<Resturants> call, Response<Resturants> response) {
                        if (response.body().getStatus() == 1) {
                            if (page == 1) {
                                lstCatagories = new ArrayList<>();
                                catagoriesAdapter = new CatagoriesAdapter( getActivity(),
                                        lstCatagories, MyCategoriesFragment.this,
                                        MyCategoriesFragment.this );
                                privious = 1;
                                onEndLess.currentPage = 1;
                                onEndLess.preiviosTotal = 0;
                            }
                            maxPage = response.body().getData().getLastPage();
                            lstCatagories.addAll( response.body().getData().getData() );
                            initRecycle();
                            catagoriesAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                            Log.d( TAG, "onResponse: " + response.body().getMsg() );
                        }
                        // Stopping swipe refresh
                        swipeContainerMyCatg.setRefreshing( false );
                    }

                    @Override
                    public void onFailure(Call<Resturants> call, Throwable t) {
                        Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                        Log.d( TAG, "onFailure: " + t.getMessage() );
                        // Stopping swipe refresh
                        swipeContainerMyCatg.setRefreshing( false );
                    }
                } );
    }

    @Override
    public void onPositive(int position) {
        updateMode = true;
        ResturantData dataPos = lstCatagories.get( position );
        initDilag( true, dataPos );

    }

    @Override
    public void onNegtive(final int position) {
        ResturantData dataNag = lstCatagories.get( position );
        apiServices.deleteCatg( dataNag.getId(),
                HelperMethodCustom.getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ) )
                .enqueue( new Callback<Resturants>() {
                    @Override
                    public void onResponse(Call<Resturants> call, Response<Resturants> response) {
                        if (response.body().getStatus() == 1) {
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_LONG ).show();
                            catagoriesAdapter.notifyItemRemoved( position );
                        } else {
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_LONG ).show();
                            Log.d( TAG, "onResponse: getStatus() == 0 " + response.body().getMsg() );
                        }
                    }

                    @Override
                    public void onFailure(Call<Resturants> call, Throwable t) {
                        Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_LONG ).show();
                        Log.d( TAG, "onFailure: " + t.getMessage() );
                    }
                } );
    }

    @OnClick({R.id.expirment, R.id.fab_mycatgory})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_mycatgory:
                updateMode = false;
                initDilag( false, null );
                break;
        }
    }

    private void initDilag(boolean setImage, final ResturantData catg) {

        AppDialog appDialog = new AppDialog();
        dialog = appDialog.showDialog( getActivity(), R.layout.dialog_add_categry );
        final EditText edtNameCatgry = dialog.findViewById( R.id.edt_name_catgry_dialog_catgry );
        Button btnAddCatgry = dialog.findViewById( R.id.btn_add_catgry_dialog_catgry );
        final ImageView imgAddImgDialog = dialog.findViewById( R.id.img_add_img_dialog_catgry );
        dialog.show();

        if (setImage) {//if you want  update
            //todo you should get strPathImg value from server before do this step
            strPathImg = catg.getPhotoUrl();
            Log.d( TAG, "initDilag: befor upload " + strPathImg );
            if (!strPathImg.isEmpty()) {
                HelperMethodCustom.onLoadImageFromUrl( imgAddImgDialog,
                        strPathImg,
                        getActivity(),
                        1 );
                edtNameCatgry.setText( catg.getName() );
            }
        }
        imgAddImgDialog.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperMethodCustom.setImageProfile( getActivity(), MyCategoriesFragment.this,
                        mAlbumFiles, imgAddImgDialog );
                updateImgMode = true;
            }
        } );


        btnAddCatgry.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameCate = edtNameCatgry.getText().toString();
                if (updateImgMode) {
                    strPathImg = mAlbumFiles.get( 0 ).getPath();
                }
                if (strPathImg != null) {
                    if (!nameCate.isEmpty()) {
                        if (updateMode) {
                            updateteCategory( nameCate, strPathImg, catg );
                        } else {
                            createCategory( nameCate, strPathImg );
                        }
                    } else {
                        Toast.makeText( getActivity(), R.string.missing_data, Toast.LENGTH_SHORT ).show();
                    }
                } else {
                    Toast.makeText( getActivity(), R.string.please_upload_image, Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }

    private void updateteCategory(String nameCate, String strPathImg, ResturantData dataup) {
        onCall( apiServices.updateCatg( convertToRequestBody( nameCate ),
                convertFileToMultipart( strPathImg, "photo" ),
                convertToRequestBody( String.valueOf( dataup.getId() ) ),
                convertToRequestBody( HelperMethodCustom.getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ) ) ),
                dataup.getId() );

    }

    private void createCategory(String nameCate, String strPathImg) {
        onCall( apiServices.createCategry( convertToRequestBody( nameCate ),
                convertFileToMultipart( strPathImg, "photo" ),
                convertToRequestBody( HelperMethodCustom
                        .getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ) ) ), 0 );
    }

    private void onCall(Call<Resturants> resturantsCall, final int catgId) {
        resturantsCall.enqueue( new Callback<Resturants>() {
            @Override
            public void onResponse(Call<Resturants> call, Response<Resturants> response) {
                if (response.body().getStatus() == 1) {
                    Log.d( TAG, "onResponse: response " + response.body().getMsg() );
                    if (updateMode) {
                        lstCatagories.set( catgId, response.body().getData().getData().get( catgId ) );
                        catagoriesAdapter.notifyItemChanged( catgId );
                    } else {
                        getItemsCatg( 1 );
                    }
                } else {
                    Log.d( TAG, "onResponse: getStatus() == 0 " + response.body().getMsg() );
                    Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                }
                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<Resturants> call, Throwable t) {
                Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                dialog.dismiss();
            }
        } );
    }

    @Override
    public void onBack() {
        if (HelperMethodCustom.getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ) != null) {
            startActivity( new Intent( (HomeCycleActivity) getActivity(), SpalchCycleActivity.class ) );
        }
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        strPathImg = "";
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        if (catagoriesAdapter != null) {
            catagoriesAdapter.saveStates( outState );
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        if (catagoriesAdapter != null) {
            catagoriesAdapter.restoreStates( savedInstanceState );
        }
    }

    @Override
    public void onRefresh() {
        getItemsCatg( currentPageRegresh );
    }

    @Override
    public void onSucssess(int position) {
        ResturantData dataSuccss = lstCatagories.get( position );
        MyItemsFragment fragment = new MyItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt( Constant.ITEMS_BY_CATG, dataSuccss.getId() );
        bundle.putString( Constant.ITEM_NAME_CATG, dataSuccss.getName() );
        fragment.setArguments( bundle );
        HelperMethod.ReplaceFragment( getFragmentManager(), fragment, R.id.fram_container_home,
                null, null );
    }
}
