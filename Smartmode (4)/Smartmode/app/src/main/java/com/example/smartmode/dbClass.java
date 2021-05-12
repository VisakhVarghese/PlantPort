/*package com.example.smartmode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.jar.Attributes;

public class dbClass extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "smData.db";
    public static final String TABLE_NAME = "TABLEMAIN";
    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "LABEL";
    public static final String COL4 = "BT";
    public static final String COL5 = "WIFI";
    public static final String COL6 = "RING";
    public static final String COL7 = "APMODE";
    public static final String COL8 = "BTNVALUE";
    public static final String COL9 = "BTNENABLE";
    public static final String COL10 = "LOCATIONCODE";


    public dbClass(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,PANAME TEXT,DOB TEXT,GENDER TEXT,PHONE NUMBER," +
                "EMAIL TEXT,PASSWORD TEXT,FINGERPRINTFLAG BOOLEAN,FORGOTQUESTION TEXT,FORGOTANSWER TEXT,DPPATH TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insert_from_basic_registration(String name,String paname,String dob,String gender,String dppath){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL1,"1");
        cv.put(COL2, name);
        contentValues.put(Col_3,paname);
        contentValues.put(Col_4,dob);
        contentValues.put(Col_5,gender);
        contentValues.put(Col_6,"1234567890");
        contentValues.put(Col_7,"something@gmail.com");
        contentValues.put(Col_8,"11aaA@");
        contentValues.put(Col_9,"0");
        contentValues.put(Col_10,"what is tour name");
        contentValues.put(Col_11,"vj");
        contentValues.put(Col_12,dppath);
        long result=db.insert(Table_Name,null,contentValues);
        if (result==-1)
            return false;
        else return true;
    }

    public boolean update_from_basic_registration(String id,String name,String paname,String dob,String gender,String dppath){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Col_2,name);
        contentValues.put(Col_3,paname);
        contentValues.put(Col_4,dob);
        contentValues.put(Col_5,gender);
        contentValues.put(Col_12,dppath);
        db.update(Table_Name,contentValues,"ID=?",new String[]{id});
        return true;
    }



    public boolean update_from_userid_registration(String id,String phone,String email){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Col_6,phone);
        contentValues.put(Col_7,email);
        db.update(Table_Name,contentValues,"ID=?",new String[]{id});
        return true;
    }

    public boolean upadate_from_password_registration(String id,String password,String fingerprintflag){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Col_8,password);
        contentValues.put(Col_9,fingerprintflag);
        db.update(Table_Name,contentValues,"ID=?",new String[]{id});
        return true;
    }

    public boolean update_from_forgotpassword_registration(String id,String question,String answer){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Col_10,question);
        contentValues.put(Col_11,answer);
        db.update(Table_Name,contentValues,"ID=?",new String[]{id});
        return true;
    }

    public Cursor getbasicRegistration(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT NAME,PANAME,DOB,GENDER FROM "+Table_Name,null);
        return result;
    }

    public Cursor getuseridregistration(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT PHONE,EMAIL FROM "+Table_Name,null);
        return result;
    }

    public Cursor getpassword(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT PASSWORD,FINGERPRINTFLAG FROM "+Table_Name,null);
        return result;
    }

    public Cursor get_userid_password(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT PHONE,EMAIL,PASSWORD,FINGERPRINTFLAG FROM "+Table_Name,null);
        return result;
    }
}

 */
