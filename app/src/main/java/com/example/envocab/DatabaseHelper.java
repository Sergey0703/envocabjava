package com.example.envocab;



import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG="MainActivity";
//    private static DatabaseHelper sInstance;
//    public static DatabaseHelper getInstance(Context context,String name, int version) {
//
//        // Use the application context, which will ensure that you
//        // don't accidentally leak an Activity's context.
//        // See this article for more information: http://bit.ly/6LRzfx
//        if (sInstance == null) {
//            sInstance = new DatabaseHelper(context.getApplicationContext(),name, version);
//        }
//        return sInstance;
//    }
    private static String DB_PATH; // полный путь к базе данных
    private static String DB_NAME = "dictdb";
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "dbwords"; // название таблицы в бд
//    String dbName;
//    String dbPath;
    Context context;

//    public DatabaseHelper(Context mcontext, String name, int version) {
//        super(mcontext, name, null, version);
//        this.context=mcontext;
//        dbName="dictdb";
//        dbPath="/data/data/"+context.getPackageName()+"/databases/";
//    }
    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.context=context;
        //DB_PATH =context.getFilesDir().getPath() + DB_NAME;
        DB_PATH="/data/data/"+context.getPackageName()+"/databases/" + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public void CheckDatabase(){
//        try{
//            String path=dbPath+dbName;
//            SQLiteDatabase.openDatabase(path, null,0);
//        }catch(Exception e){
//             e.printStackTrace();
//        }
//        this.getReadableDatabase();
//        CopyDatabase();
//    }
//
//    public void CopyDatabase(){
//        try{
//            InputStream io=context.getAssets().open(dbName);
//            String outfilename=dbPath+dbName;
//            OutputStream outputStream=new FileOutputStream(outfilename);
//            int length;
//            byte[] buffer=new byte[1024];
//            while ((length=io.read(buffer))>0){
//                 outputStream.write(buffer,length,0);
//            }
//            outputStream.flush();
//            io.close();
//
//            outputStream.close();
//        }catch (Exception e){
//             e.printStackTrace();
//        }
//        Log.d(TAG,"Database copied");
//    }

    void create_db(){

        File file = new File(DB_PATH);
        if (!file.exists()) {
            //получаем локальную бд как поток
            try(InputStream myInput = context.getAssets().open(DB_NAME);
                // Открываем пустую бд
                OutputStream myOutput = new FileOutputStream(DB_PATH)) {

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
            }
            catch(IOException ex){
                Log.d("DatabaseHelper", ex.getMessage());
            }
        }
    }
//    public void OpenDatabase(){
//        String path=dbPath+dbName;
//        SQLiteDatabase.openDatabase(path,null,0);
//    }
    public SQLiteDatabase open()throws SQLException {

        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}
