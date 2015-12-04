package com.example.insys.oneibory.Utility;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

public class LinkFB extends AsyncTask<Void, Void, Void> {


	private String TAG = getClass().getName();

	public LinkFB() {

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params) {
		StringBuilder stringBuilder = new StringBuilder();

		String results = "";
//		String argsUrl = "{}";
		try {
			String url = "http://oneivory.xhtmlxpert.com/api/rest/category/"
					;
			Log.d(TAG, "URL for device Register is.." + url);

			HttpGet httpGet = new HttpGet(url);
			HttpClient client = new DefaultHttpClient();
			httpGet.setHeader("Accept", "application/json");
			httpGet.setHeader("Content-type", "application/json");
			httpGet.setHeader("Accept-Charset", "utf-8");

			HttpResponse response;
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
			results = stringBuilder.toString();
			Log.d("dxds", " " + results);

		} catch (Exception e) {
			Log.d(TAG, "errror" + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
	}

}
