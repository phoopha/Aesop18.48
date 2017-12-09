package com.example.emergencyphonenumber;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.emergencyphonenumber.db.LoginDbHelper;

public class Register extends AppCompatActivity {
    LoginDbHelper lg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


    }

    public void click(View  view){

        EditText eUser = findViewById(R.id.edit_your_user);
        EditText ePass = findViewById(R.id.edit_your_pass);
        String sUser = eUser.getText().toString();
        String sPass = ePass.getText().toString();
        lg = new LoginDbHelper(this);

        ContentValues cv = new ContentValues();
        cv.put(lg.COL_USER,sUser);
        cv.put(lg.COL_PASS,sPass);
        LoginDbHelper lgHelper = new LoginDbHelper(this);
        SQLiteDatabase db = lgHelper.getWritableDatabase();
        long result = db.insert(lg.TABLE_NAME,null,cv);

        Intent intent = new Intent(Register.this,MainActivity.class);
        startActivity(intent);
    }
}
