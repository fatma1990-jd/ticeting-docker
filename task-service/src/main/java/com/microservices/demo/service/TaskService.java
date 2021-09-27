package com.microservices.demo.service;

import com.microservices.demo.dto.ProjectDTO;
import com.microservices.demo.dto.TaskDTO;
import com.microservices.demo.entity.Task;
import com.microservices.demo.entity.User;
import com.microservices.demo.enums.Status;
import com.microservices.demo.exception.TicketingProjectException;

import java.util.List;

public interface TaskService {

    TaskDTO findById(Long id) throws TicketingProjectException;

    List<TaskDTO> listAllTasks();

    TaskDTO save(TaskDTO dto);

    TaskDTO update(TaskDTO dto) throws TicketingProjectException;

    void delete(long id) throws TicketingProjectException;

    int totalNonCompletedTasks(String projectCode);
    int totalCompletedTasks(String projectCode);

    void deleteByProject(String projectCode);

    List<TaskDTO> listAllByProjectCode(String projectCode);

    List<TaskDTO> listAllTasksByStatusIsNot(Status status) throws TicketingProjectException;

    List<TaskDTO> listAllTasksByProjectManager() throws TicketingProjectException;

    TaskDTO updateStatus(TaskDTO dto) throws TicketingProjectException;

//    List<TaskDTO> listAllTasksByStatus(Status status);

    List<TaskDTO> readAllByEmployee(User assignedEmployee);
}
