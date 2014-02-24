package com.example.simpletodo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.widget.TextView;

public class ToDoActivity extends Activity 
{	
	TextView text2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		
		text2 = (TextView)findViewById(R.id.jake);
		text2.setText("Start\n");
		text2.setTextSize(80);
		MyCount timerCount = new MyCount(30 * 1000, 1000);
        timerCount.start();
    }

    public class MyCount extends CountDownTimer 
    {
    	public MyCount(long millisInFuture, long countDownInterval) 
    	{
            super(millisInFuture, countDownInterval);
        }

    	@Override
    	public void onFinish() 
    	{
    		text2.append("BOOM!\n");        
    	}

        @Override
        public void onTick(long millisUntilFinished) 
        {
        	text2.append("tick\n");         
        }   
    }

}
