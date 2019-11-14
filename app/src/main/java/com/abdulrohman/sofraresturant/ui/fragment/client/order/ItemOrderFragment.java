package com.abdulrohman.sofraresturant.ui.fragment.client.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.local.room.AppDatabase;
import com.abdulrohman.sofraresturant.data.local.room.ItemDao;
import com.abdulrohman.sofraresturant.data.model.item.ItemData;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemOrderFragment extends BaseFragment {
    private static final String TAG = ItemOrderFragment.class.getSimpleName();
    @BindView(R.id.img_food_recycl_food)
    ImageView imgFoodRecyclFood;
    @BindView(R.id.txt_name_recycl_food)
    TextView txtNameRecyclFood;
    @BindView(R.id.txt_desc_recycl_food)
    TextView txtDescRecyclFood;
    @BindView(R.id.txt_price_recycl_food)
    TextView txtPriceRecyclFood;
    @BindView(R.id.txt_time_item_order)
    TextView txtTimeItemOrder;
    @BindView(R.id.txt_special_item_order)
    TextView txtSpecialItemOrder;
    @BindView(R.id.edt_notes_item_order)
    EditText edtNotesItemOrder;
    @BindView(R.id.txt_plus_item_order)
    TextView txtPlusItemOrder;
    @BindView(R.id.txt_counter_item_order)
    TextView txtCounterItemOrder;
    @BindView(R.id.txt_mains_item_order)
    TextView txtMainsItemOrder;
    @BindView(R.id.img_cart_item_order)
    ImageView imgCartItemOrder;


    //var
    private int count = 1;
    private Unbinder unbinder;
    private ItemData item;
    private AppDatabase appDatabase;

    public ItemOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_item_order, container, false );
        unbinder = ButterKnife.bind( this, view );
        //init room
        appDatabase = AppDatabase.getInstanceRoom( getActivity() );
        txtCounterItemOrder.setText( String.valueOf( count ) );//init count text view

        Bundle args = getArguments();
        if (args != null) {
            item = (ItemData) args.getSerializable( Constant.ITEM_FOOD_ID );
            txtNameRecyclFood.setText( item.getName() );
            txtPriceRecyclFood.setText( item.getPrice() );
            HelperMethodCustom.onLoadImageFromUrl( imgFoodRecyclFood, item.getPhotoUrl(), getActivity(), 1 );
            txtDescRecyclFood.setText( item.getDescription() );
        }

        Log.d( TAG, "onCreateView: end" );
        return view;
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

    @OnClick({R.id.txt_plus_item_order, R.id.txt_mains_item_order, R.id.img_cart_item_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_plus_item_order:
                count = count + 1;
                setQouantities( count );
                break;
            case R.id.txt_mains_item_order:
                if (count > 1) {
                    count = count - 1;
                    setQouantities( count );
                }
                break;
            case R.id.img_cart_item_order:
                addOrder();
                break;
        }
    }


    private void setQouantities(int count) {
        txtCounterItemOrder.setText( String.valueOf( count ) );
    }

    private void addOrder() {
//       ItemData itemData = new ItemData();
//        itemData.setName( item.getName() );
//        itemData.setDescription( item.getDescription() );
//        itemData.setPrice( item.getPrice() );
//        itemData.setPhotoUrl( item.getPhotoUrl() );
        item.setQuantity( String.valueOf( count ) );
        item.setNote( edtNotesItemOrder.getText().toString() );
        ItemDao itemDao = appDatabase.itemDao();
        itemDao.insert( item );
        FinalItemsOrderFragment fragment = new FinalItemsOrderFragment();
        HelperMethodCustom.ReplaceFragment( getFragmentManager(), fragment, R.id.fram_container_home,
                null, null );
    }

}
