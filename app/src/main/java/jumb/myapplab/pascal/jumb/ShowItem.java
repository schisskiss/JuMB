package jumb.myapplab.pascal.jumb;

import java.util.ArrayList;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;


public class ShowItem extends ActionBarActivity{

	private ArrayList<String> titles = new ArrayList<String>();
	private ArrayList<String> dates = new ArrayList<String>();
	private ArrayList<String> contents = new ArrayList<String>();
	
	private static String tableName = MySQLiteHelper.TABLE_POSTS;
	private static SQLiteDatabase newDB;
	
	public static SharedPreferences user;
    public static SharedPreferences.Editor editor;
	
	private int id;
	
	TextView title;
	TextView date;
	WebView content;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showitem_layout);

		user = getSharedPreferences("user",0);
		editor = user.edit();
		
		
		title = (TextView) findViewById(R.id.title);
		date = (TextView) findViewById(R.id.date);
		content = (WebView) findViewById(R.id.content);
		content.setBackgroundColor(0x00000000);
		
		Bundle b = getIntent().getExtras();
		if(b != null){
			id = b.getInt("key");
		}else{
			id = 0;
		}
		showItem(id);
	}

	
	private void showItem(int id) {
		try {
			MySQLiteHelper dbHelper = new MySQLiteHelper(this);
			newDB = dbHelper.getWritableDatabase();
			Cursor c = newDB.rawQuery("SELECT title, content, date FROM " + tableName + " ORDER BY date DESC", null);

	    	if (c != null ) {
	    		if  (c.moveToFirst()) {
	    			do {
	    				String title   = c.getString(c.getColumnIndex("title"));
	    				String date    = c.getString(c.getColumnIndex("date"));
	    				String content = c.getString(c.getColumnIndex("content"));
	    				titles.add(title);
	    				dates.add("Vom: "+ date);
	    				contents.add(content);	
	    			}while (c.moveToNext());
	    		} 
	    	}			
	    	
			} catch (SQLiteException se ) {
	        	Log.e(getClass().getSimpleName(), "Could not create or Open the database");
	        } finally {
	        	//if (newDB != null) 
	        	//	newDB.execSQL("DELETE FROM " + tableName);
	        	//	newDB.close();
	        }
		title.setTextColor(Color.BLACK);
		date.setTextColor(Color.BLACK);
		
		title.setText(titles.get(id));
		date.setText(dates.get(id));
		content.loadData(contents.get(id), "text/html; charset=UTF-8", null);
		
		titles.clear();
		dates.clear();
		contents.clear();
		
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater men = getMenuInflater();
        men.inflate(R.menu.actionbar, menu);

        MenuItem menuItemAddUser = menu.findItem(R.id.AddUser);
        MenuItem menuItemSearchUser = menu.findItem(R.id.SearchUser);
        MenuItem menuItemTelList = menu.findItem(R.id.TelList);
        MenuItem menuItemAddEntry = menu.findItem(R.id.AddEntry);
        MenuItem menuItemLog_In = menu.findItem(R.id.Log_In);
        MenuItem menuItemLog_Out = menu.findItem(R.id.Log_Out);
        MenuItem menuItemChat = menu.findItem(R.id.Chat);

        //Logineintrag passend setzten
        menuItemLog_In.setEnabled(true).setVisible(true);
        menuItemLog_Out.setEnabled(true).setVisible(true);
        menuItemAddUser.setEnabled(true).setVisible(true);
        menuItemAddEntry.setEnabled(true).setVisible(true);
        menuItemSearchUser.setEnabled(true).setVisible(true);
        menuItemTelList.setEnabled(true).setVisible(true);
        menuItemAddEntry.setEnabled(true).setVisible(true);
        menuItemAddUser.setEnabled(true).setVisible(true);
        menuItemChat.setEnabled(true).setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.SearchUser) {
            if(user.getBoolean("login", false) == true ){
                Intent intent1 = new Intent(this,SearchUser.class);
                startActivity(intent1);
            }else{
                Toast.makeText(getApplicationContext(), "Du bist nicht Eingeloggt!" , Toast.LENGTH_LONG).show();
            }
            return true;

        } else if (itemId == R.id.Chat) {
            if(user.getBoolean("login", false) == true ){
                Intent chat = new Intent(this,chat.class);
                startActivity(chat);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Du bist nicht Eingeloggt!" , Toast.LENGTH_LONG).show();
            }
            return true;

        } else if (itemId == R.id.TelList) {
            if(user.getBoolean("login", false) == true ){
                Intent intent2 = new Intent(this,TelList.class);
                startActivity(intent2);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Du bist nicht Eingeloggt!" , Toast.LENGTH_LONG).show();
            }
            return true;

        } else if (itemId == R.id.AddUser) {
            if(Integer.valueOf(user.getInt("rights", 0)) == 10 ){
                Intent intent3 = new Intent(this,AddUser.class);
                startActivity(intent3);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Du hast nicht die nötigen Rechte!" , Toast.LENGTH_LONG).show();
            }
            return true;

        } else if (itemId == R.id.AddEntry) {
            if(Integer.valueOf(user.getInt("rights", 0)) >= 2 ){
                Intent intent5 = new Intent(this,AddEntry.class);
                startActivity(intent5);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Du hast nicht die nötigen Rechte!" , Toast.LENGTH_LONG).show();
            }
            return true;

        } else if (itemId == R.id.Log_In) {
            if((user.getBoolean("login", false)) == false ){
                Intent intent5 = new Intent(this,account.class);
                startActivity(intent5);
            }else{
                Toast.makeText(getApplicationContext(), "Du bist eingeloggt" , Toast.LENGTH_LONG).show();
            }
            return true;

        } else if (itemId == R.id.Log_Out) {
            if((user.getBoolean("login", false)) == true ){
                account.logout();
                Toast.makeText(getApplicationContext(), "Du bist jetzt ausgeloggt" , Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Du bist nicht eingeloggt" , Toast.LENGTH_LONG).show();
            }
            return true;

        } else if (itemId == R.id.settings) {
            Intent intent6 = new Intent(this,settings.class);
            startActivity(intent6);
            return true;
        }

        return false;
    }
}
