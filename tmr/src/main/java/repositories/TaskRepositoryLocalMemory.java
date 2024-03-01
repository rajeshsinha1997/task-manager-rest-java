package repositories;

import java.util.HashMap;
import java.util.Map;

import models.TaskModel;

/**
 * implementation class of the ITaskRepository interface with database as a
 * local in-memory HashMap
 */
public class TaskRepositoryLocalMemory implements ITaskRepository {
    // create local in-memory HashMap to store task data
    Map<String, TaskModel> tasks = new HashMap<>();

    @Override
    public TaskModel findTaskById(String taskId) {
        // find and return the required task from the database using the given task id,
        // return null if no task exists with the given task id
        return tasks.getOrDefault(taskId, null);
    }

    @Override
    public void addNewTask(TaskModel newTask) {
        // add new task record (i.e. instance of TaskModel) to the database
        tasks.put(newTask.getTaskId(), newTask);
    }

}
