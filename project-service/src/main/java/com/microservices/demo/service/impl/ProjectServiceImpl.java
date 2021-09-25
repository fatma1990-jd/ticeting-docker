package com.microservices.demo.service.impl;

import com.microservices.demo.dto.ProjectDTO;
import com.microservices.demo.dto.TaskDTO;
import com.microservices.demo.dto.UserDTO;
import com.microservices.demo.entity.Project;
import com.microservices.demo.entity.User;
import com.microservices.demo.enums.Status;
import com.microservices.demo.exception.TicketingProjectException;
import com.microservices.demo.repository.ProjectRepository;
import com.microservices.demo.service.ProjectService;
import com.microservices.demo.userclient.service.TaskClientService;
import com.microservices.demo.userclient.service.UserClientService;
import com.microservices.demo.util.MapperUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {


    private ProjectRepository projectRepository;
    private MapperUtil mapperUtil;
    private TaskClientService taskClientService;
    private UserClientService userClientService;

    public ProjectServiceImpl(ProjectRepository projectRepository, MapperUtil mapperUtil, TaskClientService taskClientService, UserClientService userClientService) {
        this.projectRepository = projectRepository;
        this.mapperUtil = mapperUtil;
        this.taskClientService = taskClientService;
        this.userClientService = userClientService;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        Project project = projectRepository.findByProjectCode(code);
        return mapperUtil.convert(project,new ProjectDTO());
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        List<Project> list = projectRepository.findAll(Sort.by("projectCode"));
        return list.stream().map(obj -> mapperUtil.convert(obj,new ProjectDTO())).collect(Collectors.toList());
    }

    @Override
    public ProjectDTO save(ProjectDTO dto) throws TicketingProjectException {

        Project foundProject = projectRepository.findByProjectCode(dto.getProjectCode());

        if(foundProject != null){
            throw new TicketingProjectException("Project with this code already existing");
        }

        Project obj = mapperUtil.convert(dto,new Project());

        Project createdProject = projectRepository.save(obj);

        return mapperUtil.convert(createdProject,new ProjectDTO());

    }

    @Override
    public ProjectDTO update(ProjectDTO dto) throws TicketingProjectException {

        Project project = projectRepository.findByProjectCode(dto.getProjectCode());

        if(project == null){
            throw new TicketingProjectException("Project does not exist");
        }

        Project convertedProject = mapperUtil.convert(dto,new Project());

        Project updatedProject = projectRepository.save(convertedProject);

        return mapperUtil.convert(updatedProject,new ProjectDTO());

    }

    @Override
    public void delete(String code) throws TicketingProjectException {

        Project project = projectRepository.findByProjectCode(code);

        if(project == null){
            throw new TicketingProjectException("Project does not exist");
        }

        project.setIsDeleted(true);

        project.setProjectCode(project.getProjectCode() +  "-" + project.getId());

        projectRepository.save(project);

        taskClientService.deleteByProjectCode(project.getProjectCode());
    }


    @Override
    public ProjectDTO complete(String projectCode) throws TicketingProjectException {

        Project project = projectRepository.findByProjectCode(projectCode);

        if(project == null){
            throw new TicketingProjectException("Project does not exist");
        }

        project.setProjectStatus(Status.COMPLETE);
        Project completedProject = projectRepository.save(project);

        return mapperUtil.convert(completedProject,new ProjectDTO());
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails(String userName) throws TicketingProjectException {

        UserDTO user = userClientService.getUserDTOByUserName(userName);

        if(user != null){
            List<Project> list = projectRepository.findAllByAssignedManagerId(user.getId());

            if(list.size() == 0 ){
                throw new TicketingProjectException("This manager does not have any project assigned");
            }

//            return list.stream().map(project -> {
//                ProjectDTO projectDTO = new ProjectDTO();
//                projectDTO.setProjectDetail(project.getProjectDetail());
//                projectDTO.setProjectStatus(project.getProjectStatus());
//                projectDTO.setProjectName(project.getProjectName());
//                projectDTO.setProjectCode(project.getProjectCode());
//                projectDTO.setId(project.getId());
//                projectDTO.setAssignedManager(user);
//                return projectDTO;
//            }).collect(Collectors.toList());

            return list.stream().map(project -> {
                ProjectDTO obj = mapperUtil.convert(project,new ProjectDTO());
                obj.setId(project.getId());
                obj.setUnfinishedTaskCounts(totalNonCompletedTasks(project.getProjectCode()));
                obj.setCompleteTaskCounts(totalCompletedTasks(project.getProjectCode()));
                return obj;
            }).collect(Collectors.toList());

        }
        throw new TicketingProjectException("user couldn't find");
    }

    private Integer totalNonCompletedTasks(String projectCode) {
        List<TaskDTO> tasks = taskClientService.listAllByProjectCode(projectCode);
        Integer count = totalCompletedTasks(projectCode).intValue();
        return tasks.size() - count;
    }

    private Integer totalCompletedTasks(String projectCode) {
        List<TaskDTO> tasks = taskClientService.listAllByProjectCode(projectCode);
        return BigDecimal.valueOf(tasks.stream().filter(task -> task.getTaskStatus().equals(Status.COMPLETE)).count()).intValue();
    }

    @Override
    public List<ProjectDTO> readAllByAssignedManager(String username) {
        User user = mapperUtil.convert(userClientService.getUserDTOByUserName(username), new User());
        List<Project> list = projectRepository.findAllByAssignedManager(user);
        return list.stream().map(obj ->mapperUtil.convert(obj,new ProjectDTO())).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> listAllNonCompletedProjects() {

        return projectRepository.findAllByProjectStatusIsNot(Status.COMPLETE)
                .stream()
                .map(project -> mapperUtil.convert(project,new ProjectDTO()))
                .collect(Collectors.toList());
    }

}
