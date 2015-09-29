package jumb.myapplab.pascal.jumb;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
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

public class MainActivity extends ActionBarActivity {

    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<String> dates = new ArrayList<String>();
    private ArrayList<String> contents = new ArrayList<String>();

    private static String tableName = MySQLiteHelper.TABLE_POSTS;
    private EntryDataSource datasource;
    private static SQLiteDatabase newDB;

    SharedPreferences prefs = null;
    private String resp;
    private ListView listView;
    public static List<RowItem> rowItems1;

    private NotificationManager myNotificationManager;
    private int numMessagesOne = 0;
    private int notificationIdOne = 111;

    public static final long NOTIFY_INTERVAL = 600 * 1000; // 1 Stunde

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    public static boolean stop = true;

    public static SharedPreferences user;
    public static SharedPreferences.Editor editor;

    private NotificationManager mNotificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //title();

        user = getSharedPreferences("user",0);
        editor = user.edit();

        listView = (ListView) findViewById(R.id.list);
        createDB();

        datasource = new EntryDataSource(this);
        datasource.open();

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        prefs = getSharedPreferences("myapplab.jumb", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {

            editor.putString("user",  "");
            editor.putString("userpw","");
            editor.putBoolean("login",false);
            editor.putInt("rights",0);
            editor.commit();

            MainActivity.stop = false;
            new MyAsyncTask().execute("0");

            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }else{
            startUp();
        }


        //Timer initialize
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }

        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), NOTIFY_INTERVAL , NOTIFY_INTERVAL);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showItem(position);
            }
        });

    }

    //@TargetApi(11)
    //private void title(){
    //	getActionBar().setDisplayShowTitleEnabled(false);
    //}

    private void showItem(int id){
        Intent intent = new Intent(this, ShowItem.class);
        Bundle b = new Bundle();
        b.putInt("key", id); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    startUp();
                }

            });
        }
    }

    public static void staticshow(){
        new MainActivity().show();
    }

    public void show(){
        titles.clear();
        dates.clear();
        contents.clear();
        openAndQueryDatabase();
        displayResultList();
    }

    private void createDB(){
        MySQLiteHelper dbHelper = new MySQLiteHelper(this);
        newDB = dbHelper.getWritableDatabase();
    }

    public static void start(){
        new MainActivity().startUp();
    }

    public void startUp(){
        String date = "0";

        Cursor cur = newDB.rawQuery("SELECT date FROM " + tableName + " ORDER BY date DESC LIMIT 0,1", null);

        if  (cur.moveToFirst()) {
            date   = cur.getString(cur.getColumnIndex("date"));
        }

        if(MainActivity.stop == true){
            MainActivity.stop = false;
            new MyAsyncTask().execute(date);
        }
    }

    private void displayResultList() {

        rowItems1 = new ArrayList<RowItem>();
        for (int i = 0; i < titles.size(); i++) {
            RowItem item = new RowItem(titles.get(i),dates.get(i), contents.get(i));
            rowItems1.add(item);
        }
        CustomListViewAdapter adapter1 = new CustomListViewAdapter(this,
                R.layout.list_item, rowItems1);
        listView.setAdapter(adapter1);
    }

    private void openAndQueryDatabase() {
        try {

            Cursor c = newDB.rawQuery("SELECT title, content, date FROM " + tableName + " ORDER BY date DESC LIMIT 0,10", null);

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

            try {
                listView.removeAllViewsInLayout();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
        menuItemAddEntry.setEnabled(true).setVisible(true);
        menuItemSearchUser.setEnabled(false).setVisible(false);
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

    public class MyAsyncTask extends AsyncTask<String, Integer, Double>{

        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
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

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }

            runOnUiThread(new Runnable() {
                public void run() {
                    int i=0;

                    if(resp != "")
                    {
                        String[] splitResult = String.valueOf(resp).split("::");

                        for(;i < (splitResult.length-1);){

                            datasource.createEntry(splitResult[i], splitResult[i+1], splitResult[i+2]);

                            displayNotification(splitResult[i], splitResult[i+1]);

                            try {
                                update(splitResult[i],splitResult[i+1]);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            i = i + 3;

                        }

                    }else{
                        //Toast.makeText(getApplicationContext(), "Keine Verbindung oder neue Posts" , Toast.LENGTH_LONG).show();
                    }
                    show();
                }
            });

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
    protected void onResume(){
        super.onResume();
        mNotificationManager.cancel(0);
        show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}