package com.example.juras.damitest.logic.contacts;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.juras.damitest.DamiLogedActivity;
import com.example.juras.damitest.DamiStringRequest;
import com.example.juras.damitest.R;
import com.example.juras.damitest.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class AddContactActivity extends DamiLogedActivity
{
    /** */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
    }

    /** */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_contact_menu, menu);
        return true;
    }

    /** */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_apply:
                saveContact();
                return true;
            case R.id.action_discard:
                discardContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Send request to server for creating new contact
     * */
    private void saveContact()
    {
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        if (email.isEmpty())
        {
            ((EditText)findViewById(R.id.email)).setError(getString(R.string.wrong_email));
            return;
        }

        DamiStringRequest updateRequest = new DamiStringRequest(Request.Method.POST, getString(R.string.add_contact_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Utils.launchActivity(mCurrentUser, AddContactActivity.this, ContactsActivity.class);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        try
                        {
                            JSONObject data = new JSONObject(new String(error.networkResponse.data));
                            ((EditText)findViewById(R.id.email)).setError(data.getString("responseCodeText"));
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

        updateRequest.addParam("token", mCurrentUser.getToken());
        updateRequest.addParam("email", email);

        String name = ((EditText)findViewById(R.id.name)).getText().toString();
        if (!name.isEmpty())
        {
            updateRequest.addParam("name", name);
        }

        String lastname = ((EditText)findViewById(R.id.lastname)).getText().toString();
        if (!lastname.isEmpty())
        {
            updateRequest.addParam("lastname", lastname);
        }

        String phone = ((EditText)findViewById(R.id.phone)).getText().toString();
        if (!phone.isEmpty())
        {
            updateRequest.addParam("phone", phone);
        }

        Volley.newRequestQueue(getApplicationContext()).add(updateRequest);
    }

    private void discardContact()
    {
        Utils.launchActivity(mCurrentUser, this, ContactsActivity.class);
    }
}
