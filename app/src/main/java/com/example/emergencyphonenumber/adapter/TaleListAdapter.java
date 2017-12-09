package com.example.emergencyphonenumber.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emergencyphonenumber.R;
import com.example.emergencyphonenumber.model.TaleItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Promlert on 2017-11-26.
 */

public class TaleListAdapter extends ArrayAdapter<TaleItem> {

    private Context mContext;
    private int mLayoutResId;
    private ArrayList<TaleItem> mTaleItemList;

    public TaleListAdapter(@NonNull Context context, int layoutResId, @NonNull ArrayList<TaleItem> taleItemList) {
        super(context, layoutResId, taleItemList);

        this.mContext = context;
        this.mLayoutResId = layoutResId;
        this.mTaleItemList = taleItemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemLayout = inflater.inflate(mLayoutResId, null);

        TaleItem item = mTaleItemList.get(position);

        ImageView phoneImageView = itemLayout.findViewById(R.id.tale_image_view);
        TextView phoneTitleTextView = itemLayout.findViewById(R.id.tale_title_text_view);
        //TextView phoneNumberTextView = itemLayout.findViewById(R.id.phone_number_text_view);

        phoneTitleTextView.setText(item.title);
        //phoneNumberTextView.setText(item.story);

        String pictureFileName = item.picture;

        AssetManager am = mContext.getAssets();
        try {
            InputStream stream = am.open(pictureFileName);
            Drawable drawable = Drawable.createFromStream(stream, null);
            phoneImageView.setImageDrawable(drawable);

        } catch (IOException e) {
            e.printStackTrace();

            File pictureFile = new File(mContext.getFilesDir(), pictureFileName);
            Drawable drawable = Drawable.createFromPath(pictureFile.getAbsolutePath());
            phoneImageView.setImageDrawable(drawable);
        }

        return itemLayout;
    }
}
