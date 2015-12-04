package com.example.insys.oneibory.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.insys.oneibory.Login.LoginActivity;
import com.example.insys.oneibory.R;
import com.example.insys.oneibory.Search.SearchActivity;
import com.example.insys.oneibory.Utility.Constant;
import com.example.insys.oneibory.Utility.EncryptionUtil;
import com.example.insys.oneibory.Utility.PrefsUtility;
import com.example.insys.oneibory.app.AppController;
import com.example.insys.oneibory.fragments.FragmentDrawer;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ActivityDynamicTabs extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private String TAG = getClass().getName();
    private String geturl = null;
    private Context mcontext = ActivityDynamicTabs.this;
    private ProgressDialog progressDialog;
    //    private MaterialTabHost mTabHost;
    private ViewPager mViewPager;
    ArrayList<String> tabs = new ArrayList<String>();
    ArrayList<String> tabsURL = new ArrayList<String>();
    android.webkit.WebView webview, weblogin;
    TextView tvMainCategory = null;
    private TabLayout tabLayout;
    ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvMainCategory = (TextView) findViewById(R.id.tv_main_category);
        setSupportActionBar(toolbar);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle _drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0);
        mDrawerLayout.setDrawerListener(_drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        _drawerToggle.syncState();


        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(ActivityDynamicTabs.this);

        tvMainCategory.setText("Equipments");
        tvMainCategory.setPaintFlags(tvMainCategory.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);


        tabs.add("");
        tabs.add("SubCat 1");
        tabs.add("SubCat 2");
        tabs.add("SubCat 3");
        tabs.add("SubCat 4");
        tabs.add("SubCat 5");

        tabsURL.add("http://oneivory.xhtmlxpert.com/endodontic-products.html?p=2");
        tabsURL.add(Constant.About_US);
        tabsURL.add(Constant.contact_us);
        tabsURL.add(Constant.MyAccount);
        tabsURL.add(Constant.Endodontics);
        tabsURL.add(Constant.GeneralDenstitiy);


        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(mViewPager);


        tvMainCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mViewPager.setCurrentItem(0);
                mAdapter.notifyDataSetChanged();
            }
        });


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tvMainCategory.setTextColor(Color.parseColor("#41a8ed"));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00000000"));


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                if (position > 0) {

                    tvMainCategory.setTextColor(Color.parseColor("#949494"));
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#41a8ed"));
                    tvMainCategory.setPaintFlags(0);

                } else {

                    tvMainCategory.setTextColor(Color.parseColor("#41a8ed"));
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00000000"));

                    tvMainCategory.setPaintFlags(tvMainCategory.getPaintFlags()
                            | Paint.UNDERLINE_TEXT_FLAG);

                }

            }


        });

//        for (int i = 0; i < mAdapter.getCount(); i++) {
//            materialTab = mTabHost.newTab();
//            materialTab.setText(tabs.get(i));
//            mTabHost.addTab(materialTab);
//
//
//
//        }
    }

    private void setupViewPager(ViewPager viewPager) {

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_search) {
            startActivity(new Intent(mcontext, SearchActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        }


        if (id == R.id.action_myaccount) {

            Intent webintent = new Intent(mcontext, WebView.class);
            webintent.putExtra("url", Constant.MyAccount);
            startActivity(webintent);

        }

        if (id == R.id.action_cart) {

            Intent webintent = new Intent(mcontext, WebView.class);
            webintent.putExtra("url", Constant.View_ShoppingCart);
            startActivity(webintent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        FragmentManager fragmentManager;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
        }

        public Fragment getItem(int num) {
            return new DummyFragment(tabsURL.get(num));

        }


        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }

    }


    private class CustomWebViewClient extends WebViewClient {
        public CustomWebViewClient() {
            super();
        }

        @Override
        public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
            showprogressDialogue();
            if (!url.equals("http://oneivory.xhtmlxpert.com/") && !url.equals("http://oneivory.xhtmlxpert.com/customer/account/logout/") && !url.equals("http://oneivory.xhtmlxpert.com/customer/account/logoutSuccess/")) {
//                updateSystem();
            }
        }

        @Override
        public void onPageFinished(android.webkit.WebView view, String url) {
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


            switch (url) {
                case "http://oneivory.xhtmlxpert.com/customer/account/logout/":
                    weblogin.postUrl("http://oneivory.xhtmlxpert.com/apilogin/index/logout", EncodingUtils.getBytes("", "BASE64"));
                    return false;
                case "http://oneivory.xhtmlxpert.com/customer/account/logoutSuccess/":

                    PrefsUtility.DeleteSharedPrefernce(mcontext);
                    Intent intent = new Intent(mcontext, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return false;
                case "http://oneivory.xhtmlxpert.com/":
                    return false;
                default:
                    view.loadUrl(url);
                    geturl = url;
                    showprogressDialogue();
                    return true;
            }
        }
    }

    private void updateSystem() {

        long mycurrenttime = System.currentTimeMillis();
        long savedTime = Long.parseLong(PrefsUtility.getFromPrefs(mcontext, PrefsUtility.PREFS_LOGIN_TIME, null));
        long diff = mycurrenttime - savedTime;
        // 50 min into - 3,000,000
        if (diff > 3000000) {
            dologin();
            PrefsUtility.saveToPrefs(mcontext, PrefsUtility.PREFS_LOGIN_TIME, String.valueOf(mycurrenttime));
        }
    }

    @SuppressLint("ValidFragment")
    public class DummyFragment extends Fragment {

        String geturl;

        DummyFragment(String url) {

            this.geturl = url;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.webview, container, false);
            webview = (android.webkit.WebView) layout.findViewById(R.id.custom_webview);
            weblogin = (android.webkit.WebView) layout.findViewById(R.id.login_webview_back);
            return layout;

        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

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

            JSONObject userobject = new JSONObject();
            String name = PrefsUtility.getFromPrefs(mcontext, PrefsUtility.PREFS_LOGIN_USERNAME_KEY, null);
            String pass = PrefsUtility.getFromPrefs(mcontext, PrefsUtility.PREFS_LOGIN_PASSWORD, null);
            String decryptedUserName = EncryptionUtil.decrypt(name);
            String decryptedPass = EncryptionUtil.decrypt(pass);

            userobject.accumulate("username", decryptedUserName);
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
            String tag_json_obj = "jobj_req";
            AppController.getInstance().addToRequestQueue(jsonObjReq,
                    tag_json_obj);


        } catch (Exception ex) {

            ex.printStackTrace();
        }


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
