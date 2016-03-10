package slash.development.littlereminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ImageButton floatButton;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatButton = (ImageButton) findViewById(R.id.imageButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AddReminderActivity.class);
                startActivity(intent);

            }
        });

        ArrayList<ReminderObject> arrRO = new ArrayList<ReminderObject>();
        arrRO = loadAlarmList();

        CustomAdapter myAdapter = new CustomAdapter(context, arrRO);
        final ListView myListView = (ListView) findViewById(R.id.reminderlistView);
        myListView.setAdapter(myAdapter);

    }

    public ArrayList<ReminderObject> loadAlarmList() {

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
        //Else, assign it to arrRO (ArrayReminderObjects
        else {
            arrRO = gson.fromJson(json, type);
        }

        return arrRO;
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

        arrRO = loadAlarmList();


        final CustomAdapter myAdapter = new CustomAdapter(context, arrRO);
        final ListView myListView = (ListView) findViewById(R.id.reminderlistView);
        myListView.setAdapter(myAdapter);

        final int[] zz = {0};

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                zz[0] = position;

                //Create animations from xml files.
                final Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                final Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);

                final View layout = (View) view.findViewById(R.id.DeleteRowLL);
                layout.setVisibility(View.VISIBLE);
                layout.setAnimation(slide_down);


                LinearLayout titleLayout = (LinearLayout) view.findViewById(R.id.TitleRow);
                titleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (layout.getVisibility() == View.VISIBLE) {
                            layout.startAnimation(slide_up);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    //Remove view after 1 sec
                                    layout.setVisibility(View.GONE);
                                }
                            }, 500);
                        }
                        else {
                            layout.setVisibility(View.VISIBLE);
                            layout.startAnimation(slide_down);

                        }

                    }
                });

                //Delete button pressed
                Button delButton = (Button) layout.findViewById(R.id.deleteButton);
                delButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ArrayList<ReminderObject> tmparrRO = loadAlarmList();

                        //Cancel the alarm of the object
                        cancelAlarm(tmparrRO.get(position));

                        //Remove from the actual stored objects (behind the scenes)
                        tmparrRO.remove(position);
                        saveObjects(tmparrRO);

                        //Remove from the viewable list (immediate effect)
                        myAdapter.remove(myAdapter.getItem(position));
                        myListView.deferNotifyDataSetChanged();

                    }
                }); //End of delButton

            }
        });

    }

    public void saveObjects(ArrayList<ReminderObject> arrRO) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gsonsave = new Gson();
        String jsonsave = gsonsave.toJson(arrRO);
        prefsEditor.putString("MyObject", jsonsave);
        prefsEditor.commit();
    }

    public void cancelAlarm(ReminderObject ro) {

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int rqCode = ro.getRequestCode();
        Intent myIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), rqCode, myIntent, 0);

        manager.cancel(pendingIntent);
    }
}
