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

import com.firefly.common.config.core.mappers.WebhookConfigMapper;
import com.firefly.common.config.core.services.WebhookConfigService;
import com.firefly.common.config.interfaces.dtos.WebhookConfigDTO;
import com.firefly.common.config.models.entities.WebhookConfig;
import com.firefly.common.config.models.repositories.WebhookConfigRepository;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Implementation of the WebhookConfigService interface
 */
@Service
public class WebhookConfigServiceImpl implements WebhookConfigService {

    @Autowired
    private WebhookConfigRepository repository;

    @Autowired
    private WebhookConfigMapper mapper;

    @Override
    public Mono<WebhookConfigDTO> getById(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("WebhookConfig not found with id: " + id)))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<WebhookConfigDTO>> filter(FilterRequest<WebhookConfigDTO> filterRequest) {
        return FilterUtils.createFilter(
                WebhookConfig.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    public Mono<WebhookConfigDTO> create(WebhookConfigDTO webhookConfigDTO) {
        webhookConfigDTO.setId(null);
        WebhookConfig entity = mapper.toEntity(webhookConfigDTO);
        return repository.save(entity)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<WebhookConfigDTO> update(UUID id, WebhookConfigDTO webhookConfigDTO) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("WebhookConfig not found with id: " + id)))
                .flatMap(existingEntity -> {
                    webhookConfigDTO.setId(id);
                    webhookConfigDTO.setVersion(existingEntity.getVersion());
                    WebhookConfig entity = mapper.toEntity(webhookConfigDTO);
                    return repository.save(entity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("WebhookConfig not found with id: " + id)))
                .flatMap(repository::delete);
    }
}

