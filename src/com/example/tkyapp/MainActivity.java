package com.example.tkyapp;
/**
 * 欢迎界面
 * 
 * */
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.example.content.*;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		new WelcomeHttp().execute();
	}
	class WelcomeHttp extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!MainActivity.this.isFinishing()) {
				showDialog(101);
			}
		}

		@Override
		protected String doInBackground(String... arg0) {
			String s = null;
			JSONObject json=new JSONObject();
			try {
				json.put(JSONKey.FLAG,JSONKey.FLAG_WELCOM);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			try {
//				 联网，传入URL地址，json格式字符串
				Log.i("200param", "main");
				String result = TKYHttpClient.connect(Constants.BASE_HTTP_URL,json.toString());
				if (result == null) {
					s = getResources().getString(R.string.toast_http_error);
				} else {
					JSONObject jsonObject = new JSONObject(result);
					s=jsonObject.getString("welcom");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				s = getResources().getString(R.string.toast_http_error);
			}
			return s;
		}
		protected void onCancelled(){}

		@Override
		protected void onPostExecute(String result) {
			
			startActivity(new Intent(MainActivity.this,LoginActivity.class));
			if (result.equals("联网成功") ) {
				// 启动功能菜单
				Toast.makeText(MainActivity.this, result,Toast.LENGTH_LONG).show();
				startActivity(new Intent(MainActivity.this,LoginActivity.class));
				finish();
			} else {
				// 登陆失败的错误提示
				Toast.makeText(MainActivity.this, result,Toast.LENGTH_LONG).show();
				finish();
			}
		}
	}
}
