package constants;

public class ErrorMessage {

    // private constructor
    private ErrorMessage() {
    }

    // error messages to be used when a custom exception is being thrown
    public static final String INVALID_REQUEST_URL = "INVALID REQUEST URL";
    public static final String TASK_ID_NOT_PROVIDED = "A TASK ID WAS NOT PROVIDED";
    public static final String INVALID_TASK_ID = "INVALID TASK ID: ";
    public static final String NO_TASK_FOUND_WITH_ID = "NO TASK FOUND WITH GIVEN ID: ";
    public static final String EMPTY_REQUEST_BODY_NOT_VALID = "AN EMPTY REQUEST BODY IS NOT VALID";
    public static final String TASK_TITLE_CAN_NOT_BE_EMPTY = "TASK TITLE CAN'T BE NULL OR EMPTY";
    public static final String TASK_DESCRIPTION_CAN_NOT_BE_EMPTY = "TASK DESCRIPTION CAN'T BE NULL OR EMPTY";
}
