package com.example.juras.damitest.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PointOnMap implements Parcelable
{
    private int mId;

    private double mLatitude;
    private double mLongitude;

    private String mTitle;
    private String mDesc;

    private ArrayList<String> mPhotos;

    /**
     * Create point on map from JSON config
     * */
    public PointOnMap(JSONObject config)
    {
        try
        {
            mId = config.getInt("id");

            mLatitude = config.getDouble("lat");
            mLongitude = config.getDouble("lng");

            mTitle = config.getString("title");
            mDesc = config.getString("desc");

            JSONArray photosArrayConfig = config.getJSONArray("photo");
            mPhotos = new ArrayList<>();
            for (int i = 0; i < photosArrayConfig.length(); ++i)
            {
                mPhotos.add(photosArrayConfig.getString(i));
            }
        }
        catch (JSONException e)
        {
            System.out.println("Error: parsing point on the map from server response failed");
        }
    }

    /**
     * Marker will be added to the map
     * */
    public void addToMap(GoogleMap map, boolean visible)
    {
        Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(mLatitude, mLongitude)).title(mTitle));
        marker.setTag(this);
        marker.setVisible(visible);
    }

    private PointOnMap(Parcel parcel)
    {
        mId = parcel.readInt();
        mLatitude = parcel.readDouble();
        mLongitude = parcel.readDouble();
        mTitle = parcel.readString();
        mDesc = parcel.readString();
        mPhotos = parcel.createStringArrayList();
    }

    public static final Creator<PointOnMap> CREATOR = new Creator<PointOnMap>()
    {
        @Override
        public PointOnMap createFromParcel(Parcel in)
        {
            return new PointOnMap(in);
        }

        @Override
        public PointOnMap[] newArray(int size)
        {
            return new PointOnMap[size];
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
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeString(mTitle);
        dest.writeString(mDesc);
        dest.writeStringList(mPhotos);
    }

    public int getId()
    {
        return mId;
    }

    public String getDesc()
    {
        return mDesc;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public ArrayList<String> getPhotos()
    {
        return mPhotos;
    }
}
