package com.example.cameraapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.cameraapp.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class CameraActivity extends Activity 
{
	private static final String TAG = null;
	private SurfaceView cameraPreview;
	private SurfaceHolder cameraPreviewHolder;
	private Camera mCamera;
	private static Timer mTimer;
	private CameraTimer mTask;
	private static boolean inPreview = false;
	private static TextView text;
	
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_camera);
		
		text = (TextView)findViewById(R.id.textView);
		text.setText("Log Start\n");
		
		mCamera = getCamera();
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		cameraPreview = (SurfaceView) findViewById(R.id.preview);
		cameraPreviewHolder = cameraPreview.getHolder();
		cameraPreviewHolder.addCallback(surfaceCallback);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, cameraPreview,HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() 
		{
			// Cached values.
			int mControlsHeight;
			int mShortAnimTime;

			@Override
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			public void onVisibilityChange(boolean visible) 
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) 
				{
					// If the ViewPropertyAnimator API is available
					// (Honeycomb MR2 and later), use it to animate the
					// in-layout UI controls at the bottom of the
					// screen.
					
					if (mControlsHeight == 0)
					{
						mControlsHeight = controlsView.getHeight();
					}
					if (mShortAnimTime == 0) 
					{
						mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
					}
					controlsView.animate().translationY(visible ? 0 : mControlsHeight).setDuration(mShortAnimTime);					
				} 
				else
				{
					// If the ViewPropertyAnimator APIs aren't
					// available, simply show or hide the in-layout UI
					// controls.
					controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
				}

				if (visible && AUTO_HIDE) 
				{
					// Schedule a hide().
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
			}
		});

		// Set up the user interaction to manually show or hide the system UI.
		cameraPreview.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				if (TOGGLE_ON_CLICK) 
				{
					mSystemUiHider.toggle();
				} 
				else 
				{
					mSystemUiHider.show();
				}
			}
		});

	}

	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		mTimer.cancel();
		if (inPreview) 
		{
			mCamera.stopPreview();
		}
		mCamera.release();
	    mCamera=null;
	    inPreview=false;
		super.onPause();
	}

	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
		if(mCamera == null) 
		{
			mCamera = getCamera();
		}
		mCamera.startPreview();
		inPreview = true;
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() 
	{
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) 
		{
			if (AUTO_HIDE) 
			{
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() 
	{
		@Override
		public void run() 
		{
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) 
	{
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCamera() 
	{
	    Camera c = null;
	    try 
	    {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e)
	    {
	        Log.d(TAG, "Camera is not available" + e.getMessage());
	        text.append("Camera is not available\n");
	    }
	    return c; // returns null if camera is unavailable
	}
	
	public class CameraTimer extends TimerTask 
	{
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			mCamera.takePicture(null, null, mPicture);
			CameraActivity.this.runOnUiThread(new Runnable() 
			{
				@Override
				public void run() 
				{
					text.append("Picture Captured\n");
				}
			});
		}	
	}
		
	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() 
	{
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
		{
			// If your preview can change or rotate, take care of those events here.
	        // Make sure to stop the preview before resizing or reformatting it.

	        if (cameraPreviewHolder.getSurface() == null)
	        {
	          // preview surface does not exist
	          return;
	        }

	        // stop preview before making changes
	        try 
	        {
	            mCamera.stopPreview();
	            inPreview = false;
	        } 
	        catch (Exception e)
	        {
	          // ignore: tried to stop a non-existent preview
	        }

	        // set preview size and make any resize, rotate or
	        // reformatting changes here

	        // start preview with new settings
	        try 
	        {
	            mCamera.setPreviewDisplay(cameraPreviewHolder);
	            mCamera.startPreview();
	            inPreview = true;
	        } 
	        catch (Exception e)
	        {
	            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
	        }
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) 
		{
			// TODO Auto-generated method stub
			// The Surface has been created, now tell the camera where to draw the preview.
	        try {
	            mCamera.setPreviewDisplay(holder);
	            mCamera.startPreview();
	            inPreview = true;
	            mTimer = new Timer();
	            mTask = new CameraTimer();
	            mTimer.schedule(mTask, 5000, 5000);   
	        } 
	        catch (IOException e) 
	        {
	            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
	            text.append("Error setting camera preview\n");
	            mCamera.release();
	            mCamera = null;
	        } 
		}
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) 
		{
			// TODO Auto-generated method stub
		}
	};
	
	private PictureCallback mPicture = new PictureCallback() 
	{
		Bitmap pic1;
		Bitmap pic2;
		int count = 0;
		
	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) 
	    {
	    	if(count < 2) {
	   			if(count == 0) {
	   				pic1 = BitmapFactory.decodeByteArray(data, 0, data.length);
	   				count++;
	   			}
	   			else if(count == 1) {
	   				pic2 = BitmapFactory.decodeByteArray(data, 0, data.length);
	   				new Compare().execute(pic1,pic2);
	   				count++;
	   			}
	    	}
	    	else {
	    		pic1 = pic2;
	    		pic2 = BitmapFactory.decodeByteArray(data, 0, data.length);
	    		new Compare().execute(pic1,pic2);
	    		count++;
	    	}
	        
	     // Restart the preview
	        try 
	        {
				mCamera.startPreview();
	        } 
	        catch (Exception e) 
	        {
	   			Log.e(TAG, "Failed to start preview");
	     	}
	    }
	};
	
	public class Compare extends AsyncTask<Bitmap, Void, Integer> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();	
		}
		
		@Override
		protected Integer doInBackground(Bitmap... params) {
			// TODO Auto-generated method stub
			int diffCount = 0;
			double diffPer = 0.0;
			int width = params[0].getWidth();
	    	int height = params[0].getHeight();
	    	int size = width*height;
	    	int[] pix1 = new int[width * height];
	    	int[] pix2 = new int[width * height];
	    	
	    	// Apply pixel-by-pixel change
		   	if(params.length == 2) {
		   		params[0].getPixels(pix1, 0, width, 0, 0, width, height);
		    	params[1].getPixels(pix2, 0, width, 0, 0, width, height);
		   		int index = 0;
		   		for (int y = 0; y < height; y++)
			   	{
			   		for (int x = 0; x < width; x++)
			   		{
			   			int r1 = (pix1[index] >> 16) & 0xff;
			   			int g1 = (pix1[index] >> 8) & 0xff;
			    		int b1 = pix1[index] & 0xff;
			    		
			    		int r2 = (pix2[index] >> 16) & 0xff;
			    		int g2 = (pix2[index] >> 8) & 0xff;
			    		int b2 = pix2[index] & 0xff;
			    		
			    		int gr1 = (r1 + g1 + b1)/3;
		    			int gr2 = (r2 + g2 + b2)/3;
		    			
			    		if (Math.abs(gr2-gr1)>=20)
			    		{
			    			diffCount++;
			    		}
			      			index++;
			    	} // x
			    } // y
			   	diffPer = ((double)diffCount/size)*100;
		   	}
		   	/*else if(params.length == 3) {
		   		params[0].getPixels(pix1, 0, width, 0, 0, width, height);
		    	params[1].getPixels(pix2, 0, width, 0, 0, width, height);
		    	params[2].getPixels(pix3, 0, width, 0, 0, width, height);
		    	int index = 0;
		    	for (int y = 0; y < height; y++)
		    	{
		    		for (int x = 0; x < width; x++)
		    		{
		    			int r1 = (pix1[index] >> 16) & 0xff;
		    			
		    			int r2 = (pix2[index] >> 16) & 0xff;
		    			
		    			int r = (r1 + r2)/2;
		    			
		    			int r3 = (pix3[index] >> 16) & 0xff;
		    			
			    		if (Math.abs(r3-r)>=20)
			    		{
			    			diffCount++;
			    		}
		    			
		    			index++;
		    		} // x
		    	} // y
		    	diffPer = ((double)diffCount/size)*100;

		   	}*/
		   	
		   	return (int)diffPer;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//text.append("Different count is " + diffCount + "\n");
	    	//text.append("Size is " + size + "\n");
			if(result > 50) {
				new HttpWebService().execute("zhidragon",swap());
			}
			text.append("Difference is " + result + "%\n");	
		}	
	}
	
	int count = 1;
	public String swap() {
		if(count%2 == 0) {
			count++;
			return "0";
		}
		else {
			count++;
			return "1";
		}			
	}
	
	public class HttpWebService extends AsyncTask<String, Void, JSONObject> {
		
		private static final String url = "http://ec2-54-254-255-187.ap-southeast-1.compute.amazonaws.com/grp/create_record3.php";
		
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
            // Building Parameters
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("sensor_id", params[0]));
            param.add(new BasicNameValuePair("status",params[1]));
           
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = new JSONParser().makeHttpRequest(url, "POST", param);
 
            // check log cat fro response
            Log.d("Create Response", json.toString());
 
            return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			// TODO Auto-generated method stub
			super.onPostExecute(json);
			text.append(json.toString() + "\n");
		}
		
	}
}
