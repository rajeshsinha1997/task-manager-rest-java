package exceptions;

/**
 * exception to be thrown if the corresponding request is a BAD REQUEST
 */
public class BadRequestException extends RuntimeException {

    /**
     * constructor
     * 
     * @param errorMessage - error message to be displayed
     */
    public BadRequestException(String errorMessage) {
        // call super by passing the error message
        super(errorMessage);
    }
}
