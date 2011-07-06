package com.orange.place.tasks.async;

import android.content.Context;
import android.location.Location;

import com.github.droidfu.concurrent.BetterAsyncTask;
import com.github.droidfu.concurrent.BetterAsyncTaskCallable;
import com.orange.place.constants.Constants;
import com.orange.place.tasks.PlaceTask;

public class AsyncGetNearbyPlacesCallable implements BetterAsyncTaskCallable<Void, Void, Integer> {
	private Context context;
	
	public AsyncGetNearbyPlacesCallable(Context context){
		this.context = context;
	}
	
	@Override
	public Integer call(BetterAsyncTask<Void, Void, Integer> task) throws Exception {
		int resultCode = Constants.ERROR_UNKOWN;
		Location location = ((AsyncGetNearbyPlacesTask) task).getLocation();
		resultCode = PlaceTask.getNearbyPlacesFromServer(context, location);
		return resultCode;
	}
}