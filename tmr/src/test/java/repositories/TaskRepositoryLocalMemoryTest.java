package repositories;

import models.TaskModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskRepositoryLocalMemoryTest {

    private ITaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = new TaskRepositoryLocalMemory();
    }

    /**
     * tests adding a new task and retrieving it by ID.
     */
    @Test
    void addTaskAndRetrieveById() {
        String taskId = "1";
        TaskModel task = new TaskModel(null, null, null, null, null, false, false);
        task.setTaskId(taskId);
        task.setTaskTitle("Task 1");
        task.setTaskDescription("Description 1");
        task.setTaskCreatedOn("10-10-2020");
        task.setTaskLastUpdatedOn("10-10-2020");
        task.setTaskCompleted(false);
        task.setTaskDeleted(false);

        repository.addNewTask(task);

        TaskModel retrievedTask = repository.findTaskById(taskId);
        assertNotNull(retrievedTask);
        assertEquals(task.getTaskTitle(), retrievedTask.getTaskTitle());
        assertEquals(task.getTaskDescription(), retrievedTask.getTaskDescription());
    }


    @Test
    void whenFindAllTasks_thenAllTasksAreReturned() {
        TaskModel task1 = new TaskModel("1", "Task One", "Description One", "Date One", "Date One", false, false);
        TaskModel task2 = new TaskModel("2", "Task Two", "Description Two", "Date Two", "Date Two", false, false);
        repository.addNewTask(task1);
        repository.addNewTask(task2);

        List<TaskModel> retrievedTasks = repository.findAllTasks();

        assertEquals(2, retrievedTasks.size(), "Should return all tasks");
    }

}