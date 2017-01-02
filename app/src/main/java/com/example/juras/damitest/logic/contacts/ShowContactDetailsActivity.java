package com.example.juras.damitest.logic.contacts;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.juras.damitest.DamiStringRequest;
import com.example.juras.damitest.R;
import com.example.juras.damitest.logic.details.ShowDetailsActivity;
import com.example.juras.damitest.utils.User;
import com.example.juras.damitest.utils.Utils;

public class ShowContactDetailsActivity extends ShowDetailsActivity
{
    User mCurrentContact;

    /**
     * Show current contact details*/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mCurrentContact = intent.getParcelableExtra("contact");
        showDetails(mCurrentContact);
    }

    /**
     * Send current user config to server
     * */
    @Override
    public void saveChanges()
    {
        DamiStringRequest updateRequest = new DamiStringRequest(Request.Method.POST, getString(R.string.update_contact_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Utils.launchActivity(mCurrentUser, ShowContactDetailsActivity.this, ContactsActivity.class);
                    }
                }, null);

        updateRequest.addParam("token", mCurrentUser.getToken());
        updateRequest.addParam("id", Integer.toString(mCurrentContact.getId()));

        fillUpdateRequest(updateRequest);

        Volley.newRequestQueue(getApplicationContext()).add(updateRequest);
    }

    /**
     * Return to parent activity
     * */
    @Override
    public void discardChanges()
    {
        Utils.launchActivity(mCurrentUser, this, ContactsActivity.class);
    }
}
