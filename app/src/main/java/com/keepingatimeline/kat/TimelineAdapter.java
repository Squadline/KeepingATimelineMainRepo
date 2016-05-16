package com.keepingatimeline.kat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
 * Created by boredguy88 on 5/11/2016.
 */
public class TimelineAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> holder;
    private ArrayList<String> small;
    private Context context;
    private Firebase db;
    private Button b;


    public TimelineAdapter(ArrayList<String> h, Context c)
    {
        super();
        this.holder = h;
        this.context = c;
    }

    public TimelineAdapter(ArrayList<String> h, ArrayList<String> s, Context c)
    {
        super();
        this.holder = h;
        this.small = s;
        this.context = c;
    }

    @Override
    public int getCount()
    {
        Log.d("getView", "Count: " + holder.size());
        return holder.size();
    }

    @Override
    public String getItem(int position)
    {
        return holder.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }


    public View getView(final int position, View convertView, ViewGroup parent)
    {
        Log.d("getView", "getView was called, " + position);

        View v = convertView;
        if (v == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.timelineshower, null);
        }

        //b = (Button) v.findViewById(R.id.tButton);
        //b.setOnClickListener(new View.OnClickListener() {
          //  @Override
          //  public void onClick(View v) {
            //    Intent newActivity = new Intent("com.keepingatimeline.kat.MainScreen");
             //   context.startActivity(newActivity);
            //}
        //});

        TextView textL = (TextView)v.findViewById(R.id.tName);
        textL.setText(holder.get(position));

        TextView textS = (TextView)v.findViewById(R.id.tAuthor);
        textS.setText(small.get(position));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Timeline: " + holder.get(position));
                alert.setMessage("Do you want to enter this timeline?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent newActivity = new Intent("com.keepingatimeline.kat.ViewTimeline");
                        context.startActivity(newActivity);
                    }
                })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //just cancel
                            }
                        });
                alert.create();
                alert.show();
            }
        });

        return v;

    }


}
