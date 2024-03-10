package controllers;

import java.io.IOException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import dtos.request.TaskPostRequestDTO;
import dtos.response.TaskDataResponseDTO;
import exceptions.InvalidRequestAttributeValueException;
import exceptions.InvalidRequestUrlException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.TaskService;
import utilities.CommonServletUtility;

@WebServlet(urlPatterns = "/tasks/*", name = "tasks")
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
        try {
            // get request path information
            String requestPathInformation = CommonServletUtility.getRequestUrlPathInfo(req);

            // check if no path information is provided along with the request
            if (requestPathInformation.isBlank()) {
                // build success response with the result from calling the service function to
                // get list of all available tasks
                CommonServletUtility.buildSuccessResponse(resp, 200, this.taskService.getAllTasks());
            } else {
                // get path information data array
                String[] pathInformationDataArray = CommonServletUtility
                        .getRequestPathInformationDataAsArray(requestPathInformation);

                // check if the path information data array contains more than one element
                if (pathInformationDataArray.length > 1) {
                    // throw corresponding exception
                    throw new InvalidRequestUrlException("THE REQUESTED RESOURCE IS NOT AVAILABLE YET");
                }

                // find task object with given id and build success response with the existing
                // task data
                CommonServletUtility.buildSuccessResponse(resp, 200,
                        this.taskService.getTaskById(pathInformationDataArray[0]));
            }
        } catch (InvalidRequestUrlException e) {
            // build error response with status code as 404
            CommonServletUtility.buildErrorResponse(resp, 404, e);
        } catch (InvalidRequestAttributeValueException e) {
            // build error response with status code as 400
            CommonServletUtility.buildErrorResponse(resp, 400, e);
        }
    }

    @Override
    /**
     * method to process POST requests and build corresponding responses
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            // check if the request url contains any path information
            if (!CommonServletUtility.getRequestUrlPathInfo(req).isBlank()) {
                // throw corresponding exception
                throw new InvalidRequestUrlException("INVALID REQUEST URL");
            }

            // get mapped request body
            TaskPostRequestDTO postRequestDTO = CommonServletUtility.mapRequestBodyToObject(req,
                    TaskPostRequestDTO.class);

            // call service method to create a new task
            TaskDataResponseDTO responseData = this.taskService.createNewTask(postRequestDTO);

            // build success response
            CommonServletUtility.buildSuccessResponse(resp, 201, responseData);
        } catch (JsonSyntaxException | JsonIOException | IOException
                | InvalidRequestAttributeValueException | InvalidRequestUrlException e) {
            // check if the exception object is an instance of
            // InvalidRequestAttributeValueException or JsonSyntaxException
            if (e instanceof InvalidRequestAttributeValueException || e instanceof JsonSyntaxException) {
                // build error response with status code as 400
                CommonServletUtility.buildErrorResponse(resp, 400, e);
            }
            // else check if the exception object is an instance of
            // InvalidRequestUrlException
            else if (e instanceof InvalidRequestUrlException) {
                // build error response with status code as 404
                CommonServletUtility.buildErrorResponse(resp, 404, e);
            }
            // else build error response with status code as 500
            else {
                CommonServletUtility.buildErrorResponse(resp, 500, e);
            }
        }
    }

    @Override
    /**
     * method to process DELETE requests and build corresponding response
     */
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // get request path information
            String requestPathInformation = CommonServletUtility.getRequestUrlPathInfo(req);

            // check if no path information is provided along with the request
            if (requestPathInformation.isBlank()) {
                // throw corresponding exception
                throw new InvalidRequestAttributeValueException("A VALID TASK ID WAS NOT PROVIDED");
            }

            // get path information data array
            String[] pathInformationDataArray = CommonServletUtility
                    .getRequestPathInformationDataAsArray(requestPathInformation);

            // check if the path information data array contains more than one element
            if (pathInformationDataArray.length > 1) {
                // throw corresponding exception
                throw new InvalidRequestUrlException("THE REQUESTED RESOURCE IS NOT AVAILABLE YET");
            }

            // call service method to delete existing task with the given id and build
            // success response with the deleted task object data
            CommonServletUtility.buildSuccessResponse(resp, 200,
                    this.taskService.deleteTaskById(pathInformationDataArray[0]));
        } catch (InvalidRequestAttributeValueException e) {
            // build error response with status code as 400
            CommonServletUtility.buildErrorResponse(resp, 400, e);
        } catch (InvalidRequestUrlException e) {
            // build error response with status code as 404
            CommonServletUtility.buildErrorResponse(resp, 404, e);
        }
    }

}
