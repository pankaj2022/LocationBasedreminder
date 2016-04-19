package com.example.pankaj.projectf;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Pankaj on 4/27/2015.
 */

// This will create the database and the table inside the SQLLite database.
public class SQLLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasks.db";         //database name to be created.
    public static final String TABLE_NAME = "tasks";                // Table name to be created.
    public static final String COLUMN_ID = "_id";                   //column_ID name
    public static final String COLUMN_TASK = "task";                // column task name
    public static final String COLUMN_DESCRIPTION = "description";  // column task description
    public static final String COLUMN_LOCATION = "location";        //column for task location
    public static final String COLUMN_LATITUDE = "latitude";        //column for task latitude
    public static final String COLUMN_LONGITUDE = "longitude";      //column for task longitude

    public static final int VERSION_NUMBER = 1;


    // creating the query for creating the table.
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" +COLUMN_ID+
            " integer primary key autoincrement, "+COLUMN_TASK+" text not null, "+COLUMN_DESCRIPTION+" text not null, "+COLUMN_LOCATION+" text not null, "+
            COLUMN_LATITUDE+" real, "+COLUMN_LONGITUDE+" real );";



    // database is created at this time if it does not exist.
    public SQLLiteHelper(Context context) {
        super(context, DATABASE_NAME , null, VERSION_NUMBER);
    }
    //called when database is created via constructor.
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            // table is created
            db.execSQL(CREATE_TABLE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("drop table if exists "+ TABLE_NAME);
        onCreate(db);
    }
}
