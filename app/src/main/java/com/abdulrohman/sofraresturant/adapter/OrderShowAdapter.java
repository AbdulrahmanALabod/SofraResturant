package com.abdulrohman.sofraresturant.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.item.ItemData;
import com.abdulrohman.sofraresturant.ui.activity.HomeCycleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderShowAdapter extends RecyclerView.Adapter<OrderShowAdapter.ViewHolder> {


    private Context context;
    private Activity activity;
    private List<ItemData> lstItemData = new ArrayList<>();

    public OrderShowAdapter(Context context, List<ItemData> lstItemData) {
        this.context = context;
        this.lstItemData = lstItemData;
        activity = (HomeCycleActivity) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( activity ).inflate( R.layout.item_order_infromation_recycle,
                parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData( holder, position );
        setAction( holder, position );
    }

    private void setData(ViewHolder holder, int position) {
        ItemData itemData = lstItemData.get( position );
        holder.txtNameOrderInformation.setText( itemData.getName() );
        holder.txtPriceOrderInformation.setText( itemData.getPrice() );
    }

    private void setAction(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return lstItemData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name_order_information)
        TextView txtNameOrderInformation;
        @BindView(R.id.txt_price_order_information)
        TextView txtPriceOrderInformation;

        private View view;

        public ViewHolder(View itemView) {
            super( itemView );
            view = itemView;
            ButterKnife.bind( this, view );
        }
    }
}
