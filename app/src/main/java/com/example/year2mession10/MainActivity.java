package com.example.year2mession10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Intent in;
    SQLiteDatabase db;
    HelperDB hlp;
    EditText editTextNameJ;
    EditText editTextAddressJ;
    EditText editTextPhoneNumberJ;
    EditText editTextHomePhoneNumberJ;
    EditText editTextDadNameJ;
    EditText editTextDadPhoneNumberJ;
    EditText editTextMomNameJ;
    EditText editTextMomPhoneNumberJ;
    AlertDialog.Builder adb;
    AlertDialog ad;
    ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNameJ = findViewById(R.id.editTextName);
        editTextAddressJ = findViewById(R.id.editTextAddress);
        editTextPhoneNumberJ = findViewById(R.id.editTextPhoneNumber);
        editTextHomePhoneNumberJ = findViewById(R.id.editTextHomePhoneNumber);
        editTextDadNameJ = findViewById(R.id.editTextDadName);
        editTextDadPhoneNumberJ = findViewById(R.id.editTextDadPhoneNumber);
        editTextMomNameJ = findViewById(R.id.editTextMomName);
        editTextMomPhoneNumberJ = findViewById(R.id.editTextMomPhoneNumber);

        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();
        cv = new ContentValues();
    }
    public void saveStudent(View view) {
        showAlertDialog();
    }

    public void showAlertDialog() {
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Are you sure?");
        adb.setMessage("By pressing Yes you will save a new user to the database");

        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cv.clear();

                cv.put(Users.ACTIVE, 1);
                cv.put(Users.USER_FULL_NAME, editTextNameJ.getText().toString());
                cv.put(Users.HOME_ADDRESS, editTextAddressJ.getText().toString());
                cv.put(Users.USER_PHONE_NUMBER, editTextPhoneNumberJ.getText().toString());
                cv.put(Users.HOME_PHONE_NUMBER, editTextHomePhoneNumberJ.getText().toString());
                cv.put(Users.DAD_FULL_NAME, editTextDadNameJ.getText().toString());
                cv.put(Users.DAD_PHONE_NUMBER, editTextDadPhoneNumberJ.getText().toString());
                cv.put(Users.MOM_FULL_NAME, editTextMomNameJ.getText().toString());
                cv.put(Users.MOM_PHONE_NUMBER, editTextMomPhoneNumberJ.getText().toString());

                db = hlp.getWritableDatabase();
                db.insert(Users.USERS, null, cv);
                db.close();

                Toast.makeText(MainActivity.this, "User saved", Toast.LENGTH_SHORT).show();
            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        ad = adb.create();
        ad.show();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.Grade){
            in = new Intent(this, grade_input_screen.class);
            startActivity(in);
        }

        return super.onOptionsItemSelected(item);
    }
}