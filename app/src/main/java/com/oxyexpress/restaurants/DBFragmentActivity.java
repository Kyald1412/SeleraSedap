package com.oxyexpress.restaurants;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.oxyexpress.restaurants.constanst.IMyRestaurantConstants;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.dialog.utils.AlertDialogUtils;
import com.ypyproductions.dialog.utils.AlertDialogUtils.IOnDialogListener;
import com.ypyproductions.dialog.utils.IDialogFragmentListener;
import com.ypyproductions.net.task.IDBCallback;
import com.ypyproductions.net.task.IDBConstantURL;
import com.oxyexpress.restaurants.R;
import com.ypyproductions.utils.ResolutionUtils;


public class DBFragmentActivity extends FragmentActivity implements IDBConstantURL,
	IDialogFragmentListener, IMyRestaurantConstants {

	public static final String TAG = DBFragmentActivity.class.getSimpleName();
	private ProgressDialog mProgressDialog;

	private int screenWidth;
	private int screenHeight;

	public Typeface mTypeFaceLight;
	public Typeface mTypeFaceBold;
	public Typeface mTypefaceNormal;
	public Typeface mTypefaceSologan;
	public Typeface mTypefaceAdmiration;
	public Typeface mTypefaceDroid;
	public Typeface mTypefacegeorgia;
	public Typeface mTypefaceSignika;

	public ArrayList<Fragment> mListFragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFormat(PixelFormat.RGBA_8888);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.createProgressDialog();

		mTypefaceNormal = Typeface.createFromAsset(getAssets(), "fonts/Elle Futura-Medium_0.otf");
		mTypeFaceLight = Typeface.createFromAsset(getAssets(), "fonts/Elle Futura-Light_0.otf");
		mTypeFaceBold = Typeface.createFromAsset(getAssets(), "fonts/Elle Futura-Heavy_0.otf");
		mTypefaceSologan = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams_BoldItalic.ttf");
		mTypefaceAdmiration = Typeface.createFromAsset(getAssets(), "fonts/admiration.ttf");
		mTypefaceDroid = Typeface.createFromAsset(getAssets(), "fonts/DroidSerif-Regular.ttf");
		mTypefacegeorgia = Typeface.createFromAsset(getAssets(), "fonts/Georgia.ttf");
		mTypefaceSignika = Typeface.createFromAsset(getAssets(), "fonts/Signika-Light.otf");


		int[] mRes = ResolutionUtils.getDeviceResolution(this);
		if (mRes != null && mRes.length == 2) {
			screenWidth = mRes[0];
			screenHeight = mRes[1];
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialogFragment(DIALOG_QUIT_APPLICATION);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showDialogFragment(int idDialog) {
		FragmentManager mFragmentManager = getSupportFragmentManager();
		switch (idDialog) {
		case DIALOG_LOSE_CONNECTION:
			createWarningDialog(DIALOG_LOSE_CONNECTION, R.string.title_warning, R.string.info_lose_internet).show(mFragmentManager, "DIALOG_LOSE_CONNECTION");
			break;
		case DIALOG_EMPTY:
			createWarningDialog(DIALOG_EMPTY, R.string.title_warning, R.string.info_empty).show(mFragmentManager, "DIALOG_EMPTY");
			break;
		case DIALOG_QUIT_APPLICATION:
			createQuitDialog().show(mFragmentManager, "DIALOG_QUIT_APPLICATION");
			break;
		case DIALOG_SEVER_ERROR:
			createWarningDialog(DIALOG_SEVER_ERROR, R.string.title_warning, R.string.info_server_error).show(mFragmentManager, "DIALOG_SEVER_ERROR");
			break;
		default:
			break;
		}
	}

	public DialogFragment createWarningDialog(int idDialog, int titleId, int messageId) {
		DBAlertFragment mDAlertFragment = DBAlertFragment.newInstance(idDialog, android.R.drawable.ic_dialog_alert, titleId, android.R.string.ok, messageId);
		return mDAlertFragment;
	}

	public void showWarningDialog(int titleId, int messageId) {
		Dialog mAlertDialog = AlertDialogUtils.createInfoDialog(this, 0, titleId, R.string.title_ok, messageId, null);
		mAlertDialog.show();
	}

	public void showWarningDialog(int titleId, String message) {
		Dialog mAlertDialog = AlertDialogUtils.createInfoDialog(this, 0, titleId, R.string.title_ok, message, null);
		mAlertDialog.show();
	}

	public void showInfoDialog(int titleId, String message) {
		Dialog mAlertDialog = AlertDialogUtils.createInfoDialog(this, 0, titleId, R.string.title_ok, message, null);
		mAlertDialog.show();
	}

	public void showInfoDialog(int titleId, String message, final IDBCallback mDBCallback) {
		Dialog mAlertDialog = AlertDialogUtils.createInfoDialog(this, 0, titleId, R.string.title_ok, message, new IOnDialogListener() {

			@Override
			public void onClickButtonPositive() {
				if (mDBCallback != null) {
					mDBCallback.onAction();
				}
			}

			@Override
			public void onClickButtonNegative() {
				if (mDBCallback != null) {
					mDBCallback.onAction();
				}
			}
		});
		mAlertDialog.show();
	}

	public void showFullDialog(int titleId, int message, int idPositive, int idNegative, final IDBCallback mDBCallback) {
		Dialog mAlertDialog = AlertDialogUtils.createFullDialog(this, -1, titleId, idPositive, idNegative, message, new IOnDialogListener() {

			@Override
			public void onClickButtonPositive() {
				if (mDBCallback != null) {
					mDBCallback.onAction();
				}
			}

			@Override
			public void onClickButtonNegative() {

			}
		});
		mAlertDialog.show();
	}

	public void showInfoDialog(int titleId, int message, final IDBCallback mDBCallback) {
		Dialog mAlertDialog = AlertDialogUtils.createInfoDialog(this, 0, titleId, R.string.title_ok, message, new IOnDialogListener() {

			@Override
			public void onClickButtonPositive() {
				if (mDBCallback != null) {
					mDBCallback.onAction();
				}
			}

			@Override
			public void onClickButtonNegative() {
				if (mDBCallback != null) {
					mDBCallback.onAction();
				}
			}
		});
		mAlertDialog.show();
	}

	private DialogFragment createQuitDialog() {
		int mTitleId = R.string.title_confirm;
		int mYesId = R.string.title_yes;
		int mNoId = R.string.title_no;
		int iconId = R.drawable.ic_launcher;
		int messageId = R.string.info_close_app;

		DBAlertFragment mDAlertFragment = DBAlertFragment.newInstance(DIALOG_QUIT_APPLICATION, iconId, mTitleId, mYesId, mNoId, messageId);
		return mDAlertFragment;

	}

	private void createProgressDialog() {
		this.mProgressDialog = new ProgressDialog(this);
		this.mProgressDialog.setIndeterminate(true);
		this.mProgressDialog.setCancelable(false);
		this.mProgressDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
	}

	public void showProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.setMessage(this.getString(R.string.loading));
			if (!mProgressDialog.isShowing()) {
				mProgressDialog.show();
			}
		}
	}

	public void showProgressDialog(int messageId) {
		if (mProgressDialog != null) {
			mProgressDialog.setMessage(this.getString(messageId));
			if (!mProgressDialog.isShowing()) {
				mProgressDialog.show();
			}
		}
	}

	public void showProgressDialog(String message) {
		if (mProgressDialog != null) {
			mProgressDialog.setMessage(message);
			if (!mProgressDialog.isShowing()) {
				mProgressDialog.show();
			}
		}
	}

	public void dimissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}


	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void showToast(int resId) {
		showToast(getString(resId));
	}

	public void showToast(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void showToastWithLongTime(int resId) {
		showToastWithLongTime(getString(resId));
	}

	public void showToastWithLongTime(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	@Override
	public void doPositiveClick(int idDialog) {
		switch (idDialog) {
		case DIALOG_QUIT_APPLICATION:
			onDestroyData();
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void doNegativeClick(int idDialog) {

	}

	public void onDestroyData() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// if (mImgFetcher != null) {
		// mImgFetcher.setExitTasksEarly(true);
		// mImgFetcher.closeCache();
		// mImgFetcher = null;
		// }
	}

	public void createArrayFragment() {
		mListFragments = new ArrayList<Fragment>();
	}

	public void addFragment(Fragment mFragment) {
		if (mFragment != null && mListFragments != null) {
			synchronized (mListFragments) {
				mListFragments.add(mFragment);
			}
		}
	}

	public boolean backStack(IDBCallback mCallback) {
		if (mListFragments != null && mListFragments.size() > 0) {
			int count = mListFragments.size();
			if (count > 0) {
				synchronized (mListFragments) {
					Fragment mFragment = mListFragments.remove(count - 1);
					if (mFragment != null) {
						if (mFragment instanceof DBFragment) {
							((DBFragment) mFragment).backToHome(this);
							((DBFragment) mFragment).backToHome(this);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}