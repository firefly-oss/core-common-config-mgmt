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
import jakarta.validation.constraints.Size;
import org.fireflyframework.utils.annotations.FilterableId;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO for API-to-Process mapping configuration.
 * 
 * <p>This DTO represents the configuration for mapping API operations
 * to process plugins in the Firefly Plugin Architecture.</p>
 * 
 * @author Firefly Development Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "API-to-Process mapping for the plugin architecture")
public class ApiProcessMappingDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Mapping ID")
    private UUID id;

    @FilterableId
    @Schema(description = "Tenant ID (null for default/vanilla mappings)")
    private UUID tenantId;

    @Schema(description = "Product ID (null for all products)")
    private UUID productId;

    @Size(max = 50, message = "Channel type must not exceed 50 characters")
    @Schema(description = "Channel type (MOBILE, WEB, API, BRANCH) - null for all channels", 
            example = "MOBILE")
    private String channelType;

    @Size(max = 255, message = "API path must not exceed 255 characters")
    @Schema(description = "API path pattern", example = "/api/v1/accounts")
    private String apiPath;

    @Size(max = 10, message = "HTTP method must not exceed 10 characters")
    @Schema(description = "HTTP method", example = "POST")
    private String httpMethod;

    @NotBlank(message = "Operation ID is required")
    @Size(min = 1, max = 100, message = "Operation ID must be between 1 and 100 characters")
    @Schema(description = "API operation identifier", example = "createAccount", required = true)
    private String operationId;

    @NotBlank(message = "Process ID is required")
    @Size(min = 1, max = 100, message = "Process ID must be between 1 and 100 characters")
    @Schema(description = "Process plugin ID to execute", example = "vanilla-account-creation", required = true)
    private String processId;

    @Size(max = 20, message = "Process version must not exceed 20 characters")
    @Schema(description = "Optional version constraint (null = use latest)", example = "1.0.0")
    private String processVersion;

    @Schema(description = "Priority (lower = higher priority)", example = "0")
    private Integer priority;

    @Schema(description = "Whether this mapping is active", example = "true")
    private Boolean isActive;

    @Schema(description = "When this mapping becomes effective")
    private OffsetDateTime effectiveFrom;

    @Schema(description = "When this mapping expires")
    private OffsetDateTime effectiveTo;

    @Schema(description = "Additional configuration parameters (JSON)")
    private String parameters;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Schema(description = "Description for documentation")
    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "User who created this mapping")
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "User who last updated this mapping")
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Version for optimistic locking")
    private Long version;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Creation timestamp")
    private OffsetDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Last update timestamp")
    private OffsetDateTime updatedAt;

    /**
     * Checks if this is a vanilla/default mapping.
     * 
     * @return true if tenant_id is null
     */
    public boolean isVanilla() {
        return tenantId == null;
    }
}
