package com.example.emergencyphonenumber.model;

/**
 * Created by Promlert on 2017-11-26.
 */

public class TaleItem {

    public final int id;
    public final String title;
    public final String story;
    public final String picture;

    public TaleItem(int id, String title, String story, String picture) {
        this.id = id;
        this.title = title;
        this.story = story;
        this.picture = picture;
    }

    @Override
    public String toString() {
        return title;
    }
}
