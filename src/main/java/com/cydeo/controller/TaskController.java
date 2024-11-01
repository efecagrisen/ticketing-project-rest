package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.TaskService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> getTasks(){
        return ResponseEntity.ok(new ResponseWrapper("Tasks retrieved successfully",taskService.listAllTasks(), HttpStatus.OK));
    }

    @GetMapping("/{taskId}")
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> getTasksById(@PathVariable ("taskId") Long id){
        return ResponseEntity.ok(new ResponseWrapper("Task found",taskService.findById(id),HttpStatus.FOUND));
    }

    @PostMapping
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> createTasks(@RequestBody TaskDTO taskDTO){
        taskService.save(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Task created",HttpStatus.CREATED));
    }

    @PutMapping
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> updateTasks(@RequestBody TaskDTO taskDTO){
        taskService.update(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Task updated",HttpStatus.ACCEPTED));
    }

    @DeleteMapping("/{taskId}")
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> deleteTasks(@PathVariable ("taskId") Long id){
        taskService.delete(id);
        return ResponseEntity.ok(new ResponseWrapper("Task Deleted",HttpStatus.OK));
    }

    @GetMapping("/employee/pending-tasks")
    @RolesAllowed({"Employee"})
    public ResponseEntity<ResponseWrapper> employeePendingTasks(){
        return ResponseEntity.ok(new ResponseWrapper("Tasks retrieved successfully",taskService.listAllTasksByStatusIsNot(Status.COMPLETE), HttpStatus.OK));
    }

    @PutMapping("/employee/complete")
    @RolesAllowed({"Employee"})
    public ResponseEntity<ResponseWrapper> employeeUpdateTasks(TaskDTO taskDTO){
        taskService.update(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Task completed", HttpStatus.OK));
    }

    @GetMapping("/employee/archive")
    @RolesAllowed({"Employee"})
    public ResponseEntity<ResponseWrapper> employeeArchiveTasks(){
        return ResponseEntity.ok(new ResponseWrapper("Archived tasks retrieved",taskService.listAllTasksByStatus(Status.COMPLETE), HttpStatus.OK));
    }


}
