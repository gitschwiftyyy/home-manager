package org.launchcode.homemanager.models;





import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by schwifty on 10/30/17.
 */
@Entity
public class Task {


    private String name;
    private String description;
    private int frequency;
    private boolean isComplete = false;
    private int timesCompleted = 0;

    @Id
    @GeneratedValue
    private int id;


    public Task(String name, String description, int frequency, boolean isComplete, int timesCompleted) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.isComplete = isComplete;
        this.timesCompleted = timesCompleted;

    }

    public Task() {}



    public void doneOnce() {
        timesCompleted ++;

        if (timesCompleted == frequency) {
            isComplete = true;
        }
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public int getTimesCompleted() {
        return timesCompleted;
    }

    public void setTimesCompleted(int timesCompleted) {
        this.timesCompleted = timesCompleted;
    }
}
