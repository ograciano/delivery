package com.delivery.order.service;

import com.delivery.order.elasticsearch.repository.OrderElasticsearchRepository;
import com.delivery.order.model.search.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderElasticsearchService {

    private final OrderElasticsearchRepository repository;

    public OrderSearch save(OrderSearch orderSearch) {
        return repository.save(orderSearch);
    }
}
