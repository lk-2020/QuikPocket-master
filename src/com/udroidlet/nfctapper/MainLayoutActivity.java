package com.udroidlet.nfctapper;

import java.util.Timer;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

import com.facebook.android.DialogError;
import com.udroidlet.nfctapper.helper.ImageLoaderHelper;
import com.udroidlet.nfctapper.helper.JSONParser;
import com.udroidlet.nfctapper.helper.RequestHelper;
import com.udroidlet.nfctapper.helper.ToastMaker;
import com.udroidlet.nfctapper.interfaces.ImageLoadedCallback;
import com.udroidlet.nfctapper.interfaces.UrlLoadedCallback;

/**
 * Created with Eclipse. User: uday Date: 10/11/13 Time: 1:10 PM
 */
@SuppressLint("NewApi")
public class MainLayoutActivity extends Activity implements
		ImageLoadedCallback, UrlLoadedCallback {

	protected final static String LOG_TAG = MainLayoutActivity.class
			.getSimpleName();

	protected Activity mActivity;
	protected Context mContext;
	protected IntentFilter[] mWriteTagFilters;
	protected PendingIntent mNfcPendingIntent;
	protected NfcAdapter mNfcAdapter;
	protected Tag mTag;
	SocialAuthAdapter mAdapter;
	Profile currentProfile;

	private Timer _timer;
	private int _index;
	// private MyHandler handler;
	private ImageView _imagView;
	AnimationDrawable anim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		mActivity = this;
		mContext = this;
		initializeNfcAdapterAndIntentFilters();
		initSocial();
		// initTimer();
		// initAnimation();

	}

	// @Override
	// public void onWindowFocusChanged (boolean hasFocus){
	// initAnimation();
	// anim.start();
	// }

	// public void initAnimation(){
	// _imagView = (ImageView) findViewById(R.id.imageViewArrow);
	// _imagView.setBackgroundResource(R.drawable.movie);
	// anim = (AnimationDrawable) _imagView.getBackground();
	// }

	// public void initTimer() {
	// _index = 0;
	// handler = new MyHandler();
	// _timer = new Timer();
	// _timer.schedule(new TickClass(), 500, 200);
	// _imagView = (ImageView) findViewById(R.id.imageViewArrow);
	// }
	//
	// private class TickClass extends TimerTask {
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// handler.sendEmptyMessage(_index);
	// _index++;
	// if (_index >= 5) {
	// _index = 1;
	// }
	// }
	// }
	//
	// private class MyHandler extends Handler {
	// @Override
	// public void handleMessage(Message msg) {
	// // TODO Auto-generated method stub
	// super.handleMessage(msg);
	//
	// try {
	// Bitmap bmp = BitmapFactory.decodeStream(mContext.getAssets()
	// .open("arrow_" + _index + ".png"));
	// _imagView.setImageBitmap(bmp);
	//
	// Log.v("Loaing Image: ", _index + "");
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// Log.v("Exception in Handler ", e.getMessage());
	// }
	// }
	// }

	public void initSocial() {
		mAdapter = new SocialAuthAdapter(new ResponseListener());
		// mAdapter.addConfig(Provider.FACEBOOK, "535488449865256",
		// "6616f026e64339d18be6c22076e21d1f", permissions);
		mAdapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
		mAdapter.addProvider(Provider.FOURSQUARE, R.drawable.foursquare);
		mAdapter.addProvider(Provider.LINKEDIN, R.drawable.linkedin);
		mAdapter.addCallBack(Provider.FACEBOOK,
				"http://microsoftapphack.appspot.com");
		mAdapter.addCallBack(Provider.FOURSQUARE,
				"http://microsoftapphack.appspot.com");
		mAdapter.addCallBack(Provider.LINKEDIN,
				"http://microsoftapphack.appspot.com");

	}

	private final class ResponseListener implements DialogListener {
		public void onComplete(Bundle values) {
			// mAdapter.updateStatus("testPost", new MessageListener(mContext),
			// false);
		}

		public void onError(DialogError error) {
			Log.d("ShareButton", "Error");
		}

		public void onCancel() {
			Log.d("ShareButton", "Cancelled");
		}

		@Override
		public void onBack() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(SocialAuthError arg0) {
			// TODO Auto-generated method stub

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		enableForegroundDispatch(mNfcPendingIntent, mWriteTagFilters, null);
		Intent myintent = getIntent();
		if (myintent != null) {
			mTag = myintent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(myintent.getAction())
					|| NfcAdapter.ACTION_TECH_DISCOVERED.equals(myintent
							.getAction())
					|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(myintent
							.getAction())) {

//				ToastMaker.makeToastShort(mContext, "Tag scanned !",
//						ToastMaker.STYLE_SUCCESS);

//				mAdapter.authorize(mContext, Provider.FACEBOOK);
				String ID = "041F4C7A712881";
				final byte[] tagID = new byte[] {(byte)0x04,(byte)0x1F,(byte)0x4C,(byte)0x7A,(byte)0x71,0x28,(byte)0x81};
				
				 mAdapter.authorize(mContext, Provider.LINKEDIN);

				currentProfile = mAdapter.getUserProfile();

				JSONObject json = new JSONObject();

				if (currentProfile != null) {
					String profileImageUrl = currentProfile
							.getProfileImageURL();
					profileImageUrl += "?type=large";
					ImageLoaderHelper imageLoaderHelper = new ImageLoaderHelper(
							profileImageUrl, this);
						imageLoaderHelper.execute();
					
					RequestHelper requestHelper = new RequestHelper(this);
					requestHelper
							.execute("http://thawing-journey-1328.herokuapp.com/");
					// ToastMaker.makeImageToast(mContext, "Hello "
					// + currentProfile.getFullName(),
					// LoadImageFromWebOperations(profileImageUrl));
					// ToastMaker.makeToastShort(mContext, "Hello "
					// + currentProfile.getFullName(),
					// ToastMaker.STYLE_SUCCESS);
				}

				try {
					json.put("TagId", "041F4C7A712881");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONParser jsonParser = new JSONParser(this);
				jsonParser.execute(json);

			}
			setIntent(null);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		disableForegroundDispatch();
		mTag = null;

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d(LOG_TAG, "onNewIntent()");
		setIntent(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_layout, menu);
		return true;
	}

	protected void disableForegroundDispatch() {
		try {
			mNfcAdapter.disableForegroundDispatch(this);
		} catch (IllegalStateException e) {
			Log.d(LOG_TAG,
					"feature not supported or activity not in foreground");
		}
	}

	protected void enableForegroundDispatch(PendingIntent intent,
			IntentFilter[] filters, String[][] techLists) {
		try {
			String[][] techListsArray = new String[][] {
					new String[] { MifareUltralight.class.getName(),
							Ndef.class.getName(), NfcA.class.getName() },
					new String[] { MifareClassic.class.getName(),
							Ndef.class.getName(), NfcA.class.getName() } };
			mNfcAdapter.enableForegroundDispatch(this, intent, filters,
					techListsArray);
		} catch (IllegalStateException e) {
			Log.d(LOG_TAG,
					"feature not supported or activity not in foreground");
		}
	}

	protected void initializeNfcAdapterAndIntentFilters() {
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		IntentFilter tagDetected = new IntentFilter(
				NfcAdapter.ACTION_TAG_DISCOVERED);
		IntentFilter ndefDetected = new IntentFilter(
				NfcAdapter.ACTION_NDEF_DISCOVERED);
		IntentFilter techDetected = new IntentFilter(
				NfcAdapter.ACTION_TECH_DISCOVERED);
		mWriteTagFilters = new IntentFilter[] { ndefDetected, techDetected,
				tagDetected };
	}

	protected void indicateTagNotNdefFormatted() {
		// ToastMaker.makeToastLong(getApplicationContext(),"Tag not NDEF Formatted",ToastMaker.STYLE_FAILURE);
	}

	protected void indicateTagTechNotSupported() {
		// ToastMaker.makeToastLong(getApplicationContext(),"Tag Technology not Supported",ToastMaker.STYLE_FAILURE);
	}

	protected void indicateTagWriteStatus(boolean tagWriteSuccessful) {
		if (tagWriteSuccessful) {
			indicateTagWriteSuccessfull();
		} else {
			indicateTagWriteUnsuccessfull();
		}
	}

	protected void indicateTagWriteSuccessfull() {
		// ToastMaker.makeToastShort(getApplicationContext(),
		// "Tag write successful", ToastMaker.STYLE_SUCCESS);
	}

	protected void indicateTagWriteUnsuccessfull() {
		// ToastMaker.makeToastShort(getApplicationContext(),
		// "Tag write unsuccessful", ToastMaker.STYLE_SUCCESS);
	}

	@Override
	public void onImageLoaded(Drawable result) {
		// TODO Auto-generated method stub
		ToastMaker.makeImageToast(mContext,
				"Hello " + currentProfile.getFirstName() + " !!", result);
	}

	@Override
	public void onUrlLoaded(String response) {
		// TODO Auto-generated method stub
//		ToastMaker.makeToastShort(mContext, response, ToastMaker.STYLE_INFO);
	}

	@Override
	public void onJSONParsed(JSONObject response) {
		// TODO Auto-generated method stub
//		ToastMaker.makeToastShort(mContext, "Response from server is",
//				ToastMaker.STYLE_INFO);
	}

}
