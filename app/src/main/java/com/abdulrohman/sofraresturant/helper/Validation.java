package com.abdulrohman.sofraresturant.helper;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulrohman.sofraresturant.R;


/**
 * Created by sas on 08/04/2018.
 */

public class Validation {
    public static String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    Context context;


    public static boolean setEmailValidation(Activity activity, String email) {

        if (!email.matches(emailPattern)) {
            customToast(activity, activity.getString(R.string.invalid_email));
            return false;
        } else {
            return true;
        }

    }


    // this method validate  password
    public static boolean setPasswordValidation(Activity activity, String password) {

        if (password.length() < 6) {
            customToast(activity, activity.getString( R.string.invalid_password));
            return false;
        } else {
            return true;
        }

    }


    // this method validate confirmation  password you have to put password editText field and confirm password edit text field
    public static boolean setConfirmPassword(Activity activity, String password, String confirmPassword) {

        if (!password.equals(confirmPassword)) {
            customToast(activity, activity.getString(R.string.invalid_confirm_password));
            return false;
        } else {
            return true;
        }


    }

    // this method validate  user name
    public static boolean setTextValidation(Activity activity, String text, int length, String message) {

        if (text.length() <= length) {
            customToast(activity, message);
            return false;
        } else {
            return true;
        }


    }


    //  this method to validate any editText for not null
    public boolean setEmptyValidation(Activity activity, String text) {

        if (TextUtils.isEmpty(text)) {
            customToast(activity, activity.getString(R.string.failed_required));
            return false;
        } else {
            return true;
        }
    }

    public static void customToast(Activity activity, String ToastTitle) {

        LayoutInflater inflater = activity.getLayoutInflater();

        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) activity.findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.text);
        text.setText(ToastTitle);

        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
