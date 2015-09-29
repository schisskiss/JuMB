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

public class MyWidgetProvider1 extends AppWidgetProvider {

	private String tableName = MySQLiteHelper.TABLE_POSTS;
	private SQLiteDatabase newDB;
	private String title;
	private String content;
	

	  @Override
	  public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
	
		  
		  MySQLiteHelper dbHelper = new MySQLiteHelper(context);
		  newDB = dbHelper.getWritableDatabase();
		  
				Cursor cur = newDB.rawQuery("SELECT title,content FROM " + tableName + " ORDER BY date DESC LIMIT 10", null);
				
				if  (cur.moveToFirst()) {
					title     = cur.getString(cur.getColumnIndex("title"));
					content   = cur.getString(cur.getColumnIndex("content"));
				}
					
				
			
		    // Get all ids
		    ComponentName thisWidget1 = new ComponentName(context,MyWidgetProvider1.class);
		    int[] allWidgetIds1 = appWidgetManager.getAppWidgetIds(thisWidget1);
		    for (int widgetId : allWidgetIds1) {
		
		    	Intent intent1 = new Intent(context, MainActivity.class);
				PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
				      
				intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
				intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			    	
			    RemoteViews remoteViews1 = new RemoteViews(context.getPackageName(),R.layout.widget_layout1);
		      
			    Intent intent2 = new Intent(context, ShowItem.class);
			      
			    intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
				intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
				  
				PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
			      // Set the text
		      remoteViews1.setTextViewText(R.id.widgetTitle1, String.valueOf(title));
		      remoteViews1.setTextViewText(R.id.widgetContent1, String.valueOf(content));
		      remoteViews1.setOnClickPendingIntent(R.id.widgetImage1, pendingIntent1);
		      remoteViews1.setOnClickPendingIntent(R.id.widgetTitle1, pendingIntent2);
		      remoteViews1.setOnClickPendingIntent(R.id.widgetContent1, pendingIntent2);
	
		      appWidgetManager.updateAppWidget(widgetId, remoteViews1);
		      
		      pushWidgetUpdate(context, remoteViews1);
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
	
			// initiate widget update request
			Intent intent1 = new Intent();
			intent1.setAction(WidgetUtils.WIDGET_UPDATE_ACTION);
			return PendingIntent.getBroadcast(context, 0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
		}
	  
	  public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
			ComponentName myWidget1 = new ComponentName(context,
					MyWidgetProvider1.class);
			AppWidgetManager manager1 = AppWidgetManager.getInstance(context);
			manager1.updateAppWidget(myWidget1, remoteViews);
	  }
} 