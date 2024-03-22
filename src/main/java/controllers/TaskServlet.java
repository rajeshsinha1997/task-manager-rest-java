package controllers;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import constants.ErrorMessage;
import dtos.request.TaskPatchRequestDTO;
import dtos.request.TaskPostRequestDTO;
import dtos.response.TaskDataResponseDTO;
import exceptions.BadRequestException;
import exceptions.ResourceNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.ServiceFactory;
import services.TaskService;
import utilities.CommonServletUtility;

@WebServlet(urlPatterns = "/tasks/*", name = "tasks")
/**
 * servlet class for processing requests and responses related to the task
 * objects
 */
public class TaskServlet extends HttpServlet {
    // create logger instance
    private static final Logger logger = LogManager.getLogger(TaskServlet.class);

    @Override
    /**
     * method to process GET requests and build corresponding responses
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        // store request method
        String requestMethod = req.getMethod();

        // log request
        TaskServlet.logger.info(requestMethod + " request received");
        TaskServlet.logger.debug(CommonServletUtility.getJsonFromObject(req));

        // get service instance from factory
        TaskServlet.logger.debug("requesting instance of service");
        TaskService taskService = ServiceFactory.getTaskServiceInstance();
        TaskServlet.logger.debug("received instance of service");

        try {
            // check if no path information has been provided along with the request
            if (CommonServletUtility.isRequestPathInformationBlank(req)) {
                TaskServlet.logger.info(requestMethod + " request path information is blank");

                // build success response with the result from calling the service function to
                // get list of all available tasks
                TaskServlet.logger.info("fetching all available tasks and building success response");
                CommonServletUtility.buildSuccessResponse(resp, 200, taskService.getAllTasks());
                TaskServlet.logger.info("finished building success response with all available tasks");
            } else {
                TaskServlet.logger.info(requestMethod + " request contains path information data - "
                        + CommonServletUtility.getRequestUrlPathInfo(req));

                // get task id from the path information received with the request
                TaskServlet.logger.debug("extracting task id from the path information of request " + requestMethod);
                String taskId = CommonServletUtility.getResourceIdFromRequestPathInformation(req);
                TaskServlet.logger.info(
                        "extracted task id from the path information of request " + requestMethod + " - " + taskId);

                // find task object with given id and build success response with the existing
                // task data
                TaskServlet.logger
                        .info("building response with task information corresponding to given task id - " + taskId);
                CommonServletUtility.buildSuccessResponse(resp, 200, taskService.getTaskById(taskId));
                TaskServlet.logger.info(
                        "finished building response with task information corresponding to given task id - " + taskId);
            }
        } catch (ResourceNotFoundException | BadRequestException e) {
            // call exception handler method
            TaskServlet.logger.error("error processing request - " + e);
            CommonServletUtility.buildApplicationExceptionResponse(e, resp);
        }
    }

    @Override
    /**
     * method to process POST requests and build corresponding responses
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            // check if the request url does not contain any path information
            if (CommonServletUtility.isRequestPathInformationBlank(req)) {
                // get mapped request body
                TaskPostRequestDTO postRequestDTO = CommonServletUtility.mapRequestBodyToObject(req,
                        TaskPostRequestDTO.class);

                // call service method to create a new task
                TaskDataResponseDTO responseData = ServiceFactory.getTaskServiceInstance()
                        .createNewTask(postRequestDTO);

                // build success response
                CommonServletUtility.buildSuccessResponse(resp, 201, responseData);
            } else {
                // throw corresponding exception
                throw new ResourceNotFoundException(ErrorMessage.INVALID_REQUEST_URL);
            }
        } catch (JsonSyntaxException | JsonIOException | IOException
                | BadRequestException | ResourceNotFoundException e) {
            // call exception handler method
            CommonServletUtility.buildApplicationExceptionResponse(e, resp);
        }
    }

    @Override
    /**
     * method to process DELETE requests and build corresponding response
     */
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // check if no path information is provided along with the request
            if (CommonServletUtility.isRequestPathInformationBlank(req)) {
                // throw corresponding exception
                throw new BadRequestException(ErrorMessage.TASK_ID_NOT_PROVIDED);
            }

            // call service method to delete existing task with the given id and build
            // success response with the deleted task object data
            CommonServletUtility.buildSuccessResponse(resp, 200,
                    ServiceFactory.getTaskServiceInstance()
                            .deleteTaskById(CommonServletUtility.getResourceIdFromRequestPathInformation(req)));
        } catch (BadRequestException | ResourceNotFoundException e) {
            // call exception handler method
            CommonServletUtility.buildApplicationExceptionResponse(e, resp);
        }
    }

    /**
     * method to process PATCH requests and build corresponding response
     * 
     * @param req  - instance of HttpServletRequest class
     * @param resp - instance of HttpServletResponse class
     */
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // check if no path information is provided along with the request
            if (CommonServletUtility.isRequestPathInformationBlank(req)) {
                // throw corresponding exception
                throw new BadRequestException(ErrorMessage.TASK_ID_NOT_PROVIDED);
            }

            // get mapped request body
            TaskPatchRequestDTO patchRequestDTO = CommonServletUtility.mapRequestBodyToObject(req,
                    TaskPatchRequestDTO.class);

            // call service method to update the existing task with the given id and build
            // success response with the updated task object data
            CommonServletUtility.buildSuccessResponse(resp, 200,
                    ServiceFactory.getTaskServiceInstance().updateTaskById(
                            CommonServletUtility.getResourceIdFromRequestPathInformation(req),
                            patchRequestDTO));
        } catch (BadRequestException | JsonSyntaxException | JsonIOException | IOException
                | ResourceNotFoundException e) {
            // call exception handler method
            CommonServletUtility.buildApplicationExceptionResponse(e, resp);
        }
    }

    @Override
    /**
     * override the service method of HttpServlet class so that we can forward any
     * PATCH request to our own custom implementation of the PATCH endpoint. For
     * rest of the request methods we will call the super method of HttpServlet
     * class, which will handle the processing afterwards.
     */
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // get request method name
        String requestMethod = req.getMethod();

        // check if the request method name is PATCH
        if (requestMethod.equals("PATCH")) {
            // make call to the custom implemented PATCH method
            this.doPatch(req, resp);
        } else {
            // make call to the service method of HttpServlet class
            super.service(req, resp);
        }
    }

}