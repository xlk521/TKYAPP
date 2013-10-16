package com.example.tkyapp;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.example.content.*;
import com.example.tkyapp.MainActivity.WelcomeHttp;
public class ChiActivity extends Activity {

	private TabHost tabHost = null;
	private GridView gridViewA = null;
	private Button btnMoreA = null;
	private GridView gridViewB = null;
	private Button btnMoreB = null;
	private ArrayList<GridMenu> GridListA = null;
	private ArrayList<GridMenu> GridListB = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chi);
		
		tabHost = (TabHost)this.findViewById(R.id.myTabhost);
		gridViewA = (GridView)this.findViewById(R.id.TabGrid1);
		btnMoreA = (Button)this.findViewById(R.id.btnMore1);
		gridViewB = (GridView)this.findViewById(R.id.TabGrid2);
		btnMoreA = (Button)this.findViewById(R.id.btnMore2);
		/*此处是必须设置的*/
		tabHost.setup();
		/*添加标签*/
		tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator(getResources().getString(R.string.title_chi01)).setContent(R.id.tab1));
		tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator(getResources().getString(R.string.title_chi02)).setContent(R.id.tab2));
		
		new Chi().execute();
//		GridList = new ArrayList<GridMenu>();
//		
//		GridMenu menu = new GridMenu();
//		menu.imgUrl = "--1--";
//		menu.imgId = "001";
//		menu.icon = R.drawable.chi;
//		menu.title = getResources().getString(R.string.title_chi);
//		menu.money = (float) 10.35;
//		GridList.add(menu);
//		
//		menu = new GridMenu();
//		menu.imgUrl = "--2--";
//		menu.imgId = "002";
//		menu.icon = R.drawable.he;
//		menu.title = getResources().getString(R.string.title_he);
//		menu.money = (float) 10.30;
//		GridList.add(menu);
//		
//		menu = new GridMenu();
//		menu.imgUrl = "--3--";
//		menu.imgId = "003";
//		menu.icon = R.drawable.wan;
//		menu.title = getResources().getString(R.string.title_wan);
//		menu.money = (float) 11.00;
//		GridList.add(menu);
//		
//		menu = new GridMenu();
//		menu.imgUrl = "--4--";
//		menu.imgId = "004";
//		menu.icon = R.drawable.le;
//		menu.title = getResources().getString(R.string.title_le);
//		menu.money = (float) 4.75;
//		GridList.add(menu);
//		
//		GridViewAdapter adapter = new GridViewAdapter(ChiActivity.this,getLayoutInflater(),GridList);
//		gridViewA.setAdapter(adapter);
//		gridViewB.setAdapter(adapter);
	}
	class Chi extends AsyncTask<String, Void, String>{
		private String returnResout = getResources().getString(R.string.toast_http_error);
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!ChiActivity.this.isFinishing()) {
				showDialog(101);
			}
		}
		@Override
		protected String doInBackground(String... arg0) {
			String s = null;
			JSONObject json=new JSONObject();
			try {
				json.put(JSONKey.FLAG,JSONKey.FLAG_CHI);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			try {
				String result = TKYHttpClient.connect(Constants.BASE_HTTP_URL,json.toString());
				if (result == null) {
					s = returnResout;
				}
				else {
					s=result;
					Log.i("chi", "chi : "+s);
				}
			} catch (Exception e) {
				e.printStackTrace();
				s = returnResout;
			}
			return s;
		}
		protected void onCancelled(){}

		@Override
		protected void onPostExecute(String result) {
			if (result.equals(returnResout)) {
				Toast.makeText(ChiActivity.this, returnResout, Toast.LENGTH_SHORT).show();
			}else{
				try {
					JSONObject jsonObject = new JSONObject(result);
					GridListA = new ArrayList<GridMenu>();
					GridListB = new ArrayList<GridMenu>();
					GridMenu menu = new GridMenu();
					int num = jsonObject.getInt(JSONKey.TotalNum);
					for (int i = 1; i <= num; i++) {
						menu = new GridMenu();
						menu.imgUrl = jsonObject.getString(JSONKey.jsonKey_URL+i);
						menu.imgId = jsonObject.getString(JSONKey.jsonKey_id+i);
						menu.icon = R.drawable.chi;
						menu.title = jsonObject.getString(JSONKey.jsonKey_title+i);
						menu.money = (float)jsonObject.getDouble(JSONKey.jsonKey_price+i);
						menu.isHave = jsonObject.getInt(JSONKey.jsonKey_ishave+i);
						menu.imgClass = jsonObject.getString(JSONKey.jsonKey_class+i);
						menu.imgCount = jsonObject.getInt(JSONKey.jsonKey_count+i);
						if (menu.imgClass.equals(JSONKey.chiA)) {
							GridListA.add(menu);
						}else{
							GridListB.add(menu);
						}
					}
					Log.i("chi", "chi : 001");
					GridViewAdapter adapterA = new GridViewAdapter(ChiActivity.this,getLayoutInflater(),GridListA);
					GridViewAdapter adapterB = new GridViewAdapter(ChiActivity.this,getLayoutInflater(),GridListB);
					Log.i("chi", "chi : 002");
					gridViewA.setAdapter(adapterA);
					gridViewB.setAdapter(adapterB);
					
				} catch (JSONException e) {
					Log.i("chi", "chi : 003");
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.chi, menu);
		return true;
	}
}
