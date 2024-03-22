package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import constants.ErrorMessage;
import dtos.generic.GenericErrorResponseDTO;
import exceptions.BadRequestException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import utilities.CommonServletUtility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import static jakarta.servlet.http.HttpServletResponse.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServletUnitTest extends TaskServletTestBase {

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

        // simulate that taskService.getTaskById will throw an exception when passed an invalid ID
        when(taskServiceMock.getTaskById(invalidTaskId))
                .thenThrow(new BadRequestException("Invalid task ID"));

        // execute doGet
        servlet.doGet(requestMock, responseMock);

        // verify that the correct response status code has been set
        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
     * tests deletion without path info, expecting a BAD_REQUEST response
     */
    @Test
    void deleteNoPathInfoTest() throws ServletException, IOException {
        // simulate no path information
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

        // verify that buildErrorResponse is called with the correct status and exception
        verify(responseMock).getWriter(); //
        String responseContent = responseWriter.toString();
        assertTrue(responseContent.contains("INVALID REQUEST URL"));
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
     * test DoPatch call
     */
    @Test
    void whenPatchRequest_thenDoPatchIsCalled() throws ServletException, IOException {
        when(requestMock.getMethod()).thenReturn("PATCH");
        servlet.service(requestMock, responseMock);
        assertTrue(servlet.isDoPatchCalled());
    }
}
