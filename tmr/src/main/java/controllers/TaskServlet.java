package controllers;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import dtos.generic.GenericResponseDTO;
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
public class TaskServlet extends HttpServlet {

    // create instance of TaskService
    private final transient TaskService taskService = new TaskService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            // get mapped request body
            TaskPostRequestDTO postRequestDTO = CommonServletUtility.mapRequestBodyToObject(req,
                    TaskPostRequestDTO.class);

            // call service method to create a new task
            TaskDataResponseDTO responseData = this.taskService.createNewTask(postRequestDTO);

            // set response metadata
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(201);

            // add new task data to the response
            resp.getWriter().write(new Gson().toJson(new GenericResponseDTO<TaskDataResponseDTO>(responseData)));
        } catch (JsonSyntaxException | JsonIOException | IOException | InvalidRequestAttributeValueException e) {
            // handle exceptions
            CommonServletUtility.buildErrorResponse(resp,
                    (e instanceof InvalidRequestAttributeValueException) ? 400 : 500, e);
        }
    }

}
