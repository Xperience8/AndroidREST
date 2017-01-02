package com.example.juras.damitest;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DamiStringRequest extends StringRequest
{
    private Map<String,String> mParams;

    public DamiStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(method, url, listener, errorListener);
        mParams = new HashMap<>();
    }

    public void addParam(String key, String value)
    {
        mParams.put(key, value);
    }

    @Override
    protected Map<String,String> getParams()
    {
        return mParams;
    }
}
