package com.example.insys.oneibory.fragments;

/**
 * Created by Aazam on 29/07/15.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.insys.oneibory.R;
import com.example.insys.oneibory.horizontal.CenterLockHorizontalScrollview;
import com.example.insys.oneibory.horizontal.CustomListAdapter;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private ArrayList<String> headerlist = new ArrayList<String>();
    //    HomeScreenAdapter adapter = null;
    private CenterLockHorizontalScrollview centerLockHorizontalScrollview, centerLockHorizontalScrollview1, centerLockHorizontalScrollview2, centerLockHorizontalScrollview3;
    View rootView;
    CustomListAdapter customListAdapter;
    private Context context;
    private TextView docName, docMobile;
    private Button reorderBtn;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        centerLockHorizontalScrollview = (CenterLockHorizontalScrollview) rootView.findViewById(R.id.scrollView);
        centerLockHorizontalScrollview1 = (CenterLockHorizontalScrollview) rootView.findViewById(R.id.scrollView1);
        centerLockHorizontalScrollview2 = (CenterLockHorizontalScrollview) rootView.findViewById(R.id.scrollView2);
//        centerLockHorizontalScrollview3 = (CenterLockHorizontalScrollview) rootView.findViewById(R.id.scrollView3);

        reorderBtn = (Button) rootView.findViewById(R.id.btn_reorder);
        context = this.getActivity();
        headerlist.add("Categories");
        headerlist.add("RECENTLY VIEWD ITEMS");
        headerlist.add("View Past Orders");
        headerlist.add("Past Order");
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        for (int i = 0; i < headerlist.size(); i++)
            switch (i) {

                case 0:
                    customListAdapter = new CustomListAdapter(context,
                            R.layout.news_list_item, headerlist.get(i));
                    centerLockHorizontalScrollview.setAdapter(context, customListAdapter);

                    break;

                case 1:

                    customListAdapter = new CustomListAdapter(context,
                            R.layout.recentlyviewd_items, headerlist.get(i));
                    centerLockHorizontalScrollview1.setAdapter(context, customListAdapter);

                    break;

                case 2:

                    customListAdapter = new CustomListAdapter(context,
                            R.layout.recentlyviewd_items, headerlist.get(i));
                    centerLockHorizontalScrollview2.setAdapter(context, customListAdapter);

                    break;
            }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
