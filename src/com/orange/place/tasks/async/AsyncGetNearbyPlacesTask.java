package com.orange.place.tasks.async;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.github.droidfu.concurrent.BetterAsyncTask;
import com.orange.place.Activity_Main_Places;
import com.orange.place.constant.ErrorCode;
import com.orange.place.constants.Constants;

public class AsyncGetNearbyPlacesTask extends BetterAsyncTask<Void, Void, Integer> {
	private Location location;

	public AsyncGetNearbyPlacesTask(Context context) {
		super(context);
	}

	@Override
	protected void handleError(Context context, Exception error) {
		Log.e(Constants.LOG_TAG, "Error happened in " + getClass().getName(), error);
	}

	@Override
	protected void after(Context context, Integer taskResult) {
		if (taskResult == ErrorCode.ERROR_SUCCESS) {
			((Activity_Main_Places) context).updateNearbyPlacesView();
		} else {
			Log.w(Constants.LOG_TAG, getClass().getName() + " failed, nothing need to update!");
		}
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
