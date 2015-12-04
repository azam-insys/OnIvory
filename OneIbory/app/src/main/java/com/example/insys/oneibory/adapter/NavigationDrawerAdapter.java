package com.example.insys.oneibory.adapter;

/**
 * Created by Aazam on 29/07/15.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.insys.oneibory.R;
import com.example.insys.oneibory.Utility.Constant;
import com.example.insys.oneibory.activities.WebView;
import com.example.insys.oneibory.model.NavDrawerItem;

import java.util.Collections;
import java.util.List;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Object> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    private final int Categories = 0, Footer = 1;
    private Intent webintent;

    public NavigationDrawerAdapter(Context context, List<Object> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof NavDrawerItem) {
            return Categories;
        }
        if (data.get(position) instanceof Integer) {
            return Footer;
        }
        return -1;
    }


    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case Categories:
                View v1 = inflater.inflate(R.layout.nav_drawer_row, parent, false);
                viewHolder = new MyViewHolder(v1);
                break;
            case Footer:
                View v2 = inflater.inflate(R.layout.about_layout, parent, false);
                viewHolder = new MyViewHolder2(v2);
                break;
            default:

                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {

            case Categories:
                MyViewHolder vh1 = (MyViewHolder) viewHolder;
                configureViewHolder1(vh1, position);
                break;

            case Footer:
                MyViewHolder2 vh2 = (MyViewHolder2) viewHolder;
                configureViewHolder2(vh2);
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title ,tt;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.drawer_category);




        }
    }


    class MyViewHolder2 extends RecyclerView.ViewHolder {
        TextView txtview[] = new TextView[6];

        public MyViewHolder2(View itemView) {
            super(itemView);

            txtview[0] = (TextView) itemView.findViewById(R.id.tv_about_us);
            txtview[1] = (TextView) itemView.findViewById(R.id.tv_contact_us);
            txtview[2] = (TextView) itemView.findViewById(R.id.tv_myaccount);
            txtview[3] = (TextView) itemView.findViewById(R.id.tv_chnage_pass);
            txtview[4] = (TextView) itemView.findViewById(R.id.tv_track_order);
            txtview[5] = (TextView) itemView.findViewById(R.id.tv_trm_conditions);

            txtview[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webintent = new Intent(context, WebView.class);
                    webintent.putExtra("url", Constant.About_US);
                    context.startActivity(webintent);
                }
            });

            txtview[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webintent = new Intent(context, WebView.class);
                    webintent.putExtra("url", Constant.contact_us);
                    context.startActivity(webintent);
                }
            });

            txtview[2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webintent = new Intent(context, WebView.class);
                    webintent.putExtra("url", Constant.MyAccount);
                    context.startActivity(webintent);
                }
            });

            txtview[3].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    webintent = new Intent(context, WebView.class);
                    webintent.putExtra("url", Constant.Chnage_password);
                    context.startActivity(webintent);

                }
            });

            txtview[4].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    webintent = new Intent(context, WebView.class);
                    webintent.putExtra("url", Constant.Track_order);
                    context.startActivity(webintent);

                }
            });


            txtview[5].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webintent = new Intent(context, WebView.class);
                    webintent.putExtra("url", Constant.Privacy_Policy);
                    context.startActivity(webintent);

                }
            });

        }
    }


    private void configureViewHolder1(MyViewHolder vh1, int position) {
        NavDrawerItem user = (NavDrawerItem) data.get(position);
        if (user != null) {
            vh1.title.setText(user.getTitle());
            Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(),
                    "RALEWAY-MEDIUM.TTF");
            vh1.title.setTypeface(typeface);
        }
    }

    private void configureViewHolder2(MyViewHolder2 vh2) {
//        vh2.derfdre.setImageResource(R.drawable.userpic);
    }

}
