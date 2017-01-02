package com.example.juras.damitest.logic.contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.juras.damitest.DamiLogedActivity;
import com.example.juras.damitest.DamiStringRequest;
import com.example.juras.damitest.R;
import com.example.juras.damitest.utils.User;
import com.example.juras.damitest.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactsActivity extends DamiLogedActivity implements TextWatcher
{
    /**
     * Set search option and download contacts
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        final EditText searchEditText = (EditText)findViewById(R.id.search);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    Utils.clearFocusAndHideKeyboard(ContactsActivity.this, v);
                }

                return false;
            }
        });
        searchEditText.addTextChangedListener(this);
        downloadContacts();
    }

    /** */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contacts_menu, menu);
        return true;
    }

    /** */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_add:
                addContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Send request to server for deleting contact
     * */
    public void onClickDeleteContact(View v)
    {
        final View contactView = (View)v.getParent().getParent();

        DamiStringRequest deleteContactRequest = new DamiStringRequest(Request.Method.POST, getString(R.string.delete_contact_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        ViewManager parentView = ((ViewManager)contactView.getParent());
                        parentView.removeView(contactView);
                    }
                }, null);

        deleteContactRequest.addParam("token", mCurrentUser.getToken());
        deleteContactRequest.addParam("id", Integer.toString(((User)contactView.getTag(R.id.contact)).getId()));


        Volley.newRequestQueue(getApplicationContext()).add(deleteContactRequest);
    }

    /**
     * Launch new activity which shows contact details
     * */
    public void onClickShowContact(View v)
    {
        Intent intent = new Intent(ContactsActivity.this, ShowContactDetailsActivity.class);
        intent.putExtra("user", mCurrentUser);
        intent.putExtra("contact", (User)((View)v.getParent()).getTag(R.id.contact));
        startActivity(intent);
        finish();
    }

    /**
     * Download and parse all contacts
     * */
    private void downloadContacts()
    {
        DamiStringRequest getContactsRequest = new DamiStringRequest(Request.Method.POST, getString(R.string.get_contacts_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject data = new JSONObject(response);
                            parseContacts(data.getJSONArray("response"));
                        }
                        catch (JSONException e)
                        {
                            System.out.println("Error: Parsing contacts failed");
                        }
                    }
                }, null);

        getContactsRequest.addParam("token", mCurrentUser.getToken());

        Volley.newRequestQueue(getApplicationContext()).add(getContactsRequest);
    }

    /**
     * Create contacts from JSON configs
     * */
    private void parseContacts(JSONArray config) throws JSONException
    {
        for (int i = 0; i < config.length(); ++i)
        {
            ViewGroup contactsView = (ViewGroup)findViewById(R.id.contacts);

            User contact = new User(config.getJSONObject(i));

            View contactView = LayoutInflater.from(this).inflate(R.layout.contact_item, null);
            contactView.setTag(R.id.contact, contact);
            contactsView.addView(contactView);

            ((TextView)contactView.findViewById(R.id.fullname)).setText(contact.getFullName());
            downloadAndSetPhoto(contact.getPhoto(), contactView);
        }
    }

    private void downloadAndSetPhoto(String photoUrl, final View view)
    {
        if (!photoUrl.equals("null"))
        {
            ImageRequest imageRequest = new ImageRequest(photoUrl,
                    new Response.Listener<Bitmap>()
                    {
                        @Override
                        public void onResponse(Bitmap response)
                        {
                            if (response != null)
                            {
                                ((ImageView) view.findViewById(R.id.profile_picture)).setImageBitmap(response);
                            }
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, null);
            Volley.newRequestQueue(this).add(imageRequest);
        }
    }

    /**
     * Launch new activity for adding contact
     * */
    private void addContact()
    {
        Intent intent = new Intent(ContactsActivity.this, AddContactActivity.class);
        intent.putExtra("user", mCurrentUser);
        startActivity(intent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s)
    {
        EditText searchEditText = (EditText)findViewById(R.id.search);
        String searchText = searchEditText.getText().toString();

        LinearLayout contactsLayout = (LinearLayout)findViewById(R.id.contacts);
        for (int i = 0; i < contactsLayout.getChildCount(); ++i)
        {
            View contactView = contactsLayout.getChildAt(i);

            User contact = (User)contactView.getTag(R.id.contact);
            if (contact.getEmail().contains(searchText) || contact.getFullName().contains(searchText))
            {
                contactView.setVisibility(View.VISIBLE);
            }
            else
            {
                contactView.setVisibility(View.GONE);
            }
        }
    }
}
