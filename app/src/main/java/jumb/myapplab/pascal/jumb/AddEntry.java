package jumb.myapplab.pascal.jumb;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEntry extends ActionBarActivity{
	
	private EditText title;
	private EditText content;
	private Button post;
	private ProgressBar load;
	private String username;
	private String userpw;
	private String ptitle;
	private String pcontent;
	private String resp;
	
	public static SharedPreferences user;
    public static SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addentry_layout);
		
		user = getSharedPreferences("user",0);
		editor = user.edit();
		
		title   = (EditText)findViewById(R.id.addTitle);
		content = (EditText)findViewById(R.id.addContent);
		post    = (Button)findViewById(R.id.post);
		load    = (ProgressBar)findViewById(R.id.progressBar2);
		
		post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	load.setVisibility(View.VISIBLE);
            	post.setClickable(false);
                upload();
            }
        });
	}


	private void upload(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
		
		if((title.getText().toString()) == null && (content.getText().toString()) == null){
			load.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(), "Bitte Titel und Beitrag hinzuf�gen" , Toast.LENGTH_LONG).show();
		}else{
			if(Integer.valueOf(MainActivity.user.getInt("rights", 0)) >= 2 ){
				username   = MainActivity.user.getString("user", "");
				userpw     = MainActivity.user.getString("userpw", "");
				ptitle     = title.getText().toString();
				pcontent   = content.getText().toString();
				
				
				new MyAsyncTask2().execute(username,userpw,ptitle,pcontent,currentDateandTime);
        	}else{
        		Toast.makeText(getApplicationContext(), "Du hast nicht die n�tigen Rechte!" , Toast.LENGTH_LONG).show();
        	}
			
		}
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
	    menuItemAddEntry.setEnabled(false).setVisible(false);
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
	
	public class MyAsyncTask2 extends AsyncTask<String, Integer, Double>{
		 
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			postData(params[0],params[1],params[2],params[3],params[4]);
			return null;
		}
 
		protected void onPostExecute(Double result){
			
		}
		protected void onProgressUpdate(Integer... progress){	
			
		}
 
		public void postData(final String valueIWantToSend1,final String valueIWantToSend2,String valueIWantToSend3,String valueIWantToSend4,String valueIWantToSend5) {
			
			
			
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://kalle.jumbseite.de/newpost.php");
 
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("puser", valueIWantToSend1));
				nameValuePairs.add(new BasicNameValuePair("puserpw", valueIWantToSend2));
				nameValuePairs.add(new BasicNameValuePair("title", valueIWantToSend3));
				nameValuePairs.add(new BasicNameValuePair("content", valueIWantToSend4));
				nameValuePairs.add(new BasicNameValuePair("date", valueIWantToSend5));
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
			    
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			
			runOnUiThread(new Runnable() {
			    public void run() {
			    	
			    	 	if(resp.equals("true"))
		        		{
			    	 		load.setVisibility(View.INVISIBLE);
			    	 		Toast.makeText(getApplicationContext(), "Post erfolgreich" , Toast.LENGTH_LONG).show();
			    	 		finish();
			    	 		
		        		}else if(resp.equals("false:login"))
		        		{
		        			post.setClickable(true);
		        			load.setVisibility(View.INVISIBLE);
		        			Toast.makeText(getApplicationContext(), "Keine Rechte" , Toast.LENGTH_LONG).show();
		        			
		        		}else if(resp.equals("false:insert"))
		        		{
		        			post.setClickable(true);
		        			load.setVisibility(View.INVISIBLE);
		        			Toast.makeText(getApplicationContext(), "Post fehlgeschlagen" , Toast.LENGTH_LONG).show();
		        		}
			    	 	
			    }
			});	  
			
		}
 
	}
	
	
	
}
