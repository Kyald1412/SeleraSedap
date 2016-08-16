package com.oxyexpress.restaurants.adapter;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oxyexpress.restaurants.constanst.IMyRestaurantConstants;
import com.ypyproductions.abtractclass.DBBaseAdapter;
import com.oxyexpress.restaurants.object.ItemObject;
import com.ypyproductions.utils.StringUtils;

public class NewWishListAdapter extends DBBaseAdapter implements IMyRestaurantConstants {

	public static final String TAG = NewWishListAdapter.class.getSimpleName();

	private DisplayImageOptions mOptions;

	private OnWistListListener onWistListListener;

	public NewWishListAdapter(Activity mContext, ArrayList<? extends Object> listObjects, DisplayImageOptions mOptions) {
		super(mContext, listObjects);
		this.mOptions = mOptions;
	}

	@Override
	public View getAnimatedView(int arg0, View arg1, ViewGroup arg2) {
		return null;
	}

	@Override
	public View getNormalView(int position, View convertView, ViewGroup parent) {
		final ChildHolder holder;
		if (convertView == null) {
			convertView =LayoutInflater.from(mContext).inflate(com.oxyexpress.restaurants.R.layout.item_wishlist_dish, null);
			holder = new ChildHolder();
			convertView.setTag(holder);
			holder.mImgDish = (ImageView) convertView.findViewById(com.oxyexpress.restaurants.R.id.img_dish);
			holder.mLayoutRoot = (RelativeLayout) convertView.findViewById(com.oxyexpress.restaurants.R.id.layout_root);

			holder.mTvPrice = (TextView) convertView.findViewById(com.oxyexpress.restaurants.R.id.tv_price);
			holder.mTvDescription = (TextView) convertView.findViewById(com.oxyexpress.restaurants.R.id.tv_description);
			holder.mTvName = (TextView) convertView.findViewById(com.oxyexpress.restaurants.R.id.tv_description);
		}
		else {
			holder = (ChildHolder) convertView.getTag();
		}

		 final ItemObject mItemObject = (ItemObject) mListObjects.get(position);
		holder.mTvDescription.setText(mItemObject.getDescription());

		holder.mTvPrice.setText(mItemObject.getPrice());
		holder.mTvName.setText(mItemObject.getName());
		
		holder.mLayoutRoot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onWistListListener!=null){
					onWistListListener.onViewDetail(mItemObject);
				}
			}
		});


		if (!StringUtils.isEmptyString(mItemObject.getImage())) {
			String itemImg = mItemObject.getImage();
			if(!itemImg.startsWith("http")){
				itemImg="assets://"+itemImg;
			}
			ImageLoader.getInstance().displayImage(itemImg, holder.mImgDish, mOptions);
		}
		return convertView;
	}
	
	private String formatMoney(float total) {
		String formatSubTotal = String.format(Locale.US, "%.2f", total);
		try {
			formatSubTotal = formatSubTotal.replace(",", ".");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return formatSubTotal;
	}

	
	public void setOnWistListListener(OnWistListListener onWistListListener) {
		this.onWistListListener = onWistListListener;
	}

	public interface OnWistListListener{
		public void onChangeTotalPriceWishList();
		public void onRemoveItemToWishList(ItemObject mItemObject);
		public void onViewDetail(ItemObject mItemObject);
	}

	private static class ChildHolder {
		public ImageView mImgBtnSub;
		public ImageView mImgBtnAdd;
		public ImageView mImgDish;
		public TextView mTvPrice;
		public TextView mTvDescription;
		public TextView mTvName;
		public RelativeLayout mLayoutRoot;
	}

}
