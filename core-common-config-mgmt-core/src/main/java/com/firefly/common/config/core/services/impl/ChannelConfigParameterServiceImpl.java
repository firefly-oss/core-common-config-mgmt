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

import com.firefly.common.config.core.mappers.ChannelConfigParameterMapper;
import com.firefly.common.config.core.services.ChannelConfigParameterService;
import com.firefly.common.config.interfaces.dtos.ChannelConfigParameterDTO;
import com.firefly.common.config.models.entities.ChannelConfigParameter;
import com.firefly.common.config.models.repositories.ChannelConfigParameterRepository;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service implementation for ChannelConfigParameter operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelConfigParameterServiceImpl implements ChannelConfigParameterService {

    private final ChannelConfigParameterRepository repository;
    private final ChannelConfigParameterMapper mapper;

    @Override
    public Mono<ChannelConfigParameterDTO> getById(UUID id) {
        log.debug("Getting channel config parameter by ID: {}", id);
        return repository.findById(id)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Channel config parameter not found with ID: " + id)));
    }

    @Override
    public Mono<PaginationResponse<ChannelConfigParameterDTO>> filter(FilterRequest<ChannelConfigParameterDTO> filterRequest) {
        log.debug("Filtering channel config parameters with request: {}", filterRequest);
        return FilterUtils.createFilter(
                ChannelConfigParameter.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<ChannelConfigParameterDTO> create(ChannelConfigParameterDTO dto) {
        log.debug("Creating channel config parameter: {}", dto);
        ChannelConfigParameter entity = mapper.toEntity(dto);
        entity.setActive(true);
        return repository.save(entity)
                .map(mapper::toDTO)
                .doOnSuccess(created -> log.info("Created channel config parameter with ID: {}", created.getId()));
    }

    @Override
    public Mono<ChannelConfigParameterDTO> update(UUID id, ChannelConfigParameterDTO dto) {
        log.debug("Updating channel config parameter with ID: {}", id);
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Channel config parameter not found with ID: " + id)))
                .flatMap(existing -> {
                    ChannelConfigParameter updated = mapper.toEntity(dto);
                    updated.setId(existing.getId());
                    updated.setCreatedAt(existing.getCreatedAt());
                    updated.setVersion(existing.getVersion());
                    return repository.save(updated);
                })
                .map(mapper::toDTO)
                .doOnSuccess(updated -> log.info("Updated channel config parameter with ID: {}", id));
    }

    @Override
    public Mono<Void> delete(UUID id) {
        log.debug("Deleting channel config parameter with ID: {}", id);
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Channel config parameter not found with ID: " + id)))
                .flatMap(existing -> {
                    existing.setActive(false);
                    return repository.save(existing);
                })
                .doOnSuccess(deleted -> log.info("Deleted channel config parameter with ID: {}", id))
                .then();
    }
}

