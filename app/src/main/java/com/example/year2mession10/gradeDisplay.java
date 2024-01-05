package com.example.year2mession10;

import static com.example.year2mession10.Grades.GRADE;
import static com.example.year2mession10.Grades.GRADES;
import static com.example.year2mession10.Grades.GRADE_ID;
import static com.example.year2mession10.Grades.SUBJECT;
import static com.example.year2mession10.Grades.TYPE;
import static com.example.year2mession10.Grades.USER_ID_FOR_GRADE;
import static com.example.year2mession10.Users.ACTIVE;
import static com.example.year2mession10.Users.USERS;
import static com.example.year2mession10.Users.USER_FULL_NAME;
import static com.example.year2mession10.Users.USER_ID;

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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class gradeDisplay extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnCreateContextMenuListener, AdapterView.OnItemLongClickListener {
    Intent in;
    Cursor cursor;
    Spinner spinnerUsersJ;
    ListView listViewGrades;
    Context context;
    ArrayAdapter<String> spinnerAdp;
    ArrayAdapter<String> listViewAdp;
    ArrayList<String> usersTable, gradeTable;
    ArrayList<Integer> userIdTable, gradeIdTable;
    AlertDialog.Builder adb;
    AlertDialog ad;
    ContentValues cv;
    int userId;
    int index;
    int gradeId;
    SQLiteDatabase db;
    HelperDB hlp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_display);

        in = getIntent();
        spinnerUsersJ = findViewById(R.id.spinnerUsers);
        spinnerUsersJ.setOnItemSelectedListener(this);
        listViewGrades = findViewById(R.id.lvGrades);
        listViewGrades.setOnItemLongClickListener(this);
        listViewGrades.setOnCreateContextMenuListener(this);
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        cv = new ContentValues();
        usersTable = new ArrayList<String>();
        userIdTable = new ArrayList<Integer>();
        gradeIdTable = new ArrayList<Integer>();
        gradeTable = new ArrayList<String>();

        listViewAdp = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, gradeTable);
        listViewGrades.setAdapter(listViewAdp);
        spinnerAdp = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, usersTable);
        spinnerUsersJ.setAdapter(spinnerAdp);

        userDataReader();
        context = this;
    }

    /**
     * This function reads the students ids and names from the table and displays them in the
     * names spinner.
     */
    public void userDataReader() {
        String[] columns = {USER_ID, USER_FULL_NAME};
        String selection = ACTIVE + "=?";
        String[] selectionArgs = {"1"};

        int key = 0;
        String name = "";
        usersTable.clear();
        userIdTable.clear();

        db = hlp.getReadableDatabase();
        cursor = db.query(USERS, columns, selection, selectionArgs, null, null, null);

        int temp1 = cursor.getColumnIndex(USER_ID);
        int temp2 = cursor.getColumnIndex(USER_FULL_NAME);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            key = cursor.getInt(temp1);
            name = cursor.getString(temp2);
            userIdTable.add(key);
            usersTable.add(name);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        spinnerAdp.notifyDataSetChanged();

    }

    /**
     * This function reads the grades data of a given student, and displays them in the grades
     * list view.
     *
     * @param sid The id of the student to display its grades.
     */
    public void gradeDataReader(int sid) {
        String[] columns = {GRADE_ID, GRADE, SUBJECT, TYPE};
        String selection = USER_ID_FOR_GRADE + "=?";
        String[] selectionArgs = {"" + userId};
        int key = 0;
        int grade = 0;

        String subject = "";
        String type = "";
        gradeTable.clear();
        gradeIdTable.clear();

        db = hlp.getReadableDatabase();
        cursor = db.query(GRADES, columns, selection, selectionArgs, null, null, null);

        int var1 = cursor.getColumnIndex(GRADE_ID);
        int var2 = cursor.getColumnIndex(GRADE);
        int var3 = cursor.getColumnIndex(SUBJECT);
        int var4 = cursor.getColumnIndex(TYPE);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            key = cursor.getInt(var1);
            grade = cursor.getInt(var2);
            subject = cursor.getString(var3);
            type = cursor.getString(var4);

            gradeIdTable.add(key);
            gradeTable.add(subject + "(" + type + "), " + grade);

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        listViewAdp.notifyDataSetChanged();
    }

    /**
     * This function creates the context menu of actions to perform with the chosen grade from the
     * list view.
     *
     * @param menu     The menu
     * @param v        The view
     * @param menuInfo The menu info
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Grade Actions");
        menu.add("Edit");
        menu.add("Delete");
    }

    /**
     * This function deletes a grade from the database.
     *
     * @param gid The id of the grade to delete.
     */
    public void deleteGrade(int gid) {
        db = hlp.getWritableDatabase();
        db.delete(GRADES, GRADE_ID + "=?", new String[]{"" + gid});
        db.close();
    }

    /**
     * This function reacts to the choice from the context menu of actions to perform with the
     * chosen grade from the list view.
     *
     * @param item The menu item
     * @return true for the menu to react, false otherwise.
     */
    public boolean onContextItemSelected(MenuItem item) {
        String action = item.getTitle().toString();

        adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Perform action on grade");
        adb.setMessage("Are you sure you want to " + action + " the grade?");

        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (action.equals("Edit")) {
                    in.setClass(context, grade_input_screen.class);
                    in.putExtra("index", index);
                    in.putExtra("gid", gradeId);
                    startActivity(in);
                }
                else {
                    deleteGrade(gradeId);
                    gradeTable.remove(gradeId - 1);
                    listViewAdp.notifyDataSetChanged();
                }
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

        return super.onContextItemSelected(item);
    }

    /**
     * This function is called when the user chooses a student from the spinner.
     * It saves the id of the chosen student and calls the function which reads the grades data.
     *
     * @param adapterView The adapter view of the spinner
     * @param view
     * @param i           The position of the chosen student in the spinner
     * @param l           The row of the chosen student in the spinner
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        userId = userIdTable.get(i);
        index = i;
        gradeDataReader(userId);
    }

    /**
     * This function is called when the user chooses nothing from the spinner.
     *
     * @param adapterView The adapter view of the spinner
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    /**
     * This function creates the options menu of activities to move to.
     *
     * @param menu The menu
     * @return true for the menu to react, false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This function reacts to the choice from the options menu of activities to move to.
     *
     * @param item The menu
     * @return true for the menu to react, false otherwise.
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
        else if(id == R.id.UserShow)
        {
            in.setClass(this, usersDisplay.class);
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This function is called when the user long clicks on a grade from the list view.
     * It saves the id of the chosen grade.
     *
     * @param adapterView The adapter view of the list view
     * @param view
     * @param i           The position of the chosen grade in the list view
     * @param l           The row of the chosen grade in the list view
     * @return false for the menu to react, true otherwise.
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        gradeId = gradeIdTable.get(i);
        return false;
    }
}