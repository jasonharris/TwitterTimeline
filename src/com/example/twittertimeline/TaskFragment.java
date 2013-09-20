package com.example.twittertimeline;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * This Fragment manages a single background task and retains 
 * itself across configuration changes.
 */
public class TaskFragment extends Fragment {

    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    static interface TaskCallbacks {
        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute(List<Status> result);
    }

    private TaskCallbacks mCallbacks;
    private GetTimeline mTask;

    /**
     * A retained Fragment to keep ongoing work throughout the Activity life cycle.
     */
    public TaskFragment() {
        // Empty constructor per Fragment documentation
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        mTask = new GetTimeline();
        mTask.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private class GetTimeline extends AsyncTask<Void, Void, List<twitter4j.Status>> {

        public static final String TWITTER_CONSUMER_KEY = "4AH9yyCAuLL2ehB8NkZMQ";
        public static final String TWITTER_CONSUMER_SECRET = "6X1vWIVaNlpwh1dusCyqJSvqTqYikxl6n7edqeSDA";
        public static final String TWITTER_ACCESS_KEY = "353147855-dZSVNXNUri48lF2w2KRrPjO2DKbOVT9Sl4qrsAzR";
        public static final String TWITTER_ACCESS_SECRET = "1wNuvNl9Cpnkqt2NC3luYGQlLkSf7oRSMhUop1pKZ0";

        @Override
        protected List<twitter4j.Status> doInBackground(Void... ignore) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            builder.setOAuthAccessToken(TWITTER_ACCESS_KEY);
            builder.setOAuthAccessTokenSecret(TWITTER_ACCESS_SECRET);
            Configuration configuration = builder.build();
            TwitterFactory factory = new TwitterFactory(configuration);
            Twitter twitter = factory.getInstance();
            Paging page = new Paging();
            page.setCount(50);

            List<twitter4j.Status> statuses = new ArrayList<twitter4j.Status>();
            try {
                statuses = (List<twitter4j.Status>) twitter.getUserTimeline("SpartzInc", page);
            } catch (TwitterException e) {
                final Activity caller = getActivity();
                if(caller != null && !caller.isFinishing()) {
                    caller.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(caller, getString(R.string.error), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
            return statuses;
        }

        /* 
         * {@inheritDoc}
         */
        protected void onPostExecute(List<twitter4j.Status> result) {
            if (mCallbacks != null) {
                mCallbacks.onPostExecute(result);
            }
        }

    }

}
