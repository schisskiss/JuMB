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

public class AddUser extends ActionBarActivity{
	
	public static SharedPreferences user;
    public static SharedPreferences.Editor editor;
    
    public String resp;
    private Button add;
    private Button fin;
    
    private EditText tvorname;
    private EditText tnachname;
    private EditText tstraße;
    private EditText tplz;
    private EditText tort;
    private EditText tfestnetz;
    private EditText tmobil;
    private EditText temail;
    private EditText tgdate;
    
    private String vorname;
    private String nachname;
    private String straße;
    private String plz;
    private String ort;
    private String festnetz;
    private String mobil;
    private String email;
    private String gdate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adduser_layout);
		
		add       = (Button)findViewById(R.id.adduser);
		fin       = (Button)findViewById(R.id.finish);
		
		tvorname  = (EditText)findViewById(R.id.vorname);
		tnachname = (EditText)findViewById(R.id.nachname);
		tstraße   = (EditText)findViewById(R.id.strasse);
		tplz      = (EditText)findViewById(R.id.plz);
		tort      = (EditText)findViewById(R.id.ort);
		tfestnetz = (EditText)findViewById(R.id.festnetz);
		tmobil    = (EditText)findViewById(R.id.mobil);
		temail    = (EditText)findViewById(R.id.email);
		tgdate    = (EditText)findViewById(R.id.gdate);
		
		
		user = getSharedPreferences("user",0);
		editor = user.edit();
		

		add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	vorname  = tvorname.getText().toString();
        		nachname = tnachname.getText().toString();
        		straße   = tstraße.getText().toString();
        		plz      = tplz.getText().toString();
        		ort      = tort.getText().toString();
        		festnetz = tfestnetz.getText().toString();
        		mobil    = tmobil.getText().toString();
        		email    = temail.getText().toString();
        		gdate    = tgdate.getText().toString();
        		
        		add.setClickable(false);
            	new MyAsyncTask().execute(vorname,nachname,straße,plz,ort,festnetz,mobil,email,gdate);
            }
        });
		
		fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	finish();
            }
        });

	}

	
	public class MyAsyncTask extends AsyncTask<String, Integer, Double>{
		 
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			postData(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8]);
			return null;
		}
 
		protected void onPostExecute(Double result){
			//Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
		}
		protected void onProgressUpdate(Integer... progress){
		}
 
		public void postData(String valueIWantToSend1,String valueIWantToSend2,String valueIWantToSend3,String valueIWantToSend4,String valueIWantToSend5,String valueIWantToSend6,
				String valueIWantToSend7,String valueIWantToSend8,String valueIWantToSend9) {
			
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://schisskiss.no-ip.biz/jumb/newuser.php");
 
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("pvorname", valueIWantToSend1));
				nameValuePairs.add(new BasicNameValuePair("pnachname", valueIWantToSend2));
				nameValuePairs.add(new BasicNameValuePair("pstrasse", valueIWantToSend3));
				nameValuePairs.add(new BasicNameValuePair("pplz", valueIWantToSend4));
				nameValuePairs.add(new BasicNameValuePair("port", valueIWantToSend5));
				nameValuePairs.add(new BasicNameValuePair("pfestnetz", valueIWantToSend6));
				nameValuePairs.add(new BasicNameValuePair("pmobil", valueIWantToSend7));
				nameValuePairs.add(new BasicNameValuePair("pemail", valueIWantToSend8));
				nameValuePairs.add(new BasicNameValuePair("pgdate", valueIWantToSend9));
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
		    	 		Toast.makeText(getApplicationContext(), "Eintrag erfolgreich" , Toast.LENGTH_LONG).show();
		    	 		
	        		}else if(resp.equals("false"))
	        		{
	        			add.setClickable(true);
	        			Toast.makeText(getApplicationContext(), "Eintrag fehlgeschlagen" , Toast.LENGTH_LONG).show();
	        			
	        		}
			    }
			});	
			  
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
        menuItemAddUser.setEnabled(false).setVisible(false);
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
	
	@Override
	protected void onResume() {
	    super.onResume();
	}

	@Override
	protected void onPause() {
	    super.onPause();
	}
}
