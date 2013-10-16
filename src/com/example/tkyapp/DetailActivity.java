package com.example.tkyapp;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.example.content.Constants;
import com.example.content.JSONKey;
import com.example.content.SpinnerAdapter;
import com.example.content.CreatOrder;
import com.example.content.TKYHttpClient;
import com.example.tkyapp.ChiActivity.Chi;

import java.lang.Math;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint({ "CutPasteId", "NewApi" })
public class DetailActivity extends Activity {

	private EditText detailName = null;
	private EditText detailPrice = null;
	private Spinner detailNum = null;
	private EditText detailAll = null;
	private EditText detailAddress = null;
	private EditText detailOrder = null;
	private Button detailSend = null;
	private Button detailCancel = null;
	private Bundle bundle = null;
	private String title = null;
	private float price = (float) 0.00;
	private String imgId = null;
	private ArrayList<String> text = null;
	private String spinnerText = "";
	private String choose = "请选择所需数量";
	private int count = 0;
	private float allPrice = (float) 0.0;
	private LinearLayout linear = null;
	private String address = "";
	private String imgClass = "";
	@SuppressLint("CutPasteId")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		detailName = (EditText)this.findViewById(R.id.detailname);
		detailPrice = (EditText)this.findViewById(R.id.detailprice);
		detailNum = (Spinner)this.findViewById(R.id.detailspinner);
		detailAll = (EditText)this.findViewById(R.id.detailall);
		detailAddress = (EditText)this.findViewById(R.id.detailaddress);
		detailOrder = (EditText)this.findViewById(R.id.detailorder);
		detailSend = (Button)this.findViewById(R.id.detailsend);
		detailCancel = (Button)this.findViewById(R.id.detailcancel);
		linear = (LinearLayout)this.findViewById(R.id.LinearOrder);
		
		bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		price = (float)bundle.getFloat("price");
		imgId = bundle.getString("imgId");
		title = bundle.getString("name");
		imgClass = bundle.getString("class");
		address = detailAddress.getText().toString();
		
		linear.setVisibility(View.INVISIBLE);
		
		detailName.setText(title);
		detailPrice.setText(price+"");
		text = new ArrayList<String>();
		text.add(0, choose);
		for (int i = 1; i < 11; i++) {
			text.add(i, ""+i);
		}
		SpinnerAdapter adapter = new SpinnerAdapter(DetailActivity.this,text);
		detailNum.setAdapter(adapter);
		detailNum.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				//获取控件内容
				getStationItems(arg1);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		detailCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		detailSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new SendOrder().execute();
				Toast.makeText(DetailActivity.this, "正在发送数据请求。。。", Toast.LENGTH_SHORT).show();
				//将数据组合成json数据格式，然后发送到服务器端
			}
		});
	}

	protected void getStationItems(View view) {
		LinearLayout linearLayout = (LinearLayout) view;
		// 获取其中的TextView
		TextView textView = (TextView) linearLayout.getChildAt(1);
		spinnerText = textView.getText().toString();
		if (spinnerText.isEmpty() || spinnerText.equals(choose)) {
			detailSend.setEnabled(false);
			detailSend.setBackgroundResource(R.drawable.btn_forbid);
		}else{
			detailSend.setEnabled(true);
			detailSend.setBackgroundResource(R.drawable.btn_register);
			for(int i = 1; i < 11; i++){
				if (spinnerText.equals(""+i)) {
					count = i;
					break;
				}else{
					continue;
				}
			}
		}
		//订单总金额
		allPrice = (float)count * price;
		allPrice = (float) ((Math.round(allPrice*100))/100.0);
		detailAll.setText(allPrice+"");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	class SendOrder extends AsyncTask<String, Void, String> {
		private String returnResout = getResources().getString(R.string.toast_http_error);
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!DetailActivity.this.isFinishing()) {
				showDialog(101);
			}
		}

		@Override
		protected String doInBackground(String... arg0) {
			String s = null;
			JSONObject json=new JSONObject();
			try {
				json.put(JSONKey.FLAG,JSONKey.FLAG_ORDER);
				json.put(JSONKey.jsonKey_class, imgClass);
				json.put(JSONKey.jsonKey_id, imgId);
				json.put(JSONKey.jsonKey_title, title);
				json.put(JSONKey.jsonKey_price, price);
				json.put(JSONKey.jsonKey_priceAll, price);
				json.put(JSONKey.TotalNum, count);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			try {
				String result = TKYHttpClient.connect(Constants.BASE_HTTP_URL,json.toString());
				if (result == null) {
					s = returnResout;
				} else {
					s = result;
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
				Toast.makeText(DetailActivity.this, returnResout, Toast.LENGTH_SHORT).show();
			}else{
				try {
					JSONObject jsonObject = new JSONObject(result);
					String order = jsonObject.getString(JSONKey.FLAG_ORDER);
					linear.setVisibility(View.VISIBLE);
					detailOrder.setText(order);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
