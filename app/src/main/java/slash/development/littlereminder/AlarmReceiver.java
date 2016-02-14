package slash.development.littlereminder;

/**
 * Created by Martin on 10-02-2016.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    //private Context context;

    @Override
    public void onReceive(final Context context, Intent intent) {
        //MainActivity.getTextView2().setText("Enough Rest. Do Work Now!");

        /*Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();*/
        int rqcode = intent.getExtras().getInt("id");

        //Find stored ReminderObject array
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("MyObject", "");

        ArrayList<ReminderObject> arrRO = new ArrayList<ReminderObject>();
        Type type = new TypeToken<ArrayList<ReminderObject>>(){}.getType();
        arrRO = gson.fromJson(json, type);

        //Find the object which match the intent
        int matchedObject = 0;

        for (int i = 0; i < arrRO.size(); i++) {
            if (arrRO.get(i).getRequestCode() == rqcode) {
                matchedObject = i;
            }
        }

        ReminderObject foundReminder = arrRO.get(matchedObject);


        //Create notification
        Notification noti = new Notification.Builder(context)
                .setContentTitle(foundReminder.getTitle())
                .setContentText(foundReminder.getCompleteTime())
                .setSmallIcon(R.drawable.notification_icon).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

        //Alarm sound
        /*Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();*/




    }
}
