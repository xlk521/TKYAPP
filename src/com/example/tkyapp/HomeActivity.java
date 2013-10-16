package com.example.tkyapp;

import java.util.ArrayList;

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.example.content.*;

public class HomeActivity extends Activity{

	long temptime;
	long a;
	/** 菜单功能列表 */
	private ArrayList<Menu> list;
	private AnyChatCoreSDK anychat;
	public static final String IS_START_FROM_MAIN = "IS_START_FROM_MAIN";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		//启动socket
//		TKYApplication.instance.connectSocket.start();
		list = new ArrayList<Menu>();
		
		Menu menu = new Menu();
		menu.icon = R.drawable.chi;
		menu.title = getResources().getString(R.string.title_chi);
		menu.cls = ChiActivity.class;
		list.add(menu);

		menu = new Menu();
		menu.icon = R.drawable.he;
		menu.title = getResources().getString(R.string.title_he);
		menu.cls = HeActivity.class;
		list.add(menu);
		
		menu = new Menu();
		menu.icon = R.drawable.wan;
		menu.title = getResources().getString(R.string.title_wan);
		menu.cls = WanActivity.class;
		list.add(menu);

		menu = new Menu();
		menu.icon = R.drawable.le;
		menu.title = getResources().getString(R.string.title_le);
		menu.cls = LeActivity.class;
		list.add(menu);

		menu = new Menu();
		menu.icon = R.drawable.tao;
		menu.title = getResources().getString(R.string.title_taobao);
		menu.cls = TaoActivity.class;
		list.add(menu);
		
		menu = new Menu();
		menu.icon = R.drawable.guanggao;
		menu.cls = AdActivity.class;
		menu.title = getResources().getString(R.string.title_ed);
		list.add(menu);
		
		GridView gridView = (GridView) findViewById(R.id.gridview);
		GridViewAdapter adapter = new GridViewAdapter();
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (list.get(arg2).cls == null) {
					Toast.makeText(HomeActivity.this, "研发中",Toast.LENGTH_SHORT).show();
				}
				else{
					//根据不同的点击区域来启动对应的activitys
					startActivity(new Intent(HomeActivity.this, list.get(arg2).cls));
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if((keyCode == KeyEvent.KEYCODE_BACK)&&(event.getAction() == KeyEvent.ACTION_DOWN))
		{
			 if(System.currentTimeMillis() - temptime >2000)         // 2s内再次选择back键有效
			 {  
				 Toast.makeText(this, "请再按一次返回退出", Toast.LENGTH_SHORT).show();
				 temptime = (long)System.currentTimeMillis();
			 }
			 else 
			 {     
				 TKYApplication.instance.user = null; //退出时清空用户信息
				 finish();   
				 System.exit(0);     //凡是非零都表示异常退出!0表示正常退出!
			 }
		 return true; 
		}
	     return super.onKeyDown(keyCode, event);
     }
	
	class GridViewAdapter extends BaseAdapter {
		private LayoutInflater mLayoutInflater;

		public GridViewAdapter() {
			mLayoutInflater = getLayoutInflater();
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
				convertView = mLayoutInflater.inflate(R.layout.homeitem,parent, false);
				holder = new ViewHolder();

				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.icon.setClickable(false);
				holder.menuTitle = (TextView) convertView.findViewById(R.id.menu_title);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				resetViewHolder(holder);
			}

			final Menu menu = list.get(position);

			holder.icon.setBackgroundResource(menu.icon);
			holder.menuTitle.setText(menu.title);

			return convertView;
		}

		private void resetViewHolder(ViewHolder holder) {
			holder.icon.setBackgroundResource(0);
			holder.menuTitle.setText("");
		}
	}

	class ViewHolder {
		ImageView icon;
		TextView menuTitle;
	}

	class Menu {
		// 启动类
		Class cls;
		// 对应界面icon
		int icon;
		//
		int id;
		//
		int count;
		// 标题
		String title;
	}
}
