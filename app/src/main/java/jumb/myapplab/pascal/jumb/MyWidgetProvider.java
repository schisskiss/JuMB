package jumb.myapplab.pascal.jumb;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyWidgetProvider extends AppWidgetProvider {

	private String tableName = MySQLiteHelper.TABLE_POSTS;
	private SQLiteDatabase newDB;
	private String title;

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {

	  
	  	MySQLiteHelper dbHelper = new MySQLiteHelper(context);
		newDB = dbHelper.getWritableDatabase();
	  
			Cursor cur = newDB.rawQuery("SELECT title,content FROM " + tableName + " ORDER BY date DESC LIMIT 10", null);
			
			if  (cur.moveToFirst()) {
				title     = cur.getString(cur.getColumnIndex("title"));
			}
			
		
	    // Get all ids
	    ComponentName thisWidget = new ComponentName(context,MyWidgetProvider.class);
	    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
	    for (int widgetId : allWidgetIds) {
	      
	      Intent intent = new Intent(context, MainActivity.class);
	      
	      
	      intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		  intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
		  
		  PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		  
		  
		  Intent intent2 = new Intent(context, ShowItem.class);
	      
	      intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		  intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
		  
		  PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

	      RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
	      // Set the text
	      remoteViews.setTextViewText(R.id.widgetTitle, String.valueOf(title));
	      remoteViews.setOnClickPendingIntent(R.id.widgetimage, pendingIntent);
	      remoteViews.setOnClickPendingIntent(R.id.widgetTitle, pendingIntent2);
	       
	      
	      
	      appWidgetManager.updateAppWidget(widgetId, remoteViews);
	      
	      pushWidgetUpdate(context, remoteViews);
	    }
	  }
  



  
	  public void onReceive(Context context,Intent intent){
		  Bundle extras = intent.getExtras();
		   if(extras!=null) {
		    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		    ComponentName thisAppWidget = new ComponentName(context.getPackageName(), MyWidgetProvider.class.getName());
		    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
	
		    onUpdate(context, appWidgetManager, appWidgetIds);
		   }
	  }
	  
	  public static PendingIntent buildButtonPendingIntent(Context context) {
	
		  Toast.makeText(context.getApplicationContext(), "tests" , Toast.LENGTH_LONG).show();
			// initiate widget update request
			Intent intent = new Intent();
			intent.setAction(WidgetUtils.WIDGET_UPDATE_ACTION);
			return PendingIntent.getBroadcast(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
		}
	  
	  
	  
	  public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
			ComponentName myWidget = new ComponentName(context,
					MyWidgetProvider.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			manager.updateAppWidget(myWidget, remoteViews);
		}
} 