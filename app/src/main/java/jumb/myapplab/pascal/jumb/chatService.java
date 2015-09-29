package jumb.myapplab.pascal.jumb;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import java.util.ArrayList;
import android.provider.Settings;
import android.widget.Toast;


/**
 * Created by Pascal on 29.12.2014.
 */
public class chatService extends Service{

    public static Client mClient;
    private String name = null;
    public ArrayList<String> userlist;

    public static SharedPreferences user;
    public static SharedPreferences.Editor editor;
    private final IBinder mBinder = new MyBinder();

    private static String tableName = chatSQLiteHelper.TABLE_CHAT;
    public chatEntryDataSource datasource;
    public SQLiteDatabase newDB;
    public static final String NOTIFICATION = "NEW_MESSAGE";
    private int result = 0;
    private static int FOREGROUND_ID=1338;

    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        chatSQLiteHelper chatdbHelper = new chatSQLiteHelper(this);

        newDB = chatdbHelper.getWritableDatabase();
        datasource = new chatEntryDataSource(this);
        datasource.open();

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        user = getSharedPreferences("user", 0);
        editor = user.edit();
        name = user.getString("user", "guest");

        userlist = new ArrayList<String>();

        new connectTask().execute("");

        return START_STICKY;
    }


    public class connectTask extends AsyncTask<String,String,Client> {

        @Override
        protected Client doInBackground(String... message) {

            //we create a Client object and
            mClient = new Client(new Client.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    if(message.equals("-JuMB Chat-")){
                        mClient.sendMessage(name + "::546854225547487");
                        updateuserlist();
                    }else {
                        //this method calls the onProgressUpdate
                        publishProgress(message);
                    }
                }
            });
            mClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            String from;
            String msg;
            String isSelf;
            String privatemsg = "false";
            boolean notification = true;
            boolean notification2;
            boolean ismsg = true;
            boolean priv = true;

            String[] splitResult = String.valueOf(values[0]).split("::");
            String[] splitResult2 = String.valueOf(splitResult[0]).split(" >");

            if(splitResult2.length > 1){
                privatemsg = "true";


                if(splitResult2[0].equals(name)){
                    priv = true;
                    isSelf = "true";
                    //Toast.makeText(getApplicationContext(), "private" , Toast.LENGTH_LONG).show();
                }else{
                    priv = false;
                    isSelf = "false";
                }
            }

            if(splitResult.length == 1){
                msg = splitResult[0];
                from = "Server:";
                notification2 = false;
            }else{
                from = splitResult[0];
                msg  = splitResult[1];
                notification2 = true;

                if(from.equals("//user")){
                    ismsg = false;
                    userlist.add(msg);
                }else if(msg.equals("//userlist")){
                    ismsg = false;
                }
            }

            if(splitResult2[0].equals(name) && priv == true){
                isSelf = "true";
                notification = false;
            }else {
                isSelf = "false";
                if (msg.equals("hat den Chat verlassen") || msg.equals("ist dem Chat beigetreten") || msg.equals("Offline") || msg.equals("musste den Chat beigetreten")) {

                    if (msg.equals("hat den Chat verlassen")) {
                        msg = from + " " + msg;
                    }
                    if (msg.equals("ist dem Chat beigetreten")) {
                        msg = from + " " + msg;
                    }
                    if (msg.equals("Offline")) {
                        msg = from + " " + msg;
                    }

                    updateuserlist();
                    notification = false;
                } else {
                    notification = true;
                }

            }

            if(user.getBoolean("online", false) == true && msg.equals("Willkommen " + name)){
                ismsg = false;
            }else{
                editor.putBoolean("online",true);
                editor.commit();
            }


            if(ismsg == true) {

                if (notification && notification2) {
                     displayNotification(from, msg);
                }

                datasource.createChatEntry(from, msg, isSelf, privatemsg);
                result = 1;
                publishResults(result);
            }

        }
    }

    private void publishResults(int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra("RESULT", result);
        sendBroadcast(intent);
    }


    public class MyBinder extends Binder {
        chatService getService() {
            return chatService.this;
        }
    }

    public ArrayList<String> getUserlist(){
        return userlist;
    }

    public void updateuserlist(){
        userlist.clear();
        if (mClient != null) {
            mClient.sendMessage("//userlist");
        }
    }


    protected void displayNotification(String title,String content) {

        Intent intent = new Intent(getApplicationContext(), chat.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_launcher)
                .setTicker("JuMBseite: Neue Machricht!")
                .setContentTitle(title)
                .setContentText(content)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Welcome"))
                .setContentIntent(pendingIntent);

        mNotificationManager.notify(1, mBuilder.build());

    }



}
