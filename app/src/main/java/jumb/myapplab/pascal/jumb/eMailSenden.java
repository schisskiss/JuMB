package jumb.myapplab.pascal.jumb;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Pascal on 28.09.2015.
 */
public class eMailSenden extends Activity {

    private EditText toEmail = null;
    private EditText emailSubject = null;
    private EditText emailBody = null;

    private Button cancel;
    private Button send;

    public static SharedPreferences user;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_layout);

        user = getSharedPreferences("user", 0);
        editor = user.edit();

        toEmail = (EditText) findViewById(R.id.toEmail);
        emailSubject = (EditText) findViewById(R.id.subject);
        emailBody = (EditText) findViewById(R.id.emailBody);

        cancel = (Button)findViewById(R.id.emailabbrechen);
        send = (Button)findViewById(R.id.emailsenden);

        Bundle b = getIntent().getExtras();
        toEmail.setText(b.getString("email"));



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Senden Abgebrochen!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String to = toEmail.getText().toString();
                String subject = emailSubject.getText().toString();
                String message = emailBody.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                // need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client"));

                finish();
            }
        });
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
                Toast.makeText(getApplicationContext(), "Du bist nicht Eingeloggt!", Toast.LENGTH_LONG).show();
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