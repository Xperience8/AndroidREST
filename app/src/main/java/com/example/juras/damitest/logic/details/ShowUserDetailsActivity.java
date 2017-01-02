package com.example.juras.damitest.logic.details;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.juras.damitest.DamiStringRequest;
import com.example.juras.damitest.R;

public class ShowUserDetailsActivity extends ShowDetailsActivity
{
    /**
     * Show current user details
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        showDetails(mCurrentUser);
    }

    /** Send current user config to server*/
    @Override
    public void saveChanges()
    {
        DamiStringRequest updateRequest = new DamiStringRequest(Request.Method.POST, getString(R.string.update_account_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        mCurrentUser.update(response);
                    }
                }, null);

        updateRequest.addParam("token", mCurrentUser.getToken());
        fillUpdateRequest(updateRequest);

        Volley.newRequestQueue(getApplicationContext()).add(updateRequest);
    }

    /**
     * Fill and set up all EditTexts with JSON data which are saved on server side
     * all pending changes will be discarded
     * */
    @Override
    public void discardChanges()
    {
        showDetails(mCurrentUser);
        clearUpdateInfo();
    }
}
