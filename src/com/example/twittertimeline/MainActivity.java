package com.example.twittertimeline;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	public static final String TWITTER_CONSUMER_KEY = "4AH9yyCAuLL2ehB8NkZMQ";
	public static final String TWITTER_CONSUMER_SECRET = "6X1vWIVaNlpwh1dusCyqJSvqTqYikxl6n7edqeSDA";
	public static final String TWITTER_ACCESS_KEY = "353147855-dZSVNXNUri48lF2w2KRrPjO2DKbOVT9Sl4qrsAzR";
	public static final String TWITTER_ACCESS_SECRET = "1wNuvNl9Cpnkqt2NC3luYGQlLkSf7oRSMhUop1pKZ0";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new GetTimeline().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class GetTimeline extends AsyncTask<Void, Void, List<Status>> {

		@Override
		protected List<twitter4j.Status> doInBackground(Void... arg0) {
			ConfigurationBuilder builder = new ConfigurationBuilder();
		    builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
		    builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
		    builder.setOAuthAccessToken(TWITTER_ACCESS_KEY);
		    builder.setOAuthAccessTokenSecret(TWITTER_ACCESS_SECRET);
		    Configuration configuration = builder.build();
		    TwitterFactory factory = new TwitterFactory(configuration);
		    Twitter twitter = factory.getInstance();
		    Paging page = new Paging();
		    page.setCount(100);
		    
		    ArrayList<twitter4j.Status> statuses = new ArrayList<twitter4j.Status>();
		    try {
				statuses = (ArrayList<twitter4j.Status>) twitter.getUserTimeline("SpartzInc", page);
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    for (twitter4j.Status status : statuses) {
		        System.out.println("title is : " + status.getText());
		    }
			return null;
		}
		
	}
}
