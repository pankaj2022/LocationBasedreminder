package com.example.pankaj.projectf;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


// this activity will be called when the user click on the notification.
public class OnTouchReminderNotificationActivity extends ActionBarActivity implements LocationListener{

   // Declaring the variables , Respective names will suffice their functionality.
    TextView Task_;
    TextView Description_;
    TextView Location_;
    ReminderDatabase dbobj;
    Button doneButton;
    Reminder c;

    Button navigate_button;
    private LocationManager location_manager;
    private Location location_current;
    private double latitude_task_;
    private double longitude_task_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_description_activity_layout);

        //this is called when a user clicks on the notification.If the activity is starting first time then we have to call the onNewIntent() method from here.
        onNewIntent(getIntent());

    }


    // on clicking the notificatoin this method gets invoked.
    @Override
    public void onNewIntent(Intent intent){
        Bundle extras = intent.getExtras();
        if(extras != null){
            if(extras.containsKey("NotificationMessage"))
            {
                setContentView(R.layout.reminder_description_activity_layout);
                // extract the extra-data in the Notification
                long id = extras.getLong("NotificationMessage");
                System.out.println("ID is" +id);
                dbobj = new ReminderDatabase(this); // Reminder database class object created
                dbobj.open();
                c = dbobj.getComment(id);           // get all the the contents from the database using the ID of the task.
                dbobj.close();

                // Populate the fields on this activity using reminder class fucntions.
                Task_ = (TextView) findViewById(R.id.Task);
                Description_ = (TextView) findViewById(R.id.Description);
                Location_ = (TextView) findViewById(R.id.Location);
                Task_.setText(c.getTask());
                Description_.setText(c.getDescription());
                Location_.setText(c.getLocation_());


                //retrieve the location coordinates which will help in navigating to that location.
                latitude_task_= c.getLatitude();
                longitude_task_ = c.getLongitude_();
                //for getting the handler of location service.
                location_manager = (LocationManager) getSystemService(LOCATION_SERVICE);
                //For requesting the location-updates when user location is changed.
                location_manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0, Criteria.ACCURACY_COARSE,this);



                doneButton = (Button) findViewById(R.id.doneButton);

                navigate_button = (Button) findViewById(R.id.navigate_btn);

                // A user can navigate to the location of the task when he clicks on the navigation button.
                navigate_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // In the URI the location coordinates are passed and the google api will navigate to that location.
                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitude_task_+","+longitude_task_);
                        // Intent used and then package is set and then the activity is started which will do the navigation.
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                      }
                });



                // On clicking th Got it button, the entry will be deleted for that task from the databse.
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbobj.open();
                        dbobj.delete(c);        // query called to delete the resp entry.
                        dbobj.close();
                        // Home activity is called after deletion is done.
                        Intent intent = new Intent(OnTouchReminderNotificationActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                });
             }
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity22, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finishActivityFromChild(OnTouchReminderNotificationActivity.this,(int)c.getId());

    }

    @Override
    public void onLocationChanged(Location location) {
        location_current = location;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
