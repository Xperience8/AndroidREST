package com.example.juras.damitest;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.juras.damitest.logic.contacts.ContactsActivity;
import com.example.juras.damitest.logic.details.ShowUserDetailsActivity;
import com.example.juras.damitest.logic.map.MapActivity;
import com.example.juras.damitest.utils.User;
import com.example.juras.damitest.utils.Utils;


public abstract class DamiLogedActivity extends AppCompatActivity
{
    public User mCurrentUser;

    /** Save user data from intent*/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mCurrentUser = intent.getParcelableExtra("user");
    }

    /** Creates simple main menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.default_menu, menu);
        return true;
    }

    /** */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.account_details:
                Utils.launchActivity(mCurrentUser, this, ShowUserDetailsActivity.class);
                return true;
            case R.id.contacts:
                Utils.launchActivity(mCurrentUser, this, ContactsActivity.class);
                return true;
            case R.id.map:
                Utils.launchActivity(mCurrentUser, this, MapActivity.class);
                return true;
            case R.id.logout:
                Utils.logout(this);
                return true;
            case R.id.exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Clears focus from EditTexts when we click somewhere outside*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            View v = getCurrentFocus();
            if (v instanceof EditText)
            {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY()))
                {
                    Utils.clearFocusAndHideKeyboard(this, v);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}
