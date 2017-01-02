package com.example.juras.damitest.logic.map;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.example.juras.damitest.DamiLogedActivity;
import com.example.juras.damitest.R;

public class FilterActivity extends DamiLogedActivity
{
    /**
     * Shows current filter option
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        showBackButton();

        SharedPreferences sharedPref = getSharedPreferences("user_details", Context.MODE_PRIVATE);
        boolean showOnlyFavorite = sharedPref.getBoolean("show_only_favorite", false);
        ((CheckBox)findViewById(R.id.show_only_favorite)).setChecked(showOnlyFavorite);
    }

    /**
     * Save new filter option and return back to parent
     * */
    public void onClickSave(View v)
    {
        SharedPreferences sharedPref = getSharedPreferences("user_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        boolean showOnlyFavorite = ((CheckBox)findViewById(R.id.show_only_favorite)).isChecked();
        editor.putBoolean("show_only_favorite", showOnlyFavorite);
        editor.apply();

        Intent intent = new Intent();
        intent.putExtra("show_only_favorite", showOnlyFavorite);
        setResult(MapActivity.RESULT_OK, intent);
        finish();
    }

    /** Returns to parent activity when we have clicked on back button*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Shows back button which is used for returning back to parent activity*/
    private void showBackButton()
    {
        ActionBar actionBar = getActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
