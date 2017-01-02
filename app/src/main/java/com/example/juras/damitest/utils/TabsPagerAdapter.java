package com.example.juras.damitest.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.juras.damitest.fragments.LoginFragment;
import com.example.juras.damitest.fragments.MapFragment;
import com.example.juras.damitest.fragments.RegistrationFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter
{
    public TabsPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    /**
     * Returns either login, registration or map fragment based on index
     * */
    @Override
    public Fragment getItem(int index)
    {

        switch (index)
        {
            case 0:
                return new LoginFragment();
            case 1:
                return new RegistrationFragment();
            case 2:
                return new MapFragment();
        }

        return null;
    }

    @Override
    public int getCount()
    {
        return 3;
    }
}
