package com.example.juras.damitest.login;

import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.juras.damitest.R;
import com.example.juras.damitest.utils.TabsPagerAdapter;
import com.example.juras.damitest.utils.Utils;


/**
 * LoginActivity contains login, registration and map fragment which are inside tabs
 * Change
 * */
public class LoginActivity extends FragmentActivity implements TabLayout.OnTabSelectedListener
{
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(this);
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



    /** Shows desired fragment(either login, registration or map)*/
    @Override
    public void onTabSelected(TabLayout.Tab tab)
    {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) { }

    @Override
    public void onTabReselected(TabLayout.Tab tab) { }
}
