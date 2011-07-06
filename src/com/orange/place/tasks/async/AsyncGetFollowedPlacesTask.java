package com.orange.place.tasks.async;

import android.content.Context;
import android.util.Log;

import com.github.droidfu.concurrent.BetterAsyncTask;
import com.orange.place.Activity_Main_Places;
import com.orange.place.Activity_PlacePosts;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constants.Constants;

public class AsyncGetFollowedPlacesTask extends BetterAsyncTask<Void, Void, Integer> {
	public AsyncGetFollowedPlacesTask(Context context) {
		super(context);
	}

	@Override
	protected void handleError(Context context, Exception error) {
		Log.e(Constants.LOG_TAG, "Error happened in " + getClass().getName(), error);
	}

	@Override
	protected void after(Context context, Integer taskResult) {
		if (taskResult == ErrorCode.ERROR_SUCCESS) {
			if (context instanceof Activity_Main_Places) {
				((Activity_Main_Places) context).updateFollowedPlacesView();
			} else if (context instanceof Activity_PlacePosts) {
				((Activity_PlacePosts) context).updateFollowButton();
			}
		} else {
			Log.w(Constants.LOG_TAG, getClass().getName() + " failed, nothing need to update!");
		}
	}
}
