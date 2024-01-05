package com.example.year2mession10;

import static com.example.year2mession10.Users.ACTIVE;
import static com.example.year2mession10.Users.DAD_FULL_NAME;
import static com.example.year2mession10.Users.DAD_PHONE_NUMBER;
import static com.example.year2mession10.Users.HOME_ADDRESS;
import static com.example.year2mession10.Users.HOME_PHONE_NUMBER;
import static com.example.year2mession10.Users.MOM_FULL_NAME;
import static com.example.year2mession10.Users.MOM_PHONE_NUMBER;
import static com.example.year2mession10.Users.USERS;
import static com.example.year2mession10.Users.USER_FULL_NAME;
import static com.example.year2mession10.Users.USER_ID;
import static com.example.year2mession10.Users.USER_PHONE_NUMBER;

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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Intent in1;
    Intent in2;
    int userId;
    Cursor cursor;
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
    Switch isActiveJ;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flag = true;
        in2 = new Intent();
        editTextNameJ = findViewById(R.id.editTextName);
        editTextAddressJ = findViewById(R.id.editTextAddress);
        editTextPhoneNumberJ = findViewById(R.id.editTextPhoneNumber);
        editTextHomePhoneNumberJ = findViewById(R.id.editTextHomePhoneNumber);
        editTextDadNameJ = findViewById(R.id.editTextDadName);
        editTextDadPhoneNumberJ = findViewById(R.id.editTextDadPhoneNumber);
        editTextMomNameJ = findViewById(R.id.editTextMomName);
        editTextMomPhoneNumberJ = findViewById(R.id.editTextMomPhoneNumber);
        isActiveJ = findViewById(R.id.isActive);

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
        adb.setMessage("By pressing Save you will save a new user to the database");

        adb.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cv.clear();

                if(isActiveJ.isChecked())
                {
                    cv.put(ACTIVE, 1);
                }
                else
                {
                    cv.put(ACTIVE, 0);
                }
                cv.put(USER_FULL_NAME, editTextNameJ.getText().toString());
                cv.put(HOME_ADDRESS, editTextAddressJ.getText().toString());
                cv.put(USER_PHONE_NUMBER, editTextPhoneNumberJ.getText().toString());
                cv.put(HOME_PHONE_NUMBER, editTextHomePhoneNumberJ.getText().toString());
                cv.put(DAD_FULL_NAME, editTextDadNameJ.getText().toString());
                cv.put(DAD_PHONE_NUMBER, editTextDadPhoneNumberJ.getText().toString());
                cv.put(MOM_FULL_NAME, editTextMomNameJ.getText().toString());
                cv.put(MOM_PHONE_NUMBER, editTextMomPhoneNumberJ.getText().toString());

                if(flag == true)
                {
                    db = hlp.getWritableDatabase();
                    db.insert(USERS, null, cv);
                    db.close();
                    Toast.makeText(MainActivity.this, "User saved", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    db = hlp.getWritableDatabase();
                    db.update(USERS, cv, USER_ID+"=?", new String[]{"" + userId});
                    db.close();

                    Toast.makeText(MainActivity.this, "User updated", Toast.LENGTH_SHORT).show();
                    flag = true;
                }
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

    public void fillFields(int userId){
        String[] columns = {USER_FULL_NAME, ACTIVE, HOME_ADDRESS, USER_PHONE_NUMBER, HOME_PHONE_NUMBER, DAD_FULL_NAME, DAD_PHONE_NUMBER, MOM_FULL_NAME, MOM_PHONE_NUMBER};
        String selection = USER_ID + "=?";
        String[] selectionArgs = {"" + userId};

        int var1 = 0;
        int var2 = 0;
        int var3 = 0;
        int var4 = 0;
        int var5 = 0;
        int var6 = 0;
        int var7 = 0;
        int var8 = 0;
        int var9 = 0;
        int active = 0;

        db = hlp.getReadableDatabase();
        cursor = db.query(USERS, columns, selection, selectionArgs, null, null, null);

        var1 = cursor.getColumnIndex(USER_FULL_NAME);
        var2 = cursor.getColumnIndex(ACTIVE);
        var3 = cursor.getColumnIndex(HOME_ADDRESS);
        var4 = cursor.getColumnIndex(USER_PHONE_NUMBER);
        var5 = cursor.getColumnIndex(HOME_PHONE_NUMBER);
        var6 = cursor.getColumnIndex(DAD_FULL_NAME);
        var7 = cursor.getColumnIndex(DAD_PHONE_NUMBER);
        var8 = cursor.getColumnIndex(MOM_FULL_NAME);
        var9 = cursor.getColumnIndex(MOM_PHONE_NUMBER);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            editTextNameJ.setText(cursor.getString(var1));
            active = cursor.getInt(var2);
            editTextAddressJ.setText(cursor.getString(var3));
            editTextPhoneNumberJ.setText(cursor.getString(var4));
            editTextHomePhoneNumberJ.setText(cursor.getString(var5));
            editTextDadNameJ.setText(cursor.getString(var6));
            editTextDadPhoneNumberJ.setText(cursor.getString(var7));
            editTextMomNameJ.setText(cursor.getString(var8));
            editTextMomPhoneNumberJ.setText(cursor.getString(var9));

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        isActiveJ.setChecked(active == 1);
    }

    protected void onStart() {
        super.onStart();

        in1 = getIntent();
        userId = in1.getIntExtra("StudentId", -1);

        if(userId != -1)
        {
            flag = false;
            fillFields(userId);
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.Grade){
            in2.setClass(this, grade_input_screen.class);
            startActivity(in2);
        }
        else if(id == R.id.UserShow)
        {
            in2.setClass(this, usersDisplay.class);
            startActivity(in2);
        }
/*        else if(id == R.id.GradeShow)
        {
            in2.setClass(this, ShowGradesActivity.class);
            startActivity(in2);
        }
        else if(id == R.id.Filters)
        {
            in2.setClass(this, ShowGradesActivity.class);
            startActivity(in2);
        }
        else if(id == R.id.Credits)
        {
            in2.setClass(this, ShowGradesActivity.class);
            startActivity(in2);
        }*/
        else
        {
            isActiveJ.setChecked(true);
            editTextNameJ.setText("");
            editTextAddressJ.setText("");
            editTextPhoneNumberJ.setText("");
            editTextHomePhoneNumberJ.setText("");
            editTextDadNameJ.setText("");
            editTextDadPhoneNumberJ.setText("");
            editTextMomNameJ.setText("");
            editTextMomPhoneNumberJ.setText("");
        }

        return super.onOptionsItemSelected(item);
    }
}