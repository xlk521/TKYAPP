package com.example.tkyapp;

import javax.security.auth.login.LoginException;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import com.example.content.*;

@SuppressLint("NewApi")
public class LoginActivity extends Activity implements OnClickListener{

	private EditText LoginName = null;
	private EditText LoginPassword = null;
	private Button BtnExit = null;
	private Button BtnLogin = null;
	private ProgressBar LoginProgress = null;
	private String account = null;
	private String password = null;
	private Dialog dialog;
	int temptime=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		LoginName = (EditText)this.findViewById(R.id.account);
		LoginPassword = (EditText)this.findViewById(R.id.pw);
		BtnExit = (Button)this.findViewById(R.id.btn_exit);
		BtnLogin = (Button)this.findViewById(R.id.btn_login);
		LoginProgress = (ProgressBar)this.findViewById(R.id.login_progress);
		BtnExit.setOnClickListener(this);
		BtnLogin.setOnClickListener(this);
	}
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_exit:
			this.finish();
			break;
		case R.id.btn_login:
			account = LoginName.getText().toString();
			password = LoginPassword.getText().toString();
			if (account.isEmpty() || account.equals(null)) {
				Toast.makeText(this, R.string.toast_miss_user, Toast.LENGTH_SHORT).show();
			}else if(password.isEmpty() || password.equals(null)){
				Toast.makeText(this, R.string.toast_miss_pass, Toast.LENGTH_SHORT).show();
			}
			
			account = "方正";
			password = "123456";
			// 联网登陆
			new HttpTask().execute();
			break;
		default:
			Toast.makeText(this, R.string.toast_null, Toast.LENGTH_SHORT).show();
			break;
		}
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 101:
			LinearLayout localLinearLayout = new LinearLayout(this);
			dialog = new Dialog(this, R.style.TransparentDialog);
			ProgressBar localProgressBar = new ProgressBar(this, null,
					android.R.attr.progressBarStyleInverse);
			localLinearLayout.addView(localProgressBar);
			dialog.setContentView(localLinearLayout);
			dialog.setCancelable(true);
			break;
		}
		return dialog;
	}
	class HttpTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!LoginActivity.this.isFinishing()) {
				showDialog(101);
			}
		}

		@SuppressWarnings("unused")
		protected String doInBackground(String... params) {
			String s = null;
			try {
				// 联网，传入URL地址，json格式字符串
				String result = TKYHttpClient.connect(Constants.BASE_HTTP_URL,JSONDataUtil.login(account, password).toString());
				Log.i("LoginActivity", "HttpTask;result="+result);
				if (result == null) {
					s = getResources().getString(R.string.toast_http_error);
				} else {
					JSONObject jsonObject = new JSONObject(result);
					result = JSONDataUtil.handleResult(jsonObject);
					if (result != null) {
						s = result;
					} else {
						// 共享 用户
						Constants.user = JSONDataUtil.handleLogin(jsonObject);
						TKYApplication.instance.user = JSONDataUtil.handleLogin(jsonObject);
						Log.i("user:", jsonObject.toString());
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				s = getResources().getString(R.string.toast_http_error);
			}
			return s;
		}

		protected void onCancelled() {
			if (dialog.isShowing()) {
				dismissDialog(101);
			}
		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog.isShowing()) {
				dismissDialog(101);
				if (result == null) {
					// 启动功能菜单
					startActivity(new Intent(LoginActivity.this,HomeActivity.class));
					startService(new Intent(LoginActivity.this,AudioServer.class).putExtra("account", account));
//					finish();
				} else {
					// 登陆失败的错误提示
					Toast.makeText(LoginActivity.this, result,Toast.LENGTH_SHORT).show();
//					finish();
				}
				startActivity(new Intent(LoginActivity.this,HomeActivity.class));
				startService(new Intent(LoginActivity.this,AudioServer.class).putExtra("account", account));
			}
		}
	}
}
