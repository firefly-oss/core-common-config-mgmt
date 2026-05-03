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

import com.firefly.common.config.core.mappers.EnvironmentConfigMapper;
import com.firefly.common.config.core.services.EnvironmentConfigService;
import com.firefly.common.config.interfaces.dtos.EnvironmentConfigDTO;
import com.firefly.common.config.models.entities.EnvironmentConfig;
import com.firefly.common.config.models.repositories.EnvironmentConfigRepository;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Implementation of the EnvironmentConfigService interface
 */
@Service
public class EnvironmentConfigServiceImpl implements EnvironmentConfigService {

    @Autowired
    private EnvironmentConfigRepository repository;

    @Autowired
    private EnvironmentConfigMapper mapper;

    @Override
    public Mono<EnvironmentConfigDTO> getById(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("EnvironmentConfig not found with id: " + id)))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<EnvironmentConfigDTO>> filter(FilterRequest<EnvironmentConfigDTO> filterRequest) {
        return FilterUtils.createFilter(
                EnvironmentConfig.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<EnvironmentConfigDTO> create(EnvironmentConfigDTO environmentConfigDTO) {
        environmentConfigDTO.setId(null);
        EnvironmentConfig entity = mapper.toEntity(environmentConfigDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<EnvironmentConfigDTO> update(UUID id, EnvironmentConfigDTO environmentConfigDTO) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("EnvironmentConfig not found with id: " + id)))
                .flatMap(existingEntity -> {
                    environmentConfigDTO.setId(id);
                    environmentConfigDTO.setVersion(existingEntity.getVersion());
                    EnvironmentConfig entity = mapper.toEntity(environmentConfigDTO);
                    return repository.save(entity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("EnvironmentConfig not found with id: " + id)))
                .flatMap(repository::delete);
    }
}

