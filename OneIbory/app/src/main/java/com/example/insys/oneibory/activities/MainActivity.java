package com.example.insys.oneibory.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.insys.oneibory.R;
import com.example.insys.oneibory.Search.SearchActivity;
import com.example.insys.oneibory.Utility.Constant;
import com.example.insys.oneibory.app.AppController;
import com.example.insys.oneibory.fragments.FragmentDrawer;
import com.example.insys.oneibory.fragments.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private String tag_json_obj = "jobj_req";
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private Context mcontext = MainActivity.this;
    private String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findviews();
        makejsonRequest();

//        new LinkFB().execute();
        ActionBarDrawerToggle _drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0);
        mDrawerLayout.setDrawerListener(_drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        _drawerToggle.syncState();


        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
        displayView(0);


    }

    private void findviews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
//                title = getString(R.string.title_home);
                break;


            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

        }
    }


    private void makejsonRequest() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constant.CategoryUrl,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    Iterator<String> iter = response.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        try {
                            JSONObject value = (JSONObject) response.get(key);
                            String name = value.getString("name");
                            JSONObject ff = value.optJSONObject("subcategory");

                            if (ff != null) {

                                Iterator<String> iter1 = ff.keys();
                                while (iter1.hasNext()) {
                                    String key1 = iter1.next();
                                    try {
                                        JSONObject value1 = (JSONObject) ff.get(key1);
                                        String namqe2 = value1.getString("name");
                                        String url = value1.getString("url");
                                        Log.i(TAG, "FGFG");

                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            // Something went wrong!
                        }
                    }


                } catch (Exception ex) {
                    Log.d(TAG, ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "ERROR");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Accept-Charset", "utf-8");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }


}
