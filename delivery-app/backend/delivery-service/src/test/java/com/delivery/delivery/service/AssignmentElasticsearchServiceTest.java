package com.delivery.delivery.service;

import com.delivery.delivery.elasticsearch.repository.AssignmentElasticsearchRepository;
import com.delivery.delivery.model.search.AssignmentSearch;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

class AssignmentElasticsearchServiceTest {
    @Test
    void saveDelegatesToRepository() {
        AssignmentElasticsearchRepository repository = mock(AssignmentElasticsearchRepository.class);
        AssignmentElasticsearchService service = new AssignmentElasticsearchService(repository);
        AssignmentSearch assignment = AssignmentSearch.builder().id(1L).orderId(2L).status("pending").build();

        service.save(assignment);

        ArgumentCaptor<AssignmentSearch> captor = ArgumentCaptor.forClass(AssignmentSearch.class);
        verify(repository).save(captor.capture());
        assert captor.getValue().equals(assignment);
    }
}
