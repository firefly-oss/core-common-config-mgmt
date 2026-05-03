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


package com.firefly.common.config.core.services;

import com.firefly.common.config.interfaces.dtos.ChannelConfigDTO;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Service interface for managing channel configurations
 */
public interface ChannelConfigService {

    Mono<ChannelConfigDTO> getById(UUID id);

    Mono<PaginationResponse<ChannelConfigDTO>> filter(FilterRequest<ChannelConfigDTO> filterRequest);

    Mono<ChannelConfigDTO> create(ChannelConfigDTO channelConfigDTO);

    Mono<ChannelConfigDTO> update(UUID id, ChannelConfigDTO channelConfigDTO);

    Mono<Void> delete(UUID id);
}

