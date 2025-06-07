package com.delivery.order.service;

import com.delivery.order.client.CatalogClient;
import com.delivery.order.event.OrderEventPublisher;
import com.delivery.order.model.Order;
import com.delivery.order.repository.OrderRepository;
import com.delivery.order.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CatalogClient catalogClient;
    @Mock
    private OrderEventPublisher orderEventPublisher;
    @Mock
    private OrderElasticsearchService orderElasticsearchService;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository, catalogClient, orderEventPublisher, orderElasticsearchService);
    }

    @Test
    void markOrderAsPaidUpdatesOrderWhenDelivered() {
        Order order = Order.builder()
                .id(1L)
                .status("delivered")
                .paidToMerchant(false)
                .createdDate(LocalDateTime.now())
                .products(Collections.emptyList())
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.markOrderAsPaid(1L).block();

        assertNotNull(result);
        assertTrue(result.getPaidToMerchant());
        verify(orderEventPublisher).publishOrderPaid(any(Order.class));
        verify(orderElasticsearchService).save(any());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void markOrderAsPaidFailsWhenNotDelivered() {
        Order order = Order.builder()
                .id(1L)
                .status("pending")
                .paidToMerchant(false)
                .products(Collections.emptyList())
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> orderService.markOrderAsPaid(1L).block());
        assertEquals("Solo puedes pagar pedidos entregados", ex.getMessage());
        verify(orderRepository, never()).save(any());
        verify(orderEventPublisher, never()).publishOrderPaid(any());
    }
}
