package com.example.emergencyphonenumber;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emergencyphonenumber.db.LoginDbHelper;

public class MainActivity extends AppCompatActivity {

    private LoginDbHelper mdbHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });
    }

    public void cracker(View view) {

        mdbHelper = new LoginDbHelper(this);
        mDb = mdbHelper.getReadableDatabase();

        final Context context = getApplicationContext();
        final EditText usre = findViewById(R.id.editUser);
        final EditText pass = findViewById(R.id.editPass);
        Button kod = findViewById(R.id.Login);

        if (usre.getText().toString().length() == 0 && pass.getText().toString().length() == 0 ){
            usre.setError("ป้อนชื่อ");
            pass.setError("ป้อนรหัสผ่าน");
        }else if (usre.getText().toString().length() == 0){
            usre.setError("ป้อนชื่อ");
        }else if (pass.getText().toString().length() == 0 ){
            pass.setError("ป้อนรหัสผ่าน");
        }
        else{
            if (mdbHelper.check(usre.getText().toString(), pass.getText().toString())) {
                Toast toast = Toast.makeText(context, "YES", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(MainActivity.this, ListTaleActivity.class);
                intent.putExtra("position", 0);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(context, "NO", Toast.LENGTH_SHORT);
                toast.show();
            }

        }


    }
} // ปิดคลาส MainActivity
