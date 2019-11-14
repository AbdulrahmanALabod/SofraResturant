package com.abdulrohman.sofraresturant.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.item.ItemData;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.activity.HomeCycleActivity;
import com.abdulrohman.sofraresturant.ui.fragment.general.offer.OfferDetailsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {


    @BindView(R.id.img_more_item)
    ImageView imgMoreItem;
    @BindView(R.id.txt_more_item)
    TextView txtMoreItem;
    @BindView(R.id.btn_more_item)
    Button btnMoreItem;
    private Context context;
    private Activity activity;
    private List<ItemData> lstOfferData = new ArrayList<>();
    private FragmentManager fragment;


    public OfferAdapter(Context context, List<ItemData> lstOfferData, FragmentManager fragment) {
        this.context = context;
        this.lstOfferData = lstOfferData;
        this.fragment = fragment;
        this.activity=(HomeCycleActivity)context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( R.layout.item_more_recycle,
                parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData( holder, position );
        setAction( holder, position );
    }

    private void setData(ViewHolder holder, int position) {
        ItemData offerData = lstOfferData.get( position );
        HelperMethodCustom.onLoadImageFromUrl( holder.imgMoreItem, offerData.getPhotoUrl(), activity, 1 );
        holder.txtMoreItem.setText( offerData.getDescription() );
    }

    private void setAction(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return lstOfferData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_more_item)
        ImageView imgMoreItem;
        @BindView(R.id.txt_more_item)
        TextView txtMoreItem;
        @BindView(R.id.btn_more_item)
        Button btnMoreItem;
        private View view;

        public ViewHolder(View itemView) {
            super( itemView );
            view = itemView;
            ButterKnife.bind( this, view );
        }

        @OnClick(R.id.btn_more_item)
        public void onViewClicked() {
            OfferDetailsFragment offerDetailsFragment = new OfferDetailsFragment();
            Bundle bundle = new Bundle();
            ItemData offerData = lstOfferData.get( getAdapterPosition() );
            bundle.putSerializable( Constant.OFFER_ID, offerData );
            offerDetailsFragment.setArguments( bundle );
            HelperMethodCustom.ReplaceFragment( fragment, offerDetailsFragment,
                    R.id.fram_container_home, null, null );
        }
    }
}
