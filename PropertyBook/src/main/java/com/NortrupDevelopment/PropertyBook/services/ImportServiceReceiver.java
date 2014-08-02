package com.NortrupDevelopment.PropertyBook.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class ImportServiceReceiver extends ResultReceiver {
	
	PBICReceiver receiver;

	public ImportServiceReceiver(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}
	
	public void setReceiver (PBICReceiver receiver) {
		this.receiver = receiver;
	}
	
	public interface PBICReceiver {
		public void onReceiveResult(int resultCode, Bundle resultData);
	}

	@Override
	protected void onReceiveResult(int resultCode, Bundle resultData) {
		if(receiver != null) {
			receiver.onReceiveResult(resultCode, resultData);
		}
	}
}
