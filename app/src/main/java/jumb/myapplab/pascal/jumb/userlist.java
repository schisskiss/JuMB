package jumb.myapplab.pascal.jumb;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pascal on 27.12.2014.
 */
public class userlist extends ActionBarActivity {

    protected static final int NO_SELECTED_COLOR = 0xFF191919;
    protected static final int SELECTED_COLOR = 0xFF3366CC;

    private ArrayList<String> userlistshow;
    private static List<RowItemChat> userlist;
    private ListView userlistview;
    private Button sendto;
    private int result;
    private View tempview = null;


    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlist_layout);

        userlistshow = new ArrayList<>();

        Bundle b = getIntent().getExtras();
        if(b != null){
            userlistshow = b.getStringArrayList("userlist");
        }

        sendto = (Button)findViewById(R.id.sendto);
        sendto.setClickable(false);
        sendto.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent returnIntent = new Intent();
                  returnIntent.putExtra("result",userlistshow.get(result));
                  setResult(RESULT_OK,returnIntent);
                  finish();
              }
          });

        userlistview = (ListView) findViewById(R.id.userListView);
        userlistview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3){
                    sendto.setClickable(true);
                    result = position;
                    if(tempview != null) {
                        tempview.setBackgroundColor(Color.TRANSPARENT);
                    }
                    view.setBackgroundColor(Color.RED);
                    tempview = view;
                }
        });

        displayResultList();
    }


    private void displayResultList() {

        userlist = new ArrayList<RowItemChat>();

        for (int i = 0; i < userlistshow.size(); i++) {
            RowItemChat item = new RowItemChat(userlistshow.get(i));
            userlist.add(item);
        }
        CustomListViewAdapterChat adapter2 = new CustomListViewAdapterChat(this,R.layout.listviewchat, userlist);
        userlistview.setAdapter(adapter2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
