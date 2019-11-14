package com.abdulrohman.sofraresturant.ui.fragment.general;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.helper.AppDialog;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.activity.HomeCycleActivity;
import com.abdulrohman.sofraresturant.ui.activity.SpalchCycleActivity;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;
import com.abdulrohman.sofraresturant.ui.fragment.general.offer.OfferFragment;
import com.abdulrohman.sofraresturant.ui.fragment.general.resturant.CommentRateFragment;
import com.abdulrohman.sofraresturant.ui.fragment.resturant.offer.OfferRestFragment;
import com.abdulrohman.sofraresturant.ui.fragment.resturant.resturantCycle.ChangePasswordRestFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends BaseFragment {
    private static final String TAG = MoreFragment.class.getSimpleName();
    @BindView(R.id.txt_offer_more)
    TextView txtOfferMore;
    @BindView(R.id.txt_connect_us_more)
    TextView txtConnectUsMore;
    @BindView(R.id.txt_about_app_more)
    TextView txtAboutAppMore;
    @BindView(R.id.txt_log_out_more)
    TextView txtLogOutMore;
    Unbinder unbinder;
    AppDialog appDialog;
    @BindView(R.id.txt_comment_more)
    TextView txtCommentMore;
    @BindView(R.id.liner_comment_more)
    LinearLayout linerCommentMore;
    @BindView(R.id.txt_chang_pass_app_more)
    TextView txtChangPassAppMore;
    @BindView(R.id.liner_chag_pass_more)
    LinearLayout linerChagPassMore;
    @BindView(R.id.txt_line_more2)
    TextView txtLineMore2;
    @BindView(R.id.txt_line_more3)
    TextView txtLineMore3;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d( TAG, "onCreateView: start" );
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_more, container, false );
        Log.d( TAG, "onCreateView: end" );
        unbinder = ButterKnife.bind( this, view );
        linerChagPassMore.setVisibility( View.GONE );
        linerCommentMore.setVisibility( View.GONE );

        txtLineMore2.setVisibility( View.GONE );
        txtLineMore3.setVisibility( View.GONE );



        if (!HelperMethodCustom.isClient( HelperMethodCustom//if rest enter
                .getSheardTypeEnter( getActivity(), Constant.TYPE_ENTER_CLIENT ) )) {
            linerChagPassMore.setVisibility( View.VISIBLE );
            linerCommentMore.setVisibility( View.VISIBLE );
            txtLineMore2.setVisibility( View.VISIBLE );
            txtLineMore3.setVisibility( View.VISIBLE );
        }
        appDialog = new AppDialog();
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

    @OnClick({R.id.txt_offer_more, R.id.txt_connect_us_more, R.id.txt_about_app_more, R.id.txt_log_out_more,
            R.id.txt_comment_more, R.id.txt_chang_pass_app_more})
    public void onViewClicked(View view) {
        Fragment fragmentl = null;
        switch (view.getId()) {
            case R.id.txt_offer_more:
                if (HelperMethodCustom.isClient( HelperMethodCustom
                        .getSheardTypeEnter( getActivity(), Constant.TYPE_ENTER_CLIENT ) )) {
                    fragmentl = new OfferFragment();
                } else {
                    fragmentl = new OfferRestFragment();
                }
                break;
            case R.id.txt_connect_us_more:
                fragmentl = new ConnectFragment();
                break;
            case R.id.txt_about_app_more:
                fragmentl = new AboutusFragment();
                break;
            case R.id.txt_log_out_more:
                Log.d( TAG, "onViewClicked: txt_log_out_more" );
                if(HelperMethodCustom.getApiToken( getActivity(),Constant.CLIENT_API_TOKEN )!=null||
                        HelperMethodCustom.getApiToken( getActivity(),Constant.RESTAURANT_API_TOKEN )!=null){
                    Dialog dialog = appDialog.showDialog( getActivity(), R.layout.dialog_logout );
                    setDilogView( dialog );
                }else {
                    Toast.makeText( getActivity(), "you don't have account on sofra", Toast.LENGTH_SHORT ).show();
                }
                break;
            case R.id.txt_comment_more:
                fragmentl = new CommentRateFragment();
                break;
            case R.id.liner_chag_pass_more:
                fragmentl = new ChangePasswordRestFragment();
                break;

        }
        if (fragmentl != null) {
            HelperMethodCustom.ReplaceFragment( getFragmentManager(), fragmentl, R.id.fram_container_home,
                    null, null );
        }
    }

    private void setDilogView(final Dialog dialog) {

        ImageView imgCloseLogout = dialog.findViewById( R.id.img_close_logout );
        ImageView imgCheckLogout = dialog.findViewById( R.id.img_check_logout );
        TextView txtTitelDialogLogout = dialog.findViewById( R.id.txt_titel_dialog_logout );

        txtTitelDialogLogout.setText( getContext().getString( R.string.are_you_sure_log_out ) );
        imgCheckLogout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity()
                        .getSharedPreferences( Constant.SHIRD_API_TOKEN, Context.MODE_PRIVATE );
                if(Constant.CLIENT_TYPE.equals( HelperMethodCustom
                        .getSheardTypeEnter( getActivity(),Constant.TYPE_ENTER_CLIENT ) )){
                    preferences.edit().remove( Constant.CLIENT_API_TOKEN ).apply();
                }else {
                    preferences.edit().remove( Constant.RESTAURANT_API_TOKEN ).apply();
                }
                Intent intent = new Intent( (HomeCycleActivity) getActivity(), SpalchCycleActivity.class );
                startActivity( intent );
            }
        } );


        imgCloseLogout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } );

        dialog.show();
    }


}
