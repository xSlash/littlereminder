package slash.development.littlereminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddReminderActivity extends AppCompatActivity {

    private static Context context;

    public static Context getARContext() {
        return AddReminderActivity.context;

    }
    private TimePicker timep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

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
                String title = textView.getText().toString();

                String completeTime = new DecimalFormat("00").format(hour) + ":" + new DecimalFormat("00").format(min);

                ReminderObject reminderObject = new ReminderObject(title, hour, min, completeTime);

                addAlarmToList(reminderObject);

                finish();

            }
        });

    }

    private void addAlarmToList(ReminderObject ro) {

        ArrayList<ReminderObject> arrRO = new ArrayList<ReminderObject>();

        //Load objects
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("MyObject", "");

        int rqCode = appSharedPrefs.getInt("latestRequestCode", -1) + 1;
        ro.setRequestCode(rqCode);

        Type type = new TypeToken<ArrayList<ReminderObject>>(){}.getType();


        //Add to our stored objects
        //If first time, we don't have an object, and it will be null
        if (gson.fromJson(json, type) == null) {

        }
        //Else, assign it to arrRO
        else {
            arrRO = gson.fromJson(json, type);
        }

        for (int i = 0; i < arrRO.size(); i++) {
            if (ro.getHour() < arrRO.get(i).getHour()) {
                arrRO.add(i, ro);
                break;
            }
            else if (ro.getHour() == arrRO.get(i).getHour()) {
                if (ro.getMinute() < arrRO.get(i).getMinute()) {
                    arrRO.add(i, ro);
                    break;
                }
            }

            if (i == (arrRO.size()-1)) {
                arrRO.add(ro);
                break;
            }
        }

        //If it's the first alarm
        if (arrRO.size() == 0) {
            arrRO.add(ro);
        }

        //Set the alarm
        setAlarm(rqCode, ro);

        //Store the alarm
        storeAlarms(arrRO, rqCode);


    }

    public void storeAlarms(ArrayList<ReminderObject> arrRO, int rqCode) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gsonsave = new Gson();
        String jsonsave = gsonsave.toJson(arrRO);
        prefsEditor.putString("MyObject", jsonsave);
        prefsEditor.putInt("latestRequestCode", rqCode);
        prefsEditor.commit();
    }

    public void setAlarm(int rqCode, ReminderObject ro) {

        //Create alarm manager
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //Setting the time the alarm should go off
        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, ro.getHour());
        calendar.set(Calendar.MINUTE, ro.getMinute());
        calendar.set(Calendar.SECOND, 0);

        //If the time set is past for the day, set for next day.
        if(calendar.before(now)){
            calendar.add(Calendar.DATE, 1);
        }

        Intent myIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
        myIntent.putExtra("id", rqCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), rqCode, myIntent, 0);

        //Setting the alarm every day - Notice INTERVAL_DAY.
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(this, "Alarm set: " + ro.getTitle() + " - " + ro.getCompleteTime(), Toast.LENGTH_LONG).show();
    }

}
