package com.example.juras.damitest.logic.map;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.juras.damitest.utils.PointOnMap;
import com.example.juras.damitest.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PointDetailActivity extends AppCompatActivity
{
    /** Shows point details and set up picture animation*/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);

        showBackButton();
        showPointDetails((PointOnMap)getIntent().getParcelableExtra("point_on_map"));
        setUpPictureAnimation();
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

    /** Shows all details associated with point including with pictures*/
    private void showPointDetails(PointOnMap pointOnMap)
    {
        ((TextView)findViewById(R.id.title)).setText(pointOnMap.getTitle());
        ((TextView)findViewById(R.id.desc)).setText(pointOnMap.getDesc());

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        for (String photo : pointOnMap.getPhotos())
        {
            ImageRequest imageRequest = new ImageRequest(photo,
                    new Response.Listener<Bitmap>()
                    {
                        @Override
                        public void onResponse(Bitmap response)
                        {
                            ViewFlipper photosLayout = (ViewFlipper)findViewById(R.id.photos);

                            ImageView imageView = new ImageView(photosLayout.getContext());
                            imageView.setImageBitmap(response);

                            ViewFlipper.LayoutParams layoutParams = new ViewFlipper.LayoutParams(ViewFlipper.LayoutParams.MATCH_PARENT, ViewFlipper.LayoutParams.WRAP_CONTENT);
                            layoutParams.gravity = Gravity.CENTER;
                            imageView.setLayoutParams(layoutParams);

                            photosLayout.addView(imageView);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, null);
            requestQueue.add(imageRequest);
        }
    }

    /** Set up on click animation*/
    private void setUpPictureAnimation()
    {
        final ViewFlipper photosLayout = (ViewFlipper)findViewById(R.id.photos);
        photosLayout.setInAnimation(AnimationUtils.loadAnimation(PointDetailActivity.this, android.R.anim.fade_in));
        photosLayout.setOutAnimation(AnimationUtils.loadAnimation(PointDetailActivity.this, android.R.anim.fade_out));
        photosLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                photosLayout.showNext();
            }
        });
    }
}
