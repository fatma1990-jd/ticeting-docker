package com.microservices.demo.service.impl;

import com.microservices.demo.dto.ProjectDTO;
import com.microservices.demo.dto.UserDTO;
import com.microservices.demo.entity.Project;
import com.microservices.demo.entity.User;
import com.microservices.demo.enums.Status;
import com.microservices.demo.exception.TicketingProjectException;
import com.microservices.demo.repository.ProjectRepository;
import com.microservices.demo.service.ProjectService;
import com.microservices.demo.userclient.service.UserClientService;
import com.microservices.demo.util.MapperUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {


    private ProjectRepository projectRepository;
    private MapperUtil mapperUtil;
    private UserClientService userClientService;

    public ProjectServiceImpl(ProjectRepository projectRepository, MapperUtil mapperUtil, UserClientService userClientService) {
        this.projectRepository = projectRepository;
        this.mapperUtil = mapperUtil;
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

            return list.stream().map(project -> {
                ProjectDTO projectDTO = new ProjectDTO();
                projectDTO.setProjectDetail(project.getProjectDetail());
                projectDTO.setProjectStatus(project.getProjectStatus());
                projectDTO.setProjectName(project.getProjectName());
                projectDTO.setProjectCode(project.getProjectCode());
                projectDTO.setId(project.getId());
                projectDTO.setAssignedManager(user);
                return projectDTO;
            }).collect(Collectors.toList());
        }
        throw new TicketingProjectException("user couldn't find");
    }

    @Override
    public List<ProjectDTO> readAllByAssignedManager(User user) {
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
