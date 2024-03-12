package utilities;

import com.google.gson.JsonSyntaxException;
import dtos.request.TaskPostRequestDTO;
import dtos.response.TaskDataResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommonServletUtilityTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        responseWriter = new StringWriter();
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    /**
     * tests JSON deserialization to a POJO
     */
    @Test
    void deserializeJson() throws IOException {
        // sample JSON body to test deserialization
        String jsonBody = "{\"task-title\":\"New Task\", \"task-description\":\"Description of the new task\"}";

        // simulating reading from a HttpServletRequest
        BufferedReader reader = new BufferedReader(new StringReader(jsonBody));
        when(mockRequest.getReader()).thenReturn(reader);

        // performing the deserialization
        TaskPostRequestDTO dto = CommonServletUtility.mapRequestBodyToObject(mockRequest, TaskPostRequestDTO.class);

        assertNotNull(dto);
        assertEquals("New Task", dto.getTaskTitle(), "Task title does not match.");
        assertEquals("Description of the new task", dto.getTaskDescription(), "Task description does not match.");
    }

    /**
     * tests exception throwing for malformed JSON input
     */
    @Test
    void malformedJsonException() {
        // malformed JSON input
        String malformedJson = "{\"taskTitle:\"Incomplete Task\"";
        BufferedReader reader = new BufferedReader(new StringReader(malformedJson));
        try {
            when(mockRequest.getReader()).thenReturn(reader);

            // asserts that an exception is thrown for malformed JSON
            assertThrows(JsonSyntaxException.class, () ->
                            CommonServletUtility.mapRequestBodyToObject(mockRequest, TaskPostRequestDTO.class),
                    "Should throw an exception for malformed JSON");
        } catch (IOException e) {
            fail("Should not throw IOException in this test.", e);
        }
    }

    /**
     * tests error response building
     */
    @Test
    void buildErrorResponse() {
        Throwable throwable = new RuntimeException("Error message");
        CommonServletUtility.buildErrorResponse(mockResponse, HttpServletResponse.SC_BAD_REQUEST, throwable);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertTrue(responseWriter.toString().contains("Error message"));
    }

    /**
     * tests success response building
     */
    @Test
    void buildSuccessResponse() {
        // DTO to send in the response
        TaskDataResponseDTO dto = new TaskDataResponseDTO(null, null, null, null);
        dto.setTaskId("2");
        dto.setTaskTitle("New Task");
        dto.setTaskDescription("Description of the new task");
        dto.setTaskCreatedOn("01-01-2023");

        // building the success response
        CommonServletUtility.buildSuccessResponse(mockResponse, HttpServletResponse.SC_OK, dto);
        verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        assertTrue(responseWriter.toString().contains("New Task"));
    }

    /**
     * verifies buildErrorResponse when an IOException occurs
     */
    @Test
    void buildErrorResponseIOException() throws IOException {
        Throwable error = new RuntimeException("Error occurred");
        when(mockResponse.getWriter()).thenThrow(IOException.class);

        CommonServletUtility.buildErrorResponse(mockResponse, HttpServletResponse.SC_BAD_REQUEST, error);

        // assert
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    /**
     * tests retrieval of request URL path info when no path is provided
     */
    @Test
    void getRequestUrlPathInfoNoPath() {
        when(mockRequest.getPathInfo()).thenReturn(null);

        String result = CommonServletUtility.getRequestUrlPathInfo(mockRequest);
        assertTrue(result.isEmpty(), "Expected an empty string for null path info");
    }

    /**
     * verifies that getRequestUrlPathInfo method trims leading and trailing slashes
     */
    @Test
    void getRequestUrlPathInfoTrimSlashes() {
        // test with leading slash
        when(mockRequest.getPathInfo()).thenReturn("/test");
        assertEquals("test", CommonServletUtility.getRequestUrlPathInfo(mockRequest), "Should trim leading slash");

        // test with trailing slash
        when(mockRequest.getPathInfo()).thenReturn("test/");
        assertEquals("test", CommonServletUtility.getRequestUrlPathInfo(mockRequest), "Should trim trailing slash");

        // test with leading and trailing slash
        when(mockRequest.getPathInfo()).thenReturn("/test/");
        assertEquals("test", CommonServletUtility.getRequestUrlPathInfo(mockRequest), "Should trim leading and trailing slash");
    }

    /**
     * tests retrieval of valid request URL path info
     */
    @Test
    void getRequestUrlPathInfoValidPath() {
        // sample path info
        when(mockRequest.getPathInfo()).thenReturn("/12345");

        // converting path info into an array
        String result = CommonServletUtility.getRequestUrlPathInfo(mockRequest);
        assertEquals("12345", result, "Expected path info to match");
    }

    /**
     * tests conversion of request path information into an array
     */
    @Test
    void getRequestPathInformationDataAsArray() {
        String pathInfo = "12345/67890";
        String[] result = CommonServletUtility.getRequestPathInformationDataAsArray(pathInfo);

        // assertions to verify correct parsing
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("12345", result[0]);
        assertEquals("67890", result[1]);
    }

    /**
     * verifies that the getRequestUrlPathInfo method returns the correct string
     * when the path info contains multiple sections
     */
    @Test
    void getRequestUrlPathInfoMultipleSections() {
        when(mockRequest.getPathInfo()).thenReturn("/section1/section2/section3");
        assertEquals("section1/section2/section3", CommonServletUtility.getRequestUrlPathInfo(mockRequest), "Should return correct path info with multiple sections");
    }

    /**
     * verifies that getRequestUrlPathInfo does not alter a path info that is already correct
     */
    @Test
    void getRequestUrlPathInfoCorrectPath() {
        when(mockRequest.getPathInfo()).thenReturn("test/path");
        assertEquals("test/path", CommonServletUtility.getRequestUrlPathInfo(mockRequest), "Should not alter correct path info");
    }
}