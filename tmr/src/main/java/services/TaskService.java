package services;

import dtos.request.TaskPatchRequestDTO;
import dtos.request.TaskPostRequestDTO;
import dtos.response.TaskDataResponseDTO;
import exceptions.InvalidRequestAttributeValueException;
import models.TaskModel;
import repositories.ITaskRepository;
import repositories.TaskRepositoryLocalMemory;
import utilities.CommonServletUtility;
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
                .map(taskModel -> CommonServletUtility.buildTaskResponseObject(taskModel))
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
        return CommonServletUtility.buildTaskResponseObject(newTaskRecord);
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
                return CommonServletUtility.buildTaskResponseObject(existingTask);
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
            return CommonServletUtility.buildTaskResponseObject(existingTaskObject);
        } else {
            // throw corresponding exception
            throw new InvalidRequestAttributeValueException("INVALID TASK ID: " + taskId);
        }
    }

    /**
     * method to update an existing task object by it's corresponding id
     * 
     * @param taskId          - ID of the existing task record to be updated
     * @param updatedTaskData - updated task data to be stored in the database
     * @return updated task record as an instance of TaskDataResponseDTO
     * @throws InvalidRequestAttributeValueException - if the given id of the
     *                                               existing task record to update
     *                                               is invalid or no task record
     *                                               exists with the given id
     */
    public TaskDataResponseDTO updateTaskById(String taskId, TaskPatchRequestDTO updatedTaskData)
            throws InvalidRequestAttributeValueException {
        // check if the given task id is valid
        if (DataValidationUtility.isValidTaskId(taskId)) {
            // fetch the existing task object with the given id
            TaskModel existingTaskObject = this.taskRepository.findTaskById(taskId);

            // check if a task object exists with the given id
            if (existingTaskObject == null) {
                // throw corresponding exception
                throw new InvalidRequestAttributeValueException("NO TASK FOUND WITH GIVEN ID: " + taskId);
            }

            // check if an updated task title is provided
            if (updatedTaskData.getTaskTitle() != null) {
                // validate the updated task-title
                DataValidationUtility.validateTaskTitle(updatedTaskData.getTaskTitle(), false);

                // update task title in existing task record
                existingTaskObject.setTaskTitle(updatedTaskData.getTaskTitle());
            }

            // check if an updated task description is provided
            if (updatedTaskData.getTaskDescription() != null) {
                // validate the updated task-description
                DataValidationUtility.validateTaskDescription(updatedTaskData.getTaskDescription(), true);

                // update task description in existing task record
                existingTaskObject.setTaskDescription(updatedTaskData.getTaskDescription());
            }

            // check if an update task completion flag value is provided
            if (updatedTaskData.getIsTaskCompleted() != null) {
                // update task completion flag in existing task record
                existingTaskObject.setTaskCompleted(updatedTaskData.getIsTaskCompleted());
            }

            // call repository method to update the existing task record in database
            this.taskRepository.updateTaskById(taskId, existingTaskObject);

            // create and return task response object
            return CommonServletUtility.buildTaskResponseObject(existingTaskObject);
        } else {
            // throw corresponding exception
            throw new InvalidRequestAttributeValueException("INVALID TASK ID: " + taskId);
        }
    }
}
