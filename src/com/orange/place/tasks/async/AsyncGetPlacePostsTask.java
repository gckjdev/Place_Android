package com.orange.place.tasks.async;

import android.content.Context;
import android.util.Log;

import com.github.droidfu.concurrent.BetterAsyncTask;
import com.orange.place.Activity_PlacePosts;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constants.Constants;

public class AsyncGetPlacePostsTask extends BetterAsyncTask<Void, Void, Integer> {
	private String placeId;

	public AsyncGetPlacePostsTask(Context context) {
		super(context);
	}

	@Override
	protected void handleError(Context context, Exception error) {
		Log.e(Constants.LOG_TAG, "Error happened in " + getClass().getName(), error);
	}

	@Override
	protected void after(Context context, Integer taskResult) {
		if (taskResult == ErrorCode.ERROR_SUCCESS) {
			((Activity_PlacePosts) context).updatePlacePostsView();
		} else {
			Log.w(Constants.LOG_TAG, getClass().getName() + " failed, nothing need to update!");
		}
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
}