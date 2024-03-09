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
     * verifies successful task creation with provided details.
     */
    @Test
    void createNewTask() {
        TaskPostRequestDTO requestDTO = new TaskPostRequestDTO("Test Task", "This is a test task");
        String expectedId = UUID.randomUUID().toString();
        String expectedCreationDate = CommonUtility.getCurrentDateAndTimeStampString();

        when(taskRepository.findTaskById(anyString())).thenReturn(null);
        doAnswer(invocation -> {
            TaskModel model = invocation.getArgument(0);
            model.setTaskId(expectedId);
            model.setTaskCreatedOn(expectedCreationDate);
            return null;
        }).when(taskRepository).addNewTask(any(TaskModel.class));

        TaskDataResponseDTO responseDTO = taskService.createNewTask(requestDTO);

        assertNotNull(responseDTO, "The response DTO must not be null.");
        assertEquals(expectedId, responseDTO.getTaskId(), "The task ID in the response DTO must match the expected one.");
        assertEquals(requestDTO.getTaskTitle(), responseDTO.getTaskTitle(), "The task title must match the one provided in the request DTO.");
        assertEquals(requestDTO.getTaskDescription(), responseDTO.getTaskDescription(), "The task description must match the one provided in the request DTO.");
        assertEquals(expectedCreationDate, responseDTO.getTaskCreatedOn(), "The creation date in the response DTO must match the expected one.");
        
        verify(taskRepository, times(1)).findTaskById(anyString());
        verify(taskRepository, times(1)).addNewTask(argThat(task ->
                task.getTaskTitle().equals(requestDTO.getTaskTitle()) &&
                        task.getTaskDescription().equals(requestDTO.getTaskDescription()) &&
                        task.getTaskId().equals(expectedId) &&
                        task.getTaskCreatedOn().equals(expectedCreationDate)
        ));
    }

    /**
     * tests retrieval of all tasks.
     */
    @Test
    void getAllTasks() {
        List<TaskModel> taskModels = List.of(
                new TaskModel("task-1", "Task 1", "Description 1",
                        CommonUtility.getCurrentDateAndTimeStampString(), CommonUtility.getCurrentDateAndTimeStampString(), false, false),
                new TaskModel("task-2", "Task 2", "Description 2",
                        CommonUtility.getCurrentDateAndTimeStampString(), CommonUtility.getCurrentDateAndTimeStampString(), false, false)
        );
        when(taskRepository.findAllTasks()).thenReturn(taskModels);

        List<TaskDataResponseDTO> responseDTOS = taskService.getAllTasks();

        assertNotNull(responseDTOS);
        assertEquals(taskModels.size(), responseDTOS.size());
        assertEquals(taskModels.get(0).getTaskTitle(), responseDTOS.get(0).getTaskTitle());

        verify(taskRepository, times(1)).findAllTasks();
    }

    /**
     * ensures exception thrown for invalid task data.
     */
    @Test
    void createTaskInvalidData() {
        // Arrange
        TaskPostRequestDTO requestDTO = new TaskPostRequestDTO("", "");

        assertThrows(InvalidRequestAttributeValueException.class, () -> taskService.createNewTask(requestDTO));

        verify(taskRepository, never()).addNewTask(any(TaskModel.class));
    }

    /**
     * verifies task ID uniqueness during creation.
     */
    @Test
    void createTaskUniqueID() {
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

        TaskDataResponseDTO responseDTO = taskService.createNewTask(requestDTO);

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getTaskId());

        verify(taskRepository, atLeast(2)).findTaskById(anyString());
        verify(taskRepository).addNewTask(argThat(task -> task.getTaskId().equals(responseDTO.getTaskId())));
    }
}