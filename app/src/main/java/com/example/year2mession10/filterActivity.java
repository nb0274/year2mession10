package com.example.year2mession10;

import static com.example.year2mession10.Grades.GRADE;
import static com.example.year2mession10.Grades.GRADES;
import static com.example.year2mession10.Grades.GRADE_ID;
import static com.example.year2mession10.Grades.SUBJECT;
import static com.example.year2mession10.Grades.TYPE;
import static com.example.year2mession10.Grades.USER_ID_FOR_GRADE;
import static com.example.year2mession10.Users.USERS;
import static com.example.year2mession10.Users.USER_FULL_NAME;
import static com.example.year2mession10.Users.USER_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Spinner;

import java.util.ArrayList;


public class filterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Intent in;
    SQLiteDatabase db;
    HelperDB hlp;
    Spinner spinnerChoicesJ;
    ListView listViewGradesJ;
    Cursor cursor;
    int choice = 0;
    String userName;
    String[] choices = {"Descending", "Ascending"};
    ArrayList<String> usersTable, gradeTable;
    ArrayList<Integer> userIdTable;
    ArrayAdapter<String> spinnerAdp;
    ArrayAdapter<String> listViewAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        in = getIntent();
        spinnerChoicesJ = findViewById(R.id.spinnerChoices);
        spinnerChoicesJ.setOnItemSelectedListener(this);

        listViewGradesJ = findViewById(R.id.listViewGradess);

        usersTable = new ArrayList<String>();
        gradeTable = new ArrayList<String>();
        userIdTable = new ArrayList<Integer>();

        listViewAdp = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, gradeTable);
        listViewGradesJ.setAdapter(listViewAdp);
        spinnerAdp = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, choices);
        spinnerChoicesJ.setAdapter(spinnerAdp);

        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();
    }

    /**
     * This function handles the item selected event.
     *
     * @param adapterView the adapter view.
     * @param view        the view.
     * @param i           the index.
     * @param l           the long.
     */
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        choice = i;
        dataReader();
        if (choice == 0) {
            gradeDataReader(" DESC");
        } else {
            gradeDataReader(" ASC");
        }
    }

    /**
     * This function handles the item selected event.
     *
     * @param adapterView the adapter view.
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * This function reads the grades from the database and adds them to the list view.
     *
     * @param str the order by string.
     */
    public void gradeDataReader(String str) {
        String[] columns = {GRADE, SUBJECT, TYPE, USER_ID_FOR_GRADE};
        int grade = 0;
        String orderBy = GRADE + str;

        String subject = "";
        String type = "";
        int id = 0;
        gradeTable.clear();

        db = hlp.getReadableDatabase();
        cursor = db.query(GRADES, columns, null, null, null, null, orderBy);

        int var1 = cursor.getColumnIndex(GRADE);
        int var2 = cursor.getColumnIndex(SUBJECT);
        int var3 = cursor.getColumnIndex(TYPE);
        int var4 = cursor.getColumnIndex(USER_ID_FOR_GRADE);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            grade = cursor.getInt(var1);
            subject = cursor.getString(var2);
            type = cursor.getString(var3);
            id = cursor.getInt(var4);
            userName = usersTable.get(userIdTable.indexOf(id));
            gradeTable.add(subject + "(" + type + "), " + grade + ", " + userName);

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        listViewAdp.notifyDataSetChanged();
    }

    /**
     * This function reads the names of the users from the database and adds them to the list view.
     */
    public void dataReader() {
        String[] columns = {USER_FULL_NAME, USER_ID};

        int temp1 = 0;
        int temp2 = 0;
        String user = "";
        int id = 0;
        usersTable.clear();

        db = hlp.getReadableDatabase();
        cursor = db.query(USERS, columns, null, null, null, null, null);

        temp1 = cursor.getColumnIndex(USER_FULL_NAME);
        temp2 = cursor.getColumnIndex(USER_ID);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            user = cursor.getString(temp1);
            id = cursor.getInt(temp2);
            usersTable.add(user);
            userIdTable.add(id);
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
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
     * This function handles the menu items.
     *
     * @param item the item that was selected.
     * @return must return true for the menu to be shown.
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
        else if(id == R.id.UserShow)
        {
            in.setClass(this, usersDisplay.class);
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