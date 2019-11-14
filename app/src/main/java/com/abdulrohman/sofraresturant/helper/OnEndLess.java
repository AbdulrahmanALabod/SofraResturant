package com.abdulrohman.sofraresturant.helper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class OnEndLess extends RecyclerView.OnScrollListener {
    public    int preiviosTotal=0;
    private   boolean loading=true;
    private  int visibleThreshold;
    public  int firstVisibleItem,visibleItemCount,totalItemCount;
    public  int currentPage=1;
    private LinearLayoutManager mLinearLayoutManager;

    public OnEndLess(int visibleThreshold, LinearLayoutManager mLinearLayoutManager) {
        this.visibleThreshold = visibleThreshold;
        this.mLinearLayoutManager = mLinearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled( recyclerView, dx, dy );
        visibleItemCount=recyclerView.getChildCount();
        totalItemCount=mLinearLayoutManager.getItemCount();
        firstVisibleItem=mLinearLayoutManager.findFirstVisibleItemPosition();
        if (loading){
            if (totalItemCount>preiviosTotal){
                loading=false;
                preiviosTotal=totalItemCount;
            }
        }
        if (!loading&& totalItemCount-visibleItemCount<=(firstVisibleItem+visibleThreshold)){
            currentPage++;
            loading=true;
            onLoadMore(currentPage);
        }
    }

    public abstract void onLoadMore(int page);
}
