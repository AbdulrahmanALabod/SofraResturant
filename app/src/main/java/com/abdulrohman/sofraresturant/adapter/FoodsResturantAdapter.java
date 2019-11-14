package com.abdulrohman.sofraresturant.adapter;

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
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.activity.BaseActivity;
import com.abdulrohman.sofraresturant.ui.fragment.client.order.ItemOrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodsResturantAdapter extends RecyclerView.Adapter<FoodsResturantAdapter.ViewHolder> {



    private Context context;
    private List<ItemData> itemList = new ArrayList<>();
    private BaseActivity baseActivity;
    private int resturantId;

    public FoodsResturantAdapter(Context context, List<ItemData> itemList, int resturantId) {
        this.context = context;
        this.itemList = itemList;
        this.baseActivity = (BaseActivity) context;
        this.resturantId = resturantId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( R.layout.item_food_recycle,
                parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData( holder, position );
        setAction( holder, position );
    }

    private void setData(ViewHolder holder, int position) {
        ItemData data = itemList.get( position );
        HelperMethodCustom.onLoadImageFromUrl( holder.imgFoodRecyclFood, data.getPhotoUrl(), context, 1 );
        holder.txtNameRecyclFood.setText( data.getName() );
        holder.txtDescRecyclFood.setText( data.getDescription() );
        holder.txtPriceRecyclFood.setText( data.getPrice() );
        //todo uncomplete

    }

    private void setAction(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_food_recycl_food)
        ImageView imgFoodRecyclFood;
        @BindView(R.id.txt_name_recycl_food)
        TextView txtNameRecyclFood;
        @BindView(R.id.txt_desc_recycl_food)
        TextView txtDescRecyclFood;
        @BindView(R.id.txt_price_recycl_food)
        TextView txtPriceRecyclFood;
        //var
        private View view;


        public ViewHolder(View itemView) {
            super( itemView );
            view = itemView;
            ButterKnife.bind( this, view );
        }

        @OnClick(R.id.img_food_recycl_food)
        public void onViewClicked() {
            ItemOrderFragment itemOrderFragment = new ItemOrderFragment();
            Bundle args = new Bundle();
            ItemData item = itemList.get( getAdapterPosition() );
            args.putSerializable( Constant.ITEM_FOOD_ID,
                    item );
            itemOrderFragment.setArguments( args );
            HelperMethodCustom.ReplaceFragment( baseActivity.getSupportFragmentManager(),
                    itemOrderFragment,
                    R.id.fram_container_home,
                    null,
                    null );
        }
    }

}
