package com.orange.place;

import java.util.HashMap;

import com.github.droidfu.DroidFuApplication;

// to use the droid-fu 3pp lib, need use/declare DroidFuApplication
public class OrangePlaceApplication extends DroidFuApplication {
	
	// Global variables
	private static HashMap<String, Object> gVar = new HashMap<String, Object>();
	
	public static Object getGlobalVar(String key) {
		return gVar.get(key);
	}
	
	public static void setGlobalVar(String key, String value){
		gVar.put(key, value);
	}
}
