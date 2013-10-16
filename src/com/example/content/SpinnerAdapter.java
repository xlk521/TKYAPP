package com.example.content;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter{

	private ArrayList<String> items =  new ArrayList<String>();
	private Context context = null;
	public SpinnerAdapter(Context con, ArrayList<String> item) {
		Log.i("SpinnerAdapter", item.toString());
		items = item;
		this.context = con;
	}
	@Override
	public int getCount() {
		return items.size();
	}
	@Override
	public Object getItem(int position) {
		return null;
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}
	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ImageView ii = new ImageView(context);
		ll.addView(ii);
		TextView tv = new TextView(context);
		tv.setText(items.get(position));
		tv.setTextSize(24);
		ll.addView(tv);
		return ll;
	}
}
