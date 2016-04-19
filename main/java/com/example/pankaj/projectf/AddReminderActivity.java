package com.example.pankaj.projectf;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

// This activity will take the input from the user , i.e Task Name, Task Description, Task Location.
public class AddReminderActivity extends ListActivity  {
    ReminderDatabase DB_OBJ;        //object for the ReminderDatabase class.
    Button Add_Task2;               // to exit and call the home activity.
    EditText Task_Box1;             // To enter the Task Name.
    EditText Description_Box1;      // To enter the Task description.
    Button  Map_Button;             //  To select the Task location.
    int PLACE_PICKER_REQUEST = 1;
    private Double Latitude_;
    private Double Longitude_;
    CharSequence Name_;
    CharSequence Address_;
    TextView Loc_Name_;             //To display the Task location
    TextView Loc_Address_;          //to display task location
    String Location_Full_Address_;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder_item_layout);
        DB_OBJ = new ReminderDatabase(this);          // Creating an object for the ReminderDatabase class.
        DB_OBJ.open();                                // Open the Connection.
        Task_Box1 = (EditText)findViewById(R.id.taskbox_1);
        Description_Box1 = (EditText)findViewById(R.id.descriptionbox_1);
        Add_Task2 = (Button)findViewById(R.id.addtask_2);
        Loc_Name_=(TextView)findViewById(R.id.Map_Location_Name);
        Loc_Address_=(TextView)findViewById(R.id.Map_Location_Address);

        //Adding a Task when user clicks on the ADD_TASK Button after filling the Details
        Add_Task2.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Reminder reminder = null;
                String Task_ = Task_Box1.getText().toString();
                String Description_ = Description_Box1.getText().toString();
                //inserting all the input to the database which have been entered in this activity.
                reminder = DB_OBJ.insert(Task_,Description_,Location_Full_Address_,Latitude_,Longitude_);
                Intent intent = new Intent();
                intent.putExtra("TASK_ADDED", reminder.getTask());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        Map_Button = (Button)findViewById(R.id.mapbutton);

        // Location Picker , the user will select the location from the Map
        Map_Button.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                try {
                    // user call select a desired place from the place picker.
                    // Google API provides this feature using intent builder class.
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    Intent intent = builder.build(AddReminderActivity.this);
                   //location picker activity is called where we can select the place.
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    // This is called on result , when the user selects a location.The location is then stored.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                //Place class will store all the data of the location.
                Place place = PlacePicker.getPlace(data, this);
                Name_ = place.getName();  // retrieve the name of the location.
                Address_ = place.getAddress();  //address is retrieved.
                // all the name, address are updated on the text view.
                Loc_Name_.setText(Name_);
                Loc_Address_.setText(Address_);
                Location_Full_Address_ = Loc_Name_.getText().toString() + "," + Loc_Address_.getText().toString();
                Latitude_ = place.getLatLng().latitude;
                Longitude_= place.getLatLng().longitude;

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
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
}
