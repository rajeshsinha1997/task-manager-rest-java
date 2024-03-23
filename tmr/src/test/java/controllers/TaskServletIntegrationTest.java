package controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dtos.generic.GenericResponseDTO;
import dtos.request.TaskPatchRequestDTO;
import dtos.response.TaskDataResponseDTO;
import exceptions.ResourceNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import utilities.CommonServletUtility;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServletIntegrationTest extends TaskServletTestBase {

    /**
     * tests the doGet method to ensure it properly handles GET requests
     */
    @Test
    void doGet() throws ServletException {
        servlet.doGet(requestMock, responseMock);

        // verify
        verify(taskServiceMock).getAllTasks();

        // assert
        assertFalse(responseWriter.toString().isEmpty());
    }

    /**
     * tests the doGet method to check it returns a success response with task data
     */
    @Test
    void getWithSuccess() throws Exception {
        List<TaskDataResponseDTO> tasks = List.of(new TaskDataResponseDTO("1", "T1", "Description 1", false, "D1"));
        when(taskServiceMock.getAllTasks()).thenReturn(tasks);

        servlet.doGet(requestMock, responseMock);

        // checking for correct response
        verify(responseMock).setStatus(HttpServletResponse.SC_OK);

        Type responseType = new TypeToken<GenericResponseDTO<List<TaskDataResponseDTO>>>() {
        }.getType();
        GenericResponseDTO<List<TaskDataResponseDTO>> successResponse = new Gson().fromJson(responseWriter.toString(),
                responseType);

        // assert
        assertNotNull(successResponse);
        assertEquals(1, successResponse.getResponseData().size());
        assertEquals("T1", successResponse.getResponseData().getFirst().getTaskTitle());
    }

    /**
     * tests handling GET request with a specific task ID, expecting a successful response
     */
    @Test
    void getByIdSuccess() throws Exception {
        String taskId = "existing-id";
        TaskDataResponseDTO task = new TaskDataResponseDTO(taskId, "Title", "Description", false, "CreatedOn");
        when(taskServiceMock.getTaskById(taskId)).thenReturn(task);

        // mocking the request
        when(requestMock.getPathInfo()).thenReturn("/" + taskId);

        servlet.doGet(requestMock, responseMock);

        // verifying the success status
        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * tests the doPost method to ensure it correctly handles POST requests by
     * creating new tasks
     */
    @Test
    void doPost() throws ServletException, IOException {
        String jsonData = "{\"task-title\":\"Test Task\", \"task-description\":\"This is a test task\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonData));
        when(requestMock.getReader()).thenReturn(reader);

        servlet.doPost(requestMock, responseMock);

        // verifying creation of a new task and that the response is not empty.
        verify(taskServiceMock).createNewTask(any());
        assertTrue(responseWriter.toString().isEmpty());
    }

    /**
     * tests deletion with a valid task ID, expecting a successful response
     */
    @Test
    void doDeleteValidTask() throws ServletException, IOException {
        // assume a valid task ID is provided in the path info
        String validTaskId = "valid-task-id";
        when(CommonServletUtility.getRequestUrlPathInfo(requestMock)).thenReturn("/" + validTaskId);
        servlet.doDelete(requestMock, responseMock);

        // verify that the deleteTaskById method of the taskService is called with the
        // correct task ID
        verify(taskServiceMock).deleteTaskById(validTaskId);

        // verify that the correct response status code has been set
        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * tests handling DELETE request for a non-existing task ID, expecting an error response
     */
    @Test
    void deleteNotFound() throws Exception {
        String nonExistingTaskId = "non-existing-id";

        // simulating an exception
        doThrow(new ResourceNotFoundException("No task found with given ID")).when(taskServiceMock)
                .deleteTaskById(nonExistingTaskId);

        when(requestMock.getPathInfo()).thenReturn("/" + nonExistingTaskId);

        servlet.doDelete(requestMock, responseMock);

        // checking that the correct error status is set
        verify(responseMock).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    /**
     * checks the successful execution of a PATCH request
     */
    @Test
    void doPatchSuccess() throws Exception {
        String taskId = "valid-task-id";
        TaskPatchRequestDTO patchRequestDTO = new TaskPatchRequestDTO("Updated Title", "Updated Description", true);
        TaskDataResponseDTO updatedTask = new TaskDataResponseDTO(taskId, "Updated Title", "Updated Description", true, "2024-03-17");

        // JSON body of the request
        String jsonRequestBody = new Gson().toJson(patchRequestDTO);
        BufferedReader reader = new BufferedReader(new StringReader(jsonRequestBody));

        // set up the HttpServletRequest mock
        when(requestMock.getPathInfo()).thenReturn("/" + taskId);
        when(requestMock.getReader()).thenReturn(reader);

        // set up the HttpServletResponse mock
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(responseMock.getWriter()).thenReturn(writer);

        // set up the TaskService mock
        when(taskServiceMock.updateTaskById(eq(taskId), any(TaskPatchRequestDTO.class))).thenReturn(updatedTask);

        // execute the doPatch method
        servlet.doPatch(requestMock, responseMock);
        writer.flush();

        // verify that the correct HTTP status code is set in the response
        verify(responseMock).setStatus(HttpServletResponse.SC_OK);

        // convert the StringWriter content into an object for assertion
        Type type = new TypeToken<GenericResponseDTO<TaskDataResponseDTO>>(){}.getType();
        GenericResponseDTO<TaskDataResponseDTO> responseDTO = new Gson().fromJson(stringWriter.toString(), type);

        // assertions
        assertNotNull(responseDTO);
        assertEquals("Updated Title", responseDTO.getResponseData().getTaskTitle());
        assertEquals("Updated Description", responseDTO.getResponseData().getTaskDescription());
        assertTrue(responseDTO.getResponseData().isTaskCompleted());
    }

    /**
     * tests the scenario where the specified resource ID does not exist
     */
    @Test
    void doPatchWithResourceNotFoundException() throws IOException {
        // simulate non-existing task ID in request path
        when(requestMock.getPathInfo()).thenReturn("/nonexistentTaskId");
        TaskPatchRequestDTO dummyDto = new TaskPatchRequestDTO("Title", "Description", true);
        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(new Gson().toJson(dummyDto))));
        when(taskServiceMock.updateTaskById(anyString(), any(TaskPatchRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("No task found with given ID"));
        servlet.doPatch(requestMock, responseMock);

        // assert that NOT FOUND status is returned
        verify(responseMock).setStatus(SC_NOT_FOUND);
    }
}
