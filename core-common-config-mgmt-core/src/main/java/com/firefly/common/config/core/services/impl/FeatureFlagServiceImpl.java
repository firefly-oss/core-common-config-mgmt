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

import com.firefly.common.config.core.mappers.FeatureFlagMapper;
import com.firefly.common.config.core.services.FeatureFlagService;
import com.firefly.common.config.interfaces.dtos.FeatureFlagDTO;
import com.firefly.common.config.models.entities.FeatureFlag;
import com.firefly.common.config.models.repositories.FeatureFlagRepository;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Implementation of the FeatureFlagService interface
 */
@Service
public class FeatureFlagServiceImpl implements FeatureFlagService {

    @Autowired
    private FeatureFlagRepository repository;

    @Autowired
    private FeatureFlagMapper mapper;

    @Override
    public Mono<FeatureFlagDTO> getById(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("FeatureFlag not found with id: " + id)))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<FeatureFlagDTO>> filter(FilterRequest<FeatureFlagDTO> filterRequest) {
        return FilterUtils.createFilter(
                FeatureFlag.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<FeatureFlagDTO> create(FeatureFlagDTO featureFlagDTO) {
        featureFlagDTO.setId(null);
        FeatureFlag entity = mapper.toEntity(featureFlagDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<FeatureFlagDTO> update(UUID id, FeatureFlagDTO featureFlagDTO) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("FeatureFlag not found with id: " + id)))
                .flatMap(existingEntity -> {
                    featureFlagDTO.setId(id);
                    featureFlagDTO.setVersion(existingEntity.getVersion());
                    FeatureFlag entity = mapper.toEntity(featureFlagDTO);
                    return repository.save(entity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("FeatureFlag not found with id: " + id)))
                .flatMap(repository::delete);
    }
}

