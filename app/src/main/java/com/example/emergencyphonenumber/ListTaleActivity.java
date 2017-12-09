package com.example.emergencyphonenumber;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.emergencyphonenumber.adapter.TaleListAdapter;
import com.example.emergencyphonenumber.db.TaleDbHelper;
import com.example.emergencyphonenumber.model.TaleItem;

import java.util.ArrayList;

public class ListTaleActivity extends AppCompatActivity {

    private TaleDbHelper mHelper;
    private SQLiteDatabase mDb;

    private ArrayList<TaleItem> mTaleItemList = new ArrayList<>();
    private TaleListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tale);

        mHelper = new TaleDbHelper(this);
        mDb = mHelper.getReadableDatabase();

        loadDataFromDb();

        mAdapter = new TaleListAdapter(
                this,
                R.layout.item,
                mTaleItemList
        );

        ListView lv = findViewById(R.id.list_view);
        lv.setAdapter(mAdapter);

        /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TaleItem item = mTaleItemList.get(position);
                String phoneNumber = item.story;

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });*/

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaleItem item = mTaleItemList.get(i);
                Toast.makeText(ListTaleActivity.this, item.title, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ListTaleActivity.this, Story.class);
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ListTaleActivity.this);

                String[] items = new String[]{"แก้ไขข้อมูล", "ลบข้อมูล"};

                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) { // แก้ไขข้อมูล
                            TaleItem item = mTaleItemList.get(position);
                            int phoneId = item.id;

                            ContentValues cv = new ContentValues();
                            cv.put(TaleDbHelper.COL_STORY, "12345");

                            mDb.update(
                                    TaleDbHelper.TABLE_NAME,
                                    cv,
                                    TaleDbHelper.COL_ID + "=?",
                                    new String[]{String.valueOf(phoneId)}
                            );
                            loadDataFromDb();
                            mAdapter.notifyDataSetChanged();

                        } else if (i == 1) { // ลบข้อมูล
                            TaleItem item = mTaleItemList.get(position);
                            int phoneId = item.id;

                            mDb.delete(
                                    TaleDbHelper.TABLE_NAME,
                                    TaleDbHelper.COL_ID + "=?",
                                    new String[]{String.valueOf(phoneId)}
                            );
                            loadDataFromDb();
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListTaleActivity.this, AddTaleActivity.class);
                startActivityForResult(intent, 123);
            }
        });

    } // ปิดเมธอด onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                loadDataFromDb();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void loadDataFromDb() {
        Cursor cursor = mDb.query(
                TaleDbHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        mTaleItemList.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(TaleDbHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(TaleDbHelper.COL_TITLE));
            String story = cursor.getString(cursor.getColumnIndex(TaleDbHelper.COL_STORY));
            String picture = cursor.getString(cursor.getColumnIndex(TaleDbHelper.COL_PICTURE));

            TaleItem item = new TaleItem(id, title, story, picture);
            mTaleItemList.add(item);
        }
    }
}
