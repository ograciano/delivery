package com.delivery.order.elasticsearch.repository;

import com.delivery.order.model.search.OrderSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderElasticsearchRepository extends ElasticsearchRepository<OrderSearch, Long> {
}
