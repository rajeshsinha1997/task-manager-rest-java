package exceptions;

/**
 * exception to be thrown if a requested resource is not available or not found
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * constructor
     * 
     * @param errorMessage - error message to be displayed
     */
    public ResourceNotFoundException(String errorMessage) {
        // call super by passing the error message
        super(errorMessage);
    }
}
