package services;

import dtos.request.TaskPostRequestDTO;
import dtos.response.TaskDataResponseDTO;
import exceptions.InvalidRequestAttributeValueException;
import models.TaskModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repositories.ITaskRepository;
import utilities.CommonUtility;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TaskServiceTest {

    @Mock
    private ITaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * verifies successful task creation with provided details
     */
    @Test
    void createNewTask() {
        // create a DTO for the new task request
        TaskPostRequestDTO requestDTO = new TaskPostRequestDTO("Test Task", "This is a test task");
        String expectedId = UUID.randomUUID().toString(); // Simulated ID for the new task
        String expectedCreationDate = CommonUtility.getCurrentDateAndTimeStampString(); // Simulated creation date

        // mock the taskRepository
        when(taskRepository.findTaskById(anyString())).thenReturn(null);
        // mock the taskRepository
        doAnswer(invocation -> {
            TaskModel model = invocation.getArgument(0);
            model.setTaskId(expectedId);
            model.setTaskCreatedOn(expectedCreationDate);
            return null;
        }).when(taskRepository).addNewTask(any(TaskModel.class));

        TaskDataResponseDTO responseDTO = taskService.createNewTask(requestDTO);

        // assert
        assertNotNull(responseDTO, "The response DTO must not be null.");
        assertEquals(expectedId, responseDTO.getTaskId(), "The task ID in the response DTO must match the expected one.");
        assertEquals(requestDTO.getTaskTitle(), responseDTO.getTaskTitle(), "The task title must match the one provided in the request DTO.");
        assertEquals(requestDTO.getTaskDescription(), responseDTO.getTaskDescription(), "The task description must match the one provided in the request DTO.");
        assertEquals(expectedCreationDate, responseDTO.getTaskCreatedOn(), "The creation date in the response DTO must match the expected one.");

        // verify
        verify(taskRepository, times(1)).findTaskById(anyString());
        verify(taskRepository, times(1)).addNewTask(argThat(task ->
                task.getTaskTitle().equals(requestDTO.getTaskTitle()) &&
                        task.getTaskDescription().equals(requestDTO.getTaskDescription()) &&
                        task.getTaskId().equals(expectedId) &&
                        task.getTaskCreatedOn().equals(expectedCreationDate)
        ));
    }

    /**
     * tests retrieval of all tasks
     */
    @Test
    void getAllTasks() {
        // create a list of TaskModel objects
        List<TaskModel> taskModels = List.of(
                new TaskModel("task-1", "Task 1", "Description 1",
                        CommonUtility.getCurrentDateAndTimeStampString(), CommonUtility.getCurrentDateAndTimeStampString(),
                        false, false),
                new TaskModel("task-2", "Task 2", "Description 2",
                        CommonUtility.getCurrentDateAndTimeStampString(), CommonUtility.getCurrentDateAndTimeStampString(),
                        false, false)
        );
        when(taskRepository.findAllTasks()).thenReturn(taskModels);

        // call the method under test to get all tasks
        List<TaskDataResponseDTO> responseDTOS = taskService.getAllTasks();

        // assert
        assertNotNull(responseDTOS, "The list of response DTOs should not be null.");
        assertEquals(taskModels.size(), responseDTOS.size(), "The number of tasks returned should match the number of models.");
        assertEquals(taskModels.get(0).getTaskTitle(), responseDTOS.get(0).getTaskTitle(), "The task titles should match.");

        // verify
        verify(taskRepository, times(1)).findAllTasks();
    }

    /**
     * ensures exception thrown for invalid task data
     */
    @Test
    void createTaskInvalidData() {
        // test with null DTO
        assertThrows(InvalidRequestAttributeValueException.class, () -> taskService.createNewTask(null));

        // test with DTO having empty values
        TaskPostRequestDTO requestDTOWithEmptyValues = new TaskPostRequestDTO("", "");
        assertThrows(InvalidRequestAttributeValueException.class, () -> taskService.createNewTask(requestDTOWithEmptyValues));

        // verify that addNewTask was never called as the method should throw an exception before that
        verify(taskRepository, never()).addNewTask(any(TaskModel.class));
    }

    /**
     * verifies task ID uniqueness during creation
     */
    @Test
    void createTaskUniqueID() {
        // create a request DTO with task details
        TaskPostRequestDTO requestDTO = new TaskPostRequestDTO("Test Task", "This is a test task");
        AtomicInteger callCount = new AtomicInteger();

        when(taskRepository.findTaskById(anyString())).thenAnswer(invocation -> {
            if (callCount.getAndIncrement() == 0) {
                return new TaskModel(invocation.getArgument(0), "Existing Task",
                        "Existing Description", "2020-10-10", "2020-10-10", false, false);
            } else {
                return null;
            }
        });

        // attempt to create a new task with the given request DTO
        TaskDataResponseDTO responseDTO = taskService.createNewTask(requestDTO);

        // assert
        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getTaskId());
        // verify
        verify(taskRepository, atLeast(2)).findTaskById(anyString());
        verify(taskRepository).addNewTask(argThat(task -> task.getTaskId().equals(responseDTO.getTaskId())));
    }

    /**
     * tests that getTaskById returns the correct task
     */
    @Test
    void getById() {
        // set up a task ID and a TaskModel object for the test
        String taskId = UUID.randomUUID().toString();
        TaskModel expectedTask = new TaskModel(taskId, "Test Title", "Test Description", "2021-01-01", "2021-01-02", false, false);

        // mock taskRepository to return the expected object when called with the correct ID
        when(taskRepository.findTaskById(taskId)).thenReturn(expectedTask);

        // execute the method under test
        TaskDataResponseDTO result = taskService.getTaskById(taskId);

        // verify
        assertNotNull(result, "The result should not be null");
        assertEquals(taskId, result.getTaskId(), "The returned task ID should match the expected one");
        assertEquals("Test Title", result.getTaskTitle(), "The returned task title should match the expected one");

        // confirm
        verify(taskRepository, times(1)).findTaskById(taskId);
    }

    /**
     * tests getTaskById with a non-existing ID should return null
     */
    @Test
    void getNonExisting() {
        String nonExistingTaskId = UUID.randomUUID().toString();

        // mock taskRepository to return null for a non-existing ID
        when(taskRepository.findTaskById(nonExistingTaskId)).thenReturn(null);

        // execute the method under test and capture the exception
        Exception exception = assertThrows(InvalidRequestAttributeValueException.class, () -> taskService.getTaskById(nonExistingTaskId));

        // verify
        assertTrue(exception.getMessage().contains("NO TASK FOUND WITH GIVEN ID"));

        // confirm
        verify(taskRepository, times(1)).findTaskById(nonExistingTaskId);
    }

    /**
     * tests that getTaskById throws an InvalidRequestAttributeValueException for an invalid task ID
     */
    @Test
    void getTaskByInvalidId() {
        String invalidTaskId = "invalid-id"; // assuming this ID will fail validation

        // attempt to retrieve the task and verify that the correct exception is thrown with the expected message
        Exception exception = assertThrows(InvalidRequestAttributeValueException.class, () -> taskService.getTaskById(invalidTaskId));
        assertEquals("INVALID TASK ID: " + invalidTaskId, exception.getMessage());
    }

    /**
     * tests that deleteTaskById successfully removes a task
     */
    @Test
    void deleteById() {
        // set up an existing task ID for the test
        String taskId = UUID.randomUUID().toString();
        TaskModel taskModel = new TaskModel(taskId, "Task for Deletion", "This task should be deleted", "2021-01-01", "2021-01-02", false, false);

        // mock taskRepository.findTaskById to return a TaskModel object indicating the task exists
        when(taskRepository.findTaskById(taskId)).thenReturn(taskModel);

        // no need to mock the action of taskRepository.deleteTaskById(), just verify it is called
        // execute the method under test
        taskService.deleteTaskById(taskId);

        // confirm that the deleteTaskById method was called with the correct ID
        verify(taskRepository, times(1)).deleteTaskById(taskId);
    }

    /**
     * tests that deleting a task with a non-existing ID should fail
     */
    @Test
    void deleteNonExisting() {
        String nonExistingTaskId = UUID.randomUUID().toString();
        when(taskRepository.findTaskById(nonExistingTaskId)).thenReturn(null);

        // verify that the expected exception is thrown
        Exception exception = assertThrows(InvalidRequestAttributeValueException.class, () -> taskService.deleteTaskById(nonExistingTaskId));

        // verify that the message of the exception is as expected
        assertTrue(exception.getMessage().contains("NO TASK FOUND WITH GIVEN ID"));

        // verify that findTaskById was called but NOT deleteTaskById since the task does not exist
        verify(taskRepository, times(1)).findTaskById(nonExistingTaskId);
        verify(taskRepository, never()).deleteTaskById(nonExistingTaskId);
    }

    /**
     * tests that deleteTaskById throws an InvalidRequestAttributeValueException for an invalid task ID
     */
    @Test
    void deleteTaskByInvalidId() {
        String invalidTaskId = "invalid-id"; // use an ID known to fail the validation logic

        // attempt to delete the task and verify that the correct exception is thrown
        Exception exception = assertThrows(InvalidRequestAttributeValueException.class, () -> taskService.deleteTaskById(invalidTaskId));
        assertEquals("INVALID TASK ID: " + invalidTaskId, exception.getMessage());

        // verify that deleteTaskById was not called because the exception
        verify(taskRepository, never()).deleteTaskById(anyString());
    }
}