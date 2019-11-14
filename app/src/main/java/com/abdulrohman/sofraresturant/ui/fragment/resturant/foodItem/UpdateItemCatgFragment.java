package com.abdulrohman.sofraresturant.ui.fragment.resturant.foodItem;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.abdulrohman.sofraresturant.data.model.item.Item;
import com.abdulrohman.sofraresturant.data.model.item.ItemData;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.helper.MediaLoader;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;
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

import static com.abdulrohman.sofraresturant.helper.HelperMethodCustom.convertFileToMultipart;
import static com.abdulrohman.sofraresturant.helper.HelperMethodCustom.convertToRequestBody;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateItemCatgFragment extends BaseFragment {
    private static final String TAG = UpdateItemCatgFragment.class.getSimpleName();
    @BindView(R.id.img_main_update_item)
    ImageView imgMainUpdateItem;
    @BindView(R.id.edt_name_update_item)
    EditText edtNameUpdateItem;
    @BindView(R.id.edt_desc_update_item)
    EditText edtDescUpdateItem;
    @BindView(R.id.edt_price_update_item)
    EditText edtPriceUpdateItem;
    @BindView(R.id.edt_offer_price_update_item)
    EditText edtOfferPriceUpdateItem;
    @BindView(R.id.btn_send_update_item)
    Button btnSendUpdateItem;
    Unbinder unbinder;
    @BindView(R.id.edt_priod_delvary_update_item)
    EditText edtPriodDelvaryUpdateItem;
    @BindView(R.id.txt_caption_img_update)
    TextView txtCaptionImgUpdate;
    String strNameOfProduct;
    String strDescription;
    String strOfferPrice;
    String strPrice;
    String strPeriod;
    //var
    private ItemData itemData;
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();
    private String strPathImg = "";
    private Bundle bundle;
    private boolean upDateImgMode = false;
    private int catgId;

    public UpdateItemCatgFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_update_item_catg, container, false );
        unbinder = ButterKnife.bind( this, view );

        if (getActivity() != null) {
            Album.initialize( AlbumConfig.newBuilder( getContext() )
                    .setAlbumLoader( new MediaLoader() ).build() );
        }

        try {

            bundle = getArguments();
            if (bundle != null) {
                catgId = bundle.getInt( Constant.ITEM_FOOD_ID_REST );
                itemData = (ItemData) bundle.getSerializable( Constant.ITEM_FOOD_ID_REST );
                txtCaptionImgUpdate.setText( getString( R.string.image_of_product ) );
                initWedgit();
            }
        } catch (Exception e) {
            Log.d( TAG, "onCreateView: " + e.getMessage() );
        }

        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    private void initWedgit() {
        HelperMethodCustom.onLoadImageFromUrl( imgMainUpdateItem, itemData.getPhotoUrl(), getActivity(), 1 );
        edtPriceUpdateItem.setText( itemData.getPrice() );
//        edtOfferPriceUpdateItem.setText( itemData.getOfferPrice().toString() );
        edtNameUpdateItem.setText( itemData.getName() );
        edtDescUpdateItem.setText( itemData.getDescription() );
        edtPriodDelvaryUpdateItem.setText( itemData.getPreparingTime() );
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @Override
    public void onDestroyView() {
        Log.d( TAG, "onDestroyView: strPathImg " + strPathImg );
        super.onDestroyView();
        unbinder.unbind();
        strPathImg = "";
        bundle = null;
        Log.d( TAG, "onDestroyView: strPathImg " + strPathImg );

    }

    @OnClick({R.id.img_main_update_item, R.id.btn_send_update_item})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_main_update_item:
                HelperMethodCustom.setImageProfile( getActivity(), this, mAlbumFiles,
                        imgMainUpdateItem );
                upDateImgMode = true;
                break;
            case R.id.btn_send_update_item:
                if (bundle != null) {
                    Log.d( TAG, "onViewClicked: updateItemFood " );
                    updateItemFood( String.valueOf( itemData.getId() ) );
                } else {
                    Log.d( TAG, "onViewClicked: createItemFood" );
                    createItemFood( String.valueOf( catgId ) );//todo get catgId from previous screen
                }
                break;
        }

    }

    private void updateItemFood(String itemId) {
        assignWedghit();
        onCall( apiServices.updateItem( convertToRequestBody( strDescription ),
                convertToRequestBody( strPrice ),
                convertToRequestBody( strPeriod ),
                convertToRequestBody( strNameOfProduct ),
                convertFileToMultipart( strPathImg, "photo" ),
                convertToRequestBody( itemId ),
                convertToRequestBody( HelperMethodCustom
                        .getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ) ),
                convertToRequestBody( strOfferPrice ) ) );
    }

    private void createItemFood(String catg) {
        assignWedghit();
        onCall( apiServices.createItem( convertToRequestBody( strDescription ),
                convertToRequestBody( strPrice ),
                convertToRequestBody( strPeriod ),
                convertToRequestBody( strNameOfProduct ),
                convertFileToMultipart( strPathImg, "photo" ),
                convertToRequestBody( catg ),
                convertToRequestBody( HelperMethodCustom
                        .getApiToken( getActivity(), Constant.RESTAURANT_API_TOKEN ) ),
                convertToRequestBody( strOfferPrice ) ) );
    }

    private void assignWedghit() {
        strNameOfProduct = edtNameUpdateItem.getText().toString();
        strDescription = edtDescUpdateItem.getText().toString();
        strOfferPrice = edtOfferPriceUpdateItem.getText().toString();
        strPrice = edtPriceUpdateItem.getText().toString();
        strPeriod = edtPriodDelvaryUpdateItem.getText().toString();

        if (!upDateImgMode) {//if you don't upate image
            Log.d( TAG, "updateItemFood: strPathImg " + strPathImg );
            strPathImg = itemData.getPhotoUrl();
            Log.d( TAG, "updateItemFood: strPathImg " + strPathImg );
        } else {
            strPathImg = mAlbumFiles.get( 0 ).getPath();
        }
//        HelperMethodCustom.onLoadImageFromUrl( imgMainUpdateItem, strPathImg, getActivity(), 1 );
    }

    private void onCall(Call<Item> itemCall) {

        itemCall.enqueue( new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                try {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            //todo delay
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                            onBack();
                        } else {
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d( TAG, "onCall: e " + e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                try {
                    Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                } catch (Exception e) {
                    Log.d( TAG, "onCall: e " + e.getMessage() );
                }
            }
        } );

    }
}
