package com.abdulrohman.sofraresturant.helper;

import android.widget.ImageView;

import com.abdulrohman.sofraresturant.R;
import com.bumptech.glide.Glide;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;

public class MediaLoader implements AlbumLoader {
    @Override
    public void load(ImageView imageView, AlbumFile albumFile) {
        load(imageView, albumFile.getPath());
    }

    @Override
    public void load(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .error( R.drawable.kfc)
                .placeholder(R.drawable.kfc)
                .crossFade()
                .into(imageView);
    }
}
