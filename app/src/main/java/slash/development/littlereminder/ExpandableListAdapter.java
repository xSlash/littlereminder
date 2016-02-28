package slash.development.littlereminder;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Martin on 13-02-2016.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<ReminderObject> _myArrayList;
    private String _myArrayListChild = "Delete";

    public ExpandableListAdapter(Context context, ArrayList<ReminderObject> myArrayList)
    {
        this._context = context;
        this._myArrayList = myArrayList;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_delete_row, null);
        }

        TextView deleteButtonTV = (TextView) convertView.findViewById(R.id.deleteButton);
        deleteButtonTV.setText(_myArrayListChild);


        deleteButtonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(_context, "Group/child: " + groupPosition + "/" + childPosition, Toast.LENGTH_LONG).show();
            }
        }) ;


        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        //String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_row, null);
        }

        //ReminderObject rmo = getGroup(groupPosition);
        //ReminderObject rmo = parent.getExpandableListAdapter().getChild(groupPosition, childPosition);

        String title = getGroup(groupPosition).getTitle();
        //String timer = Integer.toString(getItem(position).getHour()) + ":" + Integer.toString(getItem(position).getMinute());
        String timer = getGroup(groupPosition).getCompleteTime();

        TextView titleTV = (TextView) convertView.findViewById(R.id.reminderTitle);
        TextView timeTV = (TextView) convertView.findViewById(R.id.reminderTime);

        titleTV.setText(title);
        /*Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minuttes = c.get(Calendar.MINUTE);
        String completeAlarm = Integer.toString(hour) + ":" + Integer.toString(minuttes);*/
        timeTV.setText(timer);


        if(isExpanded) {

        }
        else {

        }

        return convertView;
    }







    @Override
    public String getChild(int groupPosition, int childPosititon) {
        return "Delete";
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;

        //return this._myArrayListChild.get(this._myArrayList.get(groupPosition))
        //        .size();
    }

    @Override
    public ReminderObject getGroup(int groupPosition) {
        return this._myArrayList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return this._myArrayList.size();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

