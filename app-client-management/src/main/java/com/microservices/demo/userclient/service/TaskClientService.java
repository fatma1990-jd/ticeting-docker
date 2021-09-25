package com.microservices.demo.userclient.service;

import com.microservices.demo.dto.TaskDTO;
import com.microservices.demo.entity.ResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("task-service")
public interface TaskClientService {

    @GetMapping("/api/v1/task/project/{projectCode}")
    List<TaskDTO> listAllByProjectCode(@PathVariable("projectCode") String projectCode);

    @DeleteMapping("/api/v1/task/project/{projectCode}")
    void deleteByProjectCode(@PathVariable("projectCode") String projectCode);

    @GetMapping("/api/v1/task/employee")
    ResponseEntity<ResponseWrapper> employeeReadAllNonCompleteTask();

}
