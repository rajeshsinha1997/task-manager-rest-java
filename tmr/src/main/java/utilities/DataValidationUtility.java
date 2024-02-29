package utilities;

import exceptions.InvalidRequestAttributeValueException;

public class DataValidationUtility {

    /**
     * private constructor to forbid instantiation
     */
    private DataValidationUtility() {
    }

    /**
     * method to check if the title of a task is valid or not
     * 
     * @param taskTitle                 - given title of the task to be validated as
     *                                  String
     * @param isNullOrEmptyValueAllowed - boolean flag indicating if null or a blank
     *                                  String is allowed
     * @return validated title of the task as String
     */
    public static String validateTaskTitle(String taskTitle, boolean isNullOrEmptyValueAllowed) {
        // check if the given task title is null or is blank and a null or empty value
        // is not allowed
        if ((taskTitle == null || taskTitle.isBlank()) && !isNullOrEmptyValueAllowed) {
            // throw appropriate exception
            throw new InvalidRequestAttributeValueException("task title can't be null or empty");
        }

        // if the given task title is null or blank then return null else return the
        // given string by trimming the leading and trailing spaces
        return taskTitle == null || taskTitle.isBlank() ? null : taskTitle.trim();
    }

    /**
     * method to check if the description of a task is valid or not
     * 
     * @param taskDescription           - given description of the task to be
     *                                  validated as String
     * @param isNullOrEmptyValueAllowed - boolean flag indicating if null or a blank
     *                                  String is allowed
     * @return validated description of the task as String
     */
    public static String validateTaskDescription(String taskDescription, boolean isNullOrEmptyValueAllowed) {
        // check if the given task description is null or is blank and a null or empty
        // value
        // is not allowed
        if ((taskDescription == null || taskDescription.isBlank()) && !isNullOrEmptyValueAllowed) {
            // throw appropriate exception
            throw new InvalidRequestAttributeValueException("task description can't be null or empty");
        }

        // if the given task title is null or blank then return null else return the
        // given string by trimming the leading and trailing spaces
        return taskDescription == null || taskDescription.isBlank() ? null : taskDescription.trim();
    }
}
