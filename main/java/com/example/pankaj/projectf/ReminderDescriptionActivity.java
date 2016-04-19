package com.example.pankaj.projectf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * This Activity will display the reminder details including the Task Name, Task Description,
 * Task Location.
 */
public class ReminderDescriptionActivity extends Activity {

    Button okButton;                // To exit from the activity.
    TextView Task_;                 // To display the Task Name.
    TextView Description_;          // To display the Task Description.
    TextView Location_;             // TO display the Task Location in Text format.
    ReminderDatabase dbobj;         // To retrieve the data for the respective task to display from the database.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        okButton = (Button) findViewById(R.id.okButton);
        Description_ = (TextView) findViewById(R.id.Description);
        Location_ = (TextView) findViewById(R.id.Location);
        Task_ = (TextView) findViewById(R.id.Task);
        dbobj = new ReminderDatabase(this);
        // Getting the ID of the Task clicked on the Custom list adapter.
        String id = getIntent().getStringExtra("id");
        dbobj.open();
        // Retrieving the Task info from the database using ID.
        Reminder c = dbobj.getComment(Long.parseLong(id));
        dbobj.close();
        // Setting each of the value after they are retrieved from the database based on the TASK ID.
        Task_.setText(c.getTask());
        Description_.setText(c.getDescription());
        Location_.setText(c.getLocation_());
        // to go back to the homepage.
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderDescriptionActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

}
