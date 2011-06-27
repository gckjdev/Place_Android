package com.orange.place;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.github.droidfu.activities.BetterListActivity;
import com.orange.place.constants.Constants;
import com.orange.utils.ActivityUtil;

public class Activity_Info extends BetterListActivity {
	private List<Map<String, Object>> posts = new ArrayList<Map<String, Object>>();
	private SimpleAdapter postsAdapter;
	private ImageButton bPostNew;
	private Button bPostNearby;
	private Button bPostReplied;
	private Button bPostFollowed;
	private TextView tMore;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ActivityUtil.setNoTitle(this);
		ActivityUtil.setFullScreen(this);
		setContentView(R.layout.activity_info);
		lookupViewElements();

		postsAdapter = new SimpleAdapter(this, posts, R.layout.item_post, Constants.postsViewFrom,
				Constants.postsViewTo);
		setListAdapter(postsAdapter);
	}

	private void lookupViewElements() {
		tMore = (TextView) findViewById(R.id.more);
		bPostNew = (ImageButton) findViewById(R.id.post_new);
		bPostNearby = (Button) findViewById(R.id.post_around);
		bPostFollowed = (Button) findViewById(R.id.post_followed);
		bPostReplied = (Button) findViewById(R.id.post_replied);
	}
}