package com.step.envocab;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GroupDialogEdit extends Dialog {
    private GroupWordsRosterInterface groupWordsRosterInterface;

    EditText trans,nameGroup;
    //SwitchCompat train1Switch;
    Button btnDialog,btnClose, btnSave, btnCancel;
    Dialog dialogGroup;

    TextView titleWindow;

    Handler handler;
    Context context;
    //Boolean marked;

    //public WordDialog(@NonNull Context context) {
    public GroupDialogEdit(Context context, GroupWordsRosterInterface groupWordsRosterInterface) {
        super(context);
        this.context=context;
        this.groupWordsRosterInterface = groupWordsRosterInterface;
    }


    public void showDialog(Activity activity, int width, int height, String theme, String inform, String id, String msg, String description ){
        //final Dialog dialog = new Dialog(activity,R.style.FullHeightDialog);
        dialogGroup = new Dialog(activity);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //dialog.wind
        //dialog.setTitle("About program");
        dialogGroup.setCancelable(true);
        dialogGroup.setContentView(R.layout.group_dialog);

        titleWindow=dialogGroup.findViewById(R.id.group_title);
        titleWindow.setText(inform);

        nameGroup = (EditText) dialogGroup.findViewById(R.id.group_name);
        nameGroup.setText(msg);

        EditText descript = (EditText) dialogGroup.findViewById(R.id.description_dialog);
        descript.setText(description);



//        train1Switch = dialog.findViewById(R.id.switch_marked);
//        Log.d("word", String.valueOf(train1));
//        if(train1!=null && train1==true) train1=true;
//        else train1=false;
//        train1Switch.setChecked(train1);
//        Log.d("word2", String.valueOf(train1));



        btnDialog = (Button) dialogGroup.findViewById(R.id.btn_dialog);
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGroup.dismiss();
            }
        });

        btnClose = (Button) dialogGroup.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGroup.dismiss();
            }
        });

        btnCancel = (Button) dialogGroup.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGroup.dismiss();
            }
        });

        btnSave = (Button) dialogGroup.findViewById(R.id.btn_save);

        if(theme.equals("light")){
            titleWindow.setBackgroundColor(Color.parseColor("#3A5BAE"));
            btnClose.setBackgroundColor(Color.parseColor("#3A5BAE"));
            btnDialog.setBackgroundColor(Color.parseColor("#3A5BAE"));
            btnSave.setBackgroundColor(Color.parseColor("#3A5BAE"));
        }else{
            titleWindow.setBackgroundColor(Color.parseColor("#183c18"));
            titleWindow.setTextColor(Color.parseColor("#ffffff"));
            btnClose.setBackgroundColor(Color.parseColor("#183c18"));
            btnDialog.setBackgroundColor(Color.parseColor("#183c18"));
            btnSave.setBackgroundColor(Color.parseColor("#183c18"));
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //dialog.dismiss();
                String strGroup=String.valueOf(nameGroup.getText()).trim();
                if(strGroup.equals("")){
                    nameGroup.setError("An empty Group cannot be stored");
                    return;
                }

                //String strDescription=String.valueOf(descript.getText()).trim();
                String strDescription="";
//                String strTranscripton=String.valueOf(transcript.getText()).trim();
//                if(train1Switch.isChecked()) {
//                    marked = true;
//                }else {
//                    marked = false;
//                }
          //      Log.d("Dialog",strDescription+" id="+id);

                if (groupWordsRosterInterface != null) {
                    Log.d("D","Send");
                    groupWordsRosterInterface.sendGroup(id,strGroup);
                }
                dialogGroup.dismiss();

//                handler = new Handler();
//                if (handler == null) return;
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//                        dialog.dismiss();
//                   }
//                }, 100);

            }
        });

        dialogGroup.show();
        dialogGroup.getWindow().setLayout(width, height);
        //dialog.getWindow().setAttributes(lp);
        nameGroup.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void afterTextChanged(Editable s) {
                      fixChanges();
            }
        });


        descript.addTextChangedListener(new TextWatcher() {

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
//        train1Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                fixChanges();
//                if(train1Switch.isChecked()){
//                   // train1Switch.setThumbTintMode();
//                }
//            }
//        });

    }

    public void fixChanges(){
        btnDialog= (Button) dialogGroup.findViewById(R.id.btn_dialog);
        btnDialog.setVisibility(View.GONE);
        btnSave= (Button) dialogGroup.findViewById(R.id.btn_save);
        btnCancel= (Button) dialogGroup.findViewById(R.id.btn_cancel);
        btnSave.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
    }



    // trans.addTextChangedListener(new void TextWatcher() {

   // }
}
