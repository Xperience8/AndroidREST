package com.example.juras.damitest.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class LoginFragment extends Fragment implements FacebookCallback<LoginResult>
{
    private CallbackManager mCallbackManager;

    public LoginFragment() { }

    /**
     * Initializes facebook SDK
     * */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
    }

    /**
     * Tries to automatically log in when login data are saved,
     * initializes facebook login/registration
     * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        tryAutoLogin();
        initFacebookLogin(view);

        view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickLogin();
            }
        });

        return view;
    }

    /**
     * Passing values from facebook activity to our fragment
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Check for required input parameters correctness
     * and login on server side, saves login data for next login
     * and transfers to account details activity
     * */
    public void onClickLogin()
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

        login(email.getText().toString(), password.getText().toString(), null);
    }

    /**
     * Tries to automatically log in when login data are saved
     * */
    private void tryAutoLogin()
    {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", null);
        if (email != null)
        {
            String password = sharedPref.getString("password", null);
            String facebookId = sharedPref.getString("facebookId", null);

            login(email, password, facebookId);
        }
    }

    /**
     * Register specific callbacks fro facebook login
     * */
    private void initFacebookLogin(View view)
    {
        LoginButton loginButton = (LoginButton)view.findViewById(R.id.facebook);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.setFragment(this);
        LoginManager.getInstance().registerCallback(mCallbackManager, this);
    }

    /**
     * Login on server side, saves login data for next login
     * and transfers to account details activity
     *  */
    private void login(final String email, final String password, final String facebookId)
    {
        Utils.showConnectingDialog(getActivity());

        DamiStringRequest loginRequest = new DamiStringRequest(Request.Method.POST, getString(R.string.login_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Utils.saveLoginData(getActivity(), email, password, facebookId);
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

                                ((EditText) getActivity().findViewById(R.id.email)).setError(message);
                                ((EditText) getActivity().findViewById(R.id.password)).setError(message);
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                    }
                });

        loginRequest.addParam("email", email);
        if (password != null)
        {
            loginRequest.addParam("password", password);
        }
        else if (facebookId != null)
        {
            loginRequest.addParam("fID", facebookId);
        }
        Volley.newRequestQueue(getActivity()).add(loginRequest);
    }

    /**
     * Login on server side through facebook, saves login data for next login
     * and transfers to account details activity
     *  */
    private void facebookLogin(final String email, final String facebookId)
    {
        Utils.showConnectingDialog(getActivity());

        DamiStringRequest loginRequest = new DamiStringRequest(Request.Method.POST, getString(R.string.register_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Utils.saveLoginData(getActivity(), email, null, facebookId);
                        Utils.launchActivity(new User(response), getActivity(), ShowUserDetailsActivity.class);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        login(email, null, facebookId);
                    }
                });

        loginRequest.addParam("email", email);
        loginRequest.addParam("fID", facebookId);

        Volley.newRequestQueue(getActivity()).add(loginRequest);
    }

    /** Request id and email and begin facebook login*/
    @Override
    public void onSuccess(LoginResult loginResult)
    {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject me, GraphResponse response)
                    {
                        String id = me.optString("id");
                        String email = me.optString("email");
                        if (email == null || email.length() == 0)
                        {
                            facebookLogin(id + "@facebook.com", id);
                        }
                        else
                        {
                            facebookLogin(email, id);
                        }
                    }
                });
        request.executeAsync();
    }

    @Override
    public void onCancel() { }

    @Override
    public void onError(FacebookException error) { }
}
