package com.example.juras.damitest.logic.details;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.juras.damitest.DamiLogedActivity;
import com.example.juras.damitest.DamiStringRequest;
import com.example.juras.damitest.R;
import com.example.juras.damitest.utils.User;
import com.example.juras.damitest.utils.Utils;

import java.util.HashMap;

public abstract class ShowDetailsActivity extends DamiLogedActivity
{
    /**
     * Holds all data which were updated
     * */
    private HashMap<String, String> mUpdatedInfo;

    /**
     * Mark specified data as waiting for update
     * */
    private View.OnFocusChangeListener onAccountDataFocusChange = new View.OnFocusChangeListener()
    {
        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            if (!hasFocus && v instanceof EditText)
            {
                EditText editText = (EditText)v;
                mUpdatedInfo.put((String)editText.getTag(), editText.getText().toString());
            }
        }
    };

    /**
     * Mark specified data as waiting for update and clear their focus
     * */
    private TextView.OnEditorActionListener onUpdateAccountData =  new TextView.OnEditorActionListener()
    {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            if (actionId == EditorInfo.IME_ACTION_DONE)
            {
                mUpdatedInfo.put((String)v.getTag(), v.getText().toString());
                Utils.clearFocusAndHideKeyboard(ShowDetailsActivity.this, v);
            }

            return false;
        }
    };

    /** */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        mUpdatedInfo = new HashMap<>();
    }

    /**
     * Creates simple main menu
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_contact_menu, menu);
        return true;
    }

    /** */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_apply:
                saveChanges();
                return true;
            case R.id.action_discard:
                discardChanges();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Send current user config to server
     * */
    public abstract void saveChanges();

    /**
     * Fill and set up all EditTexts with JSON data which are saved on server side
     * all pending changes will be discarded
     * */
    public abstract void discardChanges();

    /**
     * Fill and set up all EditTexts with user data
     * */
    protected void showDetails(User user)
    {
        setAccountData(R.id.name, user.getName());
        setAccountData(R.id.lastname, user.getLastName());
        setAccountData(R.id.email, user.getEmail());
        setAccountData(R.id.phone, user.getPhone());
        setAccountData(R.id.description, user.getDescription());

        if (!user.getPhoto().equals("null"))
        {
            ImageRequest imageRequest = new ImageRequest(user.getPhoto(),
                    new Response.Listener<Bitmap>()
                    {
                        @Override
                        public void onResponse(Bitmap response)
                        {
                            if (response != null)
                            {
                                ((ImageView) findViewById(R.id.photo)).setImageBitmap(response);
                            }
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, null);
            Volley.newRequestQueue(this).add(imageRequest);
        }
    }

    /**
     * Set text to specific EditText when it is not null
     * Set appropriate listeners so we know when text will change
     * */
    private void setAccountData(int viewId, String text)
    {
        EditText editText = (EditText)findViewById(viewId);
        editText.setOnEditorActionListener(onUpdateAccountData);
        editText.setOnFocusChangeListener(onAccountDataFocusChange);

        if (!text.equals("null"))
        {
            editText.setText(text);
        }
    }

    public void fillUpdateRequest(DamiStringRequest updateRequest)
    {
        for (String key : mUpdatedInfo.keySet())
        {
            updateRequest.addParam(key, mUpdatedInfo.get(key));
        }
        clearUpdateInfo();
    }

    public void clearUpdateInfo()
    {
        mUpdatedInfo.clear();
    }
}
