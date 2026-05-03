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


package com.firefly.common.config.core.mappers;

import com.firefly.common.config.interfaces.dtos.ProviderDTO;
import com.firefly.common.config.models.entities.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for Provider entity and DTO
 */
@Mapper(componentModel = "spring", uses = {ProviderTypeMapper.class, ProviderStatusMapper.class})
public interface ProviderMapper {

    /**
     * Convert entity to DTO
     * @param entity Provider entity
     * @return ProviderDTO
     */
    ProviderDTO toDTO(Provider entity);

    /**
     * Convert DTO to entity
     * @param dto ProviderDTO
     * @return Provider entity
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Provider toEntity(ProviderDTO dto);
}
