package utilities;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import constants.ErrorMessage;
import dtos.generic.GenericErrorResponseDTO;
import dtos.generic.GenericResponseDTO;
import dtos.response.TaskDataResponseDTO;
import exceptions.BadRequestException;
import exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.TaskModel;

public class CommonServletUtility {
    // create logger instance
    private static final Logger logger = LogManager.getLogger(CommonServletUtility.class);

    /**
     * private constructor to forbid instantiation
     */
    private CommonServletUtility() {
    }

    /**
     * method to map request body data to a corresponding instance of a POJO class
     * 
     * @param <T>         - type of the target mapped object
     * @param request     - instance of HttpServletRequest containing the
     *                    request-body data
     * @param targetClass - class of the target object which will be created and
     *                    populated
     * @return an instance of the target class with the required data mapped from
     *         the request-body
     * @throws JsonSyntaxException - if json is not a valid representation for an
     *                             object of type typeOf T
     * @throws JsonIOException     - if there was a problem reading from the Reader
     * @throws IOException         - if an input or output exception occurred
     */
    public static <T> T mapRequestBodyToObject(HttpServletRequest request, Class<T> targetClass)
            throws JsonSyntaxException, JsonIOException, IOException {
        // retrieves the body data from the instance of HttpServletRequest and map that
        // JSON data to an instance of the given target class
        return new Gson().fromJson(request.getReader(), targetClass);
    }

    /**
     * method to get JSON representatiion as String of a java object
     * 
     * @param sourceObject - java object to be converted to json string
     * @return json representation as String of the given Java object
     */
    public static String getJsonFromObject(Object sourceObject) {
        CommonServletUtility.logger
                .debug("creating JSON representation for an instance of " + sourceObject.getClass().getName());
        return new Gson().toJson(sourceObject);
    }

    /**
     * method to build error response
     * 
     * @param response        - instance of HttpServletResponse
     * @param errorStatusCode - status code to be set for the error response
     * @param e               - instance of the type throwable
     */
    public static void buildErrorResponse(HttpServletResponse response, int errorStatusCode, Throwable e) {
        // set response metadata
        CommonServletUtility.logger.info("adding error response metadata");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorStatusCode);
        CommonServletUtility.logger.debug("added error response metadata");

