package jumb.myapplab.pascal.jumb;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class SearchUser extends ActionBarActivity{
	
	public static SharedPreferences user;
    public static SharedPreferences.Editor editor;
    
    private Button search;
    private EditText begriff;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.searchuser_layout);
		
		search  = (Button)findViewById(R.id.bsearchfor);
		begriff = (EditText)findViewById(R.id.searchfor);
		
		search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(getApplicationContext(), SearchEntries.class);
            	intent.putExtra("begriff", begriff.getText().toString());
            	startActivityForResult(intent,100);
            	finish();
            }
        });
	}
	
	
}
