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

import com.firefly.common.config.interfaces.dtos.TenantBrandingDTO;
import com.firefly.common.config.models.entities.TenantBranding;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for TenantBranding entity and DTO
 */
@Mapper(componentModel = "spring")
public interface TenantBrandingMapper {

    /**
     * Convert entity to DTO
     * @param entity TenantBranding entity
     * @return TenantBrandingDTO
     */
    TenantBrandingDTO toDTO(TenantBranding entity);

    /**
     * Convert DTO to entity
     * @param dto TenantBrandingDTO
     * @return TenantBranding entity
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TenantBranding toEntity(TenantBrandingDTO dto);
}

