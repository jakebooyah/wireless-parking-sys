package com.imagepro2;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.imagepro2.R;

import android.os.Bundle;
import android.widget.ImageView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class MainActivity extends Activity 
{
	Bitmap myBitmap1;
	Bitmap myBitmap2;
	Bitmap myBitmap3;
	ImageView myImageView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        firstImage();
        secImage();
        merge();
        save();
        
        myImageView = (ImageView)findViewById(R.id.imageView1);
        myImageView.setImageBitmap(myBitmap3);
        	      	
        
	}
	
	private void firstImage()
	{
		File imageFile = new File("/sdcard/Pictures/MyCameraApp/test.jpg");
        if (imageFile.exists())
        {
        	myBitmap1 = BitmapFactory.decodeFile("/sdcard/Pictures/MyCameraApp/test.jpg");
        }
	}
	
	private void secImage()
	{
		File imageFile = new File("/sdcard/Pictures/MyCameraApp/test1.jpg");
        if (imageFile.exists())
        {
        	myBitmap2 = BitmapFactory.decodeFile("/sdcard/Pictures/MyCameraApp/test1.jpg");
        }
	}
	
	private void merge()
    {
    	// Create new array
    	int width = myBitmap1.getWidth();
    	int height = myBitmap1.getHeight();
    	int[] pix1 = new int[width * height];
    	myBitmap1.getPixels(pix1, 0, width, 0, 0, width, height);
    	
    	int[] pix2 = new int[width * height];
    	myBitmap2.getPixels(pix2, 0, width, 0, 0, width, height);
    	
    	int[] pix3 = new int[width * height];
    	
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
    			
    			int r = (r1 + r2)/2;
    			int g = (g1 + g2)/2;
    			int b = (b1 + b2)/2;
    			
    			pix3[index] = 0xff000000 | (r << 16) | (g << 8) | b;
    			index++;
    		} // x
    	} // y
    	
    	// Change bitmap to use new array
    	Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    	bitmap.setPixels(pix3, 0, width, 0, 0, width, height);    	
    	myBitmap3 = null;
    	myBitmap3 = bitmap;
    	pix1 = null;
    	pix2 = null;
    	pix3 = null;
    }
	
	private void save()
	{
		try {
			File outputDirectory = new File("/sdcard/Pictures/MyCameraApp");
			String outputFile = "result.jpg";
			String outputPath = outputDirectory.toString() + "/" + outputFile;
			int quality = 100;
			FileOutputStream fileOutStr = new FileOutputStream(outputPath);
			BufferedOutputStream bufOutStr = new BufferedOutputStream(fileOutStr);
			myBitmap3.compress(CompressFormat.JPEG, quality, bufOutStr);
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