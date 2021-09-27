package com.microservices.demo.service.impl;

import com.microservices.demo.dto.ProjectDTO;
import com.microservices.demo.dto.TaskDTO;
import com.microservices.demo.dto.UserDTO;
import com.microservices.demo.entity.Project;
import com.microservices.demo.entity.Task;
import com.microservices.demo.entity.User;
import com.microservices.demo.enums.Status;
import com.microservices.demo.exception.TicketingProjectException;
import com.microservices.demo.userclient.service.ProjectClientService;
import com.microservices.demo.userclient.service.UserClientService;
import com.microservices.demo.util.MapperUtil;
import com.microservices.demo.repository.TaskRepository;
import com.microservices.demo.service.TaskService;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserClientService userClientService;
    private ProjectClientService projectClientService;
    private MapperUtil mapperUtil;

    public TaskServiceImpl(TaskRepository taskRepository, UserClientService userClientService, ProjectClientService projectClientService, MapperUtil mapperUtil) {
        this.taskRepository = taskRepository;
        this.userClientService = userClientService;
        this.projectClientService = projectClientService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public TaskDTO findById(Long id) throws TicketingProjectException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TicketingProjectException("Task does not exists"));
        return mapperUtil.convert(task,new TaskDTO());
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        List<Task> list = taskRepository.findAll();
        return list.stream().map(obj ->mapperUtil.convert(obj,new TaskDTO())).collect(Collectors.toList());
    }

    @Override
    public TaskDTO save(TaskDTO dto) {
        dto.setTaskStatus(Status.OPEN);
        dto.setAssignedDate(LocalDate.now());
        Task task = mapperUtil.convert(dto,new Task());
        Task save = taskRepository.save(task);
        return mapperUtil.convert(save,new TaskDTO());
    }

    @Override
    public TaskDTO update(TaskDTO dto) throws TicketingProjectException {

        taskRepository.findById(dto.getId()).orElseThrow(() -> new TicketingProjectException("Task does not exists"));
        Task convertedTask = mapperUtil.convert(dto,new Task());
        Task save = taskRepository.save(convertedTask);
        return mapperUtil.convert(save,new TaskDTO());


    }

    @Override
    public void delete(long id) throws TicketingProjectException {
        Task foundTask = taskRepository.findById(id).orElseThrow(() -> new TicketingProjectException("Task does not exists"));
        foundTask.setIsDeleted(true);
        taskRepository.save(foundTask);
    }

    @Override
    public int totalNonCompletedTasks(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode);
    }

    @Override
    public int totalCompletedTasks(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    @Override
    public void deleteByProject(String projectCode) {
        List<TaskDTO> taskDTOS = listAllByProjectCode(projectCode);
        taskDTOS.forEach(taskDTO -> {
            try {
                delete(taskDTO.getId());
            } catch (TicketingProjectException e) {
                e.printStackTrace();
            }
        });
    }

    public List<TaskDTO> listAllByProjectCode(String projectCode){
        ProjectDTO project = projectClientService.getByProjectCode(projectCode);
        List<Task> list = taskRepository.findAllByProject(mapperUtil.convert(project,new Project()));
        return list.stream().map(obj -> mapperUtil.convert(obj,new TaskDTO())).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) throws TicketingProjectException {
//        String id= SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findById(Long.parseLong(id)).orElseThrow(() -> new TicketingProjectException("User does not exists"));
        String username = "employee@employee.com";
        User user = mapperUtil.convert(userClientService.getUserDTOByUserName(username), new User());
        List<Task> list= taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status,user);
        return list.stream().map(obj -> mapperUtil.convert(obj,new TaskDTO())).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByProjectManager() throws TicketingProjectException {
//        String id = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findById(Long.parseLong(id)).orElseThrow(() -> new TicketingProjectException("This user does not exist"));
        String username = "manager@manager.com";
        User user = mapperUtil.convert(userClientService.getUserDTOByUserName(username), new User());
        List<Task> tasks = taskRepository.findAllByProjectAssignedManager(user);
        return tasks.stream().map(obj ->mapperUtil.convert(obj,new TaskDTO())).collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateStatus(TaskDTO dto) throws TicketingProjectException {
        Task task = taskRepository.findById(dto.getId()).orElseThrow(() -> new TicketingProjectException("Task does not exists"));
        task.setTaskStatus(dto.getTaskStatus());
        Task save = taskRepository.save(task);
        return mapperUtil.convert(save,new TaskDTO());
    }

    @Override
    public List<TaskDTO> readAllByEmployee(User assignedEmployee) {
        List<Task> tasks = taskRepository.findAllByAssignedEmployee(assignedEmployee);
        return tasks.stream().map(obj -> mapperUtil.convert(obj, new TaskDTO())).collect(Collectors.toList());
    }

    //    @Override
//    public List<TaskDTO> listAllTasksByStatus(Status status) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByUserName(username);
//        List<Task> list = taskRepository.findAllByTaskStatusAndAssignedEmployee(status,user);
//        return list.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
//    }
}
