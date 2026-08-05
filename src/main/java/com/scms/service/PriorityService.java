package com.scms.service;

import com.scms.dto.PriorityDTO;

import java.util.List;

public interface PriorityService {

    List<PriorityDTO> getAllPriorities();

    PriorityDTO getPriorityById(Long id);
}
