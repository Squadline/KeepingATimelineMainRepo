package com.keepingatimeline.kat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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

        final CircleImageView squadCircleView = (CircleImageView) convertView.findViewById(R.id.squad_image);


        TextView textL = (TextView)convertView.findViewById(R.id.timelineTitle);
        textL.setText(timelines.get(position).getTitle());


        Typeface tlTitleFont = Typeface.createFromAsset(ctx.getAssets(), ctx.getString(R.string.RobotoMedium));
        textL.setTypeface(tlTitleFont);


        TextView textS = (TextView)convertView.findViewById(R.id.timelineMembers);
        textS.setText(timelines.get(position).getMembers());

        TextView textD = (TextView) convertView.findViewById(R.id.recentEvent);
        textD.setText(timelines.get(position).getLastmodified());

        Bitmap bm_image = BitmapCache.getBitmapFromMemCache(timelines.get(position).getId());

        Log.d("Loading Thumbnails", "Updating Bitmaps: " + timelines.get(position).getTitle());
        if(bm_image != null) {
            //make the change to the timeline pic down here
            Log.d("Loading Thumbnails", "Setting Bitmaps: " + timelines.get(position).getTitle());
            squadCircleView.setImageBitmap(bm_image);
        }
        else {
            squadCircleView.setImageResource(R.drawable.default_squad);
        }

        return convertView;
    }
}