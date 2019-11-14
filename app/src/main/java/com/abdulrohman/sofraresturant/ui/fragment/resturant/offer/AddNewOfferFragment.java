package com.abdulrohman.sofraresturant.ui.fragment.resturant.offer;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.item.Item;
import com.abdulrohman.sofraresturant.data.model.item.ItemData;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.data.model.DateModel;
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
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.abdulrohman.sofraresturant.helper.HelperMethodCustom.convertFileToMultipart;
import static com.abdulrohman.sofraresturant.helper.HelperMethodCustom.convertToRequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewOfferFragment extends BaseFragment {
    private static final String TAG = AddNewOfferFragment.class.getSimpleName();
    @BindView(R.id.txt_titel_ofr_rst)
    TextView txtTitelOfrRst;
    @BindView(R.id.img_offer_ofr_rst)
    CircleImageView imgOfferOfrRst;
    @BindView(R.id.edt_name_of_offer_ofr_rst)
    EditText edtNameOfOfferOfrRst;
    @BindView(R.id.edt_desc_ofr_rst)
    EditText edtDescOfrRst;
    @BindView(R.id.txt_from_date_ofr_rst)
    TextView txtFromDateOfrRst;
    @BindView(R.id.txt_to_date_ofr_rst)
    TextView txtToDateOfrRst;
    @BindView(R.id.btn_ofr_rst)
    Button btnOfrRst;
    Unbinder unbinder;
    ItemData offerData;
    private boolean updateMode = false;
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();
    private String strPathImage = "";
    private String strToDate;
    private String strFromDate;
    private String strName;
    private String strDesc;
    private DateModel dateModel;

    public AddNewOfferFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_add_new_offer, container, false );
        unbinder = ButterKnife.bind( this, view );

        Bundle bundle = getArguments();
        if (bundle != null) {
            updateMode = true;
            offerData = (ItemData) bundle.getSerializable( Constant.OFFER_REST_ID );
            intiWedgit( offerData );
        }

        if (getActivity() != null) {
            Album.initialize( AlbumConfig.newBuilder( getActivity() )
                    .setAlbumLoader( new MediaLoader() ).build() );
        }

        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    private void intiWedgit(ItemData offerData) {
        txtFromDateOfrRst.setText( offerData.getStartingAt() );
        txtToDateOfrRst.setText( offerData.getEndingAt() );
        txtTitelOfrRst.setText( getString( R.string.Image_for_show_offer_rest ) );
        edtDescOfrRst.setText( offerData.getDescription() );
        edtNameOfOfferOfrRst.setText( offerData.getName() );
        HelperMethodCustom.onLoadImageFromUrl( imgOfferOfrRst,offerData.getPhotoUrl(),getActivity(),1 );
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

    @OnClick({R.id.txt_from_date_ofr_rst, R.id.txt_to_date_ofr_rst, R.id.btn_ofr_rst, R.id.img_offer_ofr_rst})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_offer_ofr_rst:
                HelperMethodCustom.setImageProfile( getActivity(), AddNewOfferFragment.this,
                        mAlbumFiles, imgOfferOfrRst );
                strPathImage = mAlbumFiles.get( 0 ).getPath();
                break;
            case R.id.txt_from_date_ofr_rst:
                dateModel = new DateModel();
                HelperMethodCustom.showCalender( getActivity(), getString( R.string.start_at), txtFromDateOfrRst, dateModel );
                break;
            case R.id.txt_to_date_ofr_rst:
                dateModel = new DateModel();
                HelperMethodCustom.showCalender( getActivity(), getString( R.string.end_at), txtToDateOfrRst, dateModel );
                break;
            case R.id.btn_ofr_rst:
                strName = edtNameOfOfferOfrRst.getText().toString();
                strDesc = edtDescOfrRst.getText().toString();
                if (updateMode) {
                    onCall( apiServices.updateOfferRest( convertToRequestBody( strDesc ),
                            convertToRequestBody( strFromDate ),
                            convertToRequestBody( strName ),
                            convertFileToMultipart( strPathImage, "photo" ),
                            convertToRequestBody( strToDate ),
                            convertToRequestBody( HelperMethodCustom.getApiToken( getActivity()
                                    , Constant.RESTAURANT_API_TOKEN ) ) ) );
                } else {
                    onCall( apiServices.createOfferRest( convertToRequestBody( strDesc ),
                            convertToRequestBody( strFromDate ),
                            convertToRequestBody( strName ),
                            convertFileToMultipart( strPathImage, "photo" ),
                            convertToRequestBody( strToDate ),
                            convertToRequestBody( HelperMethodCustom.getApiToken( getActivity()
                                    , Constant.RESTAURANT_API_TOKEN ) ) ) );
                }
                break;
        }
    }

    private void onCall(Call<Item> api) {
        api.enqueue( new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, final Response<Item> response) {
                if (response.body().getStatus() == 1) {
                    Handler handler = new Handler();
                    handler.postDelayed( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                        }
                    }, 2000 );
                    OfferRestFragment fragment = new OfferRestFragment();
                    HelperMethodCustom.ReplaceFragment( getFragmentManager(), fragment, R.id.fram_container_home,
                            null, null );
                } else {
                    Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                    Log.d( TAG, "onResponse: getStatus()==0 " + response.body().getMsg() );
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }
}
