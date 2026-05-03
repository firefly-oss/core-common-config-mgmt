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

import com.firefly.common.config.models.entities.ChannelConfigParameter;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for ChannelConfigParameter entity
 */
@Repository
public interface ChannelConfigParameterRepository extends R2dbcRepository<ChannelConfigParameter, UUID> {

    /**
     * Find all active parameters
     */
    Flux<ChannelConfigParameter> findByActiveTrue();

    /**
     * Find all parameters for a specific channel config
     */
    Flux<ChannelConfigParameter> findByChannelConfigId(UUID channelConfigId);

    /**
     * Find all active parameters for a specific channel config
     */
    Flux<ChannelConfigParameter> findByChannelConfigIdAndActiveTrue(UUID channelConfigId);

    /**
     * Find a specific parameter by channel config ID and parameter key
     */
    Mono<ChannelConfigParameter> findByChannelConfigIdAndParameterKey(UUID channelConfigId, String parameterKey);

    /**
     * Find all parameters by category
     */
    Flux<ChannelConfigParameter> findByCategory(String category);

    /**
     * Find all parameters by category for a specific channel config
     */
    Flux<ChannelConfigParameter> findByChannelConfigIdAndCategory(UUID channelConfigId, String category);

    /**
     * Find all required parameters for a specific channel config
     */
    Flux<ChannelConfigParameter> findByChannelConfigIdAndIsRequiredTrue(UUID channelConfigId);

    /**
     * Find all sensitive parameters for a specific channel config
     */
    Flux<ChannelConfigParameter> findByChannelConfigIdAndIsSensitiveTrue(UUID channelConfigId);

    /**
     * Find all parameters for a specific channel code
     */
    Flux<ChannelConfigParameter> findByChannelCode(String channelCode);

    /**
     * Find all active parameters for a specific channel code
     */
    Flux<ChannelConfigParameter> findByChannelCodeAndActiveTrue(String channelCode);

    /**
     * Find a specific parameter by channel code and parameter key
     */
    Mono<ChannelConfigParameter> findByChannelCodeAndParameterKey(String channelCode, String parameterKey);

    /**
     * Find all parameters by channel code and category
     */
    Flux<ChannelConfigParameter> findByChannelCodeAndCategory(String channelCode, String category);
}

