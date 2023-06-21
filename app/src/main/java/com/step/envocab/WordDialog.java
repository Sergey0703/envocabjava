package com.step.envocab;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class WordDialog extends Dialog {
    private WordRosterInterface wordRosterInterface;
    EditText trans,nameWord;
    Button btnDialog,btnClose, btnSave, btnCancel;
    Dialog dialog;

    Handler handler;
    Context context;

    //public WordDialog(@NonNull Context context) {
    public WordDialog(Context context, WordRosterInterface wordRosterInterface) {
        super(context);
        this.context=context;
        this.wordRosterInterface = wordRosterInterface;
    }


    public void showDialog(Activity activity, int width, int height, String id, String msg, String translate,String transcription ){
        //final Dialog dialog = new Dialog(activity,R.style.FullHeightDialog);
        dialog = new Dialog(activity);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //dialog.wind
        //dialog.setTitle("About program");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.word_dialog);


        nameWord = (EditText) dialog.findViewById(R.id.word_dialog);
        nameWord.setText(msg);

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

        btnSave = (Button) dialog.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dialog.dismiss();
                String strWord=String.valueOf(nameWord.getText());
                String strTranslate=String.valueOf(trans.getText());
                String strTranscripton=String.valueOf(transcript.getText());
                Log.d("Dialog",strTranslate+" id="+id);

                if (wordRosterInterface != null) {
                    Log.d("D","Send");
                    wordRosterInterface.sendData(id,strTranslate);
                }
                handler = new Handler();
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        int idup = AppDatabase.getInstance(activity.getApplicationContext())
//                                .wordDao()
//                                .upWord(Integer.parseInt(id),strTranslate);
//
//                        Log.d("Dialog", String.valueOf(idup));
//                    }
//                });
//                thread.start();
                if (handler == null) return;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                   }
                }, 100);

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
                  fixChanges();
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
                fixChanges();
            }
        });

    }

    public void fixChanges(){
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
