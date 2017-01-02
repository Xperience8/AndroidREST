package com.example.juras.damitest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.juras.damitest.DamiStringRequest;
import com.example.juras.damitest.R;
import com.example.juras.damitest.logic.map.MapActivity;
import com.example.juras.damitest.logic.map.PointDetailActivity;
import com.example.juras.damitest.utils.PointOnMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, MapActivity.RecreateMapListener
{
    private GoogleMap mMap;
    private List<PointOnMap> mPointsOnMap;

    public MapFragment() { }

    /**
     * Register specific callbacks when user is loged in
     * */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPointsOnMap = new ArrayList<>();

        if (getActivity() instanceof MapActivity)
        {
            ((MapActivity)getActivity()).setOnRecreateMapListener(this);
        }
    }

    /**
     * Inits map view and request google map
     * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        MapView mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try
        {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);

        return view;
    }

    /**
     * Download and init all makers from config
     * */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                Intent intent = new Intent(getActivity(), PointDetailActivity.class);
                intent.putExtra("point_on_map", (PointOnMap)marker.getTag());
                startActivity(intent);

                return true;
            }
        });

        downloadPointsOnMap();
    }

    /**
     * Download markers from server and add them to map
     * */
    private void downloadPointsOnMap()
    {
        DamiStringRequest updateRequest = new DamiStringRequest(Request.Method.POST, getString(R.string.get_points_on_map_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        addPointsToMap(response);

                    }
                }, null);

        Volley.newRequestQueue(getActivity()).add(updateRequest);
    }

    /**
     * Create markers based on server response and add them to map
     * */
    private void addPointsToMap(String serverResponse)
    {
        try
        {
            JSONObject data = new JSONObject(serverResponse);
            JSONArray configs = (data.getJSONArray("response"));
            for (int i = 0; i < configs.length(); ++i)
            {
                PointOnMap point = new PointOnMap(configs.getJSONObject(i));
                showPointOnMap(point);
                mPointsOnMap.add(point);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Shows point on map based on filter
     * */
    private void showPointOnMap(PointOnMap point)
    {
        boolean visible = true;
        if (getActivity() instanceof MapActivity)
        {
            visible = ((MapActivity)getActivity()).isVisible(point.getId());
        }
        point.addToMap(mMap, visible);
    }

    /** Remove all markers from map and add them to map again(some of them can be filtered out)*/
    @Override
    public void onRecreateMap()
    {
        mMap.clear();
        for (PointOnMap point : mPointsOnMap)
        {
            showPointOnMap(point);
        }
    }
}
