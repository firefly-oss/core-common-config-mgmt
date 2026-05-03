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


package com.firefly.common.config.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.fireflyframework.utils.annotations.FilterableId;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Provider Value Mapping entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Provider value mapping between Firefly and provider values")
public class ProviderValueMappingDTO {
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Provider value mapping ID")
    private UUID id;
    
    @NotNull(message = "Provider ID is required")
    @FilterableId
    @Schema(description = "ID of the provider")
    private UUID providerId;
    
    @FilterableId
    @Schema(description = "ID of the tenant (null for provider-level mappings)")
    private UUID tenantId;
    
    @NotBlank(message = "Mapping type is required")
    @Size(min = 1, max = 100, message = "Mapping type must be between 1 and 100 characters")
    @Schema(description = "Type of mapping", example = "TRANSACTION_STATUS")
    private String mappingType;
    
    @NotBlank(message = "Firefly value is required")
    @Size(min = 1, max = 255, message = "Firefly value must be between 1 and 255 characters")
    @Schema(description = "Internal Firefly value", example = "APPROVED")
    private String fireflyValue;
    
    @NotBlank(message = "Provider value is required")
    @Size(min = 1, max = 255, message = "Provider value must be between 1 and 255 characters")
    @Schema(description = "Provider-specific value", example = "SUCCESS")
    private String providerValue;
    
    @Schema(description = "Direction of mapping", example = "BIDIRECTIONAL")
    private String direction;
    
    @Schema(description = "Description of the mapping", example = "Maps Firefly APPROVED status to provider SUCCESS status")
    private String description;
    
    @Schema(description = "Priority for conflict resolution", example = "1")
    private Integer priority;
    
    @Schema(description = "Additional metadata in JSON format", example = "{\"notes\":\"Default mapping\"}")
    private String metadata;
    
    @Schema(description = "Whether the mapping is active", defaultValue = "true")
    private Boolean active;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Version for optimistic locking")
    private Long version;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}

