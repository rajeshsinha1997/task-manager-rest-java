package repositories;

import models.TaskModel;

import java.util.List;

public interface ITaskRepository {
    /**
     * method to get all tasks from database
     *
     * @return - all tasks from database
     */
    public List<TaskModel> findAllTasks();

    /**
     * method to find a task by it's corresponding id from database
     * 
     * @param taskId - id to be used in searching the task from the database
     * @return - task object having the given task id, null otherwise
     */
    public TaskModel findTaskById(String taskId);

    /**
     * method to add a new task to the database
     * 
     * @param newTask - instance of TaskModel, i.e. new task object to be added
     */
    public void addNewTask(TaskModel newTask);

    /**
     * method to delete an existing task from database by it's corresponding id
     * 
     * @param taskId - id of the existing task object to delete
     */
    public void deleteTaskById(String taskId);

    /**
     * method to update an existing task record by it's corresponding id
     * 
     * @param taskId          - id of the existing task record to be updated
     * @param updatedTaskData - updated task record
     */
    public void updateTaskById(String taskId, TaskModel updatedTaskData);
}
