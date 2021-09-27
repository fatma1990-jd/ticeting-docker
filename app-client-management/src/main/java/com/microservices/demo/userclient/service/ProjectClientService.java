package com.microservices.demo.userclient.service;

import com.microservices.demo.dto.ProjectDTO;
import com.microservices.demo.entity.ResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("project-service")
public interface ProjectClientService {

    @GetMapping("/api/v1/project/{projectCode}")
    ProjectDTO getByProjectCode(@PathVariable("projectCode") String projectCode);

    @GetMapping("/api/v1/project/user/{username}")
    ResponseEntity<ResponseWrapper> readAllByManager(@PathVariable("username") String username);

}
