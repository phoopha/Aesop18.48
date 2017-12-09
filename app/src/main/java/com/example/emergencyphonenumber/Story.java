package com.example.emergencyphonenumber;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emergencyphonenumber.adapter.TaleListAdapter;
import com.example.emergencyphonenumber.db.TaleDbHelper;
import com.example.emergencyphonenumber.model.TaleItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class Story extends AppCompatActivity {

    private TaleDbHelper mHelper;
    private SQLiteDatabase mDb;
    private ArrayList<TaleItem> mTaleItemList = new ArrayList<>();
    private TaleListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        ImageView animalImageView = (ImageView) findViewById(R.id.detail_image_view);
        TextView nameTextView = (TextView) findViewById(R.id.detail_text_view);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

        mHelper = new TaleDbHelper(this);
        mDb = mHelper.getReadableDatabase();

        loadDataFromDb();
        mAdapter = new TaleListAdapter(
                this,
                R.layout.item,
                mTaleItemList
        );

        nameTextView.setText(mTaleItemList.get(position).story);
        //animalImageView.setImageResource(mTaleItemList.get(position).picture);

        getSupportActionBar().setTitle(
                String.format(
                        Locale.getDefault(),
                        "%s",
                        mTaleItemList.get(position).title
                )
        );

        try
        {
            // get input stream
            InputStream ims = getAssets().open(mTaleItemList.get(position).picture);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            animalImageView.setImageDrawable(d);
            ims.close();
        }
        catch(IOException ex)
        {
            return;
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
