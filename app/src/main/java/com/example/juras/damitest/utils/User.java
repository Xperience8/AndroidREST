package com.example.juras.damitest.utils;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User implements Parcelable
{
    private int mId;
    private String mToken;

    private String mName;
    private String mLastName;
    private String mEmail;
    private String mFacebookId;
    private String mPhone;
    private String mPhoto;
    private String mDescription;

    private ArrayList<Integer> mFavorites;

    /**
     * Create user based on server response
     * */
    public User(String configResponse)
    {
        try
        {
            JSONObject config = new JSONObject(configResponse).getJSONObject("response");
            loadPersistentData(config);
            loadNotPersistenData(config);
        }
        catch (JSONException e)
        {
            System.out.println("Error: Creating user from server response failed");
        }
    }

    /**
     * Create user based on JSON config
     * */
    public User(JSONObject config) throws JSONException
    {
        loadPersistentData(config);
        loadNotPersistenData(config);
    }

    /**
     * Not persistent data will be updated
     * */
    public void update(String updateResponse)
    {
        try
        {
            JSONObject config = new JSONObject(updateResponse).getJSONObject("response");
            loadNotPersistenData(config);
        }
        catch (JSONException e)
        {
            System.out.println("Error: Updating user from server response failed");
        }
    }

    /**
     * Persistent data will be loaded from JSON config
     * */
    private void loadPersistentData(JSONObject config) throws JSONException
    {
        mId = config.getInt("id");

        if (config.has("token"))
        {
            mToken = config.getString("token");
        }
        else
        {
            mToken = "null";
        }

        mFacebookId = config.getString("fID");

        mFavorites = new ArrayList<>();
        if (config.has("favorites"))
        {
            JSONArray favoritesConfig = config.getJSONArray("favorites");
            for (int i = 0; i < favoritesConfig.length(); ++i)
            {
                mFavorites.add(favoritesConfig.getJSONObject(i).getInt("id"));
            }
        }
    }

    /**
     * Not persistent data will be loaded from JSON config
     * */
    private void loadNotPersistenData(JSONObject config) throws JSONException
    {
        mName = config.getString("name");
        mLastName = config.getString("lastname");
        mEmail = config.getString("email");
        mPhone = config.getString("phone");
        mPhoto = config.getString("photo");
        mDescription = config.getString("description");
    }

    private User(Parcel in)
    {
        mId = in.readInt();
        mToken = in.readString();

        mName = in.readString();
        mLastName = in.readString();
        mEmail = in.readString();
        mFacebookId = in.readString();
        mPhone = in.readString();
        mPhoto = in.readString();
        mDescription = in.readString();

        mFavorites = new ArrayList<>();
        int[] tempData = in.createIntArray();
        for (int id : tempData)
        {
            mFavorites.add(id);
        }
    }

    public static final Creator<User> CREATOR = new Creator<User>()
    {
        @Override
        public User createFromParcel(Parcel in)
        {
            return new User(in);
        }

        @Override
        public User[] newArray(int size)
        {
            return new User[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(mId);
        dest.writeString(mToken);

        dest.writeString(mName);
        dest.writeString(mLastName);
        dest.writeString(mEmail);
        dest.writeString(mFacebookId);
        dest.writeString(mPhone);
        dest.writeString(mPhoto);
        dest.writeString(mDescription);

        int[] tempData = new int[mFavorites.size()];
        for (int i = 0; i < mFavorites.size(); ++i)
        {
            tempData[i] = mFavorites.get(i);
        }
        dest.writeIntArray(tempData);
    }

    public int getId() {
        return mId;
    }

    public String getToken() {
        return mToken;
    }

    public String getName() {
        return mName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getFullName()
    {
        String fullname = "";

        if (!mName.equals("null"))
        {
            fullname += mName + " ";
        }
        if (!mLastName.equals("null"))
        {
            fullname += mLastName;
        }

        return fullname;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getFacebookId() {
        return mFacebookId;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public String getDescription() {
        return mDescription;
    }

    public ArrayList<Integer> getFavorites() {
        return mFavorites;
    }
}
