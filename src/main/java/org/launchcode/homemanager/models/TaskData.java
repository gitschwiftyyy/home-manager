package org.launchcode.homemanager.models;

import java.util.ArrayList;

/**
 * Created by schwifty on 10/31/17.
 */
public class TaskData {

    static ArrayList<Task> tasks = new ArrayList<>();

    public static void add(Task newTask) {
        tasks.add(newTask);
    }

    public static ArrayList<Task> getAll() {
        return tasks;
    }
    public static Task getById(int id) {
        Task theTask = null;

        for (Task task : tasks) {
            if (task.getId() == id) {
                theTask = task;
            }
        }

        return theTask;
    }
}
