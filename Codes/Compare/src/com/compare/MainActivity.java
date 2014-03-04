package com.compare;

import java.io.File;

import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MainActivity extends Activity 
{
	Bitmap myBitmap1;
	Bitmap myBitmap2;
	TextView text1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		text1 = (TextView)findViewById(R.id.text1);
		text1.setText("Start!\n");
		firstImage();
		secImage();
		compare();
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
	
	private void compare()
	{
		int diffCount = 0;
		double diffPer;
		int width = myBitmap1.getWidth();
    	int height = myBitmap1.getHeight();
    	int size = width*height;
    	int[] pix1 = new int[width * height];
    	myBitmap1.getPixels(pix1, 0, width, 0, 0, width, height);
    	
    	int[] pix2 = new int[width * height];
    	myBitmap2.getPixels(pix2, 0, width, 0, 0, width, height);
    	
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
    	
    	text1.append("Different count is " + diffCount + "\n");
    	text1.append("Size is " + size + "\n");
    	text1.append("Difference is " + (int)diffPer + "%\n");
    	
	}

}
