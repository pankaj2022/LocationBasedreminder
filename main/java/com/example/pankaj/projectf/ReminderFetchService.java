package com.example.pankaj.projectf;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ReminderFetchService extends Service implements LocationListener {
    private ReminderDatabase DB_OBJ_;                     // to perform database related operations Remide
    private LocationManager Location_Manager_;            // to register with location services.
    Double Latitude_Current_,Longitude_Current_;          // to store the current latitude and longitude.
    private Location Location_Current_;
    public ReminderFetchService() {
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Location_Manager_ = (LocationManager) getSystemService(LOCATION_SERVICE);                                     //for getting the handler of location service.
        Location_Manager_.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0, Criteria.ACCURACY_COARSE,this);   //For requesting the location-updates when user location is changed.
        Timer t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {
                                  @Override
                                  public void run() {

                                      //Called each time when 1000 milliseconds (1 second) (the period parameter)
                                      measuredistance();

                                  }
                              },
                //Set how long before to start calling the TimerTask (in milliseconds)
                10000,
                //Set the amount of time between each execution (in milliseconds)
                120000);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
          //Declare the timer
           return super.onStartCommand(intent, flags, startId);
          // Database Variables Initialised.


    }
    // this method will calculate the distance of each task with respect to current location in the background .
    synchronized public void measuredistance() {
        DB_OBJ_ = new ReminderDatabase(this);
        DB_OBJ_.open();
        // retrieve all the task from the database .
        ArrayList<Reminder> data = DB_OBJ_.getALLComments(); // get all the data form the database.
        float distanceInMeters = Float.MAX_VALUE;
        // loop will run for total number of  task  in the database.
        for (int i = 0; i < data.size(); i++) {
            // location of the task will be set into this object.
            Location Reminder_Loc_ = new Location("");
            // setting the latitude and longitude into the location object.
            Reminder_Loc_.setLatitude(data.get(i).getLatitude());
            Reminder_Loc_.setLongitude(data.get(i).getLongitude_());
            if(Location_Current_!= null && Reminder_Loc_ != null)
                // calculate the distance of the reminder location with respect to current location.
                distanceInMeters = Reminder_Loc_.distanceTo(Location_Current_);
            //distance is compared with 1000meters , if it is within 1000m then give the notification.
            if (distanceInMeters < 1000.00) {
                shownotification(data,i);
                Log.v("Distance_In_Metres_2", Float.toString(distanceInMeters));
                Log.v(data.get(i).getLocation_(), Float.toString(distanceInMeters));
            }
        }
        DB_OBJ_.close();
    }
    // to give the notification on the screen.
    public void shownotification(ArrayList<Reminder> alldata,int i) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ReminderFetchService.this);
        builder.setContentTitle(alldata.get(i).getTask());
        // this will be shown on the notification list
        builder.setContentText("You are Close to " + alldata.get(i).getLocation_());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        // this will be the title of the notification.
        builder.setTicker("Reminder For " + alldata.get(i).getTask());
        // remove the notification when it is clicked from the view.
        builder.setAutoCancel(true);

        Notification notification = builder.build();
        //getting the handler of the notification service.
        NotificationManager notification_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //intent is used for opening the task when the user clicks on the notification.
        Intent notificationIntent = new Intent(this, OnTouchReminderNotificationActivity.class);
       // id is passed via intent to the task so that when user clicks the notification, then the values can be retrieved using this id from the database.
        notificationIntent.putExtra("NotificationMessage", alldata.get(i).getId());
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(getApplicationContext(),(int)alldata.get(i).getId(),notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        // this will be  the notification message.
        notification.setLatestEventInfo(this, alldata.get(i).getTask(), "You are Close to " + alldata.get(i).getLocation_(), pendingNotificationIntent);
        notification_manager.notify((int)alldata.get(i).getId(),notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
     //   DB_OBJ1_.close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Latitude_Current_ = location.getLatitude();                                                                              //for storing the current position
        Longitude_Current_ = location.getLongitude();
        Location_Current_ = location;
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
