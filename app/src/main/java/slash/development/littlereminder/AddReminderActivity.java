package slash.development.littlereminder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;

public class AddReminderActivity extends AppCompatActivity {

    private TimePicker timep;
    //private ArrayList<ReminderObject> arrRO = new ArrayList<ReminderObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        //arrRO = new ArrayList<ReminderObject>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button setButton = (Button) findViewById(R.id.setButton);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker tp = (TimePicker) findViewById(R.id.timePicker);
                //Only use this, since we use API lvl 19. From API +23, it's getHour / getMinute
                int hour = tp.getCurrentHour();
                int min = tp.getCurrentMinute();


                TextView textView = (TextView) findViewById(R.id.titleEditText);
                textView.setText(hour + ":" + min);

                ReminderObject reminderObject = new ReminderObject("zz", hour, min);

                //timep.setMinute(min);

                loadObjects();
                saveObjects(reminderObject);

            }
        });


    }

    private void loadObjects() {

        /*SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("MyObject", "");
        arrRO = new ArrayList<ReminderObject>();
        Type type = new TypeToken<ArrayList<ReminderObject>>(){}.getType();
        arrRO = gson.fromJson(json, type);*/

    }

    private void saveObjects(ReminderObject ro) {

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


        //Save objects

        arrRO.add(ro);

        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gsonsave = new Gson();
        String jsonsave = gsonsave.toJson(arrRO);
        prefsEditor.putString("MyObject", jsonsave);
        prefsEditor.commit();

        Toast.makeText(this, "Alarm array size: " + arrRO.size(), Toast.LENGTH_LONG).show();

    }

}
