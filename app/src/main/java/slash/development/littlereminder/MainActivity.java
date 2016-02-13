package slash.development.littlereminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> remindertitles;
    private int numberofelements = 0;
    private final Context context = this;

    //private class thisClass = getClas


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ImageButton floatButton;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        remindertitles = new ArrayList<String>();

        floatButton = (ImageButton) findViewById(R.id.imageButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AddReminderActivity.class);
                startActivity(intent);


                //setContentView(R.layout.activity_add_reminder);
                /*numberofelements++;
                remindertitles.add("Reminder " + numberofelements);
                ListAdapter myAdapter = new CustomAdapter(context, remindertitles);
                ListView myListView = (ListView) findViewById(R.id.reminderlistView);
                myListView.setAdapter(myAdapter);

                Toast.makeText(getApplicationContext(), "Reminder " + numberofelements, Toast.LENGTH_LONG).show();*/

            }
        });

        generateAlarmList();



    }

    public void generateAlarmList() {

        ArrayList<ReminderObject> arrRO = new ArrayList<ReminderObject>();

        //Load objects
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("MyObject", "");

        Type type = new TypeToken<ArrayList<ReminderObject>>(){}.getType();

        //If first time, we don't have an object, and it will be null
        if (gson.fromJson(json, type) == null) {

        }
        //Else, assign it to arrRO
        else {
            arrRO = gson.fromJson(json, type);
        }

        ExpandableListAdapter myAdapter = new ExpandableListAdapter(context, arrRO);
        final ExpandableListView myListView = (ExpandableListView) findViewById(R.id.reminderlistView);
        myListView.setAdapter(myAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //CHANGE to add view under the clicked item.
                //parent.get

                Toast.makeText(context, "AdapterView: " + parent +  ". View: " + view + ". Item: " + position, Toast.LENGTH_LONG).show();
            }

        });

        /*myListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //parent.addView(findViewById(R.id.deleteButton));
                        //arrRO.add(position, new ReminderObject("Test", 1,1, "1-1-1-1"));

                        Toast.makeText(context, "Item: " + position, Toast.LENGTH_LONG).show();
                    }
                }
        );*/
        //Toast.makeText(getApplicationContext(), "Reminder " + arrRO.size(), Toast.LENGTH_LONG).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<ReminderObject> arrRO = new ArrayList<ReminderObject>();

        //Load objects
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("MyObject", "");

        Type type = new TypeToken<ArrayList<ReminderObject>>(){}.getType();

        //If first time, we don't have an object, and it will be null
        if (gson.fromJson(json, type) == null) {

        }
        //Else, assign it to arrRO
        else {
            arrRO = gson.fromJson(json, type);
        }

        ExpandableListAdapter myAdapter = new ExpandableListAdapter(context, arrRO);
        final ExpandableListView myListView = (ExpandableListView) findViewById(R.id.reminderlistView);
        myListView.setAdapter(myAdapter);

        //Toast.makeText(getApplicationContext(), "Reminder " + arrRO.size(), Toast.LENGTH_LONG).show();

    }
}
