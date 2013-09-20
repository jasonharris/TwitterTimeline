package com.example.twittertimeline;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import twitter4j.Status;
import twitter4j.User;

import com.example.twittertimeline.TaskFragment.TaskCallbacks;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements TaskCallbacks {

    private static final String STATUS_KEY = "statuses";

    private TaskFragment mTaskFragment;
    private ListFragment mTimelineList;
    private List<Status> mStatuses;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mStatuses = (ArrayList<Status>) savedInstanceState.getSerializable(STATUS_KEY);
        }

        FragmentManager fm = getSupportFragmentManager();
        mTimelineList = (ListFragment) fm.findFragmentById(R.id.fragmentTimeline);

        if(mStatuses != null) {
            mTimelineList.setListAdapter(new TimelineAdapter(this, mStatuses));
        }

        mTaskFragment = (TaskFragment) fm.findFragmentByTag("task");

        // Create the Fragment if it hasn't been retained.
        if (mTaskFragment == null) {
            mTaskFragment = new TaskFragment();
            fm.beginTransaction().add(mTaskFragment, "task").commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* 
     * {@inheritDoc}
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(STATUS_KEY, (Serializable) mStatuses);
    }

    @Override
    public void onPreExecute() {
        // no action

    }

    @Override
    public void onProgressUpdate(int percent) {
        // no action

    }

    @Override
    public void onCancelled() {
        // no action

    }

    @Override
    public void onPostExecute(List<Status> result) {
        mStatuses = result;
        mTimelineList.setListAdapter(new TimelineAdapter(this, result));
    }

    public class TimelineAdapter extends ArrayAdapter<Status> {


        public TimelineAdapter(Context context, List<Status> result) {
            super(context, R.id.action_settings, result);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder = null;

            if(row == null) {
                LayoutInflater inflater = 
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.status, parent, false);
                holder = new ViewHolder();
                holder.textName = (TextView) row.findViewById(R.id.textUserName);
                holder.textTweet = (TextView) row.findViewById(R.id.textTweet);
                holder.imgAvatar = (ImageView) row.findViewById(R.id.imgAvatar);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            Status item = getItem(position);
            if (item.isRetweet()) item = item.getRetweetedStatus();
            holder.textTweet.setText(item.getText());
            Linkify.addLinks(holder.textTweet, Linkify.ALL);
            final User user = item.getUser();
            if (user != null) {
                holder.textName.setText(user.getName());
                Picasso.with(getContext())
                       .load(user.getBiggerProfileImageURL())
                       .into(holder.imgAvatar);
                
            }
            
            final String url = (user != null) ? user.getURL() : null;
            holder.imgAvatar.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if(url !=null) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.no_url), Toast.LENGTH_LONG).show();
                    }
                }

            });
            return row;
        }


    }

    static class ViewHolder {
        TextView textName;
        TextView textTweet;
        ImageView imgAvatar;
    }
}
