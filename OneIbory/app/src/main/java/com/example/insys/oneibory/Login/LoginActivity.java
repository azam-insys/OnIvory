package com.example.insys.oneibory.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.insys.oneibory.activities.MainActivity;
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
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private CommonMethod networkcheck = null;
    private String TAG = LoginActivity.class.getSimpleName();
    private JSONObject userobject;
    private Context mcontext = LoginActivity.this;
    private String tag_json_obj = "jobj_req";
    private Button loginButton = null;
    private EditText login_et_email, login_et_pass;
    private ProgressDialog pDialog;
    private android.webkit.WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        findviews();
        networkcheck = new CommonMethod(mcontext);
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login_btn:
                if (login_et_email.getText().toString().length() > 1 && login_et_pass.getText().toString().length() > 1) {
                    makejsonRequest(v);
                    loginButton.setText("Please Wait...");
                } else {
                    Toast.makeText(mcontext, "Email or Password is Empty", Toast.LENGTH_SHORT).show();
                }

                break;

            default:
                break;
        }
    }

    private void makejsonRequest(final View view) {
//        showProgressDialog();

        try {
            userobject = new JSONObject();
            userobject.accumulate("username", login_et_email.getText().toString());
            userobject.accumulate("password", login_et_pass.getText().toString());

        } catch (Exception ex) {

        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST, Constant.Login_URL,
                userobject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    PrefsUtility.saveToPrefs(mcontext, PrefsUtility.PREFS_LOGIN_Name, response.getString("Name"));
                    PrefsUtility.saveToPrefs(mcontext, PrefsUtility.PREFS_LOGIN_SessionId, response.getString("Sessionid"));
                    PrefsUtility.saveToPrefs(mcontext, PrefsUtility.PREFS_LOGIN_Mobile, response.getString("mobile"));

                    PrefsUtility.saveToPrefs(mcontext, PrefsUtility.PREFS_LOGIN_USERNAME_KEY, EncryptionUtil.encrypt(login_et_email.getText().toString()));
                    PrefsUtility.saveToPrefs(mcontext, PrefsUtility.PREFS_LOGIN_PASSWORD, EncryptionUtil.encrypt(login_et_pass.getText().toString()));
                    startActivity(new Intent(mcontext, MainActivity.class));
                    enaleLoginForWeb();
                } catch (JSONException ex) {
                    Log.d(TAG, ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loginButton.setText("Login");
                Snackbar.make(view, "Invalid Username or Password", Snackbar.LENGTH_INDEFINITE).show();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("name", "Androidhive");
//                params.put("email", "abc@androidhive.info");
//                params.put("pass", "password123");

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    private void findviews() {
        loginButton = (Button) findViewById(R.id.login_btn);
        loginButton.setOnClickListener(this);
        login_et_email = (EditText) findViewById(R.id.input_email);
        login_et_pass = (EditText) findViewById(R.id.input_password);
        webview = (android.webkit.WebView) findViewById(R.id.login_webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(android.webkit.WebView view, String url) {
                finish();
            }
        });
    }

//    private void showProgressDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }
//
//    private void hideProgressDialog() {
//        if (pDialog.isShowing())
//            pDialog.hide();
//    }

    private void enaleLoginForWeb() {
        try {

            long myCurrentTimeMillis = System.currentTimeMillis();
            PrefsUtility.saveToPrefs(mcontext, PrefsUtility.PREFS_LOGIN_TIME, String.valueOf(myCurrentTimeMillis));

            String postData = userobject.toString();
            webview.postUrl(
                    "http://oneivory.xhtmlxpert.com/apilogin/index/login",
                    EncodingUtils.getBytes(postData, "BASE64"));


        } catch (Exception ex) {

        }

    }

}
