/**
 * TwitterTimeline
 * TimelineListFragment.java
 * 
 * @author Jason Harris on Sep 19, 2013
 * @copyright 2013 Poggled, Inc. All rights reserved
 * 
 * Typical Usage: 
 * 
 */
package com.example.twittertimeline;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 */
public class TimelineListFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setListAdapter(null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    
}
