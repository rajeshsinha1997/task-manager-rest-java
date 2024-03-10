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
     * retrieves all tasks from the database and maps them into a list of
     * TaskDataResponseDTO objects
     *
     * @return list of TaskDataResponseDTO objects representing all tasks in the
     *         database
     */
    public List<TaskDataResponseDTO> getAllTasks() {
        // call repository method to get list of all available tasks and return after
        // mapping the list items to the required result DTO type
        return this.taskRepository.findAllTasks().stream()
                .map(taskModel -> new TaskDataResponseDTO(
                        taskModel.getTaskId(),
                        taskModel.getTaskTitle(),
                        taskModel.getTaskDescription(),
                        taskModel.getTaskCreatedOn()))
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

    /**
     * method to find a task with a specific id
     * 
     * @param taskId - id value to use to find the expected task object
     * @return an instance of TaskDataResponseDTO if a task object found with the
     *         given id
     * @throws InvalidRequestAttributeValueException - if no task object found with
     *                                               the given task id
     * @throws InvalidRequestAttributeValueException - if the given task id is
     *                                               invalid
     */
    public TaskDataResponseDTO getTaskById(String taskId) {
        // check if the given task id is valid
        if (DataValidationUtility.isValidTaskId(taskId)) {
            // find task with the given id from the database
            TaskModel existingTask = this.taskRepository.findTaskById(taskId);

            // check if the existing task object is null
            if (existingTask == null) {
                // throw corresponding exception
                throw new InvalidRequestAttributeValueException("NO TASK FOUND WITH GIVEN ID: " + taskId);
            } else {
                // map task data to the response type DTO object and return
                return new TaskDataResponseDTO(existingTask.getTaskId(), existingTask.getTaskTitle(),
                        existingTask.getTaskDescription(), existingTask.getTaskCreatedOn());
            }
        } else {
            // throw corresponding exception
            throw new InvalidRequestAttributeValueException("INVALID TASK ID: " + taskId);
        }
    }

    /**
     * method to delete an existing task with the given id
     * 
     * @param taskId - id of the existing task object to delete
     * @return deleted task object as an instance of TaskDataResponseDTO
     * @throws InvalidRequestAttributeValueException - if the give id of the
     *                                               existing task is invalid
     */
    public TaskDataResponseDTO deleteTaskById(String taskId) {
        // check if the given task id is valid
        if (DataValidationUtility.isValidTaskId(taskId)) {
            // fetch the existing task object with the given id
            TaskModel existingTaskObject = this.taskRepository.findTaskById(taskId);

            // check if a task object exists with the given id
            if (existingTaskObject == null) {
                // throw corresponding exception
                throw new InvalidRequestAttributeValueException("NO TASK FOUND WITH GIVEN ID: " + taskId);
            }

            // delete existing task object
            this.taskRepository.deleteTaskById(taskId);

            // return the deleted task object mapped as an instance of TaskDataResponseDTO
            return new TaskDataResponseDTO(existingTaskObject.getTaskId(), existingTaskObject.getTaskTitle(),
                    existingTaskObject.getTaskDescription(), existingTaskObject.getTaskCreatedOn());
        } else {
            // throw corresponding exception
            throw new InvalidRequestAttributeValueException("INVALID TASK ID: " + taskId);
        }
    }
}
