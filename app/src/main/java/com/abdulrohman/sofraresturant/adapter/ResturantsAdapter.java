package com.abdulrohman.sofraresturant.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.resturant.ResturantData;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.activity.BaseActivity;
import com.abdulrohman.sofraresturant.ui.fragment.general.resturant.ResturantDetailesFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResturantsAdapter extends RecyclerView.Adapter<ResturantsAdapter.ViewHolder> {


    @BindView(R.id.root_rest_item)
    LinearLayout rootRestItem;

    private Context context;
    private FragmentManager mFragmentManager;

    private List<ResturantData> restaurantDataList = new ArrayList<>();
    private BaseActivity.OnPositionItem onPositionItem;
    private int position;

    public ResturantsAdapter(Context context, List<ResturantData> restaurantDataList,
                             BaseActivity.OnPositionItem onPositionItem,
                             FragmentManager fragmentManager) {
        this.context = context;
        this.restaurantDataList = restaurantDataList;
        this.onPositionItem = onPositionItem;
        this.mFragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( R.layout.item_recycl_resturnats,
                parent, false );
        return new ViewHolder( view, onPositionItem );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData( holder, position );
        setAction( holder, position );
    }

    private void setData(ViewHolder holder, int position) {
        ResturantData data = restaurantDataList.get( position );
        HelperMethodCustom.onLoadImageFromUrl( holder.imgResturantItem, data.getPhotoUrl(), context, 1 );
        holder.txtCostDelvaryItem.setText( context.getString( R.string.delivery_Cost ) + " " + data.getDeliveryCost() +
                context.getString( R.string.dollar ) );
        holder.txtCostDelvaryItem.setTextSize( (float) HelperMethodCustom
                .reSizeFont( context.getResources(), 18, 12, 8 ) );
        holder.ratingBarRestItem.setRating( data.getRate() );
        holder.txtNameRestItem.setText( data.getName() );
        holder.txtNameRestItem.setTextSize( (float) HelperMethodCustom
                .reSizeFont( context.getResources(), 20, 16, 10 ) );
        holder.txtMinimumPriceItem.setTextSize( (float) HelperMethodCustom
                .reSizeFont( context.getResources(), 18, 12, 8 ) );
        holder.txtOpenRestItem.setTextSize( (float) HelperMethodCustom
                .reSizeFont( context.getResources(), 18, 12, 8 ) );
        holder.txtMinimumPriceItem.setText( context.getString( R.string.minimum_price_for_oder ) + " "
                + data.getMinimumCharger() + context.getString( R.string.dollar ) );

    }

    private void setAction(ViewHolder holder, int position) {

        this.position = position;
//        onViewClicked();
    }


    @Override
    public int getItemCount() {
        return restaurantDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_resturant_item)
        ImageView imgResturantItem;
        @BindView(R.id.txt_naem_rest_item)
        TextView txtNameRestItem;
        @BindView(R.id.txt_minimum_price_item)
        TextView txtMinimumPriceItem;
        @BindView(R.id.txt_cost_delvary_item)
        TextView txtCostDelvaryItem;
        @BindView(R.id.ratingBar_rest_item)
        RatingBar ratingBarRestItem;
        @BindView(R.id.txt_open_rest_item)
        TextView txtOpenRestItem;
        BaseActivity.OnPositionItem onPositionItem;
        private View view;

        public ViewHolder(View itemView, BaseActivity.OnPositionItem onPositionItem) {
            super( itemView );
            view = itemView;
            ButterKnife.bind( this, view );
            this.onPositionItem = onPositionItem;
        }

        @OnClick(R.id.root_rest_item)
        public void onViewClicked() {
            ResturantDetailesFragment detailesFragment = new ResturantDetailesFragment();
            Bundle args = new Bundle();
            ResturantData resturantData = restaurantDataList.get( getAdapterPosition() );
            args.putSerializable( Constant.RESTURANT_ID, resturantData );
            detailesFragment.setArguments( args );
            HelperMethodCustom.ReplaceFragment( mFragmentManager,
                    detailesFragment,
                    R.id.fram_container_home,
                    null,
                    null );
        }


    }
}
