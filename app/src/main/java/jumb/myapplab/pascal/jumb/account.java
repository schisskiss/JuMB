package jumb.myapplab.pascal.jumb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
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
import java.util.ArrayList;
import java.util.List;


public class account extends ActionBarActivity{

	private String resp;
	private String username = null;
	private String userpw = null;
	public ProgressBar progress;
	private Button button1;
	private EditText edtex1;
	private EditText edtex2;
	
	protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.account_layout);
        progress = (ProgressBar)findViewById(R.id.progressBar1);
        button1 = (Button)findViewById(R.id.login);
        edtex1 = (EditText)findViewById(R.id.usernameEdit);
        edtex2 = (EditText)findViewById(R.id.passwordEdit);
        
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testlogin();
            }
        });
	}
	
	public void testlogin(){
		username = edtex1.getText().toString();
		userpw = edtex2.getText().toString();
		progress.setVisibility(View.VISIBLE);
		new MyAsyncTask1().execute(username,userpw,"http://kalle.jumbseite.de/testlogin.php");
	}
	
	public static void logout(){
		MainActivity.editor.putString("user",  "");
 		MainActivity.editor.putString("userpw","");
 		MainActivity.editor.putBoolean("login",false);
 		MainActivity.editor.putInt("rights",0);
 		MainActivity.editor.commit();
	}
	
	public class MyAsyncTask1 extends AsyncTask<String, Integer, Double>{
		 
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			postData(params[0],params[1],params[2]);
			return null;
		}
 
		protected void onPostExecute(Double result){
			
		}
		protected void onProgressUpdate(Integer... progress){	
			
		}
 
		public void postData(final String valueIWantToSend1,final String valueIWantToSend2,String valueIWantToSend3) {
			
			
			
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(valueIWantToSend3);
 
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("puser", valueIWantToSend1));
				nameValuePairs.add(new BasicNameValuePair("puserpw", valueIWantToSend2));
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

			    	String[] splitResult = String.valueOf(resp).split("::");
			    	
			    	 	if(splitResult[0].equals("true"))
		        		{
			    	 		Toast.makeText(getApplicationContext(), "Log In erfolgreich" , Toast.LENGTH_LONG).show();
			    	 		
			    	 		MainActivity.editor.putString("user",  valueIWantToSend1);
			    	 		MainActivity.editor.putString("userpw",valueIWantToSend2);
			    	 		MainActivity.editor.putBoolean("login",true);
			    	 		MainActivity.editor.putInt("rights",Integer.valueOf(splitResult[1]));
			    	 		MainActivity.editor.commit();
			    	 		finish();
		        		}
			    	 	
		        		else
		        		{
		        			edtex1.setText("");
		        			edtex2.setText("");
		        			progress.setVisibility(4);
		        			Toast.makeText(getApplicationContext(), "Log In fehlgeschlagen" , Toast.LENGTH_LONG).show();
		        		}	 	
			    	 	
			    }
			});	  
			
		}
 
	}
}
