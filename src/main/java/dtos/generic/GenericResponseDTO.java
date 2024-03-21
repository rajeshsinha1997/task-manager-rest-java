package dtos.generic;

import com.google.gson.annotations.SerializedName;

import utilities.CommonUtility;

public class GenericResponseDTO<T> {
    // attributes of the dto object
    @SerializedName("response-time")
    private String responseTime;

    @SerializedName("response-data")
    private T responseData;

    /**
     * constructor
     * 
     * @param responseTime - time of response generation
     * @param responseData - data to be added to the response
     */
    public GenericResponseDTO(T responseData) {
        // initialize attributes of generic success response DTO instance
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
