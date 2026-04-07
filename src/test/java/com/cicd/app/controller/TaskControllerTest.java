package com.cicd.app.controller;

import com.cicd.app.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void testHealthCheck() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/tasks/health", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("OK");
    }
    
    @Test
    public void testCreateAndGetTask() {
        Task task = new Task("Test Task", "Test Description");
        
        ResponseEntity<Task> createResponse = restTemplate.postForEntity("/api/tasks", task, Task.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Task createdTask = createResponse.getBody();
        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getTitle()).isEqualTo("Test Task");
        
        ResponseEntity<Task> getResponse = restTemplate.getForEntity("/api/tasks/" + createdTask.getId(), Task.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getTitle()).isEqualTo("Test Task");
    }
    
    @Test
    public void testGetAllTasks() {
        ResponseEntity<Task[]> response = restTemplate.getForEntity("/api/tasks", Task[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
    
    @Test
    public void testDeleteTask() {
        Task task = new Task("Delete Me", "Will be deleted");
        
        ResponseEntity<Task> createResponse = restTemplate.postForEntity("/api/tasks", task, Task.class);
        Long taskId = createResponse.getBody().getId();
        
        restTemplate.delete("/api/tasks/" + taskId);
        
        ResponseEntity<Task> getResponse = restTemplate.getForEntity("/api/tasks/" + taskId, Task.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    
    @Test
    public void testCreateTaskWithEmptyTitle() {
        Task task = new Task("", "No title");
        ResponseEntity<Task> response = restTemplate.postForEntity("/api/tasks", task, Task.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
