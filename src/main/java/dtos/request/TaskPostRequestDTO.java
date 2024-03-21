package dtos.request;

import com.google.gson.annotations.SerializedName;

public class TaskPostRequestDTO {
    // attributes of the dto object
    @SerializedName("task-title")
    private String taskTitle;

    @SerializedName("task-description")
    private String taskDescription;

    /**
     * constructor
     * 
     * @param taskTitle       - title of the task
     * @param taskDescription - description of the task
     */
    public TaskPostRequestDTO(String taskTitle, String taskDescription) {
        // initialize the attributes of the dto object
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

}
