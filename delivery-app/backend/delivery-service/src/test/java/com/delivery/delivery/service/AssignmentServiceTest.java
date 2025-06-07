package com.delivery.delivery.service;

import com.delivery.delivery.client.OrderClient;
import com.delivery.delivery.model.Assignment;
import com.delivery.delivery.model.Driver;
import com.delivery.delivery.repository.AssignmentRepository;
import com.delivery.delivery.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private DriverRepository driverRepository;
    @Mock
    private OrderClient orderClient;
    @Mock
    private AssignmentElasticsearchService assignmentElasticsearchService;

    private AssignmentService assignmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        assignmentService = new AssignmentService(assignmentRepository, driverRepository, orderClient, assignmentElasticsearchService);
    }

    @Test
    void assignOrderCreatesAssignmentWhenDriverAvailable() {
        Driver driver = Driver.builder().id(1L).available(true).build();
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(assignmentRepository.save(any(Assignment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderClient.updateOrderStatus(anyLong(), anyString())).thenReturn(Mono.empty());

        Assignment result = assignmentService.assignOrder(2L, 1L).block();

        assertNotNull(result);
        assertEquals("assigned", result.getStatus());
        verify(assignmentRepository).save(any(Assignment.class));
        verify(driverRepository).save(driver);
        verify(orderClient).updateOrderStatus(2L, "assigned");
    }

    @Test
    void assignOrderFailsWhenDriverNotAvailable() {
        Driver driver = Driver.builder().id(1L).available(false).build();
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> assignmentService.assignOrder(2L, 1L).block());
        assertEquals("Driver is not available", ex.getMessage());
        verify(assignmentRepository, never()).save(any());
    }
}
