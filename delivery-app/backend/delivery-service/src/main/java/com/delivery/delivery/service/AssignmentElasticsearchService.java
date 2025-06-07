package com.delivery.delivery.service;

import com.delivery.delivery.elasticsearch.repository.AssignmentElasticsearchRepository;
import com.delivery.delivery.model.search.AssignmentSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentElasticsearchService {

    private final AssignmentElasticsearchRepository repository;

    public AssignmentSearch save(AssignmentSearch assignmentSearch) {
        return repository.save(assignmentSearch);
    }
}
