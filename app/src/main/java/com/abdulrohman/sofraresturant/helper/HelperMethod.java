package com.abdulrohman.sofraresturant.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;
import com.abdulrohman.sofraresturant.data.model.DateModel;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class HelperMethod {
    private static final String TAG = "HelperMethod";

    public static AlertDialog alertDialog;
    public static Snackbar snackbar;
    private static ProgressDialog checkDialog;

    public static MultipartBody.Part convertFileToMultipart(String pathImageFile, String Key) {
        if (pathImageFile != null) {
            File file = new File( pathImageFile );

            RequestBody reqFileselect = RequestBody.create( MediaType.parse( "image/*" ), file );

            MultipartBody.Part Imagebody = MultipartBody.Part.createFormData( Key, file.getName(), reqFileselect );

            return Imagebody;
        } else {
            return null;
        }
    }


    public static RequestBody convertToRequestBody(String part) {
        try {
            if (!part.equals( "" )) {
                RequestBody requestBody = RequestBody.create( MediaType.parse( "multipart/form-data" ), part );
                return requestBody;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static void onLoadImageFromUrl(ImageView imageView, String URl, Context context, int drId) {
        Glide.with( context )
                .load( URl )
                .into( imageView );
    }

    public static void createSnackBar(View view, String message, Context context) {
        final Snackbar snackbar = Snackbar.make( view, message, 50000 );
        snackbar.setAction( R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        } )
                .setActionTextColor( context.getResources().getColor( android.R.color.holo_red_light ) )

                .show();
    }

    public static void createSnackBar(View view, String message, Context context, View.OnClickListener action, String Title) {
        snackbar = Snackbar.make( view, message, 50000 );
        snackbar.setAction( Title, action )
                .setActionTextColor( context.getResources().getColor( android.R.color.holo_red_light ) )
                .show();
    }

    //Calender
    public static void showCalender(Context context, String title,
                                    final TextView text_view_data,
                                    final DateModel data1) {

        DatePickerDialog mDatePicker = new DatePickerDialog( context,
                AlertDialog.THEME_HOLO_DARK,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker,
                                          int selectedYear,
                                          int selectedMonth,
                                          int selectedDay) {

                        DecimalFormatSymbols symbols = new DecimalFormatSymbols( Locale.US );
                        DecimalFormat mFormat = new DecimalFormat( "00", symbols );
                        String data = selectedYear + "-" +
                                String.format( new Locale( "en" ),
                                        mFormat.format( Double.valueOf( (selectedMonth + 1) ) ) )
                                + "-" + mFormat.format( Double.valueOf( selectedDay ) );
                        Log.d( TAG, "onDateSet: data "+data );
                        data1.setDate_txt( data );
                        data1.setDay( mFormat.format( Double.valueOf( selectedDay ) ) );
                        data1.setMonth( mFormat.format( Double.valueOf( selectedMonth + 1 ) ) );
                        data1.setYear( String.valueOf( selectedYear ) );
                        if (text_view_data != null) {
                            text_view_data.setText( data );
                        }
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get( Calendar.MONTH ),
                Calendar.getInstance().get( Calendar.DAY_OF_MONTH ) );
        mDatePicker.setTitle( title );
        mDatePicker.show();
    }

    public static void showProgressDialog(Activity activity, String title) {
        try {
            checkDialog = new ProgressDialog( activity );
            checkDialog.setMessage( title );
            checkDialog.setIndeterminate( false );
            checkDialog.setCancelable( false );
            checkDialog.show();

        } catch (Exception e) {

        }
    }

    public static void dismissProgressDialog() {
        try {
            if (checkDialog != null && checkDialog.isShowing()) {
                checkDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }

    public static void ReplaceFragment(FragmentManager supportFragmentManager, Fragment fragment, int container_id
            , TextView toolbarTitle, String title) {

        FragmentTransaction transaction = supportFragmentManager.beginTransaction();

        transaction.replace( container_id, fragment );
        transaction.addToBackStack( null );
        transaction.commit();
        if (toolbarTitle != null) {
            toolbarTitle.setText( title );
        }

    }

    public static void disappearKeypad(Activity activity, View v) {
        try {
            if (v != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService( Context.INPUT_METHOD_SERVICE );
                imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
            }
        } catch (Exception e) {

        }
    }

    public static int reSizeFont(Resources resources, int larg, int normal, int small) {
        int screenSize = resources.getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        int textSize;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                textSize = larg;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                textSize = normal;
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                textSize = small;
                break;
            default:
                textSize = 15;
        }
        return textSize;
    }

    public static String getApiToken(FragmentActivity fragmentActivity, String apiToken) {
        SharedPreferences preferences = fragmentActivity.getSharedPreferences( Constant.SHIRD_API_TOKEN, Context.MODE_PRIVATE );
        if (fragmentActivity == null) {
            preferences.edit().putString( apiToken, null ).apply();
            return "";
        }
        return preferences.getString( apiToken, null );
    }

    public static void setCloroEditText(Context context, EditText editText, int id) {
        editText.getBackground().setColorFilter( context.getResources().getColor( id ),
                PorterDuff.Mode.SRC_IN );
    }

    public static void errorResponse(Activity activity, int code) {
        // error case
        switch (code) {
            case 404:
                Toast.makeText( activity, "not found", Toast.LENGTH_SHORT ).show();
                break;
            case 500:
                Toast.makeText( activity, "server broken", Toast.LENGTH_SHORT ).show();
                break;
            default:
                Toast.makeText( activity, "unknown error", Toast.LENGTH_SHORT ).show();
                break;
        }
    }

    public static void createShared(Activity activity, String TAG_NAME, String value) {
        SharedPreferences sharedPreferences = activity
                .getSharedPreferences( Constant.SHIRD_API_TOKEN, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString( TAG_NAME, value );
        editor.apply();
    }
}
