package slash.development.littlereminder;

/**
 * Created by Martin on 10-02-2016.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    //private Context context;

    @Override
    public void onReceive(final Context context, Intent intent) {
        //MainActivity.getTextView2().setText("Enough Rest. Do Work Now!");

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        Notification noti = new Notification.Builder(context)
                .setContentTitle("Some title")
                .setContentText("Time: " + ts)
                .setSmallIcon(R.drawable.notification_icon).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

        //Alarm sound
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();




    }
}
