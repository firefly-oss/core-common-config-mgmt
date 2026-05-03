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

import com.firefly.common.config.core.mappers.TenantSettingsMapper;
import com.firefly.common.config.core.services.TenantSettingsService;
import com.firefly.common.config.interfaces.dtos.TenantSettingsDTO;
import com.firefly.common.config.models.entities.TenantSettings;
import com.firefly.common.config.models.repositories.TenantSettingsRepository;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Implementation of the TenantSettingsService interface
 */
@Service
public class TenantSettingsServiceImpl implements TenantSettingsService {

    @Autowired
    private TenantSettingsRepository repository;

    @Autowired
    private TenantSettingsMapper mapper;

    @Override
    public Mono<TenantSettingsDTO> getById(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("TenantSettings not found with id: " + id)))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<TenantSettingsDTO>> filter(FilterRequest<TenantSettingsDTO> filterRequest) {
        return FilterUtils.createFilter(
                TenantSettings.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<TenantSettingsDTO> create(TenantSettingsDTO tenantSettingsDTO) {
        tenantSettingsDTO.setId(null);
        TenantSettings entity = mapper.toEntity(tenantSettingsDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<TenantSettingsDTO> update(UUID id, TenantSettingsDTO tenantSettingsDTO) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("TenantSettings not found with id: " + id)))
                .flatMap(existingEntity -> {
                    tenantSettingsDTO.setId(id);
                    tenantSettingsDTO.setVersion(existingEntity.getVersion());
                    TenantSettings entity = mapper.toEntity(tenantSettingsDTO);
                    return repository.save(entity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("TenantSettings not found with id: " + id)))
                .flatMap(repository::delete);
    }
}

