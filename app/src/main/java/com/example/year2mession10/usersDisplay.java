package com.example.year2mession10;

import static com.example.year2mession10.Users.USERS;
import static com.example.year2mession10.Users.USER_FULL_NAME;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class usersDisplay extends AppCompatActivity implements AdapterView.OnItemClickListener {
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor cursor;
    ContentValues cv;
    ListView listViewUsers;
    ArrayAdapter<String> adp;
    ArrayList<String> usersTable;
    Intent in1;
    int selectedStudentId;
    AlertDialog.Builder adb;
    AlertDialog ad;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_display);

        in1 = getIntent();
        context = this;
        listViewUsers = findViewById(R.id.listViewUsers);
        listViewUsers.setOnItemClickListener(this);

        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();
        cv = new ContentValues();
        usersTable = new ArrayList<>();
        adp = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, usersTable);
        listViewUsers.setAdapter(adp);
        readStudentsData();
    }

    /**
     * This function reads the names and ids of the students from the database and saves them in
     * the namesTbl array list.
     */
    public void readStudentsData() {
        String[] columns = {USER_FULL_NAME};

        int temp = 0;

        String user = "";
        usersTable.clear();

        db = hlp.getReadableDatabase();
        cursor = db.query(USERS, columns, null, null, null, null, null);

        temp = cursor.getColumnIndex(USER_FULL_NAME);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            user = cursor.getString(temp);
            usersTable.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        adp.notifyDataSetChanged();
    }

    /**
     * This function shows an alert dialog which asks the user if he wants to show and edit the
     * data of the chosen student.
     *
     * @param chosenStudentIndex The index of the chosen student.
     */
    public void showAlertDialog(int chosenStudentIndex) {
        adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Edit student");
        adb.setMessage("By pressing Edit will be referred to the editing page" + usersTable.get(chosenStudentIndex) + "?");

        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                in1.setClass(context, usersDisplay.class);
                in1.putExtra("StudentId", selectedStudentId);
                startActivity(in1);
            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        ad = adb.create();
        ad.show();
    }

    /**
     * This function is called when the user clicks on a student in the list view.
     * It saves the id of the chosen student and calls the function which shows the alert dialog.
     *
     * @param adapterView The adapter view.
     * @param view        The view.
     * @param i           The index of the chosen student.
     * @param l           The id of the chosen student.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedStudentId = i + 1;
        showAlertDialog(i);
    }
}

