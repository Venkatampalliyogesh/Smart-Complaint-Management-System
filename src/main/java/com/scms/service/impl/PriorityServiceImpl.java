package com.scms.service.impl;

import com.scms.dto.PriorityDTO;
import com.scms.entity.Priority;
import com.scms.exception.ResourceNotFoundException;
import com.scms.mapper.ComplaintMapper;
import com.scms.repository.PriorityRepository;
import com.scms.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriorityServiceImpl implements PriorityService {

    private final PriorityRepository priorityRepository;
    private final ComplaintMapper complaintMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PriorityDTO> getAllPriorities() {
        return priorityRepository.findAll().stream()
                .map(complaintMapper::toPriorityDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PriorityDTO getPriorityById(Long id) {
        Priority priority = priorityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Priority not found with id: " + id));
        return complaintMapper.toPriorityDto(priority);
    }
}