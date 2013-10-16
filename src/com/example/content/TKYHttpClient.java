package com.example.content;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class TKYHttpClient {

	public static String connect(String url, String param) {
		try {
			Log.i("200param", "url:"+url);
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("json", param));
			Log.i("200param", param);
			httpRequest.setHeader("content-type",
					"application/x-www-form-urlencoded; charset=utf-8");
			httpRequest.addHeader("Accept-Charset", "utf-8");
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			Log.i("200param", "003");
			HttpParams my_httpParams = new BasicHttpParams();
			Log.i("200param", "003-1");
			HttpConnectionParams.setConnectionTimeout(my_httpParams, 3000);
			Log.i("200param", "003-2");
			HttpConnectionParams.setSoTimeout(my_httpParams, 3000);
			Log.i("200param", "003-3");
			DefaultHttpClient mHttpClient = new DefaultHttpClient(my_httpParams);
			Log.i("200param", "003-4");
			BasicHttpResponse httpResponse = (BasicHttpResponse) mHttpClient.execute(httpRequest);
			Log.i("200param", "003-5");
			// ======================================================
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.i("200param", "002:"+statusCode);
			if (statusCode == 200) {
				Log.i("200", "200");
				return EntityUtils.toString(httpResponse.getEntity(), "GB2312");
			}else{
				Log.i("200statusCode", ""+statusCode);
			}
			Log.i("200param", "004");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
