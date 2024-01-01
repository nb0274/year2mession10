package com.example.year2mession10;


import static com.example.year2mession10.Grades.GRADES;
import static com.example.year2mession10.Grades.GRADE_ID;
import static com.example.year2mession10.Grades.GRADE;
import static com.example.year2mession10.Grades.SUBJECT;
import static com.example.year2mession10.Grades.TYPE;
import static com.example.year2mession10.Grades.QUARTER;

import static com.example.year2mession10.Users.USERS;
import static com.example.year2mession10.Users.USER_ID;
import static com.example.year2mession10.Users.USER_FULL_NAME;
import static com.example.year2mession10.Users.ACTIVE;
import static com.example.year2mession10.Users.HOME_ADDRESS;
import static com.example.year2mession10.Users.USER_PHONE_NUMBER;
import static com.example.year2mession10.Users.HOME_PHONE_NUMBER;
import static com.example.year2mession10.Users.DAD_PHONE_NUMBER;
import static com.example.year2mession10.Users.MOM_PHONE_NUMBER;
import static com.example.year2mession10.Users.DAD_FULL_NAME;
import static com.example.year2mession10.Users.MOM_FULL_NAME;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    String strCreate, strDelete;
    public HelperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

        strCreate = "CREATE TABLE "+USERS;
        strCreate += " ("+USER_ID+" INTEGER PRIMARY KEY,";
        strCreate += " "+USER_FULL_NAME+" TEXT,";
        strCreate += " "+ACTIVE+" INTEGER,";
        strCreate += " "+HOME_ADDRESS+" TEXT,";
        strCreate += " "+USER_PHONE_NUMBER+" TEXT,";
        strCreate += " "+HOME_PHONE_NUMBER+" TEXT,";
        strCreate += " "+DAD_FULL_NAME+" TEXT,";
        strCreate += " "+DAD_PHONE_NUMBER+" TEXT,";
        strCreate += " "+MOM_FULL_NAME+" TEXT,";
        strCreate += " "+MOM_PHONE_NUMBER+" TEXT";
        strCreate += ");";
        db.execSQL(strCreate);

        strCreate = "CREATE TABLE "+GRADES;
        strCreate += " ("+GRADE_ID+" INTEGER,";
        strCreate += " "+GRADE+" REAL,";
        strCreate += " "+SUBJECT+" TEXT,";
        strCreate += " "+TYPE+" TEXT,";
        strCreate += " "+QUARTER+" INTEGER";
        strCreate += ");";
        db.execSQL(strCreate);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {

        strDelete="DROP TABLE IF EXISTS "+USERS;
        db.execSQL(strDelete);
        strDelete="DROP TABLE IF EXISTS "+GRADES;
        db.execSQL(strDelete);

        onCreate(db);
    }

}