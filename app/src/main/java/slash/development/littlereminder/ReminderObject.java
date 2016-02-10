package slash.development.littlereminder;

import java.sql.Time;

/**
 * Created by Martin on 04-02-2016.
 */
public class ReminderObject {

    private String Title;
    private int Hour;
    private int Minute;

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


    public ReminderObject(String title, int hour, int minute) {

        this.Title = title;
        this.Hour = hour;
        this.Minute = minute;
    }
}
