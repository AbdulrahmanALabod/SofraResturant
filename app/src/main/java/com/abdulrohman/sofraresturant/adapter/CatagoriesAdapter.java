package com.abdulrohman.sofraresturant.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.resturant.ResturantData;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.activity.BaseActivity;
import com.abdulrohman.sofraresturant.ui.activity.HomeCycleActivity;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CatagoriesAdapter extends RecyclerView.Adapter<CatagoriesAdapter.ViewHolder> {
    private static final String TAG = "CatagoriesAdapter";
    // This object helps you save/restore the open/close state of each view
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();



    private BaseActivity.OnPressButton onPressButton;
    private BaseActivity.OnPositionItem onPositionItem;
    private Context context;
    private Activity activity;
    private List<ResturantData> lstCatagory = new ArrayList<>();
    private ResturantData itemData;


    public CatagoriesAdapter(Context context, List<ResturantData> lstCatagory,
                             BaseActivity.OnPressButton onPressButton,
                             BaseActivity.OnPositionItem onPositionItem) {
        this.context = context;
        this.activity = (HomeCycleActivity)context;
        this.lstCatagory = lstCatagory;
        this.onPressButton = onPressButton;
        this.onPositionItem = onPositionItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( R.layout.item_catagory_recycle,
                parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData( holder, position );
        setAction( holder, position );
    }

    private void setData(ViewHolder holder, int position) {
        itemData = lstCatagory.get( position );
        Log.d( TAG, "setData: item "+itemData.getName() );
        // Save/restore the open/close state.
        // You need to provid9e a String id which uniquely defines the data object.
        viewBinderHelper.bind( holder.swipeRootMyCatg, String.valueOf( itemData.getId() ) );
        HelperMethodCustom.onLoadImageFromUrl( holder.imgCataogryCatagoriesItem, itemData.getPhotoUrl(), activity,
                1 );
        holder.txtNameCatagoriesItem.setText( itemData.getName() );
    }

    private void setAction(ViewHolder holder, int position) {

    }

    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates( outState );
    }

    public void restoreStates(Bundle inState) {
        viewBinderHelper.restoreStates( inState );
    }

    @Override
    public int getItemCount() {
        return lstCatagory.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.img_edit_catagories_item)
        ImageView imgEditCatagoriesItem;
        @BindView(R.id.img_trash_catagories_item)
        ImageView imgTrashCatagoriesItem;
        @BindView(R.id.img_cataogry_catagories_item)
        ImageView imgCataogryCatagoriesItem;
        @BindView(R.id.txt_name_catagories_item)
        TextView txtNameCatagoriesItem;
        @BindView(R.id.liner_root_my_catg)
        LinearLayout linerRootMyCatg;
        @BindView(R.id.swipe_root_my_catg)
        SwipeRevealLayout swipeRootMyCatg;

        //var
        private View view;

        public ViewHolder(View itemView) {
            super( itemView );
            view = itemView;
            ButterKnife.bind( this, view );
        }

        @OnClick({R.id.img_edit_catagories_item, R.id.img_trash_catagories_item, R.id.liner_root_my_catg})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.img_edit_catagories_item:
                    onPressButton.onPositive( getAdapterPosition() );
                    break;
                case R.id.img_trash_catagories_item:
                    onPressButton.onNegtive( getAdapterPosition() );
                    break;
                case R.id.liner_root_my_catg:
                    onPositionItem.onSucssess( getAdapterPosition() );
            }
        }
    }
}
