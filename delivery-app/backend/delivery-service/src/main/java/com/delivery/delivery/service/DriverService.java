package com.delivery.delivery.service;

import com.delivery.delivery.model.Driver;
import com.delivery.delivery.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {

    private final DriverRepository driverRepository;

    public Flux<Driver> getAllDrivers() {
        List<Driver> drivers = driverRepository.findAll();
        log.info("Fetched all drivers: {}", drivers);
        return Flux.fromIterable(drivers);
    }

    public Mono<Driver> createDriver(Driver driver) {
        driver.setAvailable(true);
        return Mono.just(driverRepository.save(driver));
    }
}

