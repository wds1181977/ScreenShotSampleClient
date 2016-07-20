package com.example.screenshotclient;

import com.example.screenshotsample.IScreenshotControl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	protected static final String TAG = "MainActivity";
	private TextView tv;
	IScreenshotControl mScreenshotControll;
	ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mScreenshotControll = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.v(TAG, "onServiceConnected");
			mScreenshotControll = IScreenshotControl.Stub.asInterface(service);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = new Intent("com.example.screenshotsample.start");
		intent.setPackage("com.example.screenshotsample");
    	boolean result = bindService(intent, conn, Context.BIND_AUTO_CREATE);
		tv = (TextView) findViewById(R.id.textView1);
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					if (mScreenshotControll != null) {
						Log.v(TAG, "onServiceConnected");
						String path = mScreenshotControll.takeScreenshot();

						tv.setText("截图文件保存在"+path);
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
    protected void onDestroy() {
    	super.onDestroy();
    	unbindService(conn);
    }
	

}
