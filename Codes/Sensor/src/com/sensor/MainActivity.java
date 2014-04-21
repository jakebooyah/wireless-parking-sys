/* Written by:
 * Tang Wei Qi (Image Processing Algorithm, Camera)
 * Lim Zhi En (Connection to Server, Camera)
*/

package com.sensor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.proto1.R;
import com.sensor.JSONParser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity 
{
	//Declaration of variables
	private String sensorid;
	private TextView text;
	private Camera mCamera;
	private SurfaceView cameraPreview;
	private SurfaceHolder cameraPreviewHolder;
	private static Timer mTimer;
	private CameraTimer mTask;
	private static boolean inPreview = false;
	private Bitmap pic1 = null;
	private Bitmap pic2 = null;
	private String status = "0";
	private double diffPer;
	private Button flash;
	private boolean isLighOn = false;
	private int perthres = 60;	//Percentage threshold of acceptance
	private int intthres = 20;	//Threshold for intensity differences allowed
	private int count = 2;
	
	//On the start of the application, the application does
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		//Standard import of layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Keeping the screen from sleeping
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		//GUI components
		flash = (Button) findViewById(R.id.flash);
		text = (TextView)findViewById(R.id.textView);
		text.setTextColor(Color.RED);
		text.setText("Log Started\n");
		
		//Initialise the camera and preview
		getCamera();
		getPreview();
		
		//Get parameters for flash
		final Parameters p = mCamera.getParameters();
		
		//GUI component: spinner selection of parking bay ID
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setOnItemSelectedListener(getOnItemSelectedListener());
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.sensor, R.layout.spinner_item);
		spinner.setAdapter(adapter);
		
		//Action listener for Flash toggle button
		flash.setOnClickListener(new OnClickListener() 
		{		 
			@Override
			public void onClick(View arg0) 
			{
				//if light is on
				if (isLighOn) 
				{
					//set to off
					p.setFlashMode(Parameters.FLASH_MODE_OFF);
					mCamera.setParameters(p);
					isLighOn = false;
 
				} 
				else 
				{
					//set to on
					p.setFlashMode(Parameters.FLASH_MODE_TORCH);
 
					mCamera.setParameters(p);
					isLighOn = true;
				}
 
			}
		});
	}
	
	//listener for spinner selection of sensor ID
	AdapterView.OnItemSelectedListener getOnItemSelectedListener() 
	{
		return new AdapterView.OnItemSelectedListener() 
		{

			//capture the result
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
			{
				Object item = parent.getItemAtPosition(pos);
				sensorid = item.toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}		
		};
	}
	
	//Task to be carry out in one loop
	public class CameraTimer extends TimerTask 
	{

		@Override
		public void run()
		{
			MainActivity.this.runOnUiThread(new Runnable() 
			{
				@Override
				public void run() 
				{
			    	text.append("\nTimerTask\n");
				}
			});
			
			if (pic1==null)
			{
				//takes picture 1
				takePic1();
			}
			else
			{
				//takes picture 2
				takePic2();				
			}		
		}			
	}
	
	//Checking of the internet connectivity
	private boolean isNetworkAvailable() 
	{
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	//Camera initialisation
	public void getCamera() 
	{
	    mCamera = null;
	    try 
	    {
	    	mCamera = Camera.open();	
	    	mCamera.setDisplayOrientation(90);

	    }
	    catch (Exception e)
	    {
	        //text.append("Camera is not available\n");
	    }
	}
	
	//Preview initialisation
	public void getPreview()
	{
		cameraPreview = (SurfaceView)findViewById(R.id.preview);
		cameraPreviewHolder = cameraPreview.getHolder();
		cameraPreviewHolder.addCallback(surfaceCallback);
	}
	
	//When the activity is paused (Prevent crashing when multitasking)
	@Override
	protected void onPause() 
	{		
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
	
	//When the activity is resumed (Prevent crashing when multitasking)
	@Override
	protected void onResume() 
	{		
		super.onResume();
		if(mCamera == null) 
		{
			getCamera();
		}
		mCamera.startPreview();
		inPreview = true;
	}
	
	//Surface Holder of Preview
	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() 
	{
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
		{
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
	        catch (Exception e) {}

	        // start preview with new settings
	        try 
	        {
	        	mCamera.setPreviewDisplay(cameraPreviewHolder);
	            mCamera.startPreview();	  
	            inPreview = true;
	        } 
	        catch (Exception e) {}	      
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) 
		{
			// The Surface has been created, now tell the camera where to draw the preview.
	        try {
	            mCamera.setPreviewDisplay(holder);
	            mCamera.startPreview();  
	            inPreview = true;
	            mTimer = new Timer();
	            mTask = new CameraTimer();
	            mTimer.schedule(mTask, 7000, 7000);		
	        } 
	        catch (IOException e) 
	        {
	            text.append("Error setting camera preview\n");
	            mCamera.release();
	            mCamera = null;
	        } 
		}
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {}
	};  

	//Storing the picture captured
	private PictureCallback mPicture1 = new PictureCallback() 
	{		
		@Override
		public void onPictureTaken(byte[] data, Camera mCamera) 
		{
			pic1 = BitmapFactory.decodeByteArray(data, 0, data.length);
		    
			// Restart the preview
			try 
			{
				mCamera.startPreview();
				inPreview = true;
			} 
			catch (Exception e) 
			{
		      
			}
		}
	};
	
	//Storing the picutre captured
	private PictureCallback mPicture2 = new PictureCallback() 
	{		
		@Override
		public void onPictureTaken(byte[] data, Camera mCamera) 
		{
			pic2 = BitmapFactory.decodeByteArray(data, 0, data.length);
			compare();
			updateServer();
			swap();
		    
			// Restart the preview
			try 
			{
				mCamera.startPreview();
				inPreview = true;
			} 
			catch (Exception e) 
			{
		        	
			}
		}
	};
	
	//Capturing the picture
	public void takePic1()
	{
		mCamera.takePicture(null, null, mPicture1);
		MainActivity.this.runOnUiThread(new Runnable() 
		{
			@Override
			public void run() 
			{
				text.append("Picture1 Captured\n");
			}
		});
	}
	
	//Capturing the picture
	private void takePic2()
	{
		mCamera.takePicture(null, null, mPicture2);
		MainActivity.this.runOnUiThread(new Runnable() 
		{
			@Override
			public void run() 
			{
				text.append("Picture" + count++ + " Captured\n");
			}
		});
	}
	
	//Image processing algorithm
	//Checking for changes
	private void compare()
	{
		MainActivity.this.runOnUiThread(new Runnable() 
		{
			@Override
			public void run() 
			{
			   	text.append("Comparing..\n");
			}
		});
			
			int diffCount = 0;
			int width = pic1.getWidth();
	    	int height = pic1.getHeight();
	    	int size = width*height;
	    	
	    	int[] pix1 = new int[width * height];
	    	pic1.getPixels(pix1, 0, width, 0, 0, width, height);
	    	
	    	int[] pix2 = new int[width * height];
	    	pic2.getPixels(pix2, 0, width, 0, 0, width, height);
	    	
	    	// Apply pixel-by-pixel change
	    	int index = 0;
	    	for (int y = 0; y < height; y++)
	    	{
	    		for (int x = 0; x < width; x++)
	    		{
	    			//getting r,g,b of both pictures at coordinate
	    			int r1 = (pix1[index] >> 16) & 0xff;
	    			int g1 = (pix1[index] >> 8) & 0xff;
	    			int b1 = pix1[index] & 0xff;
	    			
	    			int r2 = (pix2[index] >> 16) & 0xff;
	    			int g2 = (pix2[index] >> 8) & 0xff;
	    			int b2 = pix2[index] & 0xff;
	    			
	    			//converting into greyscale
	    			int gr1 = (r1 + g1 + b1)/3;
	    			int gr2 = (r2 + g2 + b2)/3;
	    			
	    			//compareing differences with threshold
	    			if (Math.abs(gr2-gr1)>=intthres)
	    			{
	    				diffCount++;
	    			}
	    			
	    			index++;
	    		} // x
	    	} // y
	    	
	    	//calculating percentage differences
	    	diffPer = ((double)diffCount/size)*100;
	    	
	    	MainActivity.this.runOnUiThread(new Runnable() 
			{
				@Override
				public void run() 
				{
			    	text.append("Difference is " + (int)diffPer + "%\n");
				}
			});
		}
	
	//Swapping pic 2 to pic 1 and clear pic 2 
	private void swap() 
	{
		pic1 = pic2;
		pic2 = null;
	}
	
	//Flipping switch of status
	private void updateServer()
	{
		if(diffPer > perthres) 
		{
			if(status.equals("0"))
			{
				status = "1";
			}
			else
			{
				status = "0";
			}
			if(isNetworkAvailable()) {
				new HttpWebService().execute(sensorid,status);
			}
			else {
				text.append("No Internet connection\n");
			}
		}
	}
	
	//Mechanism to send data to server and update status in database
	public class HttpWebService extends AsyncTask<String, Void, String> {
		
		private static final String url = "http://ec2-54-254-255-187.ap-southeast-1.compute.amazonaws.com/grp/update_record.php";
		
		@Override
		protected String doInBackground(String... params) 
		{
            // Building Parameters
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("sensor_id", params[0]));
            param.add(new BasicNameValuePair("status",params[1]));
           
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = new JSONParser().makeHttpRequest(url, "POST", param);
 
            // check log cat for response
            Log.d("Create Response", json.toString());
 
            try {
				return json.getString("message") + json.getInt("status") + "\n";
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "No response from server\n";
			}
		}

		@Override
		protected void onPostExecute(String status) 
		{
			super.onPostExecute(status);
			text.append(status);
		}
		
	}


}
