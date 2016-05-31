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

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Jimmy on 5/11/2016.
 * Made useful by Trevor on 5/17/2016.
 */
public class TimelineAdapter extends BaseAdapter implements ListAdapter {

    private Context ctx;
    private ArrayList<Timeline> timelines;

    public TimelineAdapter(Context context, ArrayList<Timeline> timelines)
    {
        super();

        this.ctx = context;
        this.timelines = timelines;
    }

    @Override
    public int getCount()
    {
        return timelines.size();
    }

    @Override
    public String getItem(int position)
    {
        return timelines.get(position).getTitle();
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
        textL.setText(timelines.get(position).getTitle());


        Typeface tlTitleFont = Typeface.createFromAsset(ctx.getAssets(), ctx.getString(R.string.RobotoMedium));
        textL.setTypeface(tlTitleFont);


        TextView textS = (TextView)convertView.findViewById(R.id.timelineMembers);
        textS.setText(timelines.get(position).getMembers());

        TextView textD = (TextView) convertView.findViewById(R.id.recentEvent);
        textD.setText(timelines.get(position).getLastmodified());

        return convertView;
    }
}