package repositories;

import models.TaskModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskRepositoryLocalMemoryTest {

    private ITaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = new TaskRepositoryLocalMemory();
    }

    /**
     * tests adding a new task and retrieving it by ID
     */
    @Test
    void addAndGetById() {
        String taskId = "1";
        TaskModel task = new TaskModel(taskId, "Task 1", "Description 1", "10-10-2023",
                "10-10-2023", false, false);

        // adding the task to the repository
        repository.addNewTask(task);

        // retrieving the task by ID
        TaskModel retrievedTask = repository.findTaskById(taskId);

        assertNotNull(retrievedTask);
        assertEquals(task.getTaskTitle(), retrievedTask.getTaskTitle());
        assertEquals(task.getTaskDescription(), retrievedTask.getTaskDescription());
    }

    /**
     * tests adding multiple tasks and retrieving all of them
     */
    @Test
    void addTasksAndRetrieveAll() {
        // creating and adding multiple tasks to the repository
        TaskModel task1 = new TaskModel("1", "Task One", "Description One",
                "10-10-2023", "10-10-2023", false, false);
        TaskModel task2 = new TaskModel("2", "Task Two", "Description Two",
                "11-10-2023", "11-10-2023", false, false);
        repository.addNewTask(task1);
        repository.addNewTask(task2);

        // retrieving all tasks from the repository
        List<TaskModel> retrievedTasks = repository.findAllTasks();

        assertEquals(2, retrievedTasks.size(), "Should return all tasks");
    }

    /**
     * tests deleting a task should exclude it from the results of findAll
     */
    @Test
    void deleteAndCheckAll() {
        // adding a task to the repository
        String taskId = "1";
        TaskModel task = new TaskModel(taskId, "Task 1", "Description 1", "10-10-2023", "10-10-2023", false, false);
        repository.addNewTask(task);

        // retrieving all tasks after deletion
        repository.deleteTaskById(taskId);

        List<TaskModel> tasksAfterDeletion = repository.findAllTasks();

        // asserting that the list of tasks is empty
        assertTrue(tasksAfterDeletion.isEmpty(), "Deleted task should not be included in the list of all tasks.");
    }

    /**
     * tests that findTaskById returns null for both non-existent and deleted tasks
     */
    @Test
    void findTaskByIdNullIfNotPresent() {
        // attempt to retrieve a non-existent task should return null
        String nonExistentTaskId = "non-existent";
        assertNull(repository.findTaskById(nonExistentTaskId), "Retrieving a non-existent task should return null.");

        // add a task and then mark it as deleted
        String taskId = "1";
        TaskModel task = new TaskModel(taskId, "Task 1", "Description 1", "10-10-2023", "10-10-2023", false, false);
        repository.addNewTask(task);
        repository.deleteTaskById(taskId);

        // attempt to retrieve the deleted task by ID should return null
        TaskModel deletedTask = repository.findTaskById(taskId);
        assertNull(deletedTask, "Retrieving a deleted task should return null.");
    }
}