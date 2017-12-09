package com.example.emergencyphonenumber;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.emergencyphonenumber.db.TaleDbHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class AddTaleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AddTaleActivity.class.getName();

    private EditText mTaleTitleEditText, mTaleNumberEditText;
    private ImageView mTaleImageView;
    private Button mSaveButton;

    private File mSelectedPictureFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tale);

        mTaleTitleEditText = findViewById(R.id.tale_title_edit_text);
        mTaleNumberEditText = findViewById(R.id.tale_story_edit_text);
        mTaleImageView = findViewById(R.id.tale_image_view);
        mSaveButton = findViewById(R.id.save_button);

        // กำหนดให้ Activity เป็น Listener ของ ImageView
        mTaleImageView.setOnClickListener(this);
        // กำหนดให้ Activity เป็น Listener ของ Button
        mSaveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.tale_image_view) {
            EasyImage.openChooserWithGallery(
                    AddTaleActivity.this,
                    "ถ่ายรูปหรือเลือกรูปภาพที่ต้องการ",
                    0
            );

        } else if (viewId == R.id.save_button) {
            if (mSelectedPictureFile == null) {
                Toast.makeText(
                        getApplicationContext(),
                        "คุณยังไม่ได้เลือกรูปภาพ",
                        Toast.LENGTH_LONG
                ).show();
                return;
            }

            File privateDir = getApplicationContext().getFilesDir();
            File dstFile = new File(privateDir, mSelectedPictureFile.getName());

            try {
                copyFile(mSelectedPictureFile, dstFile);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error copying picture file.");
                return;
            }

            saveDataToDb();
            setResult(RESULT_OK);
            finish();
        }
    }

    private void saveDataToDb() {
        String taleTitle = mTaleTitleEditText.getText().toString();
        String taleNumber = mTaleNumberEditText.getText().toString();
        String pictureFileName = mSelectedPictureFile.getName();

        ContentValues cv = new ContentValues();
        cv.put(TaleDbHelper.COL_TITLE, taleTitle);
        cv.put(TaleDbHelper.COL_STORY, taleNumber);
        cv.put(TaleDbHelper.COL_PICTURE, pictureFileName);

        TaleDbHelper dbHelper = new TaleDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.insert(TaleDbHelper.TABLE_NAME, null, cv);
        if (result == -1) {
            //
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                Log.e(TAG, "Error choosing picture file: " + e.getMessage());
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                //Handle the images
                mSelectedPictureFile = imagesFiles.get(0);
                Drawable drawable = Drawable.createFromPath(mSelectedPictureFile.getAbsolutePath());
                mTaleImageView.setImageDrawable(drawable);

                Log.i(TAG, mSelectedPictureFile.getAbsolutePath());
            }
        });
    }

    public static void copyFile(File src, File dst) throws IOException {
        FileInputStream inputStream = new FileInputStream(src);
        FileOutputStream outputStream = new FileOutputStream(dst);
        byte[] buffer = new byte[1024];

        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();
    }
}
