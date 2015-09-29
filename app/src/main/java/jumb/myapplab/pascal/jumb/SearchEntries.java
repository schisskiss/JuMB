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
import android.widget.AdapterView;
import android.widget.ListView;
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

public class SearchEntries extends ActionBarActivity{
	
	public static SharedPreferences user;
    public static SharedPreferences.Editor editor;

    private ArrayList<String> ID           = new ArrayList<String>();
    private ArrayList<String> Vorname      = new ArrayList<String>();
    private ArrayList<String> Nachname     = new ArrayList<String>();
    private ArrayList<String> Strasse      = new ArrayList<String>();
    private ArrayList<String> Plz          = new ArrayList<String>();
    private ArrayList<String> Ort          = new ArrayList<String>();
    private ArrayList<String> Festnetz     = new ArrayList<String>();
    private ArrayList<String> Mobil        = new ArrayList<String>();
    private ArrayList<String> Email        = new ArrayList<String>();
    private ArrayList<String> Geburtsdatum = new ArrayList<String>();
    private static List<RowItemSearch> rowItems;
    
    private String resp;
    private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchentries_layout);
		
		user = getSharedPreferences("user",0);
		
		listView = (ListView) findViewById(R.id.list);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long arg3) {

                if(Integer.valueOf(user.getInt("rights", 0)) == 10 ){
                    updateuser(position);
                }else{
                    Toast.makeText(getApplicationContext(), "Du hast nicht die nötigen Rechte!" , Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });


        Bundle extras = getIntent().getExtras();
		if (extras != null) { 
			String text=extras.getString("begriff"); 
			new MyAsyncTask().execute(text); 
		}
	}


    private void updateuser(int position){
        Intent intent5 = new Intent(this, updateUser.class);
        Bundle b = new Bundle();
            b.putString("ID", ID.get(position)); //Your id
            b.putString("Vorname", Vorname.get(position)); //Your id
            b.putString("Nachname", Nachname.get(position)); //Your id
            b.putString("Strasse", Strasse.get(position)); //Your id
            b.putString("Plz", Plz.get(position)); //Your id
            b.putString("Ort", Ort.get(position)); //Your id
            b.putString("Festnetz", Festnetz.get(position)); //Your id
            b.putString("Mobil", Mobil.get(position)); //Your id
            b.putString("Email", Email.get(position)); //Your id
            b.putString("Geburtsdatum", Geburtsdatum.get(position)); //Your id
            intent5.putExtras(b); //Put your id to your next Intent
        startActivity(intent5);
    }

	private void displayResultList() {
		
		rowItems = new ArrayList<RowItemSearch>();
        for (int i = 0; i < Vorname.size(); i++) {
            RowItemSearch item = new RowItemSearch(ID.get(i), Vorname.get(i),Nachname.get(i), Strasse.get(i), Plz.get(i), Ort.get(i), Festnetz.get(i), Mobil.get(i), Email.get(i), Geburtsdatum.get(i));
            rowItems.add(item);
        }
        CustomListViewAdapter2 adapter = new CustomListViewAdapter2(this,
                R.layout.listviewsearch, rowItems);
        listView.setAdapter(adapter);
        
        
	}
	
	public class MyAsyncTask extends AsyncTask<String, Integer, Double>{
		 
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			postData(params[0]);
			return null;
		}
 
		protected void onPostExecute(Double result){
			
		}
		protected void onProgressUpdate(Integer... progress){	
			
		}
 
		public void postData(String valueIWantToSend1) {
			
			
			
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://schisskiss.no-ip.biz/jumb/searchuser.php");
 
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("search", valueIWantToSend1));
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
			    	
			    	if(resp != null){
				    	String[] splitResult = String.valueOf(resp).split("::");
				    	int i=0;

                            ID.clear();
					    	Vorname.clear();
		    	 			Nachname.clear();
		    	 			Strasse.clear();
		    	 			Plz.clear();
		    	 			Ort.clear();
		    	 			Festnetz.clear();
		    	 			Mobil.clear();
		    	 			Email.clear();
		    	 			Geburtsdatum.clear();
		    	 			//listView.removeAllViewsInLayout();
				    	
				    	for(;i < (splitResult.length-1);){

                            ID.add(splitResult[i++]);
		    	 			Nachname.add(splitResult[i++]);
		    	 			Vorname.add(splitResult[i++]);
		    	 			Strasse.add(splitResult[i++]);
		    	 			Plz.add(splitResult[i++]);
		    	 			Ort.add(splitResult[i++]);
		    	 			Festnetz.add(splitResult[i++]);
		    	 			Mobil.add(splitResult[i++]);
		    	 			Email.add(splitResult[i++]);
		    	 			Geburtsdatum.add(splitResult[i++]);
		    	 			
		    	 			displayResultList();	
		    	 			
		    	 		}
				    	
				    	if(splitResult[0] == ""){
				    		Intent inte = new Intent(getApplicationContext(),SearchUser.class);
				        	startActivity(inte);
				        	Toast.makeText(getApplicationContext(), "Es wurde nichts gefunden!! \n Probiere es nochmal" , Toast.LENGTH_LONG).show();
				        	finish();
				    	}
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
        menuItemAddUser.setEnabled(true).setVisible(true);
        menuItemAddEntry.setEnabled(true).setVisible(true);
        menuItemSearchUser.setEnabled(false).setVisible(true);
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
