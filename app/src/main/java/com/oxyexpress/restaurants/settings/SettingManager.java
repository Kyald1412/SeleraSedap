package com.oxyexpress.restaurants.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.oxyexpress.restaurants.constanst.Constants;

public class SettingManager implements ISettingConstants {

	public static final String TAG = SettingManager.class.getSimpleName();

	public static final String DYSONIT_SHARPREFS = "SHARED_PREFERENCES";

	private SharedPreferences prefs;

	private Context context;

	public static final String FIREBASE_CLOUD_MESSAGING = "fcm";

	public static final String SET_NOTIFY = "set_notify";

	public SettingManager(Context context){
		this.context = context;
		prefs = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
	}

	public void saveNotificationSubscription(String value, boolean bool){
		SharedPreferences.Editor edits = prefs.edit();
		edits.putString(Constants.UNIQUE_ID, value);
		edits.putBoolean(Constants.REGISTERED, bool);
		edits.apply();
	}

	public void saveuniqueid(String value){
		SharedPreferences.Editor edits = prefs.edit();
		edits.putString(Constants.unik, value);
		edits.apply();
	}

	public boolean isRegistered(){
		return prefs.getBoolean(Constants.REGISTERED, false);
	}

	public String unik(){
		return prefs.getString(Constants.unik, null);
	}

	public void clearAllSubscriptions(){
		prefs.edit().clear().apply();
	}




	public static void saveSetting(Context mContext, String mKey, String mValue) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(DYSONIT_SHARPREFS, Context.MODE_PRIVATE);
		Editor editor = mSharedPreferences.edit();
		editor.putString(mKey, mValue);
		editor.commit();
	}

	public static String getSetting(Context mContext, String mKey, String mDefValue) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(DYSONIT_SHARPREFS, Context.MODE_PRIVATE);
		return mSharedPreferences.getString(mKey, mDefValue);
	}

}
