package com.imagepro;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Bundle;
import android.widget.ImageView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;


public class Processing extends Activity 
{	
	Bitmap myBitmap;
	ImageView myImageView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_processing);
		
		File imageFile = new File("/sdcard/Pictures/MyCameraApp/test.jpg");
        if (imageFile.exists())
        {
        	// Load the image from file
        	myBitmap = BitmapFactory.decodeFile("/sdcard/Pictures/MyCameraApp/test.jpg");
        	
        	
        	negative();
        	save();
        	
        	myImageView = (ImageView)findViewById(R.id.image);
        	myImageView.setImageBitmap(myBitmap);
        	      	
        }
	}

	private void negative()
    {
    	// Create new array
    	int width = myBitmap.getWidth();
    	int height = myBitmap.getHeight();
    	int[] pix = new int[width * height];
    	myBitmap.getPixels(pix, 0, width, 0, 0, width, height);
    	
    	// Apply pixel-by-pixel change
    	int index = 0;
    	for (int y = 0; y < height; y++)
    	{
    		for (int x = 0; x < width; x++)
    		{
    			int r = (pix[index] >> 16) & 0xff;
    			int g = (pix[index] >> 8) & 0xff;
    			int b = pix[index] & 0xff;
    			r = Math.max(0, Math.min(255, 225-r));
    			g = Math.max(0, Math.min(255, 225-g));
    			b = Math.max(0, Math.min(255, 225-b));
    			pix[index] = 0xff000000 | (r << 16) | (g << 8) | b;
    			index++;
    		} // x
    	} // y
    	
    	// Change bitmap to use new array
    	Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    	bitmap.setPixels(pix, 0, width, 0, 0, width, height);    	
    	myBitmap = null;
    	myBitmap = bitmap;
    	pix = null;
    }
	
	private void save()
	{
		try {
			File outputDirectory = new File("/sdcard/Pictures/MyCameraApp");
			String outputFile = "test1.jpg";
			String outputPath = outputDirectory.toString() + "/" + outputFile;
			int quality = 100;
			FileOutputStream fileOutStr = new FileOutputStream(outputPath);
			BufferedOutputStream bufOutStr = new BufferedOutputStream(fileOutStr);
			myBitmap.compress(CompressFormat.JPEG, quality, bufOutStr);
			bufOutStr.flush(); bufOutStr.close();
		}
		catch (FileNotFoundException exception)
		{        			
		}
		catch (IOException exception)
		{
		}
	}
}
