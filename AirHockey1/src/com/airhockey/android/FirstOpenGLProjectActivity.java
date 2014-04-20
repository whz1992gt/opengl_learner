package com.airhockey.android;



import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class FirstOpenGLProjectActivity extends Activity {
	private GLSurfaceView glSurfaceView;
	private boolean rendererSet = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_first_open_glproject);
		glSurfaceView = new GLSurfaceView(this);
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000 || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
				&& (Build.FINGERPRINT.startsWith("generic")
						|| Build.FINGERPRINT.startsWith("unknown")
						|| Build.MODEL.contains("google_sdk")
						|| Build.MODEL.contains("Emulator")
						|| Build.MODEL.contains("Android SDK built for x86")));
		if (supportsEs2){
			//Request an OpenGL ES 2.0 compatible context
			glSurfaceView.setEGLContextClientVersion(2);
			
			//Assign our renderer
			glSurfaceView.setRenderer(new AirHockeyRenderer());
			rendererSet = true;
		}else {
			Toast.makeText(this,"This device does not support OpenGL ES 2.0", Toast.LENGTH_LONG).show();
			return ;
		}
		setContentView(glSurfaceView);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first_open_glproject, menu);
		return true;
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		if(rendererSet) {
			glSurfaceView.onPause();
		}
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(rendererSet){
			glSurfaceView.onResume();
		}
	}
	
}
