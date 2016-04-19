package com.example.pankaj.projectf;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

// this is home activity . Ths will show up when the application starts.
public class HomeActivity extends ListActivity implements View.OnClickListener {
    static final int REQUEST_CODE = 1;
    ImageButton addtask;            // to add task this button is used
    ReminderDatabase dbobj;         // ReminderDatabase class object for database related queries.
    ListView view;
    int flag = 1;
    ArrayList<Reminder> alldata;    // This list will store all the data from the database.
    HomeListAdapter adapter;        // To display the task on the Home Activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        view = (ListView) findViewById(android.R.id.list);
        refreshView();  // to update the adapter and gather results from the database to the list adapter.

        // Reminder Started Service will be started when the user will click on the Add task button.
        addtask = (ImageButton) findViewById(R.id.button3);
        addtask.setOnClickListener(this);
            if (flag == 1) {
            flag++;
            Intent intent = new Intent(this, ReminderFetchService.class);
            this.startService(intent);//service started via intent.
        }
    }

    @Override
    public void onClick(View view) {
        // When the user clicks on the Add Task button, then new activity will be started to take the input.
        Intent intent=new Intent(HomeActivity.this,AddReminderActivity.class); // Explicit intent is used.
        startActivityForResult(intent, REQUEST_CODE); // called for result.
    }

    @Override
    // After user input the activity, this is called.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == REQUEST_CODE)
        {
            String add_task_new=data.getStringExtra("edittextvalue");

        }
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    protected void onResume() {
        super.onResume();
        refreshView();// to update the home screen after adding a new task .

         }
    // this will gather all the results from the database and show on the home screen using list view.
    void refreshView(){
        dbobj = new ReminderDatabase(this);  // database object created fo all the data from the database.
        dbobj.open();
        alldata = dbobj.getALLComments(); // get all the data form the database.
        dbobj.close();
        adapter = new HomeListAdapter(this, alldata);  // passing all the data fetched from the database to the constructor to update the list adapter.
        setListAdapter(adapter);  // it is will populate a list view.
    }
}
