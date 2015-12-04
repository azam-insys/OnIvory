package com.example.insys.oneibory.fragments;

/**
 * Created by Aazam on 29/10/15.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.insys.oneibory.Login.LoginActivity;
import com.example.insys.oneibory.R;
import com.example.insys.oneibory.Utility.CommonMethod;
import com.example.insys.oneibory.Utility.Constant;
import com.example.insys.oneibory.Utility.PrefsUtility;
import com.example.insys.oneibory.activities.ActivityDynamicTabs;
import com.example.insys.oneibory.activities.MainActivity;
import com.example.insys.oneibory.activities.WebView;
import com.example.insys.oneibory.adapter.NavigationDrawerAdapter;
import com.example.insys.oneibory.model.NavDrawerItem;

import org.apache.http.util.EncodingUtils;

import java.util.ArrayList;
import java.util.List;

public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    private TextView docName;
    private RelativeLayout rlLogout;
    private Context context;
    private Intent webintent;
    private CommonMethod networkManagerCheck;
    private android.webkit.WebView webview;
    private ProgressDialog progressDialog;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<Object> getData() {
        List<Object> data = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);

        }
        // adding integer only help to determine and help to inflate two layout in drawer layout
        data.add(2);
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        docName = (TextView) layout.findViewById(R.id.tv_draewer_doc_name);
        webview = (android.webkit.WebView) layout.findViewById(R.id.custom_webview1);
        String name = PrefsUtility.getFromPrefs(getActivity(), PrefsUtility.PREFS_LOGIN_Name, null);
        docName.setText(name);


        TextView home = (TextView) layout.findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (context instanceof MainActivity) {
                } else {
                    ((Activity) context).finish();
                }

                mDrawerLayout.closeDrawer(containerView);


            }
        });


        rlLogout = (RelativeLayout) layout.findViewById(R.id.logout_rl);
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSystem();

            }
        });

        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override

            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
                networkManagerCheck = new CommonMethod(context);

                if (networkManagerCheck.isNetworkAvailable()) {
                    switch (position) {

                        case 0:

//                            webintent = new Intent(context, WebView.class);
//                            webintent.putExtra("url", Constant.Equipment);
//                            context.startActivity(webintent);
                            startActivity(new Intent(context, ActivityDynamicTabs.class));


                            break;

                        case 1:

                            webintent = new Intent(context, WebView.class);
                            webintent.putExtra("url", Constant.GeneralDenstitiy);
                            context.startActivity(webintent);


                            break;

                        case 2:

                            webintent = new Intent(context, WebView.class);
                            webintent.putExtra("url", Constant.Endodontics);
                            context.startActivity(webintent);

                            break;

                        case 3:

                            webintent = new Intent(context, WebView.class);
                            webintent.putExtra("url", Constant.Prosthodontics);
                            context.startActivity(webintent);

                            break;

                        case 4:

                            webintent = new Intent(context, WebView.class);
                            webintent.putExtra("url", Constant.Periodontics);
                            context.startActivity(webintent);


                            break;

                        case 5:

                            webintent = new Intent(context, WebView.class);
                            webintent.putExtra("url", Constant.Pedodontics);
                            context.startActivity(webintent);

                            break;

                        case 6:


                            webintent = new Intent(context, WebView.class);
                            webintent.putExtra("url", Constant.Restorative_Dentistry);
                            context.startActivity(webintent);


                            break;

                        case 7:

                            webintent = new Intent(context, WebView.class);
                            webintent.putExtra("url", Constant.Oral_Surgery);
                            context.startActivity(webintent);

                            break;

                        case 8:

                            webintent = new Intent(context, WebView.class);
                            webintent.putExtra("url", Constant.Orthodontics);
                            context.startActivity(webintent);

                            break;

                        default:

                    }
                } else {

                    Snackbar.make(view, "Internet is not available", Snackbar.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return layout;
    }

    private void UpdateSystem() {


        long mycurrenttime = System.currentTimeMillis();
        long savedTime = Long.parseLong(PrefsUtility.getFromPrefs(context, PrefsUtility.PREFS_LOGIN_TIME, null));
        long diff = mycurrenttime - savedTime;
        // 50 min into - 3,000,000
        if (diff < 3000000) {

            webview.postUrl("http://uat.oneivory.com/apilogin/index/logout", EncodingUtils.getBytes("", "BASE64"));
            webview.setWebViewClient(new WebViewClient() {


                @Override
                public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {

                    showprogressDialogue();

                }


                @Override
                public void onPageFinished(android.webkit.WebView view, String url) {
//                        super.onPageFinished(view, url);


                    try {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    PrefsUtility.DeleteSharedPrefernce(getActivity());
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    getActivity().finish();
                }
            });


        }


    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }

    private void showprogressDialogue() {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }
}
