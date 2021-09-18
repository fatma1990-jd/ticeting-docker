package com.microservices.demo.userclient.service;

import com.microservices.demo.dto.UserDTO;
import com.microservices.demo.entity.ResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("user-service")
public interface UserClientService {

    @GetMapping("/api/v1/user/{username}")
    UserDTO getUserDTOByUserName(@PathVariable("username") String username);
}
