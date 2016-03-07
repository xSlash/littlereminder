package slash.development.littlereminder;

import android.app.AlarmManager;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

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

        CustomAdapter myAdapter = new CustomAdapter(context, arrRO);
        final ListView myListView = (ListView) findViewById(R.id.reminderlistView);
        myListView.setAdapter(myAdapter);

        //myListView.deferNotifyDataSetChanged();


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

        final CustomAdapter myAdapter = new CustomAdapter(context, arrRO);
        final ListView myListView = (ListView) findViewById(R.id.reminderlistView);
        myListView.setAdapter(myAdapter);

        final int[] zz = {0};

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                zz[0] = position;

                Toast.makeText(context, "pos: " + position, Toast.LENGTH_LONG).show();

                final Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                final Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                //view.setAnimation(slide_down);


                final View layout = (View) view.findViewById(R.id.DeleteRowLL);
                layout.setVisibility(View.VISIBLE);
                layout.setAnimation(slide_down);

                /*View titleview = (View) view.findViewById(R.id.TitleRow);
                titleview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Radio Kappa ", Toast.LENGTH_LONG).show();
                    }
                });*/

                Button delButton = (Button) layout.findViewById(R.id.deleteButton);
                delButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context, "Kappa " + zz[0], Toast.LENGTH_LONG).show();

                        //CHANGE HERE!
                        ArrayList<ReminderObject> tmparrRO = getSavedObjects();

                        //Cancel the alarm of the object
                        cancelAlarm(tmparrRO.get(position));

                        //Remove from the actual stored objects (behind the scenes)
                        tmparrRO.remove(position);
                        saveObjects(tmparrRO);

                        //Remove from the viewable list (immediate effect)
                        myAdapter.remove(myAdapter.getItem(position));
                        myListView.deferNotifyDataSetChanged();


                        //Slide up animation. Handler for making View invisible after animation
                        /*layout.startAnimation(slide_up);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                // Actions to do after 10 seconds
                                layout.setVisibility(View.GONE);
                            }
                        }, 1000);*/

                    }
                });
                /*if (layout.getVisibility() == View.INVISIBLE) {
                    layout.setVisibility(View.VISIBLE);
                }
                else {
                    layout.setVisibility(View.INVISIBLE);
                }*/


            }
        });

    }

    public ArrayList<ReminderObject> getSavedObjects() {

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

        return arrRO;
    }

    public void saveObjects(ArrayList<ReminderObject> arrRO) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gsonsave = new Gson();
        String jsonsave = gsonsave.toJson(arrRO);
        prefsEditor.putString("MyObject", jsonsave);
        //prefsEditor.putInt("latestRequestCode", rqCode);
        prefsEditor.commit();
    }

    public void cancelAlarm(ReminderObject ro) {

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        manager.cancel(ro.getPendingIntent());
    }
}
