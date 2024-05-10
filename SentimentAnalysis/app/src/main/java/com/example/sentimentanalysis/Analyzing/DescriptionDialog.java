package com.example.sentimentanalysis.Analyzing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.sentimentanalysis.R;

public class DescriptionDialog {

    Activity activity;
    AlertDialog dialog;

    public DescriptionDialog(Activity myActivity){
        activity = myActivity;
    }

    @SuppressLint("InflateParams")
    public void StartLoadingDialog(String title, String description){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_2, null);
        builder.setView(dialogView);
        builder.setCancelable(true);

        TextView customDialogTitleTextView = dialogView.findViewById(R.id.custom_dialog_title_text_view);
        customDialogTitleTextView.setText(title);

        TextView customDialogDescriptionTextView = dialogView.findViewById(R.id.custom_dialog_description_text_view);
        customDialogDescriptionTextView.setText(description);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
