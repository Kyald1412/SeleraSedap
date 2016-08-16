
package com.oxyexpress.restaurants.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.oxyexpress.restaurants.MainActivity;
import com.oxyexpress.restaurants.R;
import com.oxyexpress.restaurants.constanst.IMyRestaurantConstants;
import com.oxyexpress.restaurants.object.OfferObject;
import com.oxyexpress.restaurants.parsingdata.JsonParsingUtils;
import com.oxyexpress.restaurants.parsingdata.TotalDataManager;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.ypyproductions.abtractclass.fragment.DBFragmentAdapter;
import com.ypyproductions.abtractclass.fragment.IDBFragmentConstants;
import com.ypyproductions.net.task.DBTask;
import com.ypyproductions.net.task.IDBTaskListener;
import com.ypyproductions.utils.ApplicationUtils;
import com.ypyproductions.utils.DownloadUtils;
import com.ypyproductions.utils.IOUtils;
import com.ypyproductions.utils.ResolutionUtils;
import com.ypyproductions.utils.StringUtils;

import java.util.ArrayList;

public class FragmentCatering extends DBFragment implements IMyRestaurantConstants,IDBFragmentConstants {

	public static final String TAG = FragmentCatering.class.getSimpleName();

	private MainActivity mContext;
	private LinearLayout mLayoutPage;
	private ViewPager mViewPagers;

	public static final int TIME_NEXT_AUTO=1000;
	private Button mBtBack;
	private Button mBtnCall;
	private Handler mHandler = new Handler();

	private DBTask mDBTask;

	protected ArrayList<OfferObject> mListOffers;

	private TextView mTvResult;
	private TextView txt1,txt2,txt3,txt4;
	private ArrayList<Fragment> mListFragments= new ArrayList<Fragment>();

	private DBFragmentAdapter mPagerAdapter;

	@Override
	public View onInflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(com.oxyexpress.restaurants.R.layout.fragment_catering, container, false);
	}

	@Override
	public void findView() {
		this.mContext = (MainActivity) getActivity();
		this.mBtBack = (Button) mRootView.findViewById(com.oxyexpress.restaurants.R.id.btn_back);
		this.mTvResult =(TextView) mRootView.findViewById(com.oxyexpress.restaurants.R.id.tv_no_result);
		this.mBtnCall = (Button) mRootView.findViewById(R.id.btn_call);
		this.txt1 = (TextView)mRootView.findViewById(R.id.textView2);
		this.txt2 = (TextView)mRootView.findViewById(R.id.textView4);
		this.txt3 = (TextView)mRootView.findViewById(R.id.textView3);
		this.txt4 = (TextView)mRootView.findViewById(R.id.textView10);

		Typeface mTypefaceSignika = Typeface.createFromAsset(getContext().getAssets(), "fonts/Signika-Light.otf");
		this.mTvResult.setTypeface(mTypefaceSignika);
		this.txt1.setTypeface(mTypefaceSignika);
		this.txt2.setTypeface(mTypefaceSignika);
		this.txt3.setTypeface(mTypefaceSignika);
		this.txt4.setTypeface(mTypefaceSignika);


		mBtnCall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.commit();
			}
		});



		mBtBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mContext.backStack(null);
			}
		});
		//mLayoutPage = (LinearLayout) mRootView.findViewById(com.oxyexpress.restaurants.R.id.layout_indicator);
		//mViewPagers = (ViewPager) mRootView.findViewById(com.oxyexpress.restaurants.R.id.layout_offers);

		startLoadOffers();
	}

	public void addFragment(Fragment mFragment) {
		if (mFragment != null && mListFragments != null) {
			synchronized (mListFragments) {
				mListFragments.add(mFragment);
			}
		}
	}

	private void startLoadOffers(){
		if(!ApplicationUtils.isOnline(mContext)  && !StringUtils.isEmptyString(URL_LIST_ALL_ITEM) && URL_LIST_ALL_ITEM.startsWith("http")){
			mContext.showToast(com.oxyexpress.restaurants.R.string.info_lose_internet);
			return;
		}
		mDBTask = new DBTask(new IDBTaskListener() {

			@Override
			public void onPreExcute() {
				mContext.showProgressDialog();
			}

			@Override
			public void onDoInBackground() {
				String dataOffers="";
				if(!StringUtils.isEmptyString(URL_LIST_CATERING)){
					if(URL_LIST_CATERING.startsWith("http")){
						dataOffers = DownloadUtils.downloadString(URL_LIST_CATERING);
					}
					else{
						dataOffers=IOUtils.readStringFromAssets(mContext, URL_LIST_CATERING);
					}
				}
				if(!StringUtils.isEmptyString(dataOffers)){
					mListOffers = JsonParsingUtils.parseListOfferObject(dataOffers);
					if(mListOffers!=null && mListOffers.size()>0){
						TotalDataManager.getInstance().setListOfferObjects(mListOffers);
					}
				}

			}
			@Override
			public void onPostExcute() {
				mContext.dimissProgressDialog();
				setUpFragment();
			}

		});
		mDBTask.execute();
	}

	private void scheduleFeatureDisplay(){
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (mViewPagers != null && mListFragments != null && mListFragments.size() >= 2) {
					int current = mViewPagers.getCurrentItem() + 1;
					if (current >= mListFragments.size()) {
						current = 0;
					}
					mViewPagers.setCurrentItem(current, true);
					scheduleFeatureDisplay();
				}
			}
		}, TIME_NEXT_AUTO);
	}

	public void setUpFragment() {
		if(mListOffers!=null && mListOffers.size()>0){
			mTvResult.setVisibility(View.GONE);
			int size = mListOffers.size();
			for (int i = 0; i < size; i++) {
				Bundle mBundle1 = new Bundle();
				mBundle1.putString(KEY_ID, mListOffers.get(i).getId());
				Fragment mFragment = Fragment.instantiate(mContext,FragmentCateringDetail.class.getName(), mBundle1);
				mListFragments.add(mFragment);

				if(size>1){
					ImageView mImageView = new ImageView(mContext);
					int width = (int) ResolutionUtils.convertDpToPixel(mContext, 10);
					LinearLayout.LayoutParams mLayoutParams = new LayoutParams(width, width);
					if (i != 0) {
						mLayoutParams.leftMargin = (int) ResolutionUtils.convertDpToPixel(mContext, 5);
						mImageView.setImageResource(com.oxyexpress.restaurants.R.drawable.circle_grey);
					}
					else {
						mImageView.setImageResource(com.oxyexpress.restaurants.R.drawable.circle_primary_color);
					}
					//mLayoutPage.addView(mImageView, mLayoutParams);
				}
			}
			mPagerAdapter = new DBFragmentAdapter(getChildFragmentManager(), mListFragments);
			/*mViewPagers.setAdapter(mPagerAdapter);
			mViewPagers.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					int count = mLayoutPage.getChildCount();
					for (int i = 0; i < count; i++) {
						ImageView mImgView = (ImageView) mLayoutPage.getChildAt(i);
						if (position != i) {
							mImgView.setImageResource(com.oxyexpress.restaurants.R.drawable.circle_grey);
						}
						else {
							mImgView.setImageResource(com.oxyexpress.restaurants.R.drawable.circle_primary_color);
						}
					}
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}

				@Override
				public void onPageScrollStateChanged(int state) {

				}
			});

			mViewPagers.setCurrentItem(0, false);
			scheduleFeatureDisplay();*/
		}
		else{
			mTvResult.setVisibility(View.VISIBLE);

		}
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mListFragments!=null){
			mListFragments.clear();
			mListFragments=null;
		}
	}
}
