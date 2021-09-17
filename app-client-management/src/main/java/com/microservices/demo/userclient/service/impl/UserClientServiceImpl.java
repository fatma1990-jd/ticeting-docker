package com.microservices.demo.userclient.service.impl;

import com.microservices.demo.dto.UserDTO;
import com.microservices.demo.userclient.config.UserClientConfiguration;
import com.microservices.demo.userclient.service.UserClientService;
import org.springframework.web.client.RestTemplate;

public class UserClientServiceImpl implements UserClientService {

    private RestTemplate restTemplate;
    private UserClientConfiguration userClientConfiguration;

    public UserClientServiceImpl(RestTemplate restTemplate, UserClientConfiguration userClientConfiguration) {
        this.restTemplate = restTemplate;
        this.userClientConfiguration = userClientConfiguration;
    }

    @Override
    public UserDTO getUserDTOByUserName() {
        return restTemplate.getForObject(userClientConfiguration.getUrl(),UserDTO.class) ;
    }
}
