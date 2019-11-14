package com.abdulrohman.sofraresturant.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.item.ItemData;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.activity.BaseActivity;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OfferRestAdapter extends RecyclerView.Adapter<OfferRestAdapter.ViewHolder> {


    // This object helps you save/restore the open/close state of each view
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    //var
    private Context context;
    private Activity activity;
    private List<ItemData> lstItemsData = new ArrayList<>();
    private BaseActivity.OnPressButton onPressButton;

    public OfferRestAdapter(Context context, Activity activity,
                            List<ItemData> lstItemsData,
                            BaseActivity.OnPressButton onPressButton) {
        this.context = context;
        this.activity = activity;
        this.lstItemsData = lstItemsData;
        this.onPressButton = onPressButton;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( R.layout.item_offer_rest_recycle,
                parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData( holder, position );
        setAction( holder, position );
    }

    private void setData(ViewHolder holder, int position) {
        ItemData itemData = lstItemsData.get( position );
        // You need to provide a String id which uniquely defines the data object.
        //can change name of swipeRootMyCatg
        viewBinderHelper.bind( holder.swipeRootMyCatg, String.valueOf( itemData.getId() ) );
        viewBinderHelper.setOpenOnlyOne( true );
        HelperMethodCustom.onLoadImageFromUrl( holder.imgOfferRestItem,itemData.getPhotoUrl(),activity,1 );
        holder.txtOferRestItem.setText( itemData.getName() );

    }

    private void setAction(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return lstItemsData.size();
    }

    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates( outState );
    }

    public void restoreStates(Bundle inState) {
        viewBinderHelper.restoreStates( inState );
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_edit_ofer_rest_item)
        ImageView imgEditOferRestItem;
        @BindView(R.id.img_trash_offer_rest_item)
        ImageView imgTrashOfferRestItem;
        @BindView(R.id.img_offer_rest_item)
        ImageView imgOfferRestItem;
        @BindView(R.id.txt_ofer_rest_item)
        TextView txtOferRestItem;
        @BindView(R.id.swipe_root_my_catg)
        SwipeRevealLayout swipeRootMyCatg;
        private View view;

        public ViewHolder(View itemView) {
            super( itemView );
            view = itemView;
            ButterKnife.bind( this, view );
        }

        @OnClick({R.id.img_edit_ofer_rest_item, R.id.img_trash_offer_rest_item})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.img_edit_ofer_rest_item:
                    onPressButton.onPositive( getAdapterPosition() );
                    break;
                case R.id.img_trash_offer_rest_item:
                    onPressButton.onNegtive( getAdapterPosition() );
                    break;
            }
        }
    }
}
