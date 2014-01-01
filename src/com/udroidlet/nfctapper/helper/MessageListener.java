package com.udroidlet.nfctapper.helper;

import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.content.Context;


//To get status of message after authentication
public final class MessageListener implements SocialAuthListener<Integer> {

	Context mContext;
	
	public MessageListener(Context context){
		this.mContext = context;
	}
	

public void onError(SocialAuthError e) {

}

@Override
public void onExecute(String arg0, Integer arg1) {
	// TODO Auto-generated method stub
	Integer status = arg1;
	if (status.intValue() == 200 || status.intValue() == 201 ||status.intValue() == 204)
	ToastMaker.makeToastShort(mContext, "Message posted",ToastMaker.STYLE_SUCCESS);
	else
	ToastMaker.makeToastShort(mContext, "Message posted",ToastMaker.STYLE_FAILURE);
}
}