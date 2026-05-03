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

import com.firefly.common.config.core.mappers.ChannelConfigMapper;
import com.firefly.common.config.core.services.ChannelConfigService;
import com.firefly.common.config.interfaces.dtos.ChannelConfigDTO;
import com.firefly.common.config.models.entities.ChannelConfig;
import com.firefly.common.config.models.repositories.ChannelConfigRepository;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Implementation of the ChannelConfigService interface
 */
@Service
public class ChannelConfigServiceImpl implements ChannelConfigService {

    @Autowired
    private ChannelConfigRepository repository;

    @Autowired
    private ChannelConfigMapper mapper;

    @Override
    public Mono<ChannelConfigDTO> getById(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("ChannelConfig not found with id: " + id)))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<ChannelConfigDTO>> filter(FilterRequest<ChannelConfigDTO> filterRequest) {
        return FilterUtils.createFilter(
                ChannelConfig.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<ChannelConfigDTO> create(ChannelConfigDTO channelConfigDTO) {
        ChannelConfig entity = mapper.toEntity(channelConfigDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ChannelConfigDTO> update(UUID id, ChannelConfigDTO channelConfigDTO) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("ChannelConfig not found with id: " + id)))
                .flatMap(existingEntity -> {
                    ChannelConfig updatedEntity = mapper.toEntity(channelConfigDTO);
                    updatedEntity.setId(existingEntity.getId());
                    updatedEntity.setCreatedAt(existingEntity.getCreatedAt());
                    updatedEntity.setVersion(existingEntity.getVersion());
                    return repository.save(updatedEntity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("ChannelConfig not found with id: " + id)))
                .flatMap(entity -> {
                    entity.setActive(false);
                    return repository.save(entity);
                })
                .then();
    }
}

