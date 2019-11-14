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

public class ItemsCatgAdapter extends RecyclerView.Adapter<ItemsCatgAdapter.ViewHolder> {

    // This object helps you save/restore the open/close state of each view
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    private BaseActivity.OnPressButton onPressButton;
    private Context context;
    private Activity activity;
    private List<ItemData> lstItemsCatg = new ArrayList<>();


    public ItemsCatgAdapter(Context context, Activity activity, List<ItemData> lstItemsCatg,
                            BaseActivity.OnPressButton onPressButton) {
        this.context = context;
        this.activity = activity;
        this.lstItemsCatg = lstItemsCatg;
        this.onPressButton = onPressButton;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( R.layout.item_items_category_recycle,
                parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData( holder, position );
        setAction( holder, position );
    }

    private void setData(ViewHolder holder, int position) {
        ItemData itemData = lstItemsCatg.get( position );
        // Save/restore the open/close state.
        // You need to provid9e a String id which uniquely defines the data object.
        viewBinderHelper.bind( holder.swipeRevealLayout, String.valueOf( itemData.getId() ) );
        HelperMethodCustom.onLoadImageFromUrl( holder.imgCataogryItemsCatItem, itemData.getPhotoUrl(), activity,
                1 );
        holder.txtNameItemsCatItem.setText( itemData.getName() );
        holder.txtDescItemsCatItem.setText( itemData.getDescription() );
        holder.txtPriceCatItem.setText( itemData.getPrice() );

    }

    private void setAction(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return lstItemsCatg.size();
    }

    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates( outState );
    }

    public void restoreStates(Bundle inState) {
        viewBinderHelper.restoreStates( inState );
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_edit_items_cat_item)
        ImageView imgEditItemsCatItem;
        @BindView(R.id.img_trash_items_cat_item)
        ImageView imgTrashItemsCatItem;
        @BindView(R.id.img_cataogry_items_cat_item)
        ImageView imgCataogryItemsCatItem;
        @BindView(R.id.txt_name_items_cat_item)
        TextView txtNameItemsCatItem;
        @BindView(R.id.txt_desc_items_cat_item)
        TextView txtDescItemsCatItem;
        @BindView(R.id.txt_price_cat_item)
        TextView txtPriceCatItem;
        @BindView(R.id.swip_container_my_item)
        SwipeRevealLayout swipeRevealLayout;

        private View view;

        public ViewHolder(View itemView) {
            super( itemView );
            view = itemView;
            ButterKnife.bind( this, view );
        }

        @OnClick({R.id.img_edit_items_cat_item, R.id.img_trash_items_cat_item})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.img_edit_items_cat_item:
                    onPressButton.onPositive( getAdapterPosition() );
                    break;
                case R.id.img_trash_items_cat_item:
                    onPressButton.onNegtive( getAdapterPosition() );
                    break;
            }
        }
    }
}
