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
        // return all tasks from database which are not deleted as a list
        return tasks.values().stream().filter(task -> !task.isTaskDeleted()).toList();
    }

    @Override
    public TaskModel findTaskById(String taskId) {
        // find the required task from the database using the given task id
        TaskModel existingTaskObject = tasks.getOrDefault(taskId, null);

        // check if no existing task object found or the existing task object is deleted
        // already
        if (existingTaskObject == null || existingTaskObject.isTaskDeleted()) {
            // return null as no task found with the given id
            return null;
        }
        // else return the existing task object
        else
            return existingTaskObject;
    }

    @Override
    public void addNewTask(TaskModel newTask) {
        // add new task record (i.e. instance of TaskModel) to the database
        tasks.put(newTask.getTaskId(), newTask);
    }

    @Override
    public void deleteTaskById(String taskId) {
        // update 'isDeleted' flag attribute value of the existing task object
        tasks.get(taskId).setTaskDeleted(true);
    }

}
