package com.keepingatimeline.kat;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;

/**
 * Created by Jimmy on 5/11/2016.
 * Made useful by Trevor on 5/17/2016.
 */
public class TimelineAdapter extends BaseAdapter implements ListAdapter {

    private Context ctx;
    private ArrayList<String> tlTitles;
    private ArrayList<String> tlMembers;

    public TimelineAdapter(Context context, ArrayList<String> tlTitles, ArrayList<String> tlMembers)
    {
        super();

        this.ctx = context;
        this.tlTitles = tlTitles;
        this.tlMembers = tlMembers;
    }

    @Override
    public int getCount()
    {
        return tlTitles.size();
    }

    @Override
    public String getItem(int position)
    {
        return tlTitles.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }


    public View getView(final int position, View convertView, ViewGroup parent)
    {

        if (convertView == null) {
            LayoutInflater listInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = listInflater.inflate(R.layout.main_timelines, parent, false);
        }

        TextView textL = (TextView)convertView.findViewById(R.id.timelineTitle);
        textL.setText(tlTitles.get(position));

        /*
        Context context = parent.getContext();
        Typeface myCustomFont = Typeface.createFromAsset(context.getAssets(), parent.getContext().getString(R.string.primaryFont));
        textL.setTypeface(myCustomFont);
        */

        TextView textS = (TextView)convertView.findViewById(R.id.timelineMembers);
        textS.setText(tlMembers.get(position));

        return convertView;
    }
}