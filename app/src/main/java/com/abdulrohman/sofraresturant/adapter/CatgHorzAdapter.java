package com.abdulrohman.sofraresturant.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.category.CategoryData;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.ui.activity.BaseActivity;
import com.abdulrohman.sofraresturant.ui.activity.HomeCycleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class CatgHorzAdapter extends RecyclerView.Adapter<CatgHorzAdapter.ViewHolder> {


    BaseActivity.OnPositionItem onPositionItem;
    private Context context;
    private Activity activity;
    private List<CategoryData> lstCatg = new ArrayList<>();

    public CatgHorzAdapter(Context context, List<CategoryData> lstCatg, BaseActivity.OnPositionItem onPositionItem) {
        this.context = context;
        this.lstCatg = lstCatg;
        activity = (HomeCycleActivity) context;
        this.onPositionItem = onPositionItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( R.layout.item_catg_horz_recycle,
                parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData( holder, position );
        setAction( holder, position );
    }

    private void setData(ViewHolder holder, int position) {
        CategoryData catgData = lstCatg.get( position );
        holder.txtNameHorzCatg.setText( catgData.getName() );
        HelperMethodCustom.onLoadImageFromUrl( holder.imgHorzCatg, catgData.getPhotoUrl(), activity, 1 );
    }

    private void setAction(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return lstCatg.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_horz_catg)
        CircleImageView imgHorzCatg;
        @BindView(R.id.txt_name_horz_catg)
        TextView txtNameHorzCatg;
        @BindView(R.id.card_catg_horz)
        CardView cardCatgHorz;
        private View view;

        public ViewHolder(View itemView) {
            super( itemView );
            view = itemView;
            ButterKnife.bind( this, view );

        }

        @OnClick(R.id.card_catg_horz)
        public void onViewClicked() {
            onPositionItem.onSucssess( getAdapterPosition() );
        }


    }
}
