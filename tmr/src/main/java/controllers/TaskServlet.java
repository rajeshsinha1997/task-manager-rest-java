package controllers;

import java.io.IOException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import dtos.request.TaskPostRequestDTO;
import dtos.response.TaskDataResponseDTO;
import exceptions.InvalidRequestAttributeValueException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.TaskService;
import utilities.CommonServletUtility;

@WebServlet(urlPatterns = "/tasks", name = "tasks")
/**
 * servlet class for processing requests and responses related to the task
 * objects
 */
public class TaskServlet extends HttpServlet {
    // create instance of TaskService
    private final transient TaskService taskService = new TaskService();

    @Override
    /**
     * method to process GET requests and build corresponding responses
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        // build success response with the result from calling the service function to
        // get list of all available tasks
        CommonServletUtility.buildSuccessResponse(resp, 200, this.taskService.getAllTasks());
    }

    @Override
    /**
     * method to process POST requests and build corresponding responses
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            // get mapped request body
            TaskPostRequestDTO postRequestDTO = CommonServletUtility.mapRequestBodyToObject(req,
                    TaskPostRequestDTO.class);

            // call service method to create a new task
            TaskDataResponseDTO responseData = this.taskService.createNewTask(postRequestDTO);

            // build success response
            CommonServletUtility.buildSuccessResponse(resp, 201, responseData);
        } catch (JsonSyntaxException | JsonIOException | IOException
                | InvalidRequestAttributeValueException e) {
            // handle exceptions
            CommonServletUtility.buildErrorResponse(resp,
                    (e instanceof InvalidRequestAttributeValueException || e instanceof JsonSyntaxException) ? 400
                            : 500,
                    e);
        }
    }

}
