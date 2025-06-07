package com.delivery.delivery.elasticsearch.repository;

import com.delivery.delivery.model.search.AssignmentSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentElasticsearchRepository extends ElasticsearchRepository<AssignmentSearch, Long> {
}
