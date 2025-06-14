package com.delivery.delivery.listener;

import com.delivery.delivery.model.Order;
import com.delivery.delivery.repository.AssignmentRepository;
import com.delivery.delivery.repository.DriverRepository;
import com.delivery.delivery.service.AssignmentElasticsearchService;
import com.delivery.delivery.mappers.Mappers;
import com.delivery.delivery.model.search.AssignmentSearch;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class Commons {

    private final AssignmentRepository assignmentRepository;
    private final DriverRepository driverRepository;
    private final AssignmentElasticsearchService assignmentElasticsearchService;

    @Transactional
    public void updateAssignmentAndDriverStatus(Order order, String assignmentStatus) {
        assignmentRepository.findByOrderId(order.getId())
                .ifPresentOrElse(assignment -> {
                    assignment.setStatus(assignmentStatus);
                    assignmentRepository.save(assignment);
                    AssignmentSearch search = Mappers.map(assignment, AssignmentSearch.class);
                    assignmentElasticsearchService.save(search);
                    log.info("Audit: Assignment [{}] updated to [{}]", assignment.getId(), assignmentStatus);

                    driverRepository.findById(assignment.getDriverId())
                            .ifPresentOrElse(driver -> {
                                driver.setAvailable(true);
                                driverRepository.save(driver);
                                log.info("Audit: Driver [{}] marked as available", driver.getId());
                            }, () -> log.warn("Driver with ID {} not found", assignment.getDriverId()));

                }, () -> log.warn("Assignment for order {} not found", order.getId()));
    }
}
