package jumb.myapplab.pascal.jumb;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class settings extends ActionBarActivity{
	
	private static final String TAG = null;
	private SeekBar updatetime = null;
	private Button button1;
	private TextView textinterval;
	private TextView text;
	private int progressChanged = 30;
	
	protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_layout);
        
        button1 = (Button)findViewById(R.id.end);

        text    = (TextView)findViewById(R.id.info);
        textinterval    = (TextView)findViewById(R.id.text1);
        
        text.setText("Bei Fragen oder Ideen meldet euch bei kalle@jumbseite.de");
        
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        updatetime = (SeekBar) findViewById(R.id.seekBar1);

        updatetime.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int stepSize = 5;
			
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				progress = ((int)Math.round(progress/stepSize))*stepSize;
			    if(progress < 5){
			    	seekBar.setProgress(5);
			    	progressChanged = 5;
			    }else{
			    	seekBar.setProgress(progress);
			    	progressChanged = progress;
			    }
			    textinterval.setText("Aktualisierungs Intervall " + progress + " min:");
				
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				textinterval.setText("Aktualisierungs Intervall " + progressChanged + " min:");
			}
		});
           
	}
		
	
	   // Method to start the service
	   public void startService(View view) {
		   MainActivity.editor.putInt("inter",  progressChanged);
		   MainActivity.editor.commit();

		   if(isMyServiceRunning(MyService.class.getName())){
				Log.d(TAG, "active" );
			} else {
				startService(new Intent(getBaseContext(), MyService.class));
			}
	      
	   }
	   
	   public void autoStopService(View view) {
		   	MyService.autostop = true;
		   	
		   	MainActivity.editor.putInt("inter",  progressChanged);
			MainActivity.editor.commit();  
			
		   	if(isMyServiceRunning(MyService.class.getName())){
				Log.d(TAG, "active" );
			} else {
				startService(new Intent(getBaseContext(), MyService.class));
			}
		   }

	   // Method to stop the service
	   public void stopService(View view) {
	      stopService(new Intent(getBaseContext(), MyService.class));
	   }
	   
	   private boolean isMyServiceRunning(String className) {
		    ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
		        if (className.equals(service.service.getClassName())) {
		            return true;
		        }
		    }
		    return false;
		}
}
