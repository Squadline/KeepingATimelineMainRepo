package com.keepingatimeline.kat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Jimmy on 5/11/2016.
 * Made it useful by Trevor on 5/17/2016.
 */
public class TimelineAdapter extends BaseAdapter implements ListAdapter {

    private Context ctx;
    private ArrayList<String> tlTitles;
    private ArrayList<String> tlFriends;

    public TimelineAdapter(Context context, ArrayList<String> tlTitles, ArrayList<String> tlFriends)
    {
        super();

        this.ctx = context;
        this.tlTitles = tlTitles;
        this.tlFriends = tlFriends;
    }

    @Override
    public int getCount()
    {
        Log.d("getView", "Count: " + tlTitles.size());
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
        Log.d("getView", "getView was called, " + position);

        if (convertView == null) {
            LayoutInflater listInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = listInflater.inflate(R.layout.main_timelines, parent, false);
        }

        TextView textL = (TextView)convertView.findViewById(R.id.timelineTitle);
        textL.setText(tlTitles.get(position));

        TextView textS = (TextView)convertView.findViewById(R.id.timelineFriends);
        textS.setText(tlFriends.get(position));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newActivity = new Intent("com.keepingatimeline.kat.ViewTimeline");
                ctx.startActivity(newActivity);
            }

                /*
                AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                alert.setTitle("Timeline: " + tlTitles.get(position));
                alert.setMessage("Do you want to enter this timeline?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent newActivity = new Intent("com.keepingatimeline.kat.ViewTimeline");
                        ctx.startActivity(newActivity);
                    }
                })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing (cancel)
                            }
                        });
                alert.create();
                alert.show();
            } */
        });

        return convertView;

    }


}
