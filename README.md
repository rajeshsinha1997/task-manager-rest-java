# Task Manager REST API
This is a basic Java based REST Application using which an user can ___Create a New Task___, ___View List of All Active Tasks___, ___Find a Particular Task Using It's Corresponding ID___, ___Delete a Task Using It's Corresponding ID___.

## Repository Status
[![Build And Publish Docker Image](https://github.com/rajeshsinha1997/task-manager-rest-java/actions/workflows/build_and_publish_docker_image.yml/badge.svg?branch=main)](https://github.com/rajeshsinha1997/task-manager-rest-java/actions/workflows/build_and_publish_docker_image.yml)\
[![CodeQL Analysis](https://github.com/rajeshsinha1997/task-manager-rest-java/actions/workflows/code_ql_analysis.yml/badge.svg?branch=main)](https://github.com/rajeshsinha1997/task-manager-rest-java/actions/workflows/code_ql_analysis.yml)\
[![Dependency Review](https://github.com/rajeshsinha1997/task-manager-rest-java/actions/workflows/dependency_review.yml/badge.svg?branch=main)](https://github.com/rajeshsinha1997/task-manager-rest-java/actions/workflows/dependency_review.yml)\
[![Java CI with Maven](https://github.com/rajeshsinha1997/task-manager-rest-java/actions/workflows/maven-test.yml/badge.svg?branch=main)](https://github.com/rajeshsinha1997/task-manager-rest-java/actions/workflows/maven-test.yml)

## Development Information
- Developer:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;[RAJESH SINHA](https://github.com/rajeshsinha1997)
- Development Language:&emsp;Java
- Web-Framework Used:&emsp;&nbsp;&nbsp;Servlet
- Database Used:&emsp;&emsp;&emsp;&emsp;&emsp;Local In-Memory
- Published:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;[Docker Image](https://hub.docker.com/r/rajeshsinha1997/task-manager-rest-api)
- Contributors:&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;[ALEXEY SHARY](https://github.com/AlexeyShary)
- Postman Collection:&emsp;&emsp;&emsp;[Click Here](https://github.com/rajeshsinha1997/task-manager-rest-java/blob/main/tmr/postman_collection/task-manager-rest-api.postman_collection.json)

## Steps to Setup Project In Local System Without Docker
1. Clone or Download the project from this [GitHub Repository](https://github.com/rajeshsinha1997/task-manager-rest-java/tree/main)
2. Open the cloned or downloaded project directory in any Java IDE (Visual Studio Code, JetBrains IntelliJ Idea, Apache NetBeans, Eclipse IDE) as per your choice.
3. Download all project dependencies using **Maven**. To download the project dependencies, run the below command inside the project sub-directory where the file **pom.xml** is present.
```
mvn clean install
```
4. Once all dependencies have been downloaded and the project build is successfully, a new 'target' folder will be created, which will contain the deployable ___war___ file (**tmr.war**).
5. You can deploy the generated war file on your choice of **web-server** (Take a note of the **PORT** value on which the server is listening to the requests for the deployed ___war___ file).
6. Now you can send the HTTP requests which are already added in the postman collection present in the repository. Make sure to update the PORT value in the postman request URLs (___if you're using any PORT value other than 8080___) before sending any request.

## Steps to Setup Project In Local System With Docker
1. Pull the docker image from Docker Hub using the below command (make sure to replace the '**<version_number>**' with the corresponding available version of your choice),
```
docker pull rajeshsinha1997/task-manager-rest-api:<version_number>
```

2. Run the docker image using the below command (make sure to replace the '**<PORT_NUMBER>**' with the port value you want to use to receive the requests for this application, and replace the '**<version_number>**' with the corresponding available version of your choice),
```
docker run -it --rm -d -p <PORT_NUMBER>:8080 rajeshsinha1997/task-manager-rest-api:<version_number>
```

3. Now you can send the HTTP requests which are already added in the postman collection present in the repository. Make sure to update the PORT value in the postman request URLs (___if you're using any PORT value other than 8080___) before sending any request.

## Endpoints
Below are the endpoints which are available to use,
1. POST /tasks&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;- create a new task
2. GET /tasks&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;- get a list of all active tasks
3. GET /tasks/{task_id}&emsp;&emsp;&nbsp;&nbsp;- find a particular task by it's corresponding id
4. DELETE /tasks/{task_id}&nbsp;&nbsp;&nbsp;- delete a particular task by it's corresponding id

The [Postman Collection](https://github.com/rajeshsinha1997/task-manager-rest-java/blob/main/tmr/postman_collection/task-manager-rest-api.postman_collection.json) present in the repository contains more detailed information about these endpoints.

## Contribute to the Repository

- Check the [ISSUES](https://github.com/rajeshsinha1997/task-manager-rest-java/issues) section of the repository for any open issue which is available to be picked up, and add a comment informing that you're willing to work on that particular issue. It will be assigned to you. Also, if you have any suggestion or enhancement in mind, which can be implemented then start a discussion on that in the [DISCUSSIONS](https://github.com/rajeshsinha1997/task-manager-rest-java/discussions) section of the repository. After verifying the requirement a new issue will be created.

- Once the issue has been assigned to you, create a separate feature branch for that issue, and implement your changes in that feature branch. Make sure to update the ___Test Cases___, ___Postman Collection___, and the ___README.md___ file accordingly.

- Once you're done with implementing the changes, raise a pull request towards the '**develop**' branch from your feature branch, and wait for code reviews. 

- After the code review, if code review comments have been added to the pull request, go through the comments and make the suggested changes. If you have any thought or suggestions related to the suggested changes, feel free to add those as comments to those suggestions.

- Once the code review is finished, the pull request will be merged to the '**develop**' branch, and will move to the main branch with the next upcoming release. **Once the pull request has been merged to the develop branch, your name will be added as a __contributor__ in the project repository**.