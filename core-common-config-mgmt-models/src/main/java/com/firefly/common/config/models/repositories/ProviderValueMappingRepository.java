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


package com.firefly.common.config.models.repositories;

import com.firefly.common.config.models.entities.ProviderValueMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for ProviderValueMapping entity
 */
public interface ProviderValueMappingRepository extends BaseRepository<ProviderValueMapping, UUID> {

    /**
     * Find all active provider value mappings
     * @return Flux of active provider value mappings
     */
    Flux<ProviderValueMapping> findByActiveTrue();

    /**
     * Find provider value mappings by provider ID
     * @param providerId Provider ID
     * @return Flux of provider value mappings
     */
    Flux<ProviderValueMapping> findByProviderId(UUID providerId);

    /**
     * Find provider value mappings by tenant ID
     * @param tenantId Tenant ID
     * @return Flux of provider value mappings
     */
    Flux<ProviderValueMapping> findByTenantId(UUID tenantId);

    /**
     * Find provider value mappings by provider ID and tenant ID
     * @param providerId Provider ID
     * @param tenantId Tenant ID
     * @return Flux of provider value mappings
     */
    Flux<ProviderValueMapping> findByProviderIdAndTenantId(UUID providerId, UUID tenantId);

    /**
     * Find provider value mappings by mapping type
     * @param mappingType Mapping type
     * @return Flux of provider value mappings
     */
    Flux<ProviderValueMapping> findByMappingType(String mappingType);

    /**
     * Find provider value mapping by provider ID, mapping type and Firefly value
     * @param providerId Provider ID
     * @param mappingType Mapping type
     * @param fireflyValue Firefly value
     * @return Mono of provider value mapping
     */
    Mono<ProviderValueMapping> findByProviderIdAndMappingTypeAndFireflyValue(UUID providerId, String mappingType, String fireflyValue);

    /**
     * Find provider value mapping by provider ID, mapping type and provider value
     * @param providerId Provider ID
     * @param mappingType Mapping type
     * @param providerValue Provider value
     * @return Mono of provider value mapping
     */
    Mono<ProviderValueMapping> findByProviderIdAndMappingTypeAndProviderValue(UUID providerId, String mappingType, String providerValue);

    /**
     * Find provider value mappings by direction
     * @param direction Direction (TO_PROVIDER, FROM_PROVIDER, BIDIRECTIONAL)
     * @return Flux of provider value mappings
     */
    Flux<ProviderValueMapping> findByDirection(String direction);
}

