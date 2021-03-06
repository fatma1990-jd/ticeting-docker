package com.microservices.demo.service.impl;

import com.microservices.demo.dto.ProjectDTO;
import com.microservices.demo.dto.TaskDTO;
import com.microservices.demo.dto.UserDTO;
import com.microservices.demo.entity.ResponseWrapper;
import com.microservices.demo.entity.User;
import com.microservices.demo.exception.TicketingProjectException;
import com.microservices.demo.repository.UserRepository;
import com.microservices.demo.service.UserService;
import com.microservices.demo.userclient.service.ProjectClientService;
import com.microservices.demo.userclient.service.TaskClientService;
import com.microservices.demo.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private MapperUtil mapperUtil;
    private TaskClientService taskClientService;
    private ProjectClientService projectClientService;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, TaskClientService taskClientService, ProjectClientService projectClientService) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.taskClientService = taskClientService;
        this.projectClientService = projectClientService;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        List<User> list = userRepository.findAll(Sort.by("firstName"));
        return list.stream().map(obj -> mapperUtil.convert(obj,new UserDTO())).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        User user = userRepository.findByUserName(username);
        return mapperUtil.convert(user,new UserDTO());
    }

    @Override
    public UserDTO save(UserDTO dto) throws TicketingProjectException {

        User foundUser = userRepository.findByUserName(dto.getUserName());

        if(foundUser!=null){
            throw new TicketingProjectException("User already exists");
        }

        User user =  mapperUtil.convert(dto,new User());

        User save = userRepository.save(user);

        return mapperUtil.convert(save,new UserDTO());

    }

    @Override
    public UserDTO update(UserDTO dto) throws TicketingProjectException, AccessDeniedException {

        //Find current user
        User user = userRepository.findByUserName(dto.getUserName());

        if(user == null){
            throw new TicketingProjectException("User Does Not Exists");
        }
        //Map update user dto to entity object
        User convertedUser = mapperUtil.convert(dto,new User());

        convertedUser.setEnabled(true);

        //set id to the converted object
        convertedUser.setId(user.getId());
        //save updated user
        userRepository.save(convertedUser);

        return findByUserName(dto.getUserName());
    }

    @Override
    public void delete(String username) throws TicketingProjectException {
        User user = userRepository.findByUserName(username);

        if(user == null){
            throw new TicketingProjectException("User Does Not Exists");
        }

        if(!checkIfUserCanBeDeleted(user)){
            throw new TicketingProjectException("User can not be deleted. It is linked by a project ot task");
        }

        user.setUserName(user.getUserName() + "-" + user.getId());

        user.setIsDeleted(true);
        userRepository.save(user);
    }

    //hard delete
    @Override
    public void deleteByUserName(String username) {
        userRepository.deleteByUserName(username);
    }


    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User> users = userRepository.findAllByRoleDescriptionIgnoreCase(role);
        return users.stream().map(obj -> {return mapperUtil.convert(obj,new UserDTO());}).collect(Collectors.toList());
    }

    @Override
    public Boolean checkIfUserCanBeDeleted(User user) {

//        ResponseEntity<ResponseWrapper> responseWrapperResponseEntity = projectClientService.readAllByManager(user.getUserName());

        switch(user.getRole().getDescription()){
            case "Manager":
                List<ProjectDTO> projectList = (List<ProjectDTO>) projectClientService.readAllByManager(user.getUserName()).getBody().getData();
                return projectList.size() == 0;
            case "Employee":
                List<TaskDTO> taskList = (List<TaskDTO>) taskClientService.employeeReadAllNonCompleteTask().getBody().getData();
                return taskList.size() == 0;
            default:
                return true;
        }
    }

    @Override
    public UserDTO confirm(User user) {

        user.setEnabled(true);
        User confirmedUser = userRepository.save(user);

        return mapperUtil.convert(confirmedUser,new UserDTO());
    }


}
