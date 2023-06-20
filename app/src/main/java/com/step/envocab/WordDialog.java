package com.step.envocab;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WordDialog {
    EditText trans;
    Button btnDialog,btnClose, btnSave, btnCancel;
    Dialog dialog;

    public void showDialog(Activity activity, int width, int height, String msg, String translate,String transcription ){
        //final Dialog dialog = new Dialog(activity,R.style.FullHeightDialog);
        dialog = new Dialog(activity);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //dialog.wind
        //dialog.setTitle("About program");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.word_dialog);


        TextView text = (TextView) dialog.findViewById(R.id.word_dialog);
        text.setText(msg);

        EditText transcript = (EditText) dialog.findViewById(R.id.transcription_dialog);
        transcript.setText(transcription);

        trans = (EditText) dialog.findViewById(R.id.translate_dialog);
        trans.setText(translate);

        btnDialog = (Button) dialog.findViewById(R.id.btn_dialog);
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnClose = (Button) dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(width, height);
        //dialog.getWindow().setAttributes(lp);
         trans.addTextChangedListener(new TextWatcher() {

             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {

             }

             @Override
             public void afterTextChanged(Editable s) {
                  saveChanges();
             }
         });

        transcript.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                saveChanges();
            }
        });

    }

    public void saveChanges(){
        btnDialog= (Button) dialog.findViewById(R.id.btn_dialog);
        btnDialog.setVisibility(View.GONE);
        btnSave= (Button) dialog.findViewById(R.id.btn_save);
        btnCancel= (Button) dialog.findViewById(R.id.btn_cancel);
        btnSave.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
    }

   // trans.addTextChangedListener(new void TextWatcher() {

   // }
}
