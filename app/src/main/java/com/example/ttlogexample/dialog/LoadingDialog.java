package com.example.ttlogexample.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;

import com.example.ttlogexample.R;

public class LoadingDialog {
    Activity activity;
    Dialog dialog;
    private boolean show = false;

    public boolean isShow() {
        return show;
    }

    public LoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    public void showLoad() {
        show =true;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_load, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            int width = dpToPx(100); // Example: 300dp width
            int height = dpToPx(100); // Example: 200dp height
            window.setLayout(width, height);
            window.setGravity(Gravity.CENTER_VERTICAL);
            window.setBackgroundDrawableResource(R.drawable.background_dialog);

// Replace 600 and 400 with your desired width and height in pixels
        }


    }
    public void dismissLoad(){
        show =false;
        dialog.dismiss();
    }
    private int dpToPx(int dp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

}

