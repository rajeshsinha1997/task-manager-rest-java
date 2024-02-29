package repositories;

import models.TaskModel;

public interface ITaskRepository {
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
}
