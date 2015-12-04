package com.example.insys.oneibory.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.insys.oneibory.Login.LoginActivity;
import com.example.insys.oneibory.R;
import com.example.insys.oneibory.Utility.Constant;
import com.example.insys.oneibory.Utility.EncryptionUtil;
import com.example.insys.oneibory.Utility.PrefsUtility;
import com.example.insys.oneibory.app.AppController;
import com.example.insys.oneibory.fragments.FragmentDrawer;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aazam on 11/2/2015.
 */
public class WebView extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    boolean loginrequired = false;
    private Toolbar toolbar;
    private android.webkit.WebView webview, weblogin;
    private String TAG = getClass().getName();
    private String geturl = null;
    private JSONObject userobject;
    int c = 1;
    private Context mcontext = WebView.this;
    private int d = 1;
    private DrawerLayout mDrawerLayout;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_items);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle _drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0);
        mDrawerLayout.setDrawerListener(_drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        _drawerToggle.syncState();



        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(WebView.this);

        geturl = getIntent().getStringExtra("url");

        if (geturl != null) {
            webview = (android.webkit.WebView) findViewById(R.id.custom_webview);
            weblogin = (android.webkit.WebView) findViewById(R.id.login_webview_back);
            WebSettings webSettings = webview.getSettings();
            WebSettings webSettings1 = weblogin.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings1.setJavaScriptEnabled(true);

            webview.setWebViewClient(new CustomWebViewClient());
            webview.loadUrl(geturl);
        }
    }


    private void dologin() {
        try {
            userobject = new JSONObject();

            String name = PrefsUtility.getFromPrefs(mcontext, PrefsUtility.PREFS_LOGIN_USERNAME_KEY, null);
            String pass = PrefsUtility.getFromPrefs(mcontext, PrefsUtility.PREFS_LOGIN_PASSWORD, null);
            String decryptedUserName = EncryptionUtil.decrypt(name);
            String decryptedPass = EncryptionUtil.decrypt(pass);

            userobject.accumulate("username",decryptedUserName);
            userobject.accumulate("password", decryptedPass);

            final String postData = userobject.toString();

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constant.Login_URL,
                    userobject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());

                    weblogin.postUrl(
                            "http://oneivory.xhtmlxpert.com/apilogin/index/login",
                            EncodingUtils.getBytes(postData, "BASE64"));

                    weblogin.setWebViewClient(new WebViewClient() {

                        @Override
                        public void onPageFinished(android.webkit.WebView view, String url) {
                            super.onPageFinished(view, url);

                            if (!url.equals("\"http://oneivory.xhtmlxpert.com/customer/account/logout/")) {
                                webview.loadUrl(geturl);
                            }


                        }
                    });
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(WebView.this);
                    builder.setMessage("Login Again to Conitnue...").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(mcontext, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
                    try {
                        builder.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq,
                    tag_json_obj);


        } catch (Exception ex) {

            ex.printStackTrace();
        }


    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
    }


    private class CustomWebViewClient extends WebViewClient {

        public CustomWebViewClient() {
            super();
        }

        @Override
        public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
            showprogressDialogue();
            if (!url.equals("http://oneivory.xhtmlxpert.com") && !url.equals("http://oneivory.xhtmlxpert.com/customer/account/logout/") && !url.equals("http://oneivory.xhtmlxpert.com/customer/account/logoutSuccess/")) {
                updateSystem();
            }
        }

        @Override
        public void onPageFinished(android.webkit.WebView view, String url) {
//            super.onPageFinished(view, url);
            try {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {

            //update page linked url to redicted url otherwise it loaded old urls for once
//            if (url.equals("http://uat.oneivory.com/customer/account/login/")) {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(WebView.this);
//                builder.setMessage("Login Again to Conitnue...").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        startActivity(new Intent(mcontext, LoginActivity.class));
//                    }
//                });
//                try {
//                    builder.show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return false;
//            }


            if (url.equals("http://oneivory.xhtmlxpert.com/customer/account/logout/")) {
                weblogin.postUrl("http://oneivory.xhtmlxpert.com/apilogin/index/logout", EncodingUtils.getBytes("", "BASE64"));
                return false;
            }

            else if (url.equals("http://oneivory.xhtmlxpert.com/customer/account/logoutSuccess/")) {

                PrefsUtility.DeleteSharedPrefernce(mcontext);
                Intent intent = new Intent(mcontext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return false;
            }


            else if (url.equals("http://oneivory.xhtmlxpert.com/")) {
                return false;
            }



            else {
                view.loadUrl(url);
                geturl = url;
                showprogressDialogue();
                return true;
            }


        }

    }

    private void updateSystem() {
        long mycurrenttime = System.currentTimeMillis();
        long savedTime = Long.parseLong(PrefsUtility.getFromPrefs(WebView.this, PrefsUtility.PREFS_LOGIN_TIME, null));
        long diff = mycurrenttime - savedTime;
        // 50 min into - 3,000,000
        if (diff > 3000000) {
            //here the magic happens.
            dologin();
            PrefsUtility.saveToPrefs(mcontext, PrefsUtility.PREFS_LOGIN_TIME, String.valueOf(mycurrenttime));
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateSystem();
    }


    private void showprogressDialogue() {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }
}
