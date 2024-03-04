package services;

import dtos.request.TaskPostRequestDTO;
import dtos.response.TaskDataResponseDTO;
import exceptions.InvalidRequestAttributeValueException;
import models.TaskModel;
import repositories.ITaskRepository;
import repositories.TaskRepositoryLocalMemory;
import utilities.CommonUtility;
import utilities.DataValidationUtility;

import java.util.List;
import java.util.stream.Collectors;

public class TaskService {
    // create instance of ITaskRepository
    private ITaskRepository taskRepository = new TaskRepositoryLocalMemory();

    /**
     * retrieves all tasks from the database and converts them into a list of TaskDataResponseDTO objects
     *
     * @return list of TaskDataResponseDTO objects representing all tasks in the database
     */
    public List<TaskDataResponseDTO> getAllTasks() {
        // get all tasks from repository
        List<TaskModel> allTasks = taskRepository.findAllTasks();

        // map all TaskModel to TaskDataResponseDTO
        return allTasks.stream()
                .map(taskModel -> new TaskDataResponseDTO(
                        taskModel.getTaskId(),
                        taskModel.getTaskTitle(),
                        taskModel.getTaskDescription(),
                        taskModel.getTaskCreatedOn()
                ))
                .collect(Collectors.toList());
    }

    /**
     * method to create a new task record
     * 
     * @param newTaskToBeCreated - dto instance containing data of the new task to
     *                           be created
     */
    public TaskDataResponseDTO createNewTask(TaskPostRequestDTO newTaskToBeCreated) {
        // check if the instance of the dto is null, i.e. no request body was provided
        if (newTaskToBeCreated == null) {
            // throw appropriate exception
            throw new InvalidRequestAttributeValueException("AN EMPTY REQUEST BODY IS NOT VALID");
        }

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
        return new TaskDataResponseDTO(newTaskRecord.getTaskId(), newTaskRecord.getTaskTitle(),
                newTaskRecord.getTaskDescription(), newTaskRecord.getTaskCreatedOn());
    }

}
