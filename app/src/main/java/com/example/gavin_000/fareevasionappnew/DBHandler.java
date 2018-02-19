package com.example.gavin_000.fareevasionappnew;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.gavin_000.fareevasionappnew.models.Offender;

import java.util.ArrayList;
import java.util.List;
//import java.sql.*;

/**
 * Created by X00119182 on 14/02/2018.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;  //db version
    private static final String DATABASE_NAME = "offenderInfo"; //name of db

    private static final String TABLE_OFFENDER = "offenders"; //table name
    //Column names
    private static final String KEY_ID = "id";
    private static final String KEY_FNAME = "first_name";
    private static final String KEY_LNAME = "last_name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONE = "phone_number";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_STOPNAME = "stop_name";

    DBHandler helper;
    SQLiteDatabase db;

    public DBHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_OFFENDERS_TABLE = "CREATE TABLE " + TABLE_OFFENDER +
                "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_FNAME + " TEXT, "
        + KEY_LNAME + " TEXT, " + KEY_ADDRESS + " TEXT, " + KEY_PHONE + " TEXT, "
        + KEY_EMAIL + " TEXT, " + KEY_STOPNAME + " TEXT" + ") ";
        db.execSQL(CREATE_OFFENDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFENDER);
        onCreate(db);
    }

    //Inserting an offender
    public void addOffender(Offender offender)
    {
        SQLiteDatabase db = this.getWritableDatabase(); //open a db
        ContentValues values = new ContentValues(); // maps offenders to tables
        values.put(KEY_FNAME, offender.getFname());
        values.put(KEY_LNAME, offender.getLname());
        values.put(KEY_ADDRESS, offender.getAddress());
        values.put(KEY_PHONE, offender.getPhoneNo());
        values.put(KEY_EMAIL, offender.getEmail());
        values.put(KEY_STOPNAME, offender.getStopName());

        db.insert(TABLE_OFFENDER,null,values);
        db.close();
    }

    //Read row of db
    public Offender getOffender(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase(); //open read only db
        Cursor cursor = db.query(TABLE_OFFENDER, new String[] { KEY_ID,
                        KEY_FNAME,KEY_LNAME,KEY_ADDRESS,KEY_PHONE,KEY_EMAIL,KEY_STOPNAME }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Offender offenderReadable = new Offender(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),
                cursor.getString(5),cursor.getString(6), cursor.getString(7));
        //return offender
        return offenderReadable;
    }

    // Get all rows
    public List<Offender> getAllOffenders()
    {
        List<Offender> offenderList = new ArrayList<Offender>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_OFFENDER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// loop through all rows and add to the list
        if (cursor.moveToFirst())
        {
            do
            {
                Offender offender = new Offender();
                offender.setId(Integer.parseInt(cursor.getString(0)));
                offender.setFname(cursor.getString(1));
                offender.setLname(cursor.getString(2));
                offender.setAddress(cursor.getString(3));
                offender.setPhoneNo(cursor.getString(4));
                offender.setEmail(cursor.getString(5));
                offender.setStopName(cursor.getString(6));

                //add offender
                offenderList.add(offender);
            }
            while (cursor.moveToNext());
        }
        // return offender list
        return offenderList;
    }

    //Update record
    public int updateOffender(Offender offender)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FNAME, offender.getFname());
        values.put(KEY_LNAME, offender.getLname());
        values.put(KEY_ADDRESS, offender.getAddress());
        values.put(KEY_PHONE, offender.getPhoneNo());
        values.put(KEY_EMAIL, offender.getEmail());
        values.put(KEY_STOPNAME, offender.getStopName());
        // update row
        return db.update(TABLE_OFFENDER, values, KEY_ID + " = ?",
                new String[]{String.valueOf(offender.getId())}); //update row with ID value of x
    }

    //Delete
    public void DeleteOffender(Offender offender)
    {
        SQLiteDatabase db = this.getWritableDatabase(); //open writable db
        db.delete(TABLE_OFFENDER, KEY_ID + " = ?",
                new String[] { String.valueOf(offender.getId()) }); //delete row with ID value of x
        db.close();
    }

    // OPEN THE DB
    public DBHandler openDB()
    {
        try
        {
            db=helper.getWritableDatabase();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;

    }

    //CLOSE THE DB
    public void close()
    {
        helper.close();
    }



}

