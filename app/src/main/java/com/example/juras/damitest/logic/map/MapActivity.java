package com.example.juras.damitest.logic.map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.juras.damitest.DamiLogedActivity;
import com.example.juras.damitest.R;

public class MapActivity extends DamiLogedActivity
{
    private static int FILTER_ACTIVITY = 1;

    private boolean mShowOnlyFavorite;

    private RecreateMapListener recreateMapListener;

    /**
     * Loads from preferences whether we want to show only favorite points
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SharedPreferences sharedPref = getSharedPreferences("user_details", Context.MODE_PRIVATE);
        mShowOnlyFavorite = sharedPref.getBoolean("show_only_favorite", false);
    }

    /** */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }

    /** */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case R.id.action_filter:
                showFilter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Recreate map based on new filter option
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK && requestCode == FILTER_ACTIVITY)
        {
            boolean showOnlyFavorite = data.getBooleanExtra("show_only_favorite", mShowOnlyFavorite);
            if (showOnlyFavorite == mShowOnlyFavorite)
            {
                return;
            }

            mShowOnlyFavorite = showOnlyFavorite;
            recreateMapListener.onRecreateMap();
        }
    }

    public void setOnRecreateMapListener(RecreateMapListener listener)
    {
        recreateMapListener = listener;
    }

    public boolean isVisible(int id)
    {
        return !mShowOnlyFavorite || mCurrentUser.getFavorites().contains(id);
    }

    private void showFilter()
    {
        startActivityForResult(new Intent(MapActivity.this, FilterActivity.class), FILTER_ACTIVITY);
    }

    public interface RecreateMapListener
    {
        void onRecreateMap();
    }
}
