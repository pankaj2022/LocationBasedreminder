package com.example.pankaj.projectf;

/**
 * Created by Pankaj on 5/1/2015.
 */
public class Reminder {
    private long id;
    private String task;
    private String description;
    private Double latitude_;
    private Double longitude_;
    private String location_;


    // this class will help in putting the data returned from the cursor to the Array list of type Reminder.
    // getter and setter are created which will store the data and retreive the data respectively in the respective instance of the class via objects.
    // the variable names itself defines their usage.
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getLocation_() {
        return location_;
    }

    public void setLocation_(String location_) {
        this.location_ = location_;
    }

    public Double getLongitude_() {
        return longitude_;
    }

    public void setLongitude_(Double longitude_) {
        this.longitude_ = longitude_;
    }

    public Double getLatitude() {
        return latitude_;
    }

    public void setLatitude(Double latitude_) {
        this.latitude_ = latitude_;
    }

}
