package com.microservices.demo.controller;

import com.microservices.demo.annotation.DefaultExceptionMessage;
import com.microservices.demo.dto.ProjectDTO;
import com.microservices.demo.entity.ResponseWrapper;
import com.microservices.demo.exception.TicketingProjectException;
import com.microservices.demo.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@Tag(name = "Project Controller",description = "Project API")
public class ProjectController {


    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @GetMapping
    @Operation(summary = "Read all projects")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
//    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    public ResponseEntity<ResponseWrapper> readAll(){
        List<ProjectDTO> projectDTOS = projectService.listAllProjects();
        return ResponseEntity.ok(new ResponseWrapper("Project are retrieved",projectDTOS));
    }

    @GetMapping("/{projectcode}")
    @Operation(summary = "Read by project code")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
//    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    public ResponseEntity<ResponseWrapper> readByProjectCode(@PathVariable("projectcode") String projectcode){
        ProjectDTO projectDTO = projectService.getByProjectCode(projectcode);
        return ResponseEntity.ok(new ResponseWrapper("Project is retrieved",projectDTO));
    }

    @PostMapping
    @Operation(summary = "Create project")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
//    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    public ResponseEntity<ResponseWrapper> create(@RequestBody ProjectDTO projectDTO) throws TicketingProjectException {
        ProjectDTO createdProject = projectService.save(projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("Project is retrieved",projectDTO));
    }

    @PutMapping
    @Operation(summary = "Update project")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
//    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO) throws TicketingProjectException {
        ProjectDTO updatedProject = projectService.update(projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("Project is updated",updatedProject));
    }

    @DeleteMapping("/{projectcode}")
    @Operation(summary = "Delete project")
    @DefaultExceptionMessage(defaultMessage = "Failed to delete project!")
//    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("projectcode") String projectcode) throws TicketingProjectException {
        projectService.delete(projectcode);
        return ResponseEntity.ok(new ResponseWrapper("Project is deleted"));
    }

    @PutMapping("/complete/{projectcode}")
    @Operation(summary = "Complete project")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
//    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> completeProject(@PathVariable("projectcode") String projectcode) throws TicketingProjectException {
        ProjectDTO projectDTO = projectService.complete(projectcode);
        return ResponseEntity.ok(new ResponseWrapper("Project is completed",projectDTO));
    }

    @GetMapping("/details/{userName}")
    @Operation(summary = "Read all project details")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
//    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> readAllProjectDetails(@PathVariable("userName") String userName) throws TicketingProjectException {
        try {

            List<ProjectDTO> projectDTOs = projectService.listAllProjectDetails(userName);
            return ResponseEntity.ok(new ResponseWrapper("Projects are retrieved with details",projectDTOs));
        }catch (TicketingProjectException e){
            return ResponseEntity.ok(new ResponseWrapper("none project are retrieved with details",new ArrayList<>()));

        }
    }
}
