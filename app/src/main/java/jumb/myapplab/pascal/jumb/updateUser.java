package jumb.myapplab.pascal.jumb;

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

/**
 * Created by Pascal on 20.12.2014.
 */
public class updateUser extends ActionBarActivity implements View.OnClickListener{

    private String id;
    private String resp;

    private Button update;
    private Button cancel;

    private EditText vorname;
    private EditText nachname;
    private EditText strasse;
    private EditText plz;
    private EditText ort;
    private EditText festnetz;
    private EditText mobil;
    private EditText email;
    private EditText geburtsdatum;

    public static SharedPreferences user;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateuser_layout);

        update       = (Button)findViewById(R.id.update);
        cancel       = (Button)findViewById(R.id.cancel);

        vorname  = (EditText)findViewById(R.id.uvorname);
        nachname = (EditText)findViewById(R.id.unachname);
        strasse  = (EditText)findViewById(R.id.ustrasse);
        plz      = (EditText)findViewById(R.id.uplz);
        ort      = (EditText)findViewById(R.id.uort);
        festnetz = (EditText)findViewById(R.id.ufestnetz);
        mobil    = (EditText)findViewById(R.id.umobil);
        email    = (EditText)findViewById(R.id.uemail);
        geburtsdatum    = (EditText)findViewById(R.id.ugeburtsdatum);

        update.setOnClickListener(this);
        cancel.setOnClickListener(this);

        user = getSharedPreferences("user",0);
        editor = user.edit();

        Bundle b = getIntent().getExtras();
        if(b != null){
            id = b.getString("ID");
            vorname.setText(b.getString("Vorname"));
            nachname.setText(b.getString("Nachname"));
            strasse.setText(b.getString("Strasse"));
            plz.setText(b.getString("Plz"));
            ort.setText(b.getString("Ort"));
            festnetz.setText(b.getString("Festnetz"));
            mobil.setText(b.getString("Mobil"));
            email.setText(b.getString("Email"));
            geburtsdatum.setText(b.getString("Geburtsdatum"));
        }


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.update){
            new MyAsyncTask().execute();
        }
        if(v.getId() == R.id.cancel){
            finish();
        }
    }

    public class MyAsyncTask extends AsyncTask<String, Integer, Double> {

        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData();
            return null;
        }

        protected void onPostExecute(Double result){

        }
        protected void onProgressUpdate(Integer... progress){

        }

        public void postData() {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://schisskiss.no-ip.biz/jumb/updateuser.php");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("key", "1376428316"));
                nameValuePairs.add(new BasicNameValuePair("ID", id));
                nameValuePairs.add(new BasicNameValuePair("vorname", vorname.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("nachname", nachname.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("plz", plz.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("ort", ort.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("strasse", strasse.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("festnetz", festnetz.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("mobil", mobil.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("geburtsdatum", geburtsdatum.getText().toString()));
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
                    if (resp.equals("true")) {

                        Toast.makeText(getApplicationContext(), "Update erfolgreich", Toast.LENGTH_LONG).show();
                        finish();

                    } else {

                        Toast.makeText(getApplicationContext(), "Update nicht erfolgreich", Toast.LENGTH_LONG).show();

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

        } else if (itemId == R.id.TelList) {
            if(user.getBoolean("login", false) == true ){
                Intent intent2 = new Intent(this,TelList.class);
                startActivity(intent2);
            }else{
                Toast.makeText(getApplicationContext(), "Du bist nicht Eingeloggt!" , Toast.LENGTH_LONG).show();
            }
            return true;

        } else if (itemId == R.id.Chat) {
            if(user.getBoolean("login", false) == true ){
                Intent chat = new Intent(this,chat.class);
                startActivity(chat);
            }else{
                Toast.makeText(getApplicationContext(), "Du bist nicht Eingeloggt!" , Toast.LENGTH_LONG).show();
            }
            return true;


        } else if (itemId == R.id.AddUser) {
            if(Integer.valueOf(user.getInt("rights", 0)) == 10 ){
                Intent intent3 = new Intent(this,AddUser.class);
                startActivity(intent3);
            }else{
                Toast.makeText(getApplicationContext(), "Du hast nicht die nötigen Rechte!" , Toast.LENGTH_LONG).show();
            }
            return true;

        } else if (itemId == R.id.AddEntry) {
            if(Integer.valueOf(user.getInt("rights", 0)) >= 2 ){
                Intent intent5 = new Intent(this,AddEntry.class);
                startActivity(intent5);
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
