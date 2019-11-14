package com.abdulrohman.sofraresturant.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.item.ItemData;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.activity.BaseActivity;
import com.abdulrohman.sofraresturant.ui.activity.HomeCycleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FinalOrderAdapter extends RecyclerView.Adapter<FinalOrderAdapter.ViewHolder> {
    private static final String TAG = "FinalOrderAdapter";
    private Context context;
    private List<ItemData> lstItemData = new ArrayList<>();
    private BaseActivity.OnPositionItem onPositionItem;
    private Activity activity;
    private int count;
    private TextView txtAllPrice;
    private Double priceOrders = 0.0;
    private ItemData itemData;

    public FinalOrderAdapter(Context context, List<ItemData> lstItemData,
                             BaseActivity.OnPositionItem onPositionItem, TextView txtAllPrice) {
        this.context = context;
        this.lstItemData = lstItemData;
        this.onPositionItem = onPositionItem;
        this.activity = (HomeCycleActivity) context;
        this.txtAllPrice = txtAllPrice;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( R.layout.item_final_item_order,
                parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData( holder, position );
        setAction( holder, position );
    }

    private void setData(ViewHolder holder, int position) {
        itemData = lstItemData.get( position );
//        count = Integer.parseInt( itemData.getPivot().getQuantity() );
        HelperMethodCustom.onLoadImageFromUrl( holder.imgFinalOrder, itemData.getPhotoUrl(), activity, 1 );
        holder.txtCountFinalOrder.setText( itemData.getQuantity() );
        Log.d( TAG, "setData: itemData.getName()  " + itemData.getName() );
        holder.txtNameFinalOrder.setText( itemData.getName() );
        holder.txtPriceFinalOrder.setText( itemData.getPrice() + " " + R.string.dollar );
        if (Integer.parseInt( itemData.getQuantity() ) > 1) {
            priceOrders = priceOrders + Double.parseDouble( itemData.getPrice() ) *
                    Integer.parseInt( itemData.getQuantity() );
        } else {
            priceOrders = priceOrders + Double.parseDouble( itemData.getPrice() );
        }
        Log.d( TAG, "setData: priceOrders " + priceOrders );
        txtAllPrice.setText( String.valueOf( priceOrders ) );

    }

    private void setAction(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return lstItemData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_final_order)
        ImageView imgFinalOrder;
        @BindView(R.id.txt_name_final_order)
        TextView txtNameFinalOrder;
        @BindView(R.id.txt_price_final_order)
        TextView txtPriceFinalOrder;
        @BindView(R.id.txt_quantity_item_order)
        TextView txtQuantityItemOrder;
        @BindView(R.id.txt_plus_final_order)
        TextView txtPlusFinalOrder;
        @BindView(R.id.txt_count_final_order)
        TextView txtCountFinalOrder;
        @BindView(R.id.txt_mains_final_order)
        TextView txtMainsFinalOrder;
        @BindView(R.id.img_cancel_final_order)
        ImageView imgCancelFinalOrder;
        //vars
        private View view;


        public ViewHolder(View itemView) {
            super( itemView );
            view = itemView;
            ButterKnife.bind( this, view );
        }

        @OnClick({R.id.txt_plus_final_order, R.id.txt_mains_final_order, R.id.img_cancel_final_order})
        public void onViewClicked(View view) {
            Integer counter = 0;
            ItemData idp = lstItemData.get( getAdapterPosition() );
            counter = Integer.parseInt( idp.getQuantity() );
            switch (view.getId()) {
                case R.id.txt_plus_final_order:
                    counter = counter + 1;
                    idp.setQuantity( String.valueOf( counter ) );
                    Log.d( TAG, "onViewClicked: counter " + counter );
                    priceOrders = priceOrders + Double.parseDouble( itemData.getPrice() );
                    txtAllPrice.setText( String.valueOf( priceOrders ) );
                    displyCount( counter );
                    break;
                case R.id.txt_mains_final_order:
                    if (counter > 1) {
                        counter = counter - 1;
                        idp.setQuantity( String.valueOf( counter ) );
                        priceOrders = priceOrders - Double.parseDouble( itemData.getPrice() );
                        txtAllPrice.setText( String.valueOf( priceOrders ) );
                        displyCount( counter );
                    } else {
                        Toast.makeText( activity, "can't that", Toast.LENGTH_SHORT ).show();
                    }
                    break;
                case R.id.img_cancel_final_order:
                    if (Integer.parseInt( itemData.getQuantity() ) > 1) {
                        priceOrders = priceOrders - Double.parseDouble( itemData.getPrice() ) *
                                Double.parseDouble( itemData.getQuantity() );
                    } else {
                        priceOrders = priceOrders - Double.parseDouble( itemData.getQuantity() );
                    }
                    txtAllPrice.setText( String.valueOf( priceOrders ) );
                    onPositionItem.onSucssess( getAdapterPosition() );
                    break;
            }
        }

        private void displyCount(int count) {
            txtCountFinalOrder.setText( String.valueOf( count ) );
        }
    }
}
