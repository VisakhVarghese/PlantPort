package com.example.smartmode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import androidx.annotation.Nullable;

import com.example.smartmode.Model.Button_Status;
import com.example.smartmode.Model.Location;

import java.util.ArrayList;
import java.util.List;

public class database extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "SM_DATA.db";

    public static final String TABLE_USER = "USER_DETAILS";
    public static final String COL_U_NAME = "U_NAME";
    public static final String COL_ST_VALUE = "ST_VALUE";


    public static final String TABLE_HOME = "BUTTON_DETAILS";
    public static final String COL_BTN_ID = "BTN_ID";
    public static final String COL_BTN_CREATE = "BTN_CREATE";
    public static final String COL_BTN_NAME = "BTN_NME";

    //public static final String TABLE_BTNS = "BTNS";
    // public static final String COL_BTN_NO = "BTN_NO";
    // public static final String COL_BTN_NAME = "BTN_NME";


    // public static final String TABLE_STARTUP = "STARTUP";

    //public static final String COL_ST_ID = "ST_ID";


    public static final String TABLE_SETTINGS = "LB_SETTINGS";
    public static final String COL_SETTINGS_ID = "SETTINGS_ID";
    public static final String COL_WIFI = "WIFI";
    public static final String COL_BLU = "BLU";
    public static final String COL_RING = "RING";
    public static final String COL_AIROPLANE = "AIROPLANE";
    public static final String COL_ENABLE = "ENABLE";

    public static final String TABLE_TMP = "TMP";
    //public static final String COL_TPID = "TPID";
    public static final String COL_TVAL = "TVAL";


    public database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
        SQLiteDatabase db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_HOME + "(" + COL_BTN_ID + " INTEGER PRIMARY KEY, " + COL_BTN_CREATE + " INTEGER, " + COL_BTN_NAME + " TEXT)");
        // db.execSQL("create table " + TABLE_STARTUP + "(" + COL_ST_VALUE + " INTEGER, " + COL_ST_ID + " INTEGER)");
        db.execSQL("create table " + TABLE_SETTINGS + "(" + COL_SETTINGS_ID + " INTEGER PRIMARY KEY, " + COL_WIFI + " INTEGER, " + COL_BLU + " INTEGER, " + COL_RING + " INTEGER, " + COL_AIROPLANE + " INTEGER, " + COL_ENABLE + " INTEGER)");
        db.execSQL("create table " + TABLE_USER + "(" + COL_U_NAME + " TEXT, " + COL_ST_VALUE + " INTEGER)");
        // db.execSQL("create table " + TABLE_BTNS + "(" + COL_BTN_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_BTN_NAME + "STRING )");
        db.execSQL("create table " + TABLE_TMP + "(" + COL_TVAL + " INTEGER)");
        //  db.execSQL("INSERT INTO " + TABLE_STARTUP + "(ST_VALUE,ST_ID)" + "VALUES (0,0);");
        //  db.execSQL("INSERT INTO " + TABLE_HOME + "(" + COL_BTN_CREATE + ")" + "VALUES (0),(0),(0),(0),(0),(0)");
        //   db.execSQL("INSERT INTO " + TABLE_BTNS + "(" + COL_BTN_NAME + ")" + "VALUES ('Home'),('Office'),('School');");
    }

    public List<Button_Status> getBtnDetails() {

        SQLiteDatabase database = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"BTN_ID", "BTN_CREATE", "BTN_NME"};

        queryBuilder.setTables(TABLE_HOME);
        Cursor c = queryBuilder.query(database, sqlSelect, null, null, null, null, null);

        final List<Button_Status> result = new ArrayList<>();
        if (c.moveToFirst()) {

            do {
                result.add(new Button_Status(c.getString(c.getColumnIndex("BTN_ID")),
                        c.getString(c.getColumnIndex("BTN_CREATE")),
                        c.getString(c.getColumnIndex("BTN_NME"))
                ));
            } while (c.moveToNext());
        }

        return result;
    }
    public List<Location> getLocDetails() {

        SQLiteDatabase database = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"SETTINGS_ID", "WIFI", "BLU","RING","AIROPLANE","ENABLE"};

        queryBuilder.setTables(TABLE_SETTINGS);
        Cursor c = queryBuilder.query(database, sqlSelect, null, null, null, null, null);

        final List<Location> result = new ArrayList<>();
        if (c.moveToFirst()) {

            do {
                result.add(new Location(c.getString(c.getColumnIndex("SETTINGS_ID")),
                        c.getString(c.getColumnIndex("WIFI")),
                        c.getString(c.getColumnIndex("BLU")),
                        c.getString(c.getColumnIndex("RING")),
                        c.getString(c.getColumnIndex("AIROPLANE")),
                        c.getString(c.getColumnIndex("ENABLE"))
                ));
            } while (c.moveToNext());
        }

        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(("DROP TABLE IF EXISTS " + TABLE_HOME));
        //  db.execSQL(("DROP TABLE IF EXISTS "+TABLE_STARTUP));
        db.execSQL(("DROP TABLE IF EXISTS " + TABLE_SETTINGS));
        db.execSQL(("DROP TABLE IF EXISTS " + TABLE_USER));
        onCreate(db);
    }

    public boolean newUser(String name, boolean flg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_U_NAME, name);
        cv.put(COL_ST_VALUE, flg);
        long res = db.insert(TABLE_USER, null, cv);
        return true;
    }

    public Cursor startData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER, null);
        return cursor;

    }

    public boolean addLoc(int btnCrt, int bid, String btnNme, boolean flg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_BTN_CREATE, btnCrt);
        String ide = String.valueOf(bid);
        cv.put(COL_BTN_NAME, btnNme);
        // cv.put(COL_BTN_NO, btnNo);
        long res = db.update(TABLE_HOME, cv, COL_BTN_ID + " = ?", new String[] { ide });
        if (res == -1)
            return false;
        else
            return true;

    }

    public void addState(String Id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SETTINGS_ID, Id);
        db.insert(TABLE_SETTINGS, null, cv);

    }
   /*  public boolean btnTxt(String btnNme, boolean flag){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
         cv.put(COL_BTN_NAME, btnNme);
         long res = db.update(TABLE_BTNS)
     }

    */

    public boolean btnSet(int btnCrt, boolean flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_BTN_CREATE, btnCrt);
        long res = db.insert(TABLE_HOME, null, cv);
        if (res == -1)
            return false;
        else
            return true;
    }

    public boolean tmp(int valu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TVAL, valu);
        long res = db.insert(TABLE_TMP, null, cv);
        if (res == -1)
            return false;
        else
            return true;
    }

    public Cursor tmpass() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TMP, null);
        return cursor;
    }


    public Cursor btnV() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HOME, null);
        return cursor;

    }
    public Cursor locState() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SETTINGS, null);
        return cursor;

    }

    public Cursor btnDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HOME, null);
        return cursor;
    }

    public void del() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("DELETE FROM " + TABLE_TMP, null);

    }

    public boolean location(int enabl, int bid, boolean flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ENABLE, enabl);
        long res = db.insert(TABLE_SETTINGS, null, cv);
        if (res == -1)
            return false;
        else
            return true;
    }

    public boolean setStatus(boolean isTouched, String state) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ENABLE, isTouched);
        db.update(TABLE_SETTINGS, cv, "SETTINGS_ID = ?", new String[]{state});
        return true;

    }

    public boolean setEnable(int v) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_ENABLE, v);
        cv.put(COL_RING,v);
        cv.put(COL_BLU,v);
        cv.put(COL_WIFI,v);

        db.insert(TABLE_SETTINGS,null,cv);
        return true;
    }

    public boolean setFalse(int v, String state) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ENABLE, v);
        cv.put(COL_RING,v);
        cv.put(COL_BLU,v);
        cv.put(COL_WIFI,v);
        db.update(TABLE_SETTINGS, cv, "SETTINGS_ID = ?", new String[]{state});
        return true;

    }



    public boolean setAir(boolean isTouched, String state) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_WIFI, isTouched);
        db.update(TABLE_SETTINGS, cv, "SETTINGS_ID = ?", new String[]{state});
        return true;
    }

    public boolean setBlu(boolean isTouched, String state) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_BLU, isTouched);
        db.update(TABLE_SETTINGS, cv, "SETTINGS_ID = ?", new String[]{state});
        return true;
    }

    public boolean setRing(boolean isTouched, String state) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_RING, isTouched);
        db.update(TABLE_SETTINGS, cv, "SETTINGS_ID = ?", new String[]{state});
        return true;
    }
}



