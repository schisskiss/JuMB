package jumb.myapplab.pascal.jumb;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
	
	private Timer mTimer1 = null;
	private Handler mHandler = new Handler();
	private static SQLiteDatabase newDB;
	private static String tableName = MySQLiteHelper.TABLE_POSTS;
	private NotificationManager myNotificationManager;
	private int numMessagesOne = 0;
	private int notificationIdOne = 111;
	private EntryDataSource datasource;
	private String resp;
	public static boolean autostop = false;
	public static long INTERVAL = 1800000;
	
	public static SharedPreferences user;
    public static SharedPreferences.Editor editor;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		MySQLiteHelper dbHelper = new MySQLiteHelper(this);
		newDB = dbHelper.getWritableDatabase();
		datasource = new EntryDataSource(this);
	    datasource.open();
	    
	    user = getSharedPreferences("user",0);
		editor = user.edit();
	}
	
   @Override
   public IBinder onBind(Intent arg0) {
      return null;
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
	 //Timer initialize
	    if(mTimer1 != null) {
           mTimer1.cancel();
           mTimer1 = new Timer();
	    } else {
           // recreate new
           mTimer1 = new Timer();
	    }
	    
	   INTERVAL = Integer.valueOf(user.getInt("inter", 30)) * 60000; 
       mTimer1.scheduleAtFixedRate(new TimeDisplayTimerTask1(), 5000 , INTERVAL);
       
      Toast.makeText(this, "Service Gestartet", Toast.LENGTH_LONG).show();
      return START_STICKY;
   }
   
   class TimeDisplayTimerTask1 extends TimerTask {

       int number = 300 / Integer.valueOf(user.getInt("inter", 30));
	   
       @Override
       public void run() { 
           // run on another thread
           mHandler.post(new Runnable() {

               @Override
               public void run() {    
	            	String date = "0";
	            	
	            	
	            	Cursor cur = newDB.rawQuery("SELECT date FROM " + tableName + " ORDER BY date DESC LIMIT 0,1", null);
	   				
	   				if  (cur.moveToFirst()) {
	   					date   = cur.getString(cur.getColumnIndex("date"));
	   				}
	   				
	   				if(MainActivity.stop == true){
	   					MainActivity.stop = false;
	   					new MyAsyncTask().execute(date);
	   				}
	   				
	   				if(autostop == true){
	   					if(number == 0){
	   						stopService(new Intent(getBaseContext(), MyService.class));
	   					}else{
	   						number--;
	   					}
	   				}
   				
               }

           });
       }
	}
   
   
   public class MyAsyncTask extends AsyncTask<String, Integer, Double>{
		 
		protected Double doInBackground(String... params) {
			postData(params[0]);
			return null;
		}

		protected void onPostExecute(Double result){
			//Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
		}
		protected void onProgressUpdate(Integer... progress){
		}

		public void postData(String valueIWantToSend1) {
			
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://kalle.jumbseite.de/entries.php");

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("date", valueIWantToSend1));
				nameValuePairs.add(new BasicNameValuePair("key", "1376428316"));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
			    StringBuilder sb = new StringBuilder();
			    
			    String line = null;
			    try {
			        while ((line = reader.readLine()) != null) {
			            sb.append((line));
			        }
			    } catch (IOException e) {
			        e.printStackTrace();
			    } finally {
			        try {
			            is.close();
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			    }
			
			    resp = sb.toString();
			    
			    int i=0;
		    	
		    	 	if(resp != "")
	        		{
		    	 		String[] splitResult = String.valueOf(resp).split("::");
		   
		    	 		for(;i < (splitResult.length-1);){
		    	 			
		    	 			datasource.createEntry(splitResult[i], splitResult[i+1], splitResult[i+2]);
		    	 			
		    	 			displayNotification(splitResult[i], splitResult[i+1]);
		    	 			
		    	 			update(splitResult[i],splitResult[i+1]);
		    	 			i = i + 3;
		    	 		}
		    	 				
	        		}
			    
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			

			    MainActivity.stop = true;	 	
			}

	}
   
   private void update(String title,String content){
		Context context = getApplicationContext();
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		ComponentName thisWidget = new ComponentName(context, MyWidgetProvider.class);
		remoteViews.setTextViewText(R.id.widgetTitle, title);
		appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		
		AppWidgetManager appWidgetManager1 = AppWidgetManager.getInstance(context);
		RemoteViews remoteViews1 = new RemoteViews(context.getPackageName(), R.layout.widget_layout1);
		ComponentName thisWidget1 = new ComponentName(context, MyWidgetProvider1.class);
		remoteViews1.setTextViewText(R.id.widgetTitle1, title);
		remoteViews1.setTextViewText(R.id.widgetContent1, content);
		appWidgetManager1.updateAppWidget(thisWidget1, remoteViews1);
	}


	protected void displayNotification(String title,String content) {
        Intent intent = new Intent(getApplicationContext(), chat.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationManager mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_launcher)
                .setTicker("JuMBseite: Neuer Beitrag!")
                .setContentTitle(title)
                .setContentText(content)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Welcome"))
                .setContentIntent(pendingIntent);

        mNotificationManager.notify(0, mBuilder.build());
		
	}
   
   @Override
   public void onDestroy() {
      super.onDestroy();
      mTimer1.cancel();
      Toast.makeText(this, "Service Gestoppt", Toast.LENGTH_LONG).show();
   }
}