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
     * tests JSON deserialization to a POJO.
     */
    @Test
    void deserializeJson() throws IOException {
        String jsonBody = "{\"task-title\":\"New Task\", \"task-description\":\"Description of the new task\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonBody));
        when(mockRequest.getReader()).thenReturn(reader);

        TaskPostRequestDTO dto = CommonServletUtility.mapRequestBodyToObject(mockRequest, TaskPostRequestDTO.class);
        assertNotNull(dto);
        assertEquals("New Task", dto.getTaskTitle(), "Task title does not match.");
        assertEquals("Description of the new task", dto.getTaskDescription(), "Task description does not match.");
    }

    /**
     * tests exception throwing for malformed JSON input.
     */
    @Test
    void malformedJsonException() {
        String malformedJson = "{\"taskTitle:\"Incomplete Task\"";
        BufferedReader reader = new BufferedReader(new StringReader(malformedJson));
        try {
            when(mockRequest.getReader()).thenReturn(reader);

            assertThrows(JsonSyntaxException.class, () ->
                            CommonServletUtility.mapRequestBodyToObject(mockRequest, TaskPostRequestDTO.class),
                    "Should throw an exception for malformed JSON");
        } catch (IOException e) {
            fail("Should not throw IOException in this test.", e);
        }
    }

    /**
     * tests error response building.
     */
    @Test
    void buildErrorResponse() {
        Throwable throwable = new RuntimeException("Error message");
        CommonServletUtility.buildErrorResponse(mockResponse, HttpServletResponse.SC_BAD_REQUEST, throwable);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertTrue(responseWriter.toString().contains("Error message"));
    }

    /**
     * tests success response building.
     */
    @Test
    void buildSuccessResponse() {
        TaskDataResponseDTO dto = new TaskDataResponseDTO(null, null, null, null);
        dto.setTaskId("2");
        dto.setTaskTitle("New Task");
        dto.setTaskDescription("Description of the new task");
        dto.setTaskCreatedOn("01-01-2020");
        CommonServletUtility.buildSuccessResponse(mockResponse, HttpServletResponse.SC_OK, dto);
        verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        assertTrue(responseWriter.toString().contains("New Task"));
    }
}
