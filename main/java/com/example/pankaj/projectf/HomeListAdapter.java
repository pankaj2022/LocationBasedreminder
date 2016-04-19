package com.example.pankaj.projectf;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pankaj on 5/2/2015.
 */

// Custom list adapter is created to display the results on the home screen of all the Task with their names.
public class HomeListAdapter extends ArrayAdapter<Reminder> {

    ArrayList<Reminder> reminder = new ArrayList<Reminder>();
    Context context;
    ReminderDatabase dbobj1;     // database object created.

    // initialise the custom list adapter.
    public HomeListAdapter(Context context, ArrayList<Reminder> comm) {
        super(context, 0, comm);
        reminder = comm;         //initialise with all the database data to the reminder list data structure.
        this.context = context; // initialise with the current context of the home activity.
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_list_item, parent, false);
        }
        // Lookup view for data population
        // this text-view will display just the Task name on the home activity.
        TextView taskName = (TextView) convertView.findViewById(R.id.taskName);
        //delete button to delete the reminder
        ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.deleteButton);

        // Populate the data into the template view using the data object

        taskName.setText(reminder.get(position).getTask());
        // to Display the task , when a user clicks on the task from the home screen.
        taskName.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReminderDescriptionActivity.class);
                intent.putExtra("id", String.valueOf(reminder.get(position).getId()));
                ((HomeActivity) context).startActivity(intent);
            }
        });

        // This will delete the entry from the list view once clicked on the delete button.
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                dbobj1 = new ReminderDatabase(context);
                dbobj1.open();
                dbobj1.delete(reminder.get(position)); // deletion query is called
                reminder.remove(position);             // list data structure is updated.
                notifyDataSetChanged();                //adapter is updated.
                dbobj1.close();
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
