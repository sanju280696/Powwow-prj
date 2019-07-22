package com.desire.powwow;

import com.amulyakhare.textdrawable.TextDrawable;

public class ForumQuestion {
    private String mTitle;
    private String mDescription;
    private String mUrl;
    private String mKey;
    private String mUserName;
    private TextDrawable mTextDrawable;

    /*public ForumQuestion(String Title, String Description, String url, String key) {
        mTitle = Title;
        mDescription = Description;
        mUrl = url;
        mKey = key;
    }*/

    public ForumQuestion(String title, String description, String key, String username) {
        mTitle = title;
        mDescription = description;
        mKey = key;
        mUserName = username;
    }

    public ForumQuestion(String title, String description, String key, TextDrawable textDrawable,String username) {
        mTitle = title;
        mDescription = description;
        mKey = key;
        mTextDrawable = textDrawable;
        mUserName = username;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getKey() {
        return mKey;
    }

    public TextDrawable getTextDrawable() {
        return mTextDrawable;
    }

    public String getUserName() {
        return mUserName;
    }
}
