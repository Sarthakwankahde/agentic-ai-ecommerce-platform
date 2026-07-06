package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.dto.HealthResponse;
import com.sarthak.agenticai.service.HealthService;
import org.springframework.stereotype.Service;

@Service
public class HealthServiceImpl implements HealthService {

    @Override
    public HealthResponse getHealth() {

        return new HealthResponse(
                "UP",
                "Agentic AI Academic Assistant",
                "1.0.0"
        );
    }
}