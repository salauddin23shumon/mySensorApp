package com.sss.myhwmonitorapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.sss.myhwmonitorapp.R;

public class AppProgressBar {

    private static Dialog dialog;
    private static final String TAG = "AppProgressBar";

    public AppProgressBar(Context context) {
        dialog = new Dialog(context);
    }

    public static void showProgressDialog() {

        Log.d(TAG, "showProgressDialog: ");
//        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(false);
        final CircularProgressBar circularProgressBar = dialog.findViewById(R.id.circular_progressBar);
        circularProgressBar.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);
        final TextView progressUpdate = dialog.findViewById(R.id.progressTV);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && dialog.isShowing()) {
                    dialog.dismiss();
                }
                return true;
            }
        });
    }

    public static void removeDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

}
