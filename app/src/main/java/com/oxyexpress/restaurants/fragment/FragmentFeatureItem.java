package com.oxyexpress.restaurants.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.oxyexpress.restaurants.constanst.IMyRestaurantConstants;
import com.oxyexpress.restaurants.IMainCallBack;
import com.oxyexpress.restaurants.MainActivity;
import com.oxyexpress.restaurants.parsingdata.TotalDataManager;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.oxyexpress.restaurants.R;
import com.oxyexpress.restaurants.object.ItemObject;
import com.ypyproductions.utils.StringUtils;

public class FragmentFeatureItem extends DBFragment implements IMyRestaurantConstants {

	public static final String TAG = FragmentFeatureItem.class.getSimpleName();

	private MainActivity mContext;

	private TextView mTvName;

	private TextView mTvDescription;

	private TextView mTvPrice;

	private ImageView mImgFeature;

	private String mIDItem;

	private ProgressBar mProgressBar;

	private ItemObject mItem;

	private IMainCallBack mCallback;

	@Override
	public View onInflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_feature_item, container, false);
	}

	@Override
	public void findView() {
		setAllowFindViewContinous(true);

		this.mContext = (MainActivity) getActivity();
		this.mTvName = (TextView) mRootView.findViewById(R.id.tv_description);
		this.mTvDescription = (TextView) mRootView.findViewById(R.id.tv_des);
		this.mTvPrice = (TextView) mRootView.findViewById(R.id.tv_price);
		this.mImgFeature = (ImageView) mRootView.findViewById(R.id.img_feature);

		Typeface mTypefaceSignika = Typeface.createFromAsset(getContext().getAssets(), "fonts/Signika-Light.otf");
		this.mTvName.setTypeface(mTypefaceSignika);
		this.mTvDescription.setTypeface(mTypefaceSignika);
		this.mTvPrice.setTypeface(mTypefaceSignika);

		mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar1);

		if (!StringUtils.isEmptyString(mIDItem)) {
			mItem = TotalDataManager.getInstance().getItemObject(mIDItem);
			if(mItem!=null){
				setUpInfo(mItem);
			}
		}
	}

	private void setUpInfo(final ItemObject mItem) {
		mTvName.setText(mItem.getName());
		mTvDescription.setText(mItem.getDescription());
		mTvPrice.setText(CURRENCY + " " + mItem.getPrice());
		
		String itemImg = mItem.getImage();
		if (!StringUtils.isEmptyString(itemImg)) {
			if (!itemImg.startsWith("http")) {
				itemImg = "assets://" + itemImg;
			}
			ImageLoader.getInstance().displayImage(itemImg, this.mImgFeature, mContext.mBigOptions, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					mProgressBar.setVisibility(View.GONE);
				}
			});
		}
		
		this.mImgFeature.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCallback != null) {
					TotalDataManager.getInstance().setListCurrentItemObjects(TotalDataManager.getInstance().getListFeatureItemObjects());
					mContext.showTags(mItem, true);
					mCallback.onShowItemDetail(R.id.fragment_home, "", mItem.getId());
				}
			}
		});
	}

	@Override
	public void onExtractData() {
		super.onExtractData();
		Bundle args = getArguments();
		if (args != null) {
			mIDItem = args.getString(KEY_ID);
		}

	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (IMainCallBack) activity;
		}
		catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement IHomeListener");
		}
	}

}
