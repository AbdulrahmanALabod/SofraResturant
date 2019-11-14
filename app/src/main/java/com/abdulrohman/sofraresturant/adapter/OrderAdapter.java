package com.abdulrohman.sofraresturant.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.order.OrderData;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.activity.BaseActivity;
import com.abdulrohman.sofraresturant.ui.activity.HomeCycleActivity;
import com.abdulrohman.sofraresturant.ui.fragment.client.order.CurrentOrderFragment;
import com.abdulrohman.sofraresturant.ui.fragment.client.order.PendingOrderFragment;
import com.abdulrohman.sofraresturant.ui.fragment.client.order.PreviousOrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private static final String TAG = "OrderAdapter";
    BaseActivity.OnPositionItem onPositionItem;
    BaseActivity.OnPressButton onPressButton;
    Fragment mFragment;

    //var
    private Context context;
    private List<OrderData> lstItem = new ArrayList<OrderData>();
    private OrderData order;
    private Activity activity;
    private boolean clientMode = false;


    public OrderAdapter(Context context, Fragment fragment, List<OrderData> lstItem,
                        BaseActivity.OnPositionItem onPositionItem,
                        BaseActivity.OnPressButton onPressButton) {
        this.context = context;
        this.lstItem = lstItem;
        this.onPositionItem = onPositionItem;
        this.mFragment = fragment;
        this.onPressButton = onPressButton;
        activity = (HomeCycleActivity) context;
        clientMode = HelperMethodCustom.isClient( HelperMethodCustom
                .getSheardTypeEnter( activity, Constant.TYPE_ENTER_CLIENT ) );
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( R.layout.item_order_recycle,
                parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        setData( holder, position );
        setAction( holder, position );
    }

    private void setData(ViewHolder holder, int position) {
        order = lstItem.get( position );
        try {

            Log.d( TAG, "setData: order State;" + order.getState() );
            holder.txtNameRestOrder.setText( order.getRestaurant().getName() );
            holder.txtTotalOrder.setText( context.getString( R.string.total ) + " "
                    + order.getCost() + context.getString( R.string.dollar ) );
            holder.txtNumOrder.setText( context.getString( R.string.order_number_is ) +
                    " " + order.getTotal() );
            holder.txtAddressOrderItem.setText( context.getString( R.string.adress_destintion )
                    + " " + order.getAddress() );
            HelperMethodCustom.onLoadImageFromUrl( holder.imgRestOrder,
                    order.getRestaurant().getPhotoUrl(),
                    context,
                    1 );

            if (mFragment instanceof CurrentOrderFragment) {
                initCurrent( holder.btnMoreItemOrder, holder.btnMore1ItemOrder, holder.btnMore2ItemOrder );
            } else if (mFragment instanceof PreviousOrderFragment) {
                initPrevious( holder.btnMoreItemOrder, holder.btnMore1ItemOrder, holder.btnMore2ItemOrder );
            } else if (mFragment instanceof PendingOrderFragment) {
                initPending( holder.btnMoreItemOrder, holder.btnMore1ItemOrder, holder.btnMore2ItemOrder,
                        holder.imgRestOrder );
            }
        } catch (Exception e) {
            Log.d( TAG, "setData: e " + e.getMessage() );
        }
    }


    private void initCurrent(Button btnMoreItemOrder, Button btnMore1ItemOrder, Button btnMore2ItemOrder) {
        btnMore1ItemOrder.setVisibility( View.GONE );
        btnMore2ItemOrder.setVisibility( View.GONE );
        if (clientMode) {
            btnMoreItemOrder.setText( R.string.recieve );
            btnMoreItemOrder.setBackgroundColor( context.getResources().getColor( R.color.green ) );

        } else {
            btnMoreItemOrder.setText( R.string.call );
            btnMoreItemOrder.setBackgroundColor( context.getResources().getColor( R.color.colorAccent ) );
            btnMore1ItemOrder.setVisibility( View.VISIBLE );
            btnMore1ItemOrder.setText( R.string.approve );
            btnMore1ItemOrder.setBackgroundColor( context.getResources().getColor( R.color.green ) );
        }
    }

    private void initPrevious(Button btnMoreItemOrder, Button btnMore1ItemOrder, Button btnMore2ItemOrder) {
        btnMore1ItemOrder.setVisibility( View.GONE );
        btnMore2ItemOrder.setVisibility( View.GONE );
        if (Constant.DELIVERED_STATE_ORDER.equals( order.getState() )) {
            btnMoreItemOrder.setText( R.string.completed );
            btnMoreItemOrder.setBackgroundColor( context.getResources().getColor( R.color.green ) );
        } else if (Constant.REJECTED_STATE_ORDER.equals( order.getState() )) {
            btnMoreItemOrder.setText( R.string.canceled );
            btnMoreItemOrder.setBackgroundColor( context.getResources()
                    .getColor( R.color.colorAccent ) );
        }
    }

    private void initPending(Button btnMoreItemOrder, Button btnMore1ItemOrder, Button btnMore2ItemOrder,
                             ImageView imgRestOrder) {
        btnMore1ItemOrder.setVisibility( View.GONE );
        btnMore2ItemOrder.setVisibility( View.GONE );
        if (clientMode) {
            btnMoreItemOrder.setText( R.string.cancel );
            btnMoreItemOrder.setBackgroundColor( context.getResources().getColor( R.color.pampy ) );

        } else {
            btnMore1ItemOrder.setVisibility( View.VISIBLE );
            btnMore2ItemOrder.setVisibility( View.VISIBLE );
            btnMoreItemOrder.setText( R.string.call );
            btnMore1ItemOrder.setText( R.string.accept );
            btnMore2ItemOrder.setText( R.string.cancel );
            btnMoreItemOrder.setBackgroundColor( context.getResources().getColor( R.color.colorAccent ) );
            btnMore1ItemOrder.setBackgroundColor( context.getResources().getColor( R.color.green ) );
            btnMore2ItemOrder.setBackgroundColor( context.getResources().getColor( R.color.pampy ) );

        }
    }


    private void setAction(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return lstItem.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_rest_order)
        CircleImageView imgRestOrder;
        @BindView(R.id.txt_name_rest_order)
        TextView txtNameRestOrder;
        @BindView(R.id.txt_num_order)
        TextView txtNumOrder;
        @BindView(R.id.txt_total_order)
        TextView txtTotalOrder;
        @BindView(R.id.btn_more_item_order)
        Button btnMoreItemOrder;
        @BindView(R.id.txt_address_order_item)
        TextView txtAddressOrderItem;
        @BindView(R.id.btn_more1_item_order)
        Button btnMore1ItemOrder;
        @BindView(R.id.btn_more2_item_order)
        Button btnMore2ItemOrder;
        //var
        private View view;

        public ViewHolder(View itemView) {
            super( itemView );
            view = itemView;
            ButterKnife.bind( this, view );

        }

        @OnClick({R.id.btn_more_item_order, R.id.btn_more1_item_order, R.id.btn_more2_item_order})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.btn_more_item_order:
                    if (!(mFragment instanceof PreviousOrderFragment)) {
                        if (clientMode) {
                            Log.d( TAG, "onViewClicked: clientMode " + clientMode );
                            onPositionItem.onSucssess( getAdapterPosition() );
                        }

                    }
                    break;
                case R.id.btn_more1_item_order:
                    if (mFragment instanceof CurrentOrderFragment || mFragment instanceof PendingOrderFragment) {
                        onPressButton.onPositive( getAdapterPosition() );
                    }

                    break;
                case R.id.btn_more2_item_order:
                    if (mFragment instanceof PendingOrderFragment) {
                        onPressButton.onNegtive( getAdapterPosition() );
                    }
                    break;
            }
        }


    }
}
