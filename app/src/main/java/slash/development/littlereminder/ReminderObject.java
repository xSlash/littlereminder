package slash.development.littlereminder;

import android.app.PendingIntent;

import java.sql.Time;

/**
 * Created by Martin on 04-02-2016.
 */
public class ReminderObject {

    private String Title;
    private int Hour;
    private int Minute;
    private String completeTime;
    private int requestCode;
    private PendingIntent pendingIntent;

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getHour() {
        return Hour;
    }

    public void setHour(int hour) {
        Hour = hour;
    }

    public int getMinute() {
        return Minute;
    }

    public void setMinute(int minute) {
        Minute = minute;
    }


    public ReminderObject(String title, int hour, int minute, String completetime) {

        this.Title = title;
        this.Hour = hour;
        this.Minute = minute;
        this.completeTime = completetime;
    }
}
