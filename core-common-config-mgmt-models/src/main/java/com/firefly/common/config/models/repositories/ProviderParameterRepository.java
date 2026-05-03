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

import com.firefly.common.config.models.entities.ProviderParameter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for ProviderParameter entity
 */
public interface ProviderParameterRepository extends BaseRepository<ProviderParameter, UUID> {

    /**
     * Find all active provider parameters
     * @return Flux of active provider parameters
     */
    Flux<ProviderParameter> findByActiveTrue();

    /**
     * Find provider parameters by provider ID
     * @param providerId Provider ID
     * @return Flux of provider parameters
     */
    Flux<ProviderParameter> findByProviderId(UUID providerId);

    /**
     * Find provider parameters by tenant ID
     * @param tenantId Tenant ID
     * @return Flux of provider parameters
     */
    Flux<ProviderParameter> findByTenantId(UUID tenantId);

    /**
     * Find provider parameters by provider ID and tenant ID
     * @param providerId Provider ID
     * @param tenantId Tenant ID
     * @return Flux of provider parameters
     */
    Flux<ProviderParameter> findByProviderIdAndTenantId(UUID providerId, UUID tenantId);

    /**
     * Find provider parameter by provider ID and parameter name
     * @param providerId Provider ID
     * @param parameterName Parameter name
     * @return Mono of provider parameter
     */
    Mono<ProviderParameter> findByProviderIdAndParameterName(UUID providerId, String parameterName);

    /**
     * Find provider parameter by provider ID, tenant ID and parameter name
     * @param providerId Provider ID
     * @param tenantId Tenant ID
     * @param parameterName Parameter name
     * @return Mono of provider parameter
     */
    Mono<ProviderParameter> findByProviderIdAndTenantIdAndParameterName(UUID providerId, UUID tenantId, String parameterName);

    /**
     * Find provider parameters by category
     * @param category Category
     * @return Flux of provider parameters
     */
    Flux<ProviderParameter> findByCategory(String category);

    /**
     * Find provider parameters by environment
     * @param environment Environment
     * @return Flux of provider parameters
     */
    Flux<ProviderParameter> findByEnvironment(String environment);
}

