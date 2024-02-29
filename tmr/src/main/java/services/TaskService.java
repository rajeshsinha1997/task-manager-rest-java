package services;

import dtos.request.TaskPostRequestDTO;
import dtos.response.TaskDataResponseDTO;
import models.TaskModel;
import repositories.ITaskRepository;
import repositories.TaskRepositoryLocalMemory;
import utilities.CommonUtility;
import utilities.DataValidationUtility;

public class TaskService {

    // create instance of ITaskRepository
    private ITaskRepository taskRepository = new TaskRepositoryLocalMemory();

    /**
     * method to create a new task record
     * 
     * @param newTaskToBeCreated - dto instance containing data of the new task to
     *                           be created
     */
    public TaskDataResponseDTO createNewTask(TaskPostRequestDTO newTaskToBeCreated) {
        // validate new task data
        DataValidationUtility.validateTaskTitle(newTaskToBeCreated.getTaskTitle(), false);
        DataValidationUtility.validateTaskDescription(newTaskToBeCreated.getTaskDescription(), true);

        // generate id of the new task
        String taskId = CommonUtility.generateUUIDv4();

        // find an already existing task record having the generated task id
        TaskModel existingTaskRecord = this.taskRepository.findTaskById(taskId);

        // check if a task record exists having the generated task id
        if (existingTaskRecord != null) {
            // continue until we can find a unique task id
            while (existingTaskRecord != null) {
                // generate a new task id
                taskId = CommonUtility.generateUUIDv4();

                // find an already existing task record having the generated task id
                existingTaskRecord = this.taskRepository.findTaskById(taskId);
            }
        }

        // create new task object
        TaskModel newTaskRecord = new TaskModel(taskId, newTaskToBeCreated.getTaskTitle(),
                newTaskToBeCreated.getTaskDescription(), CommonUtility.getCurrentDateAndTimeStampString(),
                CommonUtility.getCurrentDateAndTimeStampString(), false, false);

        // add new task record to database
        this.taskRepository.addNewTask(newTaskRecord);

        // create and return response dto object
        return new TaskDataResponseDTO(newTaskRecord.getTaskId(),
                newTaskRecord.getTaskDescription(), newTaskRecord.getTaskCreatedOn());
    }

}
