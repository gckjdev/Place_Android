package com.orange.place.tasks.async;

import android.content.Context;

import com.github.droidfu.concurrent.BetterAsyncTask;
import com.github.droidfu.concurrent.BetterAsyncTaskCallable;
import com.orange.place.constants.Constants;
import com.orange.place.tasks.PlaceTask;

public class AsyncGetFollowedPlacesCallable implements BetterAsyncTaskCallable<Void, Void, Integer> {
	private Context context;
	
	public AsyncGetFollowedPlacesCallable(Context context){
		this.context = context;
	}
	
	@Override
	public Integer call(BetterAsyncTask<Void, Void, Integer> task) throws Exception {
		int resultCode = Constants.ERROR_UNKOWN;
		resultCode = PlaceTask.getFollowedPlacesFromServer(context);
		return resultCode;
	}
}

