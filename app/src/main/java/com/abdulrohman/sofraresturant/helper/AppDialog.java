package com.abdulrohman.sofraresturant.helper;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

public  class AppDialog {

    //var
    private Dialog dialog;
    private Activity activity;

    /**
     * you should show dialog
     *
     * @param activity
     * @param layoutId
     */
    public Dialog showDialog(final Activity activity , int layoutId) {
        this.activity = activity;
        dialog = new Dialog( this.activity );
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        dialog.setCancelable( true );
        dialog.setContentView( layoutId );
        dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        return dialog;

    }



}
