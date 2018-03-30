/*
 * This example creates a Handler queue for the main thread.
 * A background thread places messages in the queue to update
 * progress bars and write to the log.
 */
package com.course.example.messages;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;

public class Messages extends Activity {
	
	private ProgressBar bar;
	private ProgressBar bar2;
	private TextView msgWorking;

	//Create Handler object to handle messages placed on queue 
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		
		public void handleMessage(Message msg) {
			bar.incrementProgressBy(5);
			
			if (bar.getProgress() == bar.getMax()){
				msgWorking.setText("Done");
				bar.setVisibility(View.INVISIBLE);	
				bar2.setVisibility(View.INVISIBLE);
			}
			else {
				String text = "Working..." + bar.getProgress() + "%";
				msgWorking.setText(text);
			}
			//write message contents to log
			Log.i("Message", (String)msg.obj + " " + msg.what);
					
		}
	};

	//prepare progress bars and make visible
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.main);

		bar = (ProgressBar) findViewById(R.id.progress);
		bar2 = (ProgressBar) findViewById(R.id.progress2);

		bar.setMax(100);
		bar.setVisibility(View.VISIBLE);	
		bar2.setVisibility(View.VISIBLE);

		msgWorking = (TextView)findViewById(R.id.TextView01);

	}

	//create and start thread
	@Override
	public void onStart() {
		super.onStart();
		bar.setProgress(0);
		Thread t = new Thread(background);
		t.start();
	}

	//Runnable object that executes as the background thread
	Runnable background = new Runnable() {
		public void run() {
			try {
				//for each iteration of loop, create a Message object and place on queue
				for (int i = 0; i < 20; i++) {
					Thread.sleep(1000);
					Message msg = handler.obtainMessage(i, "Hello");
					handler.sendMessage(msg);					
				}
			} catch (InterruptedException e) {System.out.println(e.getMessage());
			}
		}
	};
}