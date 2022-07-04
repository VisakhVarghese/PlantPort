package com.example.plantport.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.plantport.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private final static String DB_NAME = "cartNew.db";
    private final static int DB_VERSION = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public List<Order>getCarts(){

        SQLiteDatabase database = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String [] sqlSelect = {"OwnerId","UserID","MenuId","PlantID","PlantName","Quantity","Price"};
        String dbTable = "OrderDetails";

        queryBuilder.setTables(dbTable);
        Cursor c = queryBuilder.query(database,sqlSelect,null,null,null,null,null);

        final  List<Order> result = new ArrayList<>();
        if (c.moveToFirst()){

            do{
                result.add(new Order(c.getString(c.getColumnIndex("OwnerId")),
                        c.getString(c.getColumnIndex("UserID")),
                        c.getString(c.getColumnIndex("MenuId")),
                        c.getString(c.getColumnIndex("PlantID")),
                        c.getString(c.getColumnIndex("PlantName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price"))
                ));
            }while (c.moveToNext());
        }

        return result;
    }

    public void addToCart(Order order){


        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetails (OwnerId,UserId,MenuId,PlantId,PlantName,Quantity,Price) VALUES('%s','%s','%s','%s','%s','%s','%s');",
                order.getOwnerId(),
                order.getUserId(),
                order.getMenuId(),
                order.getPlantId(),
                order.getPlantName(),
                order.getQuantity(),
                order.getPrice());

        db.execSQL(query);

    }
    public void clearCart(String userId){

        SQLiteDatabase database = getReadableDatabase();
//        String query = String.format("DELETE FROM OrderDetails WHERE UserId="+userId+"");
        String query = String.format("DELETE FROM OrderDetails WHERE UserId='"+userId+"'");
//        String deleteQuery = "DELETE FROM "+TABLE_LO_SMSDATA+" WHERE _id="+id+"";
        database.execSQL(query);

    }
    public void cart(String PlantId){

        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetails WHERE PlantId='"+PlantId+"'");
        database.execSQL(query);

    }

    public boolean setUpdate(int number, String position) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Quantity",number);
        db.update("OrderDetails", cv, "ID = ?", new String[]{String.valueOf(position)});
        return true;
    }
}
