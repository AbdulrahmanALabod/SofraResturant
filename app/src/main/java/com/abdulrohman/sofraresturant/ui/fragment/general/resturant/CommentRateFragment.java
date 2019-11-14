package com.abdulrohman.sofraresturant.ui.fragment.general.resturant;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.adapter.CommentResturantAdapter;
import com.abdulrohman.sofraresturant.data.model.review.Review;
import com.abdulrohman.sofraresturant.data.model.review.ReviewData;
import com.abdulrohman.sofraresturant.helper.AppDialog;
import com.abdulrohman.sofraresturant.helper.Constant;
import com.abdulrohman.sofraresturant.helper.HelperMethodCustom;
import com.abdulrohman.sofraresturant.helper.OnEndLess;
import com.abdulrohman.sofraresturant.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommentRateFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = CommentRateFragment.class.getSimpleName();
    @BindView(R.id.btn_add_comment_rate)
    Button btnAddCommentRate;
    @BindView(R.id.recycl_comment_comment_rate)
    RecyclerView recyclCommentCommentRate;
    Unbinder unbinder;
    AppDialog appDialog;
    @BindView(R.id.txt_no_comment_rate)
    TextView txtNoCommentRate;
    private EditText edtComment;
    private ImageView img_angry;
    private ImageView imgLaugh;
    private ImageView imgSad;
    private ImageView imgHappy;
    private ImageView imgLove;
    //var
    private int emojy;
    private String strComment;
    private Dialog dialog;
    private List<ReviewData> lstReviewData = new ArrayList<>();
    private int resturantId;
    private int maxPage;
    private int previousPage = 1;
    private CommentResturantAdapter commentAdapter;
    private OnEndLess onEndLess;
    private LinearLayoutManager layoutManager;

    public CommentRateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        super.onCreateView( inflater, container, savedInstanceState );
        View view = inflater.inflate( R.layout.fragment_comment_rate, container, false );
        unbinder = ButterKnife.bind( this, view );
        txtNoCommentRate.setVisibility( View.VISIBLE );
        layoutManager = new LinearLayoutManager( getActivity() );
        if (recyclCommentCommentRate != null) {
            recyclCommentCommentRate.setLayoutManager( layoutManager );
        }

        resturantId = HelperMethodCustom.getResturanId( getActivity() );

        initRecycle();
        if (lstReviewData.size() == 0) {
            getComment( 1 );
        }
        Log.d( TAG, "onCreateView: end" );
        return view;
    }

    private void initRecycle() {
        commentAdapter = new CommentResturantAdapter( getContext(), getActivity(), lstReviewData );

        onEndLess = new OnEndLess( 1, layoutManager ) {
            @Override
            public void onLoadMore(int page) {
                Toast.makeText( getActivity(), String.valueOf( currentPage ), Toast.LENGTH_SHORT )
                        .show();
                if (currentPage <= maxPage) {
                    if (currentPage != 1 && maxPage != 0) {
                        previousPage = currentPage;
                        getComment( currentPage );
                    } else {
                        onEndLess.currentPage = previousPage;
                    }
                } else {
                    onEndLess.currentPage = previousPage;
                }

            }
        };

        recyclCommentCommentRate.addOnScrollListener( onEndLess );
        recyclCommentCommentRate.setAdapter( commentAdapter );
        commentAdapter.notifyDataSetChanged();
    }

    private void getComment(int page) {
        onCall( apiServices.getCommentResturant( Constant.apiToken, String.valueOf( resturantId ), page ) );
    }

    private void onCall(Call<Review> reviewCall) {
        reviewCall.enqueue( new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                try {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            maxPage = response.body().getData().getLastPage();
                            lstReviewData.addAll( response.body().getData().getData() );
                            if (lstReviewData.size() > 0 && getActivity() != null) {
                                txtNoCommentRate.setVisibility( View.GONE );
                            }
                            initRecycle();
                            commentAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT )
                                    .show();
                        }
                    }

                } catch (Exception e) {
                    Log.d( TAG, "onResponse: e " + e.getMessage() );
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    @OnClick(R.id.btn_add_comment_rate)
    public void onViewClicked() {
        appDialog = new AppDialog();
        dialog = appDialog.showDialog( getActivity(), R.layout.dialog_review );
        edtComment = dialog.findViewById( R.id.edt_comment_dilog_review );
        imgLove = dialog.findViewById( R.id.img_love_dialog_review );
        imgHappy = dialog.findViewById( R.id.img_happy_dialog_review );
        imgSad = dialog.findViewById( R.id.img_sad_dialog_review );
        imgLaugh = dialog.findViewById( R.id.img_laugh_dialog_review );
        img_angry = dialog.findViewById( R.id.img_angry_dialog_review );
        btnAddCommentRate = dialog.findViewById( R.id.btn_add_comment_dilog_review );
        img_angry.setOnClickListener( this );
        imgHappy.setOnClickListener( this );
        imgSad.setOnClickListener( this );
        imgLove.setOnClickListener( this );
        imgLaugh.setOnClickListener( this );
        btnAddCommentRate.setOnClickListener( this );
        dialog.show();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_love_dialog_review:
                emojy = 1;
                Log.d( TAG, "onClick: 1" );
                chaosRmojy( "love" );
                break;
            case R.id.img_happy_dialog_review:
                emojy = 3;
                chaosRmojy( "happy" );
                break;
            case R.id.img_sad_dialog_review:
                emojy = 4;
                chaosRmojy( "sad" );
                break;
            case R.id.img_angry_dialog_review:
                emojy = 5;
                chaosRmojy( "angry" );
                break;
            case R.id.img_laugh_dialog_review:
                emojy = 2;
                chaosRmojy( "laugh" );
                break;
            case R.id.btn_add_comment_dilog_review:
                strComment = edtComment.getText().toString();
                if (strComment.isEmpty()) {
                    Toast.makeText( getActivity(), getString( R.string.enter_comment ), Toast.LENGTH_SHORT )
                            .show();
                } else {
                    postComment( strComment, String.valueOf( emojy ) );
                    dialog.cancel();
                }
                break;


        }

    }

    private void chaosRmojy(String strChose) {
        Toast.makeText( getActivity(), strChose, Toast.LENGTH_SHORT ).show();
    }

    private void postComment(String strComment, String emojy) {
        apiServices.createComment( emojy, strComment, String.valueOf( resturantId ),
                HelperMethodCustom.getApiToken( getActivity(), Constant.CLIENT_API_TOKEN ) )
                .enqueue( new Callback<Review>() {
                    @Override
                    public void onResponse(Call<Review> call, Response<Review> response) {
                        try {
                            if (response.body().getStatus() == 1) {
                                Log.d( TAG, "onResponse: " + response.message() );
                                Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                            } else {
                                Log.d( TAG, "onResponse:getStatus()==0 " + response.message() );
                                Toast.makeText( getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT ).show();
                            }

                        } catch (Exception e) {
                            Log.d( TAG, "onResponse: e " + e.getMessage() );
                        }
                    }

                    @Override
                    public void onFailure(Call<Review> call, Throwable t) {
                        Log.d( TAG, "onFailure " + t.getMessage() );
                        Toast.makeText( getActivity(), t.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                } );
    }

    @Override
    public void onBack() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        } else {
            super.onBack();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
