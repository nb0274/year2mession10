package com.example.year2mession10;

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
import android.widget.Spinner;

import java.util.ArrayList;
import static com.example.year2mession10.Users.ACTIVE;
import static com.example.year2mession10.Users.USER_ID;
import static com.example.year2mession10.Users.USER_FULL_NAME;
import static com.example.year2mession10.Users.USERS;

public class grade_input_screen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Intent in;
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor cursor;
    Spinner SpinnerUsersJ;
    ArrayAdapter adp;
    ArrayList<String> namesTbl;
    ArrayList<Integer> idsTbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_input_screen);

        SpinnerUsersJ = findViewById(R.id.SpinnerUsers);
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();
        readUsersData();
    }

    public void readUsersData(){
        String[] columns = {USER_ID, USER_FULL_NAME};
        String[] selectionArgs = {"1"};
        int col1 = 0;
        int col2 = 0;
        int key = 0;
        String name = "";
        namesTbl = new ArrayList<>();
        idsTbl = new ArrayList<>();

        db = hlp.getReadableDatabase();
        cursor = db.query(USERS, columns, ACTIVE, selectionArgs, null, null, null);

        col1 = cursor.getColumnIndex(USER_ID);
        col2 = cursor.getColumnIndex(USER_FULL_NAME);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            key = cursor.getInt(col1);
            name = cursor.getString(col2);

            idsTbl.add(key);
            namesTbl.add(name);

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        adp = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, namesTbl);
        SpinnerUsersJ.setAdapter(adp);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.User){
            in = new Intent(this, MainActivity.class);
            startActivity(in);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}