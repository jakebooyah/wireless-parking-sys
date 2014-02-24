package com.example.mycameraapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

	private static final String TAG = null;
	private Camera mCamera;
	private CameraPreview mPreview;
	private static boolean inPreview = false;

	public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
		
		private final String TAG = null;
		private SurfaceHolder mHolder;
		
		public CameraPreview(Context context) {
			super(context);
			mHolder = getHolder();
			mHolder.addCallback(this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			// If your preview can change or rotate, take care of those events here.
	        // Make sure to stop the preview before resizing or reformatting it.

	        if (mHolder.getSurface() == null){
	          // preview surface does not exist
	          return;
	        }

	        // stop preview before making changes
	        try {
	            mCamera.stopPreview();
	            inPreview = false;
	        } catch (Exception e){
	          // ignore: tried to stop a non-existent preview
	        }

	        // set preview size and make any resize, rotate or
	        // reformatting changes here

	        // start preview with new settings
	        try {
	            mCamera.setPreviewDisplay(mHolder);
	            mCamera.startPreview();
	            inPreview = true;
	        } catch (Exception e){
	            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
	        }
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			// The Surface has been created, now tell the camera where to draw the preview.
	        try {
	            mCamera.setPreviewDisplay(holder);
	            mCamera.startPreview();
	            inPreview = true;
	            /*Handler aHandler = new Handler();
	            aHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mCamera.takePicture(null, null, mPicture);
					}
	            	
	            }, 5000);*/
	            CameraTimer mTimer = new CameraTimer();
	            Thread t = new Thread(mTimer);
	            t.start();
	        } catch (IOException e) {
	            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
	            mCamera.release();
	            mCamera = null;
	        } /*catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			mCamera.stopPreview();
			inPreview = false;
			mCamera.release();
			mCamera = null;
		}	
	}
	
	public class CameraTimer implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(inPreview) {
				mCamera.takePicture(null, null, mPicture);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mCamera = getCamera();
		//mCamera.getParameters().setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
		mPreview = new CameraPreview(this);
		FrameLayout mFrame = (FrameLayout) findViewById(R.id.camera_preview);
		mFrame.addView(mPreview);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(inPreview) {
			mCamera.stopPreview();
			inPreview = false;
		}
		mCamera.release();
		mCamera = null;
		super.onPause();
	}
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCamera(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        Log.d(TAG, "Camera is not available" + e.getMessage());
	    }
	    return c; // returns null if camera is unavailable
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
		// This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.
	    
	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
	    return mediaFile;
	}
	
	private PictureCallback mPicture = new PictureCallback() {

	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {
	    	try {
				// Give the user some time to view the image
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

	        File pictureFile = getOutputMediaFile();
	        if (pictureFile == null){
	            Throwable e = null;
				Log.d(TAG, "Error creating media file, check storage permissions: " + e.getMessage());
	            return;
	        }

	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            fos.write(data);
	            fos.close();
	        } catch (FileNotFoundException e) {
	            Log.d(TAG, "File not found: " + e.getMessage());
	        } catch (IOException e) {
	            Log.d(TAG, "Error accessing file: " + e.getMessage());
	        }
	        
	     // Restart the preview
	        try {
				mCamera.startPreview();
	        } 
	        catch (Exception e) {
	   			Log.e(TAG, "Failed to start preview");
	     	}
	    }
	};
	
}
