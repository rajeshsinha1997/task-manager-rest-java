package dtos.generic;

import utilities.CommonUtility;

public class GenericErrorResponseDTO {
    // attributes of a response object
    private String responseTime;
    private String errorMessage;

    /**
     * constructor
     * 
     * @param responseTime    - time of response generation
     * @param responseMessage - message to be added to the response
     * @param responseData    - data to be added to the response
     */
    public GenericErrorResponseDTO(String errorMessage) {
        this.responseTime = CommonUtility.getCurrentDateAndTimeStampString();
        this.errorMessage = errorMessage;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
