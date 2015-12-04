package com.example.insys.oneibory.horizontal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.insys.oneibory.R;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    Context context;
    private ArrayList<String> list;
    int layoutId;
    public View view;
    private Holder holder;
    //	String newsSource;
    int prevpos;
    static int lastpos = 0;
    String type;
    Intent webintent;

    private Integer[] minteger1 = {R.drawable.air_water_syringe, R.drawable.c, R.drawable.alfanet_alignet};
    private Integer[] minteger2 = {R.drawable.cavit_g, R.drawable.supermax_gloves, R.drawable.k_files};
    private Integer[] minteger3 = {R.drawable.ic_pending_deliver, R.drawable.ic_pending_delivery, R.drawable.ic_pending_deliverya};
    private Integer[] minteger4 = {R.drawable.ic_past_orders, R.drawable.ic_past_orders, R.drawable.ic_past_orders};

    private String[] imagetext = {"Equipments", "General Denstitry", "Endodontics"};
    private LayoutInflater layoutinflater;

    public CustomListAdapter(Context context, int textViewResourceId, String type) {
        this.context = context;
//        this.prevpos = pos;
        layoutId = textViewResourceId;
        this.type = type;

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (type.equals("Categories")) {

                convertView = layoutinflater.inflate(R.layout.news_list_item, null);
            } else
                convertView = layoutinflater.inflate(R.layout.recentlyviewd_items, null);

            holder = new Holder();
            holder.title = (ImageView) convertView.findViewById(R.id.txtNewsSource);
            holder.tv_category = (TextView) convertView.findViewById(R.id.category_txt);


            if (holder.tv_category != null) {

                Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(),
                        "RALEWAY-SEMIBOLD.ttf");
                holder.tv_category.setTypeface(typeface);

            }


            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
            holder.title.setTag(position);


        }


        if (type.equals("Categories")) {
            holder.title.setImageResource(minteger1[position]);
            holder.tv_category.setText(imagetext[position]);
        } else if (type.equals("RECENTLY VIEWD ITEMS")) {
//            holder.title.setImageResource(minteger2[position]);
            holder.tv_category.setText(imagetext[position]);
        } else if (type.equals("View Past Orders")) {
//            holder.title.setImageResource(minteger3[position]);
            holder.tv_category.setVisibility(View.INVISIBLE);
        } else if (type.equals("Past Order")) {
            holder.title.setImageResource(minteger4[position]);
//            holder.tv_category.setText(getimagetext3[position]);
        }


        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (type) {

                    case "Due Re-orders":

                        Snackbar.make(v, "In Progress", Snackbar.LENGTH_SHORT).show();

//                        webintent = new Intent(context, WebView.class);
//                        webintent.putExtra("url", "http://coronadentalnetwork.com/index.php/seal-tight-1500-tips-disposable-air-water-syringe-tips-bendable-adapter-required-this.html");
//                        context.startActivity(webintent);
                        break;

                    case "Deal on Products Bought":
//                        webintent = new Intent(context, WebView.class);
//                        webintent.putExtra("url", "http://coronadentalnetwork.com/index.php/cavit-g-single-jar-gray-soft-temporary-filling-material-self-cure-28-gm-jar.html");
//                        context.startActivity(webintent);
                        Snackbar.make(v, "In Progress", Snackbar.LENGTH_SHORT).show();

                        break;

                    case "Pending Deliveries":
//                        webintent = new Intent(context, WebView.class);
//                        webintent.putExtra("url", "http://coronadentalnetwork.com/index.php/sales/order/history/");
//                        context.startActivity(webintent);
                        Snackbar.make(v, "In Progress", Snackbar.LENGTH_SHORT).show();
                        break;

                    case "Past Order":

//                        webintent = new Intent(context, WebView.class);
//                        webintent.putExtra("url", "http://coronadentalnetwork.com/index.php/sales/order/history/");
//                        context.startActivity(webintent);
                        Snackbar.make(v, "In Progress", Snackbar.LENGTH_SHORT).show();
                        break;

                }

            }
        });


        return convertView;
    }

    private static class Holder {
        private ImageView title;
        private TextView tv_category;

    }

//    public int getCurrentPosition() {
//        return currPosition;
//    }
}
