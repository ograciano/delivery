package com.delivery.delivery.controller;

import com.delivery.delivery.model.Driver;
import com.delivery.delivery.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public Flux<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @PostMapping
    public Mono<Driver> createDriver(@RequestBody Driver driver) {
        return driverService.createDriver(driver);
    }
}

