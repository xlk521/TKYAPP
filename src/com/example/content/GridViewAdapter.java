package com.example.content;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;

import com.example.tkyapp.ChiActivity;
import com.example.tkyapp.DetailActivity;
import com.example.tkyapp.HomeActivity;
import com.example.tkyapp.R;
import com.tky.manager.MMS;

public class GridViewAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private ArrayList<GridMenu> list;
	private Context context;
	private Bundle bundle = null;
	public GridViewAdapter(Context c,LayoutInflater m,ArrayList<GridMenu> li) {
		mLayoutInflater = m;
		list = li;
		context = c;
	}
	public int getCount() {
		return list.size();
	}
	@Override
	public Object getItem(int position) {
		return position;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.productitem,parent, false);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.icon.setClickable(false);
			holder.menuTitle = (TextView) convertView.findViewById(R.id.menu_title);
			holder.menuPrice = (TextView)convertView.findViewById(R.id.menu_price);
			holder.myBuy = (Button)convertView.findViewById(R.id.myBuy);
			holder.myCart = (Button)convertView.findViewById(R.id.myCart);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			resetViewHolder(holder);
		}
		final GridMenu menu = list.get(position);
		holder.icon.setBackgroundResource(menu.icon);
		holder.menuTitle.setText(menu.title);
		holder.menuPrice.setText(menu.money+"");
		holder.myBuy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "the id is :"+menu.imgId, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				bundle = new Bundle();
				bundle.putString("imgId", menu.imgId);
				bundle.putString("name", menu.title);
				bundle.putFloat("price", menu.money);
				bundle.putString("class", menu.imgClass);
				intent.putExtras(bundle);
				intent.setClass(context, DetailActivity.class);
				context.startActivity(intent);
//				AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.AppBaseTheme));
//				builder.setTitle("请选择相关操作···");
//				//通过调用本地的图片查看器来查看选中的图片
//				builder.setPositiveButton("查看图片", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {
//					}
//				});
//				//调用图片选择界面来修改图片
//				builder.setNeutralButton("修改信息", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {
//					}
//				});
//				builder.setNegativeButton("取消操作", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {}
//				});
//				builder.show();
			}
		});
		holder.myCart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "the url is :"+menu.imgUrl, Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;
	}
	private void resetViewHolder(ViewHolder holder) {
		holder.icon.setBackgroundResource(0);
		holder.menuTitle.setText("");
	}
}
