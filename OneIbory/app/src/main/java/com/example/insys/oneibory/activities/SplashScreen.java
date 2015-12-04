package com.example.insys.oneibory.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.insys.oneibory.Login.LoginActivity;
import com.example.insys.oneibory.R;
import com.example.insys.oneibory.Utility.CommonMethod;
import com.example.insys.oneibory.Utility.Constant;
import com.example.insys.oneibory.Utility.EncryptionUtil;
import com.example.insys.oneibory.Utility.PrefsUtility;
import com.example.insys.oneibory.app.AppController;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aazam on 10/28/2015.
 */
public class SplashScreen extends AppCompatActivity {

    private android.webkit.WebView webview;
    private String TAG = getClass().getName();
    //    private int SPLASH_TIME_OUT = 3000;
    private Context mcontext = SplashScreen.this;
    private JSONObject userobject;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private CommonMethod networkManagerCheck =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        networkManagerCheck = new CommonMethod(SplashScreen.this);

        webview = (android.webkit.WebView) findViewById(R.id.custom_webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(android.webkit.WebView view, String url) {
                Log.i(TAG, url);
                finish();
            }
        });

        String sessionid = PrefsUtility.getFromPrefs(mcontext, PrefsUtility.PREFS_LOGIN_SessionId, "n/a");

        if(networkManagerCheck.isNetworkAvailable()) {
            if (sessionid.length() > 4) {
                makeJsonRequest();
            }
            else {
                startActivity(new Intent(mcontext, LoginActivity.class));
                finish();
            }
        }

        else {
            CommonMethod.showAlert("Internet is not available" , mcontext);
        }

    }

    private void makeJsonRequest() {
        try {
            userobject = new JSONObject();

            String name = PrefsUtility.getFromPrefs(mcontext, PrefsUtility.PREFS_LOGIN_USERNAME_KEY, null);
            String pass = PrefsUtility.getFromPrefs(mcontext, PrefsUtility.PREFS_LOGIN_PASSWORD, null);
            String decryptedUserName = EncryptionUtil.decrypt(name);
            String decryptedPass = EncryptionUtil.decrypt(pass);
            userobject.accumulate("username",decryptedUserName);
            userobject.accumulate("password", decryptedPass);

        } catch (Exception ex) {

        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constant.Login_URL,
                userobject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    PrefsUtility.saveToPrefs(mcontext, PrefsUtility.PREFS_LOGIN_Name, response.getString("Name"));
                    PrefsUtility.saveToPrefs(mcontext, PrefsUtility.PREFS_LOGIN_SessionId, response.getString("Sessionid"));
                    PrefsUtility.saveToPrefs(mcontext, PrefsUtility.PREFS_LOGIN_Mobile, response.getString("mobile"));
                    String postData = userobject.toString();

                    webview.postUrl(
                            "http://oneivory.xhtmlxpert.com/apilogin/index/login",
                            EncodingUtils.getBytes(postData, "BASE64"));
                    long   myCurrentTimeMillis = System.currentTimeMillis();
                    PrefsUtility.saveToPrefs(mcontext, PrefsUtility.PREFS_LOGIN_TIME, String.valueOf(myCurrentTimeMillis));
                    startActivity(new Intent(mcontext, MainActivity.class));
                } catch (JSONException ex) {
                    Log.d(TAG, ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

//                String body;
//                //get status code here
//                String statusCode = String.valueOf(error.networkResponse.statusCode);
//                //get response body and parse with appropriate encoding
//                if(error.networkResponse.data!=null) {
//                    try {
//                        body = new String(error.networkResponse.data,"UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                builder.setMessage("Login Again to Conitnue...").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(mcontext, LoginActivity.class));
                    }
                });


                try {
                    builder.show();
                }

                catch (Exception e) {
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

    }

}
