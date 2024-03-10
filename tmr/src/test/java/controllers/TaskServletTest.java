package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import dtos.generic.GenericErrorResponseDTO;
import dtos.generic.GenericResponseDTO;
import dtos.response.TaskDataResponseDTO;
import exceptions.InvalidRequestAttributeValueException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.TaskService;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
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
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
            super.doGet(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
            super.doPost(req, resp);
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        servlet = new TaskServletTest.TestableTaskServlet();
        taskServiceMock = mock(TaskService.class);
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();

        // Injects the TaskService mock into the servlet using reflection.
        Field taskServiceField = TaskServlet.class.getDeclaredField("taskService");
        // Allows access to the private field
        taskServiceField.setAccessible(true);
        // Injects the mock
        taskServiceField.set(servlet, taskServiceMock);

        when(responseMock.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    /**
     * tests the doGet method to ensure it properly handles GET requests and interacts with the TaskService.
     */
    @Test
    void testDoGet() throws ServletException {
        servlet.doGet(requestMock, responseMock);

        verify(taskServiceMock).getAllTasks();
        assertFalse(responseWriter.toString().isEmpty());
    }

    /**
     * tests the doGet method to check it returns a success response with task data.
     */
    @Test
    void testDoGetReturnsSuccessResponseWithData() throws Exception {
        List<TaskDataResponseDTO> tasks = List.of(new TaskDataResponseDTO("1", "T1", "Descripción 1", "D1"));
        when(taskServiceMock.getAllTasks()).thenReturn(tasks);

        servlet.doGet(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK);

        Type responseType = new TypeToken<GenericResponseDTO<List<TaskDataResponseDTO>>>(){}.getType();
        GenericResponseDTO<List<TaskDataResponseDTO>> successResponse = new Gson().fromJson(responseWriter.toString(), responseType);
        assertNotNull(successResponse);
        assertEquals(1, successResponse.getResponseData().size());
        assertEquals("T1", successResponse.getResponseData().get(0).getTaskTitle());
    }

    /**
     * tests the doPost method to ensure it correctly handles POST requests by creating new tasks.
     */
    @Test
    void testDoPost() throws ServletException, IOException {
        String jsonData = "{\"task-title\":\"Test Task\", \"task-description\":\"This is a test task\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonData));
        when(requestMock.getReader()).thenReturn(reader);

        servlet.doPost(requestMock, responseMock);

        verify(taskServiceMock).createNewTask(any());
        assertFalse(!responseWriter.toString().isEmpty());
    }

    /**
     * tests doPost method handling invalid request data and responding with error.
     */
    @Test
    void testDoPostWithInvalidRequest() throws ServletException, IOException {
        String jsonData = "{\"task-title\":\"\", \"task-description\":\"This is a test task\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonData));
        when(requestMock.getReader()).thenReturn(reader);

        doThrow(new InvalidRequestAttributeValueException("Invalid task title")).when(taskServiceMock).createNewTask(any());

        servlet.doPost(requestMock, responseMock);

        verify(responseMock).setStatus(SC_BAD_REQUEST);

        PrintWriter writer = new PrintWriter(responseWriter);
        writer.flush();
        String responseContent = responseWriter.toString();
        assertTrue(responseContent.contains("Invalid task title"));

        GenericErrorResponseDTO errorResponse = new Gson().fromJson(responseContent, GenericErrorResponseDTO.class);
        assertEquals("Invalid task title", errorResponse.getErrorMessage());
    }

    /**
     * tests doPost method's error handling for JsonIOException.
     */
    @Test
    void testDoPostWithJsonIOException() throws IOException, ServletException {
        when(requestMock.getReader()).thenThrow(new JsonIOException("IO error reading JSON"));

        servlet.doPost(requestMock, responseMock);

        verify(responseMock).setStatus(SC_INTERNAL_SERVER_ERROR);
    }
}
