package com.step.envocab;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WordDialog {
    public void showDialog(Activity activity, String msg, String translate ){
        //final Dialog dialog = new Dialog(activity,R.style.FullHeightDialog);
        final Dialog dialog = new Dialog(activity);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //dialog.wind
        //dialog.setTitle("About program");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.word_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.word_dialog);
        text.setText(msg);

        TextView trans = (TextView) dialog.findViewById(R.id.translate_dialog);
        trans.setText(translate);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
