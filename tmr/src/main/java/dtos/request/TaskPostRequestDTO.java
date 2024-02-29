package dtos.request;

import com.google.gson.annotations.SerializedName;

public class TaskPostRequestDTO {
    @SerializedName("task-title")
    private String taskTitle;

    @SerializedName("task-description")
    private String taskDescription;

    public TaskPostRequestDTO(String taskTitle, String taskDescription) {
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
