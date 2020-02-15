package com.example.dine_manager_admin ;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.victor.loading.rotate.RotateLoading;

public class CustomDialog {

    private static final String TAG = "CustomDialog";

    private RotateLoading   progressBar;
    private TextView        messageDisplay;
    private Dialog          dialogBox;
    private Activity        activity;

    public CustomDialog(Activity activity) {
        this.activity = activity;
    }

    public void showProgressBar() {
        if(this.dialogBox == null) {
            dialogBox = new Dialog(this.activity);
            dialogBox.setContentView(R.layout.progress_dialog);
            dialogBox.setCancelable(false);
            dialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressBar = dialogBox.findViewById(R.id.dialog2_rotateLoadingProgressBar);
            progressBar.start();
            dialogBox.show();
        }
    }

    public void showProgressBarWithMessage(String message) {
        if(this.dialogBox == null) {
            dialogBox = new Dialog(this.activity);
            dialogBox.setContentView(R.layout.progress_dialog_with_message);
            dialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogBox.setCancelable(false);

            messageDisplay = dialogBox.findViewById(R.id.dialog1_progressMessage);
            progressBar = dialogBox.findViewById(R.id.dialog1_rotateLoadingProgressBar);

            progressBar.start();
            messageDisplay.setText(message);
            dialogBox.show();
        } else {
            messageDisplay.setText(message);
        }
    }

    public void hideProgressDialog() {
        if(dialogBox != null) {
            dialogBox.hide();
            dialogBox.dismiss();
            dialogBox = null;
        }
    }
}
