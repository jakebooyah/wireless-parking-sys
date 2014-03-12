package com.proto1;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.app.Activity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private TextView text;
	private Camera mCamera;
	private SurfaceView cameraPreview;
	private SurfaceHolder cameraPreviewHolder;
	private static Timer mTimer;
	private CameraTimer mTask;
	private static boolean inPreview = false;
	private Bitmap pic1;
	private Bitmap pic2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		text = (TextView)findViewById(R.id.textView);
		text.setText("Log Start\n");
		
		getCamera();
		getPreview();
		
		mTimer = new Timer();
        mTask = new CameraTimer();
        mTimer.schedule(mTask, 5000, 5000); 
		//negative();
		//save();
		
	}
	
	public class CameraTimer extends TimerTask 
	{

		@Override
		public void run() 
		{
			//takePic1();
			//takePic2();
			//compare();
			//swap();
			//takePic2();
			//compare();
			
			MainActivity.this.runOnUiThread(new Runnable() 
			{
				@Override
				public void run() 
				{
					text.append("LOOP\n");
				}
			});
		}

			
	}
	
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
	        text.append("Camera is not available\n");
	    }
	}
	
	public void getPreview()
	{
		cameraPreview = (SurfaceView)findViewById(R.id.preview);
		cameraPreviewHolder = cameraPreview.getHolder();
		cameraPreviewHolder.addCallback(surfaceCallback);
	}
	
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
	        	text.append("Failed to start preview\n");
	     	}
	    }
	};
	
	private PictureCallback mPicture2 = new PictureCallback() 
	{		
	    @Override
	    public void onPictureTaken(byte[] data, Camera mCamera) 
	    {
	   		pic2 = BitmapFactory.decodeByteArray(data, 0, data.length);
	    
	   		// Restart the preview
	        try 
	        {
				mCamera.startPreview();
				inPreview = true;
	        } 
	        catch (Exception e) 
	        {
	        	text.append("Failed to start preview\n");
	     	}
	    }
	};
	
	public void takePic1()
	{
		mCamera.takePicture(null, null, mPicture1);
		text.append("Picture1 Captured\n");
	}
	
	private void takePic2()
	{
		mCamera.takePicture(null, null, mPicture2);
		text.append("Picture2 Captured\n");
	}
	
	private void compare()
	{
		int diffCount = 0;
		double diffPer;
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
    	
    	text.append("Different count is " + diffCount + "\n");
    	text.append("Size is " + size + "\n");
    	text.append("Difference is " + (int)diffPer + "%\n");
	}
	
	private void swap()
	{
		pic1 = pic2;
		pic2 = null;
	}
	
	
}
