package com.oxyexpress.restaurants.fragment;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.oxyexpress.restaurants.MainActivity;
import com.oxyexpress.restaurants.adapter.NewWishListAdapter;
import com.oxyexpress.restaurants.constanst.IMyRestaurantConstants;
import com.oxyexpress.restaurants.object.ItemObject;
import com.oxyexpress.restaurants.object.WishListObject;
import com.oxyexpress.restaurants.IMainCallBack;
import com.oxyexpress.restaurants.parsingdata.TotalDataManager;
import com.ypyproductions.abtractclass.fragment.DBFragment;
import com.oxyexpress.restaurants.R;

public class FragmentWishList extends DBFragment implements IMyRestaurantConstants, OnClickListener {

	public static final String TAG = FragmentWishList.class.getSimpleName();

	private MainActivity mContext;

	private Button mBtnBack;

	private ListView mListViewItem;

	private TextView mTvResult;

	private WishListObject mWishListObject;

	private IMainCallBack mCallback;

	private NewWishListAdapter mWishListAdapter;

	private TextView mTvSubTotal;
	private TextView mTvLevy;
	private TextView mTvTotal;

	private DisplayImageOptions mOptions;

	@Override
	public View onInflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_wishlist, container, false);
	}

	@Override
	public void findView() {
		this.mContext = (MainActivity) getActivity();
		this.mListViewItem = (ListView) mRootView.findViewById(R.id.list_wish_list);
		this.mTvResult = (TextView) mRootView.findViewById(R.id.tv_no_result);

		this.mOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_launcher).resetViewBeforeLoading(false).cacheInMemory(true).cacheOnDisk(true)
				.displayer(new RoundedBitmapDisplayer(1000)).considerExifParams(true).build();

		mWishListObject = TotalDataManager.getInstance().getWishListObject();
		if (mWishListObject != null && mWishListObject.getListItemObjects().size() > 0) {
			mWishListAdapter = new NewWishListAdapter(mContext, mWishListObject.getListItemObjects(), mOptions);
			mListViewItem.setAdapter(mWishListAdapter);
			mWishListAdapter.setOnWistListListener(new NewWishListAdapter.OnWistListListener() {

				@Override
				public void onChangeTotalPriceWishList() {
					//onUpdateTotal();
				}

				@Override
				public void onRemoveItemToWishList(ItemObject mItemObject) {
					removeItemToWishList(mItemObject);
				}

				@Override
				public void onViewDetail(ItemObject mItemObject) {
					TotalDataManager.getInstance().setListCurrentItemObjects(mWishListObject.getListItemObjects());
					if (mCallback != null) {
						mContext.showTags(mItemObject, true);
						mCallback.onShowItemDetail(-1, TAG_WISHLIST_ITEM, mItemObject.getId());
					}
				}
			});
			//onUpdateTotal();
		}
		else {
		}
	}
	
	private void removeItemToWishList(ItemObject mItemObject){
		TotalDataManager.getInstance().removeItemInWishList(mContext, mItemObject);
		String info = String.format(mContext.getString(R.string.format_info_remove_wishlist), mItemObject.getName());
		mContext.showToast(info);
		mWishListAdapter.notifyDataSetChanged();
		mContext.updateWishList();
		if(mWishListObject.getListItemObjects().size()==0){

		}
		//onUpdateTotal();
	}

	public void onNotificationUpdate(ItemObject mItemObject, boolean isAdd) {
		if (mWishListAdapter != null) {
			if (!isAdd) {
				removeItemToWishList(mItemObject);
			}
			else {
				mWishListAdapter.notifyDataSetChanged();
			}
		}
	}

	private void onUpdateTotal() {
		float subTotal = 0;
		if (mWishListObject != null) {
			ArrayList<ItemObject> mListItems = mWishListObject.getListItemObjects();
			if (mListItems != null && mListItems.size() > 0) {
				for (ItemObject mItemObject : mListItems) {
					float unitPrice = Float.parseFloat(mItemObject.getPrice());
					subTotal = subTotal + unitPrice * mItemObject.getQuantity();
				}
			}
		}
		mTvSubTotal.setText(String.format(getString(R.string.format_sub_total), formatMoney(subTotal)));
		float tax = subTotal * ((float) YOUR_TAX_PERCENT / 100f);
		mTvLevy.setText(String.format(getString(R.string.format_levy), formatMoney(tax)));
		float total = tax + subTotal;
		mTvTotal.setText(String.format(getString(R.string.format_total), formatMoney(total)));

	}

	private String formatMoney(float total) {
		String formatSubTotal = String.format(Locale.US, "%.2f", total);
		try {
			formatSubTotal = formatSubTotal.replace(",", ".")+" "+CURRENCY;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return formatSubTotal;
	}

	@Override
	public void onClick(View v) {
		/*switch (v.getId()) {
		case R.id.btn_add_more:
			mContext.backStack(null);
			break;
		default:
			break;
		}*/
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
