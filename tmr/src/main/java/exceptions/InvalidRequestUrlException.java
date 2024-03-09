package exceptions;

/**
 * exception to be thrown if the request url is invalid
 */
public class InvalidRequestUrlException extends RuntimeException {

    /**
     * constructor
     * 
     * @param errorMessage - error message to be displayed
     */
    public InvalidRequestUrlException(String errorMessage) {
        // call super by passing the error message
        super(errorMessage);
    }
}
