package com.abdulrohman.sofraresturant.ui.fragment.client.order;


import android.content.Intent;
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
import com.abdulrohman.sofraresturant.adapter.FinalOrderAdapter;
import com.abdulrohman.sofraresturant.data.local.room.AppDatabase;
import com.abdulrohman.sofraresturant.data.model.item.ItemData;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.activity.BaseActivity;
import com.abdulrohman.sofraresturant.ui.activity.HomeCycleActivity;
import com.abdulrohman.sofraresturant.ui.activity.UserCycleActivity;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;
import com.abdulrohman.sofraresturant.ui.fragment.general.resturant.MenuClientFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinalItemsOrderFragment extends BaseFragment implements BaseActivity.OnPositionItem {
    private static final String TAG = FinalItemsOrderFragment.class.getSimpleName();
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.txt_all_price_final_order)
    TextView txtAllPriceFinalOrder;
    @BindView(R.id.btn_confirm_final_order)
    Button btnConfirmFinalOrder;
    @BindView(R.id.btn_add_more_final_order)
    Button btnAddMoreFinalOrder;
    //vars
    Unbinder unbinder;
    private AppDatabase apppDatabase;
    private List<ItemData> lstItemData = new ArrayList<>();
    private FinalOrderAdapter finalOrderAdapter;

    public FinalItemsOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_final_item_order, container, false );
        unbinder = ButterKnife.bind( this, view );
        // initRoom
        apppDatabase = AppDatabase.getInstanceRoom( getActivity() );
        lstItemData.addAll( apppDatabase.itemDao().getAllOrder() );
        initRecycle();

        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    private void initRecycle() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity() );
        recycleView.setLayoutManager( linearLayoutManager );
        finalOrderAdapter = new FinalOrderAdapter( getActivity(), lstItemData, this,
                txtAllPriceFinalOrder );
        recycleView.setAdapter( finalOrderAdapter );
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

    @OnClick({R.id.btn_confirm_final_order, R.id.btn_add_more_final_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm_final_order:
                if (HelperMethodCustom.getApiToken( getActivity(), Constant.CLIENT_API_TOKEN ) != null) {
                    if(lstItemData.size()!=0){
                        Fragment fragment = new CheckFinalFragment();
                        updateIncDec();
                        HelperMethodCustom.ReplaceFragment( getFragmentManager(),
                                fragment, R.id.fram_container_home, null, null );
                    }else {
                        Toast.makeText( getActivity(), getString( R.string.there_arent_food), Toast.LENGTH_SHORT ).show();
                    }

                } else {
                    Intent intent = new Intent( (HomeCycleActivity) getActivity(), UserCycleActivity.class );
                    startActivity( intent );
                }
                HelperMethodCustom.createShared( getActivity(), Constant.ALL_PRAICE,
                        txtAllPriceFinalOrder.getText().toString() );
                break;
            case R.id.btn_add_more_final_order:
                Fragment fragment = new MenuClientFragment();
                updateIncDec();
                HelperMethodCustom.ReplaceFragment( getFragmentManager(),
                        fragment, R.id.fram_container_home, null, null );
                break;
        }
    }

    @Override
    public void onSucssess(int position) {
        ItemData itemData = lstItemData.get( position );
        apppDatabase.itemDao().delete( itemData );
        lstItemData.remove( position );
        finalOrderAdapter.notifyItemRemoved( position );
        Toast.makeText( getActivity(), "done", Toast.LENGTH_SHORT ).show();
    }

    private void updateIncDec() {
        apppDatabase.itemDao().updateCount( lstItemData );
    }
}
