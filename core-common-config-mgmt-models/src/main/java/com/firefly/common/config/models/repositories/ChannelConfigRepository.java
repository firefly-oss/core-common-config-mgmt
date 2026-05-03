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

import com.firefly.common.config.models.entities.ChannelConfig;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Repository
public interface ChannelConfigRepository extends BaseRepository<ChannelConfig, UUID> {

    /**
     * Find all active channel configs
     */
    Flux<ChannelConfig> findByActiveTrue();

    /**
     * Find all channel configs for a specific tenant
     */
    Flux<ChannelConfig> findByTenantId(UUID tenantId);

    /**
     * Find all active channel configs for a specific tenant
     */
    Flux<ChannelConfig> findByTenantIdAndActiveTrue(UUID tenantId);

    /**
     * Find all enabled channel configs for a specific tenant
     */
    Flux<ChannelConfig> findByTenantIdAndEnabledTrue(UUID tenantId);

    /**
     * Find a specific channel config by tenant ID and channel code
     */
    Mono<ChannelConfig> findByTenantIdAndChannelCode(UUID tenantId, String channelCode);

    /**
     * Find all channel configs by channel code
     */
    Flux<ChannelConfig> findByChannelCode(String channelCode);

    /**
     * Find all channel configs for a tenant ordered by priority (ascending)
     */
    Flux<ChannelConfig> findByTenantIdOrderByPriorityAsc(UUID tenantId);
}

