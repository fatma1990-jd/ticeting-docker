package com.microservices.demo.service;

import com.microservices.demo.dto.ProjectDTO;
import com.microservices.demo.dto.UserDTO;
import com.microservices.demo.entity.User;
import com.microservices.demo.exception.TicketingProjectException;

import java.util.List;

public interface ProjectService {

    ProjectDTO getByProjectCode(String code);

    List<ProjectDTO> listAllProjects();

    ProjectDTO save(ProjectDTO dto) throws TicketingProjectException;

    ProjectDTO update(ProjectDTO dto) throws TicketingProjectException;

    void delete(String code) throws TicketingProjectException;

    ProjectDTO complete(String projectCode) throws TicketingProjectException;

    List<ProjectDTO> listAllProjectDetails(String userName) throws TicketingProjectException;

    List<ProjectDTO> readAllByAssignedManager(String username);

    List<ProjectDTO> listAllNonCompletedProjects();

}
