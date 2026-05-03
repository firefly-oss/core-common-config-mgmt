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

import com.firefly.common.config.core.mappers.ApiProcessMappingMapper;
import com.firefly.common.config.core.services.ApiProcessMappingService;
import com.firefly.common.config.interfaces.dtos.ApiProcessMappingDTO;
import com.firefly.common.config.models.entities.ApiProcessMapping;
import com.firefly.common.config.models.repositories.ApiProcessMappingRepository;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Implementation of ApiProcessMappingService for managing API-to-Process mappings.
 * 
 * <p>This service provides CRUD operations and resolution logic for the
 * Firefly Plugin Architecture's process mapping configuration.</p>
 * 
 * @author Firefly Development Team
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiProcessMappingServiceImpl implements ApiProcessMappingService {

    private static final String CACHE_NAME = "api-process-mappings";

    private final ApiProcessMappingRepository repository;
    private final ApiProcessMappingMapper mapper;

    @Override
    public Mono<ApiProcessMappingDTO> getById(UUID id) {
        log.debug("Getting API process mapping by ID: {}", id);
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException(
                        "ApiProcessMapping not found with id: " + id)))
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PaginationResponse<ApiProcessMappingDTO>> filter(
            FilterRequest<ApiProcessMappingDTO> filterRequest) {
        log.debug("Filtering API process mappings with request: {}", filterRequest);
        return FilterUtils.createFilter(
                ApiProcessMapping.class,
                mapper::toDTO
        ).filter(filterRequest);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public Mono<ApiProcessMappingDTO> create(ApiProcessMappingDTO dto) {
        log.info("Creating API process mapping: operationId={}, processId={}, tenantId={}",
                dto.getOperationId(), dto.getProcessId(), dto.getTenantId());
        
        dto.setId(null);
        
        // Set defaults
        if (dto.getIsActive() == null) {
            dto.setIsActive(true);
        }
        if (dto.getPriority() == null) {
            dto.setPriority(0);
        }
        
        ApiProcessMapping entity = mapper.toEntity(dto);
        return repository.save(entity)
                .doOnSuccess(saved -> log.info("Created API process mapping: id={}", saved.getId()))
                .map(mapper::toDTO);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public Mono<ApiProcessMappingDTO> update(UUID id, ApiProcessMappingDTO dto) {
        log.info("Updating API process mapping: id={}", id);
        
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException(
                        "ApiProcessMapping not found with id: " + id)))
                .flatMap(existingEntity -> {
                    // Preserve version for optimistic locking
                    dto.setId(id);
                    dto.setVersion(existingEntity.getVersion());
                    
                    // Use update method to preserve audit fields
                    mapper.updateEntityFromDTO(dto, existingEntity);
                    
                    return repository.save(existingEntity);
                })
                .doOnSuccess(saved -> log.info("Updated API process mapping: id={}", saved.getId()))
                .map(mapper::toDTO);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public Mono<Void> delete(UUID id) {
        log.info("Deleting API process mapping: id={}", id);
        
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException(
                        "ApiProcessMapping not found with id: " + id)))
                .flatMap(repository::delete)
                .doOnSuccess(v -> log.info("Deleted API process mapping: id={}", id));
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'resolve:' + #tenantId + ':' + #operationId + ':' + #productId + ':' + #channelType")
    public Mono<ApiProcessMappingDTO> resolveMapping(
            UUID tenantId,
            String operationId,
            UUID productId,
            String channelType) {
        
        log.debug("Resolving process mapping: tenantId={}, operationId={}, productId={}, channelType={}",
                tenantId, operationId, productId, channelType);
        
        // Use the repository's best match query which orders by specificity
        return repository.findBestMatch(tenantId, operationId, productId, channelType)
                .next()  // Get the first (best) match
                .switchIfEmpty(
                        // Fall back to vanilla mapping if no tenant-specific match
                        repository.findVanillaMapping(operationId)
                )
                .doOnNext(mapping -> log.debug(
                        "Resolved mapping: operationId={} -> processId={} (tenant={}, vanilla={})",
                        operationId, mapping.getProcessId(), mapping.getTenantId(), mapping.isVanilla()))
                .map(mapper::toDTO);
    }

    @Override
    public Flux<ApiProcessMappingDTO> getByTenantId(UUID tenantId) {
        log.debug("Getting API process mappings for tenant: {}", tenantId);
        return repository.findByTenantId(tenantId)
                .map(mapper::toDTO);
    }

    @Override
    public Flux<ApiProcessMappingDTO> getVanillaMappings() {
        log.debug("Getting vanilla (default) API process mappings");
        return repository.findByTenantIdIsNullAndIsActiveTrue()
                .map(mapper::toDTO);
    }

    @Override
    public Flux<ApiProcessMappingDTO> getByProcessId(String processId) {
        log.debug("Getting API process mappings for process: {}", processId);
        return repository.findByProcessId(processId)
                .map(mapper::toDTO);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public Mono<Void> invalidateCache(UUID tenantId) {
        if (tenantId != null) {
            log.info("Invalidating API process mapping cache for tenant: {}", tenantId);
        } else {
            log.info("Invalidating all API process mapping cache");
        }
        // Cache eviction is handled by the @CacheEvict annotation
        // For more granular eviction, we could implement a custom cache manager
        return Mono.empty();
    }
}
