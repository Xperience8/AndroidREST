package com.example.juras.damitest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.juras.damitest.DamiStringRequest;
import com.example.juras.damitest.R;
import com.example.juras.damitest.logic.details.ShowUserDetailsActivity;
import com.example.juras.damitest.utils.User;
import com.example.juras.damitest.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationFragment extends Fragment
{
    public RegistrationFragment() { }

    /** Registers callback for registration button*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        view.findViewById(R.id.registration).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickRegistration();
            }
        });

        return view;
    }

    /**
     * Check for required input parameters correctness
     * and register on server side, saves login data for next login
     * and transfers to account detail activity
     * */
    public void onClickRegistration()
    {
        final EditText email = (EditText)getActivity().findViewById(R.id.email);
        if (!Utils.isValidMail(email.getText().toString()))
        {
            email.setError(getString(R.string.wrong_email));
            return;
        }

        final EditText password = (EditText)getActivity().findViewById(R.id.password);
        if (!Utils.isValidPassword(password.getText().toString()))
        {
            password.setError(getString(R.string.short_password));
            return;
        }

        final EditText passwordAgain = (EditText)getActivity().findViewById(R.id.password_again);
        if (!password.getText().toString().equals(passwordAgain.getText().toString()))
        {
            passwordAgain.setError(getString(R.string.not_same_password));
            return;
        }

        register(email.getText().toString(), password.getText().toString());
    }

    /**
     * Register on server side, saves login data for next login
     * and transfers to account detail activity
     * */
    private void register(final String email, final String password)
    {
        Utils.showConnectingDialog(getActivity());

        DamiStringRequest loginRequest = new DamiStringRequest(Request.Method.POST, getString(R.string.register_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Utils.saveLoginData(getActivity(), email, password, null);
                        Utils.launchActivity(new User(response), getActivity(), ShowUserDetailsActivity.class);
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
                            String message = data.getString("responseCodeText");

                            ((EditText)getActivity().findViewById(R.id.email)).setError(message);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

        loginRequest.addParam("email", email);
        loginRequest.addParam("password", password);

        Volley.newRequestQueue(getActivity()).add(loginRequest);
    }
}
