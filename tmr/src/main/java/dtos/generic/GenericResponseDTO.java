package dtos.generic;

import utilities.CommonUtility;

public class GenericResponseDTO<T> {
    // attributes of a response object
    private String responseTime;
    private T responseData;

    /**
     * constructor
     * 
     * @param responseTime - time of response generation
     * @param responseData - data to be added to the response
     */
    public GenericResponseDTO(T responseData) {
        this.responseTime = CommonUtility.getCurrentDateAndTimeStampString();
        this.responseData = responseData;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public T getResponseData() {
        return responseData;
    }

    public void setResponseData(T responseData) {
        this.responseData = responseData;
    }

}
