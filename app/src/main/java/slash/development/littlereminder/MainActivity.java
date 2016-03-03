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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
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

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(context, "pos: " + position, Toast.LENGTH_LONG).show();

                Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                //view.setAnimation(slide_down);


                View layout = (View) view.findViewById(R.id.DeleteRowLL);
                layout.setVisibility(View.VISIBLE);
                layout.setAnimation(slide_down);

                /*if (layout.getVisibility() == View.INVISIBLE) {
                    layout.setVisibility(View.VISIBLE);
                }
                else {
                    layout.setVisibility(View.INVISIBLE);
                }*/


            }
        });

        /*myListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                //myListView.collapseGroup()

                if (myListView.isGroupExpanded(groupPosition)) {
                    //mProgAdap.prepareToCollapseGroup(groupPosition);
                    //setupLayoutAnimationClose(groupPosition);
                    //myListView.requestLayout();
                    myListView.collapseGroup(groupPosition);
                } else {
                    boolean autoScrollToExpandedGroup = false;
                    myListView.expandGroup(groupPosition, autoScrollToExpandedGroup);
                    setupLayoutAnimation(groupPosition);
                    //
                }
                //telling the listView we have handled the group click, and don't want the default actions.
                return true;
            }

            private void setupLayoutAnimation(int groupPosition) {

                AnimationSet set = new AnimationSet(true);
                Animation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setDuration(100);
                set.addAnimation(animation);

                animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f, 0.5f, 1.0f);
                animation.setDuration(100);
                set.addAnimation(animation);

                LayoutAnimationController controller = new LayoutAnimationController(set, 0.75f);
                myListView.setLayoutAnimationListener(null);
                myListView.setLayoutAnimation(controller);

                //myListView.setLayout

                Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

                myListView.getChildAt(groupPosition).setAnimation(slide_down);

                //myListView.setAnimation(slide_down);


            }

            private void setupLayoutAnimationClose(final int groupPosition) {
                AnimationSet set = new AnimationSet(true);
                Animation animation = new AlphaAnimation(1.0f, 0.0f);
                animation.setDuration(50);
                animation.setFillAfter(true);
                animation.setFillEnabled(true);
                set.addAnimation(animation);
                animation = new ScaleAnimation(1.0f, 1.0f, 1.0f, 0.0f, 0.5f, 0.0f);
                animation.setDuration(50);
                animation.setFillAfter(true);
                animation.setFillEnabled(true);
                set.addAnimation(animation);
                set.setFillAfter(true);
                set.setFillEnabled(true);
                LayoutAnimationController controller = new LayoutAnimationController(set, 0.75f);
                controller.setOrder(LayoutAnimationController.ORDER_REVERSE);
                myListView.setLayoutAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        myListView.collapseGroup(groupPosition);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                myListView.setLayoutAnimation(controller);
            }

        });*/

        //Toast.makeText(getApplicationContext(), "Reminder " + arrRO.size(), Toast.LENGTH_LONG).show();

    }
}
