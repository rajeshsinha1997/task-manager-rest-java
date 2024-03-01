package dtos.response;

import com.google.gson.annotations.SerializedName;

public class TaskDataResponseDTO {
    // attributes of the dto object
    @SerializedName("task-id")
    private String taskId;

    @SerializedName("task-description")
    private String taskDescription;

    @SerializedName("task-created-on")
    private String taskCreatedOn;

    /**
     * constructor
     * 
     * @param taskId          - id of the task object as String
     * @param taskDescription - description of the task object as String
     * @param taskCreatedOn   - date and timestamp as String on which the task was
     *                        created
     */
    public TaskDataResponseDTO(String taskId, String taskDescription, String taskCreatedOn) {
        // initialize the attributes of the dto object
        this.taskId = taskId;
        this.taskDescription = taskDescription;
        this.taskCreatedOn = taskCreatedOn;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskCreatedOn() {
        return taskCreatedOn;
    }

    public void setTaskCreatedOn(String taskCreatedOn) {
        this.taskCreatedOn = taskCreatedOn;
    }

}
