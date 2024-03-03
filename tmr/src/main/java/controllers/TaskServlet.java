package controllers;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
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
    private final transient Gson gson = new Gson();

    @Override
    /**
     * method to process GET request to fetch all available tasks
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<TaskDataResponseDTO> tasks = this.taskService.getAllTasks();

            String jsonResponse = gson.toJson(tasks);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            resp.getWriter().write(jsonResponse);
        } catch (JsonSyntaxException | JsonIOException e) {
            CommonServletUtility.buildErrorResponse(resp, 500, e);
        }
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
