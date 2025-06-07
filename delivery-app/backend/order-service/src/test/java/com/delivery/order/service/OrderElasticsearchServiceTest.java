package com.delivery.order.service;

import com.delivery.order.elasticsearch.repository.OrderElasticsearchRepository;
import com.delivery.order.model.search.OrderSearch;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

class OrderElasticsearchServiceTest {
    @Test
    void saveDelegatesToRepository() {
        OrderElasticsearchRepository repository = mock(OrderElasticsearchRepository.class);
        OrderElasticsearchService service = new OrderElasticsearchService(repository);
        OrderSearch order = OrderSearch.builder().id(1L).customerName("John").build();

        service.save(order);

        ArgumentCaptor<OrderSearch> captor = ArgumentCaptor.forClass(OrderSearch.class);
        verify(repository).save(captor.capture());
        assert captor.getValue().equals(order);
    }
}
