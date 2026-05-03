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

import com.firefly.common.config.models.entities.TenantBranding;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for TenantBranding entity
 */
public interface TenantBrandingRepository extends BaseRepository<TenantBranding, UUID> {

    /**
     * Find all active tenant brandings
     * @return Flux of active tenant brandings
     */
    Flux<TenantBranding> findByActiveTrue();

    /**
     * Find tenant branding by tenant ID
     * @param tenantId Tenant ID
     * @return Mono of tenant branding
     */
    Mono<TenantBranding> findByTenantId(UUID tenantId);
}

