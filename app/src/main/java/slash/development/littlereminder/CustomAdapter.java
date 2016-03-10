package slash.development.littlereminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Martin on 04-02-2016.
 */
public class CustomAdapter extends ArrayAdapter<ReminderObject> {


    public CustomAdapter(Context context, ArrayList<ReminderObject> myArrayList) {
        super(context, R.layout.custom_row , myArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_row, parent, false);

        String title = getItem(position).getTitle();
        String timer = getItem(position).getCompleteTime();

        TextView titleTV = (TextView) customView.findViewById(R.id.reminderTitle);
        TextView timeTV = (TextView) customView.findViewById(R.id.reminderTime);

        titleTV.setText(title);
        timeTV.setText(timer);


        return customView;
    }


}
