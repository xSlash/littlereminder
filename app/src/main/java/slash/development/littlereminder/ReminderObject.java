package slash.development.littlereminder;

import java.sql.Time;

/**
 * Created by Martin on 04-02-2016.
 */
public class ReminderObject {

    private String Title;
    private Time reminderTimer;

    public ReminderObject(String title, Time time) {
        this.reminderTimer = time;
        this.Title = title;

    }
}
