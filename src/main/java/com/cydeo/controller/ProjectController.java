package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.ProjectService;
import com.cydeo.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> getProject(){
        return ResponseEntity.ok(new ResponseWrapper("Projects are retrieved successfully",projectService.listAllProjects(), HttpStatus.OK));
    }

    @GetMapping("/{code}")
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> getProjectByCode(@PathVariable String code){
        return ResponseEntity.ok(new ResponseWrapper("Project with code " + code + " is retrieved",projectService.getByProjectCode(code),HttpStatus.FOUND));
    }

    @PostMapping
    @RolesAllowed({"Admin","Manager"})
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO){
        projectService.save(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("New project created",HttpStatus.CREATED));
    }

    @PutMapping
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO){
        projectService.update(projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("Project updated",HttpStatus.OK));
    }

    @DeleteMapping("/{code}")
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable String code){
        projectService.delete(code);
        return ResponseEntity.ok(new ResponseWrapper("Project deleted",HttpStatus.OK));
    }

    @GetMapping("/manager/project-status")
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> getProjectsByManager(){
        List<ProjectDTO> projectDTOList = projectService.listAllProjectDetails();
        return ResponseEntity.ok(new ResponseWrapper("Projects are successfully retrieved",projectDTOList,HttpStatus.FOUND));
    }

    @PutMapping("/manager/complete/{projectCode}")
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable ("projectCode") String projectCode){
        ProjectDTO projectDTO = projectService.getByProjectCode(projectCode);
        projectService.complete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("Project completed", HttpStatus.OK));
    }

}
