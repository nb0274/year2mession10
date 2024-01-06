package com.example.year2mession10;

import static com.example.year2mession10.Users.USERS;
import static com.example.year2mession10.Users.USER_FULL_NAME;

import androidx.annotation.NonNull;
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
import android.view.Menu;
import android.view.MenuItem;
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
    Intent in;
    int selectedStudentId;
    AlertDialog.Builder adb;
    AlertDialog ad;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_display);

        in = getIntent();
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
        dataReader();
    }

    /**
     * This function reads the names of the users from the database and adds them to the list view.
     */
    public void dataReader() {
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
     * This function shows an alert dialog which asks the user if he wants to edit the chosen student.
     * If the user presses yes, he will be referred to the editing page.
     * If the user presses cancel, the alert dialog will be closed.
     *
     * @param index The index of the chosen student.
     */
    public void showAlertDialog(int index) {
        adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Edit student");
        adb.setMessage("By pressing Edit will be referred to the editing page" + usersTable.get(index) + "?");

        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                in.setClass(context, MainActivity.class);
                in.putExtra("sid", selectedStudentId);
                startActivity(in);
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

    /**
     * This function creates the options menu.
     *
     * @param menu the menu to create.
     * @return must return true for the menu to be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This function is called when the user clicks on the back button.
     * It refers the user to the main menu.
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.UserInput){
            in.setClass(this, MainActivity.class);
            startActivity(in);
        }
        else if(id == R.id.GradeInput){
            in.setClass(this, grade_input_screen.class);
            startActivity(in);
        }
        else if(id == R.id.GradeShow)
        {
            in.setClass(this, gradeDisplay.class);
            startActivity(in);
        }
        else if(id == R.id.Filters)
        {
            in.setClass(this, filterActivity.class);
            startActivity(in);
        }
        else if(id == R.id.Credits)
        {
            in.setClass(this, Credits.class);
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }

}



