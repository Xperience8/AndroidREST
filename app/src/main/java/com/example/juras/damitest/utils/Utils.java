package com.example.juras.damitest.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.juras.damitest.login.LoginActivity;
import com.facebook.login.LoginManager;

/** Helper methods for common tasks*/
public class Utils
{
    /**
     * Saves login data, email must be specified, others can be null(null means clear)
     * */
    public static void saveLoginData(Activity activity, String email, String password, String facebookId)
    {
        SharedPreferences sharedPref = activity.getSharedPreferences("user_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("email", email);

        if (password != null)
        {
            editor.putString("password", password);
        }
        else
        {
            editor.remove("password");
        }
        if (facebookId != null)
        {
            editor.putString("facebookId", facebookId);
        }
        else
        {
            editor.remove("facebookId");
        }
        editor.apply();
    }

    /**
     * Removes focus from specified EditText and hides keyboard
     * */
    public static void clearFocusAndHideKeyboard(Activity activity, View focusedView)
    {
        focusedView.clearFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
    }

    /**
     * Launches new activity, put user inside intent and close current one
     * */
    public static void launchActivity(User currentUser, Activity currentActivity, Class<?> nextActivityClass)
    {
        if (currentActivity.getClass() != nextActivityClass)
        {
            Intent intent = new Intent(currentActivity, nextActivityClass);
            intent.putExtra("user", currentUser);
            currentActivity.startActivity(intent);
            currentActivity.finish();
        }
    }

    /** Clears saved login info and returns to login screen*/
    public static void logout(Activity currentActivity)
    {
        LoginManager.getInstance().logOut();

        SharedPreferences sharedPref = currentActivity.getSharedPreferences("user_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(currentActivity, LoginActivity.class);
        currentActivity.startActivity(intent);
        currentActivity.finish();
    }

    /***
     * Shows progress dialog until successfull log in
     * */
    public static void showConnectingDialog(Activity currentActivity)
    {
        ProgressDialog.show(currentActivity, "Connecting", "Wait while connecting...");
    }


    /**
     * Returns true if given string is a mail
     * */
    public static boolean isValidMail(String mail)
    {
        return !mail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(mail).matches();
    }

    /**
     * Returns true if given string's length is more than 4
     * */
    public static boolean isValidPassword(String password)
    {
        return password.length() > 4;
    }
}
