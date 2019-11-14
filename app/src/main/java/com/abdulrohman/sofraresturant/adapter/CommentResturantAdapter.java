package com.abdulrohman.sofraresturant.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.review.ReviewData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentResturantAdapter extends RecyclerView.Adapter<CommentResturantAdapter.ViewHolder> {
    private static final String TAG = "CommentResturantAdapter";

    private int[] arrayEmojy = {R.id.img_love_dialog_review, R.id.img_laugh_dialog_review,
            R.id.img_happy_dialog_review, R.id.img_sad_dialog_review, R.id.img_angry_dialog_review};
    private Context context;
    private Activity activity;
    private List<ReviewData> lstComment = new ArrayList<>();


    public CommentResturantAdapter(Context context, Activity activity, List<ReviewData> lstComment) {
        this.context = context;
        this.activity = activity;
        this.lstComment = lstComment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( R.layout.item_comment_recycle,
                parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData( holder, position );
        setAction( holder, position );
    }

    private void setData(ViewHolder holder, int position) {
        ReviewData reviewData = lstComment.get( position );
        int index = Integer.valueOf( reviewData.getRate() );
        Log.d( TAG, "setData: index " + index );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.imgEmojyCommentItem.setImageDrawable( activity.getDrawable( R.drawable.ic_sad_regular ) );
        }
        holder.txtCommentCommentItem.setText( reviewData.getComment() );
        holder.txtNameCommentItem.setText( reviewData.getClient().getName() );
    }

    private void setAction(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return lstComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name_comment_item)
        TextView txtNameCommentItem;
        @BindView(R.id.txt_comment_comment_item)
        TextView txtCommentCommentItem;
        @BindView(R.id.img_emojy_comment_item)
        ImageView imgEmojyCommentItem;
        private View view;

        public ViewHolder(View itemView) {
            super( itemView );
            view = itemView;
            ButterKnife.bind( this, view );
        }
    }
}
