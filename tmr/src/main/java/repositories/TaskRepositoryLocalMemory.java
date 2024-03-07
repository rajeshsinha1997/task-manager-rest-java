package repositories;

import models.TaskModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * implementation class of the ITaskRepository interface with database as a
 * local in-memory HashMap
 */
public class TaskRepositoryLocalMemory implements ITaskRepository {
    // create local in-memory HashMap to store task data
    Map<String, TaskModel> tasks = new HashMap<>();

    @Override
    public List<TaskModel> findAllTasks() {
        // return all tasks from database as a list
        return tasks.values().stream().toList();
    }

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
