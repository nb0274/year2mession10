package com.example.year2mession10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Credits extends AppCompatActivity {
    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        in = getIntent();
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
        else if(id == R.id.Filters)
        {
            in.setClass(this, filterActivity.class);
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }
}