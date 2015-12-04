package com.example.insys.oneibory.Search;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

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
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aazam on 10/28/2015.
 */
public class SearchActivity extends AppCompatActivity {

    private String TAG = getClass().getName();
    private Toolbar toolbar;
    private EditText search_et;
    private WebView search_webview;
    private Context mcontext = SearchActivity.this;
    private String tag_json_obj = TAG ;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        search_et = (EditText) findViewById(R.id.et_search_view);
        search_webview = (WebView) findViewById(R.id.custom_webview);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
            }
        });

        search_et.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String input = search_et.getText().toString();
                    CommonMethod.hideSoftKeyboard(mcontext, search_et);
                    if (input != null) {

                        search_webview = (android.webkit.WebView) findViewById(R.id.custom_webview);
                        WebSettings webSettings = search_webview.getSettings();
                        webSettings.setJavaScriptEnabled(true);
                        search_webview.getSettings().setLoadWithOverviewMode(true);
                        search_webview.getSettings().setUseWideViewPort(true);

                        search_webview.setWebViewClient(new WebViewClient() {


                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                                super.onPageStarted(view, url, favicon);
                                updateSystem();
                                showprogressDialogue();
                            }



                            @Override
                            public void onLoadResource(WebView view, String url) {
                                super.onLoadResource(view, url);

//                                showprogressDialogue();
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                try {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                        progressDialog = null;
                                    }
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            }
                        });
                        search_webview.loadUrl(Constant.Search_result + input);
                    }

                    return true;
                }

                return false;
            }
        });

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();

    }

    private void showprogressDialogue() {

        if (progressDialog == null) {
            // in standard case YourActivity.this
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }


    private void updateSystem() {

        long mycurrenttime = System.currentTimeMillis();
        long savedTime = Long.parseLong(PrefsUtility.getFromPrefs(SearchActivity.this, PrefsUtility.PREFS_LOGIN_TIME, null));
        long diff = mycurrenttime - savedTime;
        // 50 min into - 3,000,000
        if (diff > 3000000) {
            //here the magic happens.
            dologin();
            PrefsUtility.saveToPrefs(mcontext, PrefsUtility.PREFS_LOGIN_TIME, String.valueOf(mycurrenttime));
        }

    }

    private void dologin() {
        try {
            JSONObject     userobject = new JSONObject();

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

                    search_webview.postUrl(
                            "http://oneivory.xhtmlxpert.com/apilogin/index/login",
                            EncodingUtils.getBytes(postData, "BASE64"));

                    search_webview.setWebViewClient(new WebViewClient() {

                        @Override
                        public void onPageFinished(android.webkit.WebView view, String url) {
                            super.onPageFinished(view, url);

                            if (!url.equals("\"http://oneivory.xhtmlxpert.com/customer/account/logout/")) {

                                search_webview.loadUrl(url);

                            }
                        }

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            showprogressDialogue();
                            return true;
                        }
                    });
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
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
}
