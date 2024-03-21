package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import constants.ErrorMessage;
import dtos.generic.GenericErrorResponseDTO;
import dtos.generic.GenericResponseDTO;
import dtos.request.TaskPatchRequestDTO;
import dtos.response.TaskDataResponseDTO;
import exceptions.BadRequestException;
import exceptions.ResourceNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import services.ServiceFactory;
import services.TaskService;
import utilities.CommonServletUtility;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServletTest {

    private TaskServletTest.TestableTaskServlet servlet;
    private TaskService taskServiceMock;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private StringWriter responseWriter;

    /**
     * inner class that extends TaskServlet to expose the protected methods
     */
    private static class TestableTaskServlet extends TaskServlet {
        private boolean doPatchCalled = false;
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
            super.doGet(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
            super.doPost(req, resp);
        }
        @Override
        protected void doPatch(HttpServletRequest req, HttpServletResponse resp) {
            super.doPatch(req, resp);
            doPatchCalled = true;
        }

        public boolean isDoPatchCalled() {
            return doPatchCalled;
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        servlet = new TaskServletTest.TestableTaskServlet();
        taskServiceMock = mock(TaskService.class);
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();

        // set service factory to use the mock service
        ServiceFactory.setTaskServiceInstance(taskServiceMock);

        when(responseMock.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

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
     * tests handling GET request with a specific task ID, expecting a successful
     * response
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
     * tests handling GET request with invalid URL, expecting a 404 response
     */
    @Test
    void getInvalidUrl() throws Exception {
        // simulating a request
        when(requestMock.getPathInfo()).thenReturn("/invalid/url");

        servlet.doGet(requestMock, responseMock);

        // verifying that a 404 status is correctly set for invalid URLs
        verify(responseMock).setStatus(SC_BAD_REQUEST);
    }

    /**
     * tests handling of an invalid task ID during a GET request
     */
    @Test
    void doGetBadRequestException() throws ServletException {
        // assume the request path info is an invalid task ID
        String invalidTaskId = "invalid-id";
        when(CommonServletUtility.getRequestUrlPathInfo(requestMock)).thenReturn("/" + invalidTaskId);

        // simulate that taskService.getTaskById will throw an exception when passed an
        // invalid ID
        when(taskServiceMock.getTaskById(invalidTaskId))
                .thenThrow(new BadRequestException("Invalid task ID"));

        // execute doGet
        servlet.doGet(requestMock, responseMock);

        // verify that the correct response status code has been set
        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
     * tests doPost method handling invalid request data and responding with error
     */
    @Test
    void doPostInvalidRequest() throws ServletException, IOException {
        String jsonData = "{\"task-title\":\"\", \"task-description\":\"This is a test task\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonData));
        when(requestMock.getReader()).thenReturn(reader);

        // simulating an exception
        doThrow(new BadRequestException("Invalid task title")).when(taskServiceMock)
                .createNewTask(any());

        servlet.doPost(requestMock, responseMock);

        verify(responseMock).setStatus(SC_BAD_REQUEST);

        PrintWriter writer = new PrintWriter(responseWriter);
        writer.flush();
        String responseContent = responseWriter.toString();
        assertTrue(responseContent.contains("Invalid task title"));

        GenericErrorResponseDTO errorResponse = new Gson().fromJson(responseContent,
                GenericErrorResponseDTO.class);
        assertEquals("Invalid task title", errorResponse.getErrorMessage());
    }

    /**
     * tests handling of malformed JSON in POST requests, expecting a 400 response
     */
    @Test
    void doPostWithJsonSyntaxException() throws ServletException, IOException {
        // simulate a malformed JSON body that will cause a JsonSyntaxException
        String jsonData = "{\"task-title\": \"Task Title\", \"task-description\":";
        BufferedReader reader = new BufferedReader(new StringReader(jsonData));
        when(requestMock.getReader()).thenReturn(reader);

        // simulate a JsonSyntaxException being thrown when processing the request body
        doThrow(new JsonSyntaxException("Malformed JSON")).when(taskServiceMock).createNewTask(any());

        // execute doPost
        servlet.doPost(requestMock, responseMock);

        // verify that the response status code has been set to SC_BAD_REQUEST
        verify(responseMock).setStatus(SC_BAD_REQUEST);
    }

    /**
     * tests doPost method's error handling for JsonIOException
     */
    @Test
    void doPostWithJsonIOException() throws IOException, ServletException {
        // simulating a JsonIOException
        when(requestMock.getReader()).thenThrow(new JsonIOException("IO error reading JSON"));

        servlet.doPost(requestMock, responseMock);

        // expecting the servlet to set the internal server error status
        verify(responseMock).setStatus(SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * tests handling POST request with invalid URL, expecting a 404 response
     */
    @Test
    void postInvalidUrl() throws Exception {
        // simulating a POST request
        when(requestMock.getPathInfo()).thenReturn("/some/unexpected/path");

        servlet.doPost(requestMock, responseMock);

        // ensuring that a 404 status is set
        verify(responseMock).setStatus(SC_NOT_FOUND);
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
     * tests handling DELETE request for a non-existing task ID, expecting an
     * error
     * response
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
     * tests deletion without path info, expecting a BAD_REQUEST response
     */
    @Test
    void deleteNoPathInfoTest() throws ServletException, IOException {
        // Simulate no path information
        when(requestMock.getPathInfo()).thenReturn("");
        servlet.doDelete(requestMock, responseMock);

        // verify that the correct response status code has been set to SC_BAD_REQUEST
        verify(responseMock).setStatus(SC_BAD_REQUEST);
        assertTrue(responseWriter.toString().contains("A TASK ID WAS NOT PROVIDED"));
    }

    /**
     * tests deletion with extra path segments, expecting a NOT_FOUND response
     */
    @Test
    void deleteExtraPathSegmentsTest() throws ServletException, IOException {
        // simulate path information
        when(requestMock.getPathInfo()).thenReturn("/task-id/additional-segment");
        servlet.doDelete(requestMock, responseMock);

        // verify that the correct response status code has been set to SC_NOT_FOUND
        verify(responseMock).setStatus(SC_BAD_REQUEST);

        assertTrue(responseWriter.toString().contains("INVALID REQUEST URL"));
    }

    /**
     * tests deletion with an invalid URL, expecting a 404 error response
     */
    @Test
    void deleteInvalidUrlTest() throws Exception {
        // set up mock to simulate an invalid URL
        when(CommonServletUtility.getRequestUrlPathInfo(requestMock)).thenReturn("/invalid/url/extra/segment");
        servlet.doDelete(requestMock, responseMock);

        // verify that the response has the HTTP 404 status
        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        // verify that buildErrorResponse is called with the correct status and
        // exception
        verify(responseMock).getWriter(); //
        String responseContent = responseWriter.toString();
        assertTrue(responseContent.contains("INVALID REQUEST URL"));
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
     * tests the scenario where no path information is provided in the request
     */
    @Test
    void doPatchNoPathInfoTest() {
        // simulate no path information in the request
        when(requestMock.getPathInfo()).thenReturn("");
        servlet.doPatch(requestMock, responseMock);

        verify(responseMock).setStatus(SC_BAD_REQUEST);
        String responseContent = responseWriter.toString();
        assertTrue(responseContent.contains(ErrorMessage.TASK_ID_NOT_PROVIDED));
    }

    /**
     * tests the scenario where the request leads to a BadRequestException
     */
    @Test
    void doPatchWithBadRequestException() {
        // simulate request path leading to a BadRequestException
        when(requestMock.getPathInfo()).thenReturn("");
        servlet.doPatch(requestMock, responseMock);

        verify(responseMock).setStatus(SC_BAD_REQUEST);
    }

    /**
     * tests the scenario where the request body contains invalid JSON
     */
    @Test
    void doPatchWithJsonSyntaxException() throws IOException {
        // simulate invalid JSON in the request body
        when(requestMock.getPathInfo()).thenReturn("/validTaskId");
        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader("{invalidJson:")));
        servlet.doPatch(requestMock, responseMock);

        verify(responseMock).setStatus(SC_BAD_REQUEST);
    }

    /**
     * tests the scenario where reading the request body leads to a JsonIOException
     */
    @Test
    void doPatchWithJsonIOException() throws IOException {
        // simulate JsonIOException when attempting to read the request body
        when(requestMock.getPathInfo()).thenReturn("/validTaskId");
        when(requestMock.getReader()).thenThrow(new JsonIOException("IO error"));
        servlet.doPatch(requestMock, responseMock);

        verify(responseMock).setStatus(SC_INTERNAL_SERVER_ERROR);
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

    /**
     * test DoPatch call
     */
    @Test
    void whenPatchRequest_thenDoPatchIsCalled() throws ServletException, IOException {
        when(requestMock.getMethod()).thenReturn("PATCH");
        servlet.service(requestMock, responseMock);
        assertTrue(servlet.isDoPatchCalled());
    }
}