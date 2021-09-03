package com.microservices.demo.service;

import com.microservices.demo.dto.RoleDTO;
import com.microservices.demo.exception.TicketingProjectException;

import java.util.List;

public interface RoleService {

    List<RoleDTO> listAllRoles();
    RoleDTO findById(Long id) throws TicketingProjectException;
}
