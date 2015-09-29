package jumb.myapplab.pascal.jumb;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.res.Resources;
import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.ServiceConnection;
import android.content.ComponentName;



/**
 * Created by Pascal on 21.12.2014.
 */
public class chat extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //private ListView listViewMessages;
    public ArrayList<String> onlineuser;
    private ArrayList<String> chatfrom = new ArrayList<String>();
    private ArrayList<String> chatmsg = new ArrayList<String>();
    private ArrayList<String> chatisSelf = new ArrayList<String>();
    private ArrayList<String> chatprivatemsg = new ArrayList<String>();


    // Chat messages list adapter
    private MessagesListAdapter mAdapter;
    private List<Message> messageList;
    private ListView mList;

    public EditText inputMsg;
    private Button send;

    private chatService s;
    private Client mClientService;
    private String name = null;

    public static SharedPreferences user;
    public static SharedPreferences.Editor editor;

    private static String tableName = chatSQLiteHelper.TABLE_CHAT;
    private chatEntryDataSource datasource;
    private static SQLiteDatabase newDB;

    private NotificationManager mNotificationManager;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int resultCode = bundle.getInt("RESULT");
                if (resultCode == 1) {
                    show();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void show(){
        openAndQueryDatabase();
        displayResultList();
    }

    private void displayResultList() {

        messageList = new ArrayList<Message>();

        for (int i = 0; i < chatfrom.size(); i++) {
            Message m = new Message(chatfrom.get(i), chatmsg.get(i), chatisSelf.get(i), chatprivatemsg.get(i));
            messageList.add(m);
        }

        mAdapter = new MessagesListAdapter(this, messageList);
        mList.setAdapter(mAdapter);
    }

    private void openAndQueryDatabase() {
        try {
            chatfrom.clear();
            chatmsg.clear();
            chatisSelf.clear();
            chatprivatemsg.clear();

            Cursor c = newDB.rawQuery("SELECT _id, msgfrom, message, isSelf, privatemsg FROM " + tableName + " ORDER BY _id ASC", null);

            if (c != null ) {
                if  (c.moveToFirst()) {
                    do {

                        String from       = c.getString(c.getColumnIndex("msgfrom"));
                        String msg        = c.getString(c.getColumnIndex("message"));
                        String isSelf     = c.getString(c.getColumnIndex("isself"));
                        String privatemsg = c.getString(c.getColumnIndex("privatemsg"));

                        chatfrom.add(from);
                        chatmsg.add(msg);
                        chatisSelf.add(isSelf);
                        chatprivatemsg.add(privatemsg);

                    }while (c.moveToNext());
                    displayResultList();
                }
            }

        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        } finally {

            try {
                mList.removeAllViewsInLayout();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    private void updatelist(){
        onlineuser.clear();
        onlineuser.addAll(s.getUserlist());
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,IBinder binder) {
            chatService.MyBinder b = (chatService.MyBinder) binder;
            s = b.getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            s = null;
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        isNetworkAvailable();

        mNotificationManager.cancel(1);

        chatSQLiteHelper chatdbHelper = new chatSQLiteHelper(this);
        newDB = chatdbHelper.getReadableDatabase();
        datasource = new chatEntryDataSource(this);
        datasource.open();

        onlineuser = new ArrayList<String>();

        user = getSharedPreferences("user", 0);
        editor = user.edit();
        name = user.getString("user", "guest");

        inputMsg = (EditText) findViewById(R.id.inputMsg);
        send = (Button) findViewById(R.id.btnSend);
        mList = (ListView)findViewById(R.id.list_view_messages);

        if(isMyServiceRunning(chatService.class.getName())){
            //Service läuft
        } else {
            editor.putBoolean("online",false);
            editor.commit();

            chatSQLiteHelper.cleanChatTable(newDB);
            startService(new Intent(getBaseContext(), chatService.class));
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(inputMsg.getText().toString().length() != 0) {
                    mClientService = chatService.mClient;
                    String message = inputMsg.getText().toString();

                    //sends the message to the server
                    if (mClientService != null) {
                        mClientService.sendMessage(message);
                        //refresh the list
                        //mAdapter.notifyDataSetChanged();
                        inputMsg.setText("");
                    }
                }

            }
        });

        registerReceiver(receiver, new IntentFilter(chatService.NOTIFICATION));

        Intent intent= new Intent(this, chatService.class);
        bindService(intent, mConnection,BIND_AUTO_CREATE);

        show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    private void isNetworkAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Resources res = getResources();
        if(mobile.isConnected() == false && wifi.isConnected() == false) {
            Toast.makeText(getApplicationContext(), "Keine Internet Verbindung" , Toast.LENGTH_LONG).show();
        }
    }

    private boolean isMyServiceRunning(String className) {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (className.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater men = getMenuInflater();
        men.inflate(R.menu.chatactionbar, menu);

        MenuItem Userlist = menu.findItem(R.id.Userlist);
        MenuItem disconnect = menu.findItem(R.id.disconnect);


        //Logineintrag passend setzten
        Userlist.setEnabled(true).setVisible(true);
        disconnect.setEnabled(true).setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                inputMsg.setText(result + " ");
            }
        }
    }

    private void closeChat(){

        AlertDialog.Builder msgBox = new AlertDialog.Builder(this);

        msgBox.setTitle("JuMB");
        msgBox.setMessage("Chat wirklich komplett beenden?");
        msgBox.setPositiveButton("Hintergrund" , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        finish();

                    }
                });

        msgBox.setNegativeButton("Beenden" , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mClientService = chatService.mClient;
                        if (mClientService != null) {
                            mClientService.sendMessage("/quit");
                            mClientService.stopClient();
                        }
                        stopService(new Intent(getBaseContext(), chatService.class));
                        chatSQLiteHelper.cleanChatTable(newDB);

                        editor.putBoolean("online",false);
                        editor.commit();

                        finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = msgBox.create();

        // show it
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.Userlist) {
            if(user.getBoolean("login", false) == true ){
                Intent intent1 = new Intent(this,userlist.class);
                Bundle b = new Bundle();
                b.putStringArrayList("userlist",s.userlist);
                intent1.putExtras(b); //Put your id to your next Intent
                startActivityForResult(intent1,1);
            }
            return true;

        } else if (itemId == R.id.disconnect) {
            closeChat();
            return true;
        }
        return false;
    }


}
