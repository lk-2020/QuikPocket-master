package com.udroidlet.nfctapper.helper;

import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.udroidlet.nfctapper.interfaces.ImageLoadedCallback;

public class ImageLoaderHelper extends AsyncTask<Void, Void, Drawable> {

	ImageLoadedCallback mImageLoadedCallback;
	String mUrl;

	public ImageLoaderHelper(String url, ImageLoadedCallback imageLoadedCallback) {
		mImageLoadedCallback = imageLoadedCallback;
		mUrl = url;
	}

	@Override
	protected Drawable doInBackground(Void... params) {
		// TODO Auto-generated method stub

		try {
			InputStream is = (InputStream) new URL(mUrl).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			return null;
		}
	}

	protected void onPostExecute(Drawable result) {
		mImageLoadedCallback.onImageLoaded(result);
	}
}
