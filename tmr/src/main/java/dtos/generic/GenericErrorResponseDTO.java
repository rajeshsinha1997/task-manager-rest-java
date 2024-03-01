package dtos.generic;

import com.google.gson.annotations.SerializedName;

import utilities.CommonUtility;

public class GenericErrorResponseDTO {
    // attributes of the dto object
    @SerializedName("response-time")
    private String responseTime;

    @SerializedName("response-error-message")
    private String errorMessage;

    /**
     * constructor
     * 
     * @param responseTime    - time of response generation
     * @param responseMessage - message to be added to the response
     * @param responseData    - data to be added to the response
     */
    public GenericErrorResponseDTO(String errorMessage) {
        // initialize attributes of the generic error response DTO instance
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
