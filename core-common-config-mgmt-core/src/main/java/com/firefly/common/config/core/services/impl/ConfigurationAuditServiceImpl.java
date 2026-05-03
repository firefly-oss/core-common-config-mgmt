/*
 * Copyright 2025 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.common.config.core.services.impl;

import com.firefly.common.config.core.mappers.ConfigurationAuditMapper;
import com.firefly.common.config.core.services.ConfigurationAuditService;
import com.firefly.common.config.interfaces.dtos.ConfigurationAuditDTO;
import com.firefly.common.config.models.entities.ConfigurationAudit;
import com.firefly.common.config.models.repositories.ConfigurationAuditRepository;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Implementation of the ConfigurationAuditService interface
 */
@Service
public class ConfigurationAuditServiceImpl implements ConfigurationAuditService {

    @Autowired
    private ConfigurationAuditRepository repository;

    @Autowired
    private ConfigurationAuditMapper mapper;

    @Override
    public Mono<ConfigurationAuditDTO> getById(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("ConfigurationAudit not found with id: " + id)))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<ConfigurationAuditDTO>> filter(FilterRequest<ConfigurationAuditDTO> filterRequest) {
        return FilterUtils.createFilter(
                ConfigurationAudit.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<ConfigurationAuditDTO> create(ConfigurationAuditDTO configurationAuditDTO) {
        configurationAuditDTO.setId(null);
        ConfigurationAudit entity = mapper.toEntity(configurationAuditDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }
}

