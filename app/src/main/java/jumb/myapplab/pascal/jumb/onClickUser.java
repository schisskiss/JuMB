package jumb.myapplab.pascal.jumb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Pascal on 28.09.2015.
 */
public class onClickUser extends ActionBarActivity {

    public static SharedPreferences user;
    public static SharedPreferences.Editor editor;

    private Button kontaktanrufenfestnetz;
    private Button kontaktanrufenmobil;
    private Button kontaktemail;
    private Button kontakthinzufügen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.onclickuser_layout);

        kontaktanrufenfestnetz  = (Button)findViewById(R.id.kontaktanrufenfestnetz);
        kontaktanrufenmobil  = (Button)findViewById(R.id.kontaktanrufenmobil);
        kontaktemail  = (Button)findViewById(R.id.kontaktemail);
        kontakthinzufügen  = (Button)findViewById(R.id.kontakthinzufügen);


        kontaktanrufenfestnetz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = getIntent().getExtras();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + b.getString("Festnetz")));

                startActivity(intent);
                finish();
            }
        });

        kontaktanrufenmobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = getIntent().getExtras();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + b.getString("Mobil")));

                startActivity(intent);
                finish();
            }
        });

        kontaktemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = getIntent().getExtras();

                Intent intent = new Intent(getApplicationContext(), eMailSenden.class);
                intent.putExtra("email", b.getString("Email"));
                startActivity(intent);
                finish();
            }
        });

        kontakthinzufügen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = getIntent().getExtras();

                Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL, b.getString("Strasse") + "," + b.getString("Plz") + " " + b.getString("Ort"));
                intent.putExtra(ContactsContract.Intents.Insert.NAME, b.getString("Vorname") +" "+ b.getString("Nachname"));
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, b.getString("Email"));
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, b.getString("Mobil"));
                intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, b.getString("Festnetz"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

                finish();
            }
        });
    }




}
