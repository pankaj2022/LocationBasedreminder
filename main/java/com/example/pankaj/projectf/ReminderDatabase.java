package com.example.pankaj.projectf;

/**
 * Created by Pankaj on 5/1/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

    // This class will perform all the actual database connection , database creation, database deletion, database queries on the SQLLite database.
public class ReminderDatabase {
    SQLLiteHelper databasehelper;
    private String[] columns = {SQLLiteHelper.COLUMN_ID, SQLLiteHelper.COLUMN_TASK,SQLLiteHelper.COLUMN_DESCRIPTION,SQLLiteHelper.COLUMN_LOCATION,SQLLiteHelper.COLUMN_LATITUDE,SQLLiteHelper.COLUMN_LONGITUDE};

    //pointer to actual database
    private SQLiteDatabase db;

    public ReminderDatabase(Context context) {
        //this will call constructor of SQLitehelper and create the database and then table as well.
        databasehelper = new SQLLiteHelper(context);
    }
    //database connection open.
    public void open() {
        try {
            //returns back the  (sqlliteDatabase db object)
            db = databasehelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //database connection close.
    public void close() {
        try {
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This function will return all rows with all the columns.
    public ArrayList<Reminder> getALLComments() {
        // ArrayList of type Reminder to store the results and then return to the home activity in the form of array list.
        ArrayList<Reminder> allcomments = new ArrayList<Reminder>();
        //cursor is be used for traversing through the result of query , a cursor is similar to iterator.
        // cursor points to first element of table and is used to move through rows which are returned by the query.
        // the following query will give back the result of the query in the form of rows which are handled by the cursor.
        Cursor mycursor = db.query(SQLLiteHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (mycursor != null) {
            mycursor.moveToFirst(); // move to the first row of all the rows returned.
            while (!mycursor.isAfterLast()) {
                Reminder tmp = new Reminder();  // to set data into the reminder class and then add it to array list of type Reminder , this object is created.
                // Setting all the data from the database to the respective variables of reminder class using the cursor.
                tmp.setId(mycursor.getLong(0));
                tmp.setTask(mycursor.getString(1));
                tmp.setDescription(mycursor.getString(2));
                tmp.setLatitude(mycursor.getDouble(mycursor.getColumnIndex(SQLLiteHelper.COLUMN_LATITUDE)));
                tmp.setLongitude_(mycursor.getDouble(mycursor.getColumnIndex(SQLLiteHelper.COLUMN_LONGITUDE)));
                tmp.setLocation_(mycursor.getString(3));
                // all the data stored in the particular instance of the reminder class object is added to the array list of type is REMINDER.
                allcomments.add(tmp);
                mycursor.moveToNext();
            }
            mycursor.close();
        }
        return allcomments;
    }

        // This function will return the row only with specific column_id mentioned in the id.
        public Reminder getComment(Long id) {
        Reminder reminder = new Reminder();
        System.out.println(id);
            // this query will return row with a specific id only.
        Cursor mycursor = db.query(SQLLiteHelper.TABLE_NAME, columns, SQLLiteHelper.COLUMN_ID + "=?" ,new String[]{id.toString()}, null, null, null);
        if (mycursor != null) {
            mycursor.moveToFirst();
            while (!mycursor.isAfterLast()) {
            // all the comments are same as in the above ALLcomments function.
                reminder.setId(mycursor.getLong(0));//return the  row of the first column.
                reminder.setTask(mycursor.getString(1));
                reminder.setDescription(mycursor.getString(2));
                reminder.setLatitude(mycursor.getDouble(mycursor.getColumnIndex(SQLLiteHelper.COLUMN_LATITUDE)));
                reminder.setLongitude_(mycursor.getDouble(mycursor.getColumnIndex(SQLLiteHelper.COLUMN_LONGITUDE)));
                reminder.setLocation_(mycursor.getString(3));
                mycursor.moveToNext();
            }
            mycursor.close();
        }
        return reminder; // returning a specific row only.so only a single object of Reminder is returned at a time.
    }

        //method to delete the row based on the column_id from the database.
    public void delete(Reminder reminder) {
        try {
            // This query will perform the delete operation.
            db.delete(SQLLiteHelper.TABLE_NAME, SQLLiteHelper.COLUMN_ID + "=" + reminder.getId(), null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This will insert the values in the database as entered by the user in the addreminderactivity class.
    public Reminder insert(String Task_,String Description_, String Location_Address_,Double Latitude_,Double Longitude_) {
        Reminder newReminder = null;
        try { //ContentValues are used to enter the values in the database column wise.
            ContentValues values = new ContentValues();
            //key will be column name and content value will be actual value of the column.
            //content value is object of an class which can create a key value pair which  can be inserted into database.
            values.put(SQLLiteHelper.COLUMN_TASK, Task_);
            values.put(SQLLiteHelper.COLUMN_DESCRIPTION, Description_);
            values.put(SQLLiteHelper.COLUMN_LOCATION, Location_Address_);
            values.put(SQLLiteHelper.COLUMN_LATITUDE, Latitude_);
            values.put(SQLLiteHelper.COLUMN_LONGITUDE, Longitude_);
            //A unique id  is returned for a particular row where the data is inserted.keep tracks of a row and is used for deletion and tracking purpose.
            long insertid = db.insert(SQLLiteHelper.TABLE_NAME, null, values);

           // Now , the values inserted into the database are stored in the reminder class object for reference.
            newReminder = new Reminder();
            newReminder.setId(insertid);
            newReminder.setTask(Task_);
            newReminder.setLatitude(Latitude_);
            newReminder.setLocation_(Location_Address_);
            newReminder.setLongitude_(Longitude_);
            newReminder.setDescription(Description_);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return newReminder;
    }
}

