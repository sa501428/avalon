package com.visor.illusionh;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * Main Activity for Hering Illusion App
 * 
 * @author Muhammad Saad Shamim
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	// take up the full screen in protrait orientation
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
        
		// synchronization of SeekBar with canvas
        final CanvasView cView = (CanvasView) findViewById(R.id.cView1);
        SeekBar bar = (SeekBar) findViewById(R.id.seekBar1);
        cView.setSeekBar(bar);
        
        // help button setup
        ImageButton iButton = (ImageButton) findViewById(R.id.ib1);
        iButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "Adjust the lines so that they are straight."
						+ "\nCurrent Value is: " + cView.getScore(), Toast.LENGTH_LONG).show();
			}
        });
    }
}
