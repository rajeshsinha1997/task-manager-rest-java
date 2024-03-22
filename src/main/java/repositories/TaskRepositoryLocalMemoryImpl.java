package repositories;

import models.TaskModel;
import utilities.CommonServletUtility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * implementation class of the ITaskRepository interface with database as a
 * local in-memory HashMap
 */
public class TaskRepositoryLocalMemoryImpl implements ITaskRepository {
    // create logger instance
    private static final Logger logger = LogManager.getLogger(TaskRepositoryLocalMemoryImpl.class);

    // create local in-memory HashMap to store task data
    Map<String, TaskModel> tasks = new HashMap<>();

    // create default constructor to limit instantiation capabilities to the current
    // package only
    TaskRepositoryLocalMemoryImpl() {
        TaskRepositoryLocalMemoryImpl.logger.info("initialized task repository with local memory implementation");
    }

    @Override
    public List<TaskModel> findAllTasks() {
        // return all tasks from database which are not deleted as a list
        TaskRepositoryLocalMemoryImpl.logger
                .info("finding all available tasks from database and returning the result as a list");
        return tasks.values().stream().filter(task -> !task.isTaskDeleted()).toList();
    }

    @Override
    public TaskModel findTaskById(String taskId) {
        // find the required task from the database using the given task id
        TaskRepositoryLocalMemoryImpl.logger.info("finding task record from the database using task id - " + taskId);
        TaskModel existingTaskObject = tasks.getOrDefault(taskId, null);
        TaskRepositoryLocalMemoryImpl.logger
                .debug("found task record from the database using task id " + taskId + " - "
                        + existingTaskObject == null ? null
                                : CommonServletUtility.getJsonFromObject(existingTaskObject));

        // check if no existing task object found or the existing task object is deleted
        // already
        TaskRepositoryLocalMemoryImpl.logger
                .info("checking if the task record fetched from the databse using the task id " + taskId
                        + " is null or deleted");
        if (existingTaskObject == null || existingTaskObject.isTaskDeleted()) {
            // return null as no task found with the given id
            TaskRepositoryLocalMemoryImpl.logger.info(
                    "task record fetched from the database using the task id " + taskId + " is either null or deleted");
            return null;
        }
        // else return the existing task object
        else {
            TaskRepositoryLocalMemoryImpl.logger
                    .info("returning task record fetched from database using task id - " + taskId);
            return existingTaskObject;
        }
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

    @Override
    public void updateTaskById(String taskId, TaskModel updatedTaskData) {
        // update the existing task object mapped by it's corresponding id
        this.tasks.put(taskId, updatedTaskData);
    }

}