        try {
            // add error information data to the error response if the given error object is
            // not null
            CommonServletUtility.logger.debug("checking if any error object has been provided");
            if (e != null) {
                CommonServletUtility.logger.info("adding error information to the error response");
                response.getWriter()
                        .write(CommonServletUtility
                                .getJsonFromObject(new GenericErrorResponseDTO(e.getLocalizedMessage())));
                CommonServletUtility.logger.debug("added error information to the error response");
            }

            CommonServletUtility.logger.info("finished building error response");
        } catch (IOException exception) {
            CommonServletUtility.logger.error("error building error response - " + e);
        }
    }

    /**
     * method to build success response
     * 
     * @param <T>                - type of the response data object to be added with
     *                           the instance of HttpServletResponse
     * @param response           - instance of HttpServletResponse
     * @param responseStatusCode - status code to be set for the response
     * @param responseData       - response data to be added with the instance of
     *                           the HttpServletResponse
     */
    public static <T> void buildSuccessResponse(HttpServletResponse response, int responseStatusCode, T responseData) {
        // set response metadata
        CommonServletUtility.logger.info("adding success response metadada");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(responseStatusCode);
        CommonServletUtility.logger.debug("added success response metadata");

        try {
            // add response data to response if the given response data is not null
            CommonServletUtility.logger.debug("checking if success response data is null");
            if (responseData != null) {
                CommonServletUtility.logger.info("adding success response data to the response");
                CommonServletUtility.logger.debug(CommonServletUtility.getJsonFromObject(responseData));

                response.getWriter().write(
                        CommonServletUtility.getJsonFromObject(new GenericResponseDTO<T>(responseData)));
                CommonServletUtility.logger.debug("added success response data to the response");
            }

            CommonServletUtility.logger.info("finished building success response");
        } catch (IOException e) {
            CommonServletUtility.logger.error("error building success response - " + e);
        }
    }

    /**
     * method to get path information from the request object
     * 
     * @param request - an instance of HttpServletRequest as the request object
     * @return an empty string if no path information is available, else the path
     *         information string
     */
    public static String getRequestUrlPathInfo(HttpServletRequest request) {
        // get path information from the request url
        CommonServletUtility.logger.info("extracting path information from the request");
        String pathInformation = request.getPathInfo();
        CommonServletUtility.logger.debug("extracted path information from the request - " + pathInformation);

        // check if the path information was not present or is an empty string or only
        // contains a single '/'
        CommonServletUtility.logger.info("checking if the extracted path information is null or blank");
        if (pathInformation == null || pathInformation.isBlank() || pathInformation.trim().equals("/")) {
            // return an empty string value
            CommonServletUtility.logger.info("extracted path information is blank");
            return "";
        } else {
            // trim the leading and trailing white-spaces from the path information data
            CommonServletUtility.logger
                    .info("trimming the leading and trailing white-spaces from the path information");
            pathInformation = pathInformation.trim();
            CommonServletUtility.logger
                    .debug("trimmed the leading and trailing white-spaces from the path information");

            // remove any leading '/' from the path information
            CommonServletUtility.logger.info("removing any leading '/' from the path information");
            while (pathInformation.length() > 0 && pathInformation.charAt(0) == '/') {
                pathInformation = pathInformation.substring(1,
                        pathInformation.length());
            }
            CommonServletUtility.logger.debug("removed all leading '/' from the path information");

            // remove any trailing '/' from the path information
            CommonServletUtility.logger.info("removing any trailing '/' from the path information");
            while (pathInformation.length() > 0 && pathInformation.charAt(pathInformation.length() - 1) == '/') {
                pathInformation = pathInformation.substring(0,
                        pathInformation.length() - 1);
            }
            CommonServletUtility.logger.debug("removed trailing '/' from the path information");

            // return the path information string
            CommonServletUtility.logger
                    .info("returning formatted path information extracted from the request - "
                            + pathInformation.trim());
            return pathInformation.trim();
        }
    }

    /**
     * method to create request path information data array
     * 
     * @param requestPathInformationData - non-empty and not-null path information
     *                                   data received from the request
     * @return an String array which contains the path information data
     */
    public static String[] getRequestPathInformationDataAsArray(String requestPathInformationData) {
        // create and return the string array by splitting the request path information
        // data by '/'
        CommonServletUtility.logger.debug("creating an array from the path information data received with the request");
        return requestPathInformationData.split("/");
    }

    /**
     * method to create and return an instance of TaskDataResponseDTO from the task
     * record as an instance of TaskModel
     * 
     * @param taskData - an instance of TaskModel
     * @return an instance of TaskDataResponseDTO
     */
    public static TaskDataResponseDTO buildTaskResponseObject(TaskModel taskData) {
        // create a new instance of TaskDataResponseDTO and return it after populating
        // the required data
        CommonServletUtility.logger.debug("creating response dto object to return task data");
        CommonServletUtility.logger.debug(CommonServletUtility.getJsonFromObject(taskData));
        ;
        return new TaskDataResponseDTO(taskData.getTaskId(), taskData.getTaskTitle(), taskData.getTaskDescription(),
                taskData.isTaskCompleted(),
                taskData.getTaskCreatedOn());
    }

    /**
     * method to build error response on exception
     * 
     * @param e        - exception instance
     * @param response - instance of HttpServletResponse
     */
    public static void buildApplicationExceptionResponse(Exception e, HttpServletResponse response) {
        // check if the exception instance belongs to BadRequestException or
        // JsonSyntaxException
        if (e instanceof BadRequestException || e instanceof JsonSyntaxException) {
            // build error response with response status code as 400 - BAD REQUEST
            CommonServletUtility.logger.info("building a BAD-REQUEST error response with message - " + e.getMessage());
            CommonServletUtility.buildErrorResponse(response, 400, e);
        }
        // else check if the exception instance belongs to BadRequestException
        else if (e instanceof ResourceNotFoundException) {
            // build error response with response status code as 404 - NOT FOUND
            CommonServletUtility.logger.info("building a NOT-FOUND error response with message - " + e.getMessage());
            CommonServletUtility.buildErrorResponse(response, 404, e);
        }
        // else build error response with response status code as 500 - INTERNAL SERVER
        // ERROR
        else {
            CommonServletUtility.logger
                    .info("building a INTERNAL-SERVER-ERROR error response with message - " + e.getMessage());
            CommonServletUtility.buildErrorResponse(response, 500, e);
        }
    }

    /**
     * method to check if the path information received with the request is blank
     * 
     * @param request - instance of HttpServletRequest
     * @return true if the path information with the request is blank, false
     *         otherwise
     */
    public static boolean isRequestPathInformationBlank(HttpServletRequest request) {
        CommonServletUtility.logger.debug("checking if request contains any path information");
        return CommonServletUtility.getRequestUrlPathInfo(request).isBlank();
    }

    /**
     * method to get id of a resource from the path information received with a
     * request
     * 
     * @param request - instance of HttpServletRequest
     * @return id of the resource received with the request
     */
    public static String getResourceIdFromRequestPathInformation(HttpServletRequest request) {
        // get path information data array
        CommonServletUtility.logger.info("creating an array from the path information data received with the request");
        String[] pathInformationDataArray = CommonServletUtility
                .getRequestPathInformationDataAsArray(CommonServletUtility.getRequestUrlPathInfo(request));

        // check if the path information data array contains more than one element
        CommonServletUtility.logger.debug("checking if the path information array contains more than one element");
        if (pathInformationDataArray.length > 1) {
            // throw corresponding exception
            CommonServletUtility.logger.error("throw error as path information array contains more than one element");
            throw new BadRequestException(ErrorMessage.INVALID_REQUEST_URL);
        }

        // return the first entry of the path information data array
        CommonServletUtility.logger.info("return first elememt of the path information array");
        return pathInformationDataArray[0];
    }
}
