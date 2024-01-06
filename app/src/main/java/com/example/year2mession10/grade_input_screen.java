package com.example.year2mession10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.year2mession10.Grades.GRADE;
import static com.example.year2mession10.Grades.GRADES;
import static com.example.year2mession10.Grades.GRADE_ID;
import static com.example.year2mession10.Grades.QUARTER;
import static com.example.year2mession10.Grades.SUBJECT;
import static com.example.year2mession10.Grades.TYPE;
import static com.example.year2mession10.Grades.USER_ID_FOR_GRADE;
import static com.example.year2mession10.Users.ACTIVE;
import static com.example.year2mession10.Users.USER_ID;
import static com.example.year2mession10.Users.USER_FULL_NAME;
import static com.example.year2mession10.Users.USERS;

public class grade_input_screen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    AlertDialog.Builder adb;
    AlertDialog ad;
    EditText editTextGradeJ;
    EditText editTextSubjectJ;
    EditText editTextTypeJ;
    EditText editTextQuarterJ;
    Intent in1;
    Intent in2;
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor cursor;
    Spinner spinnerUsersJ;
    ArrayAdapter adp;
    ContentValues cv;
    ArrayList<String> usersTable;
    ArrayList<Integer> idListTable;
    int userId;
    boolean flag;
    int gradeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_input_screen);

        in1 = getIntent();
        flag = true;
        spinnerUsersJ = findViewById(R.id.spinnerUsers);
        spinnerUsersJ.setOnItemSelectedListener(this);
        editTextGradeJ = findViewById(R.id.editTextGrade);
        editTextSubjectJ = findViewById(R.id.editTextSubject);
        editTextTypeJ = findViewById(R.id.editTextType);
        editTextQuarterJ = findViewById(R.id.editTextQuarter);

        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        cv = new ContentValues();
        usersTable = new ArrayList<>();
        idListTable = new ArrayList<>();
        adp = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, usersTable);
        spinnerUsersJ.setAdapter(adp);
        readNamesAndIds();
    }

    /**
     * This function is called when the user clicks on the save button.
     * It checks if all the fields are filled and if so, it calls the showAlertDialog function.
     * @param view
     */
    public void saveGrade(View view) {
        if(!((editTextGradeJ.getText().toString().equals("")) || (editTextSubjectJ.getText().toString().equals("")) || (editTextTypeJ.getText().toString().equals("")) || (editTextQuarterJ.getText().toString().equals("")))) {
            showAlertDialog();
        }
        else {
            Toast.makeText(this, "ERROR: There are empty fields", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This function shows an alert dialog that asks the user if he is sure he wants to save the grade.
     */
    public void showAlertDialog() {
        adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Save Grade");
        adb.setMessage("By pressing Save you will save a new grade to the database");

        adb.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cv.clear();
                cv.put(USER_ID_FOR_GRADE, userId);
                cv.put(GRADE, Integer.parseInt(editTextGradeJ.getText().toString()));
                cv.put(Grades.SUBJECT, editTextSubjectJ.getText().toString());
                cv.put(Grades.TYPE, editTextTypeJ.getText().toString());
                cv.put(Grades.QUARTER, Integer.parseInt(editTextQuarterJ.getText().toString()));

                if(flag == true) {
                    db = hlp.getWritableDatabase();
                    db.insert(GRADES, null, cv);
                    db.close();

                    Toast.makeText(grade_input_screen.this, "Grade saves", Toast.LENGTH_SHORT).show();
                }
                else {
                    db = hlp.getWritableDatabase();
                    db.update(GRADES, cv, GRADE_ID+"=?", new String[]{"" + gradeId});
                    db.close();

                    Toast.makeText(grade_input_screen.this, "Grade updated", Toast.LENGTH_SHORT).show();
                    flag = true;
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
    }

    /**
     * This function reads the names and ids of the users from the database and adds them to the spinner.
     */
    public void readNamesAndIds(){
        String[] columns = {USER_ID, USER_FULL_NAME};
        String[] selectionArgs = {"1"};
        int temp1 = 0;
        int temp2 = 0;
        int key = 0;
        String name = "";
        usersTable = new ArrayList<>();
        idListTable = new ArrayList<>();

        db = hlp.getReadableDatabase();
        cursor = db.query(USERS, columns, ACTIVE + "=?", selectionArgs, null, null, null);

        temp1 = cursor.getColumnIndex(USER_ID);
        temp2 = cursor.getColumnIndex(USER_FULL_NAME);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            key = cursor.getInt(temp1);
            name = cursor.getString(temp2);

            idListTable.add(key);
            usersTable.add(name);

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        adp = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, usersTable);
        spinnerUsersJ.setAdapter(adp);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This function fills the fields with the data of the grade that the user wants to edit.
     * @param gradeId
     */
    public void fillFields(int gradeId){
        String[] columns = {GRADE, SUBJECT, TYPE, QUARTER};
        String selection = GRADE_ID + "=?";
        String[] selectionArgs = {"" + gradeId};


        db = hlp.getReadableDatabase();
        cursor = db.query(GRADES, columns, selection, selectionArgs, null, null, null);

        int var1 = cursor.getColumnIndex(GRADE);
        int var2 = cursor.getColumnIndex(SUBJECT);
        int var3 = cursor.getColumnIndex(TYPE);
        int var4 = cursor.getColumnIndex(QUARTER);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            editTextGradeJ.setText(cursor.getString(var1));
            editTextSubjectJ.setText(cursor.getString(var2));
            editTextTypeJ.setText(cursor.getString(var3));
            editTextQuarterJ.setText(cursor.getString(var4));

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    /**
     * This function is called when the activity starts.
     * It checks if the user came from the ShowGradesActivity activity and if so, it fills the fields
     * with the data of the chosen grade.
     */
    protected void onStart() {
        super.onStart();

        in1 = getIntent();
        gradeId = in1.getIntExtra("gid", -1);

        if(gradeId != -1)
        {
            flag = false;
            spinnerUsersJ.setSelection(in1.getIntExtra("index", 0));
            fillFields(gradeId);
        }
    }

    /**
     * This function is called when the user clicks on the back button.
     * It goes back to the ShowGradesActivity activity.
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.UserInput){
            in2.setClass(this, MainActivity.class);
            startActivity(in2);
        }
        else if(id == R.id.UserShow)
        {
            in2.setClass(this, usersDisplay.class);
            startActivity(in2);
        }
        else if(id == R.id.GradeShow)
        {
            in2.setClass(this, gradeDisplay.class);
            startActivity(in2);
        }
        else if(id == R.id.Filters)
        {
            in2.setClass(this, filterActivity.class);
            startActivity(in2);
        }
        else if(id == R.id.Credits)
        {
            in2.setClass(this, Credits.class);
            startActivity(in2);
        }
        else
        {
            spinnerUsersJ.setSelection(0);
            editTextGradeJ.setText("");
            editTextSubjectJ.setText("");
            editTextTypeJ.setText("");
            editTextQuarterJ.setText("");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        userId = idListTable.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}