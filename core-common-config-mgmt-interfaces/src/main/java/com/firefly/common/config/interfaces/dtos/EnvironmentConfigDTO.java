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

import com.firefly.common.config.interfaces.validation.ValidSecretConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.fireflyframework.utils.annotations.FilterableId;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for EnvironmentConfig entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Environment-specific configuration")
@ValidSecretConfiguration(
    valueField = "configValue",
    message = "Invalid secret configuration: when isSecret=true, credentialVaultId is required and configValue must be null; when isSecret=false, configValue is required and credentialVaultId must be null"
)
public class EnvironmentConfigDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Environment config ID")
    private UUID id;

    @FilterableId
    @Schema(description = "Tenant ID (null for global configs)")
    private UUID tenantId;

    @NotBlank(message = "Environment name is required")
    @Schema(description = "Environment name (dev, staging, prod)", example = "prod")
    private String environmentName;

    @NotBlank(message = "Config key is required")
    @Size(min = 2, max = 100, message = "Config key must be between 2 and 100 characters")
    @Schema(description = "Configuration key", example = "database.pool.size")
    private String configKey;

    @Schema(description = "Configuration value (only for non-secret configs). Required when isSecret=false", example = "50")
    private String configValue;

    @Schema(description = "Reference ID to credential stored in security-vault (only for secret configs). Required when isSecret=true", example = "vault-cred-uuid-67890")
    private String credentialVaultId;

    @Schema(description = "Configuration type (STRING, INTEGER, BOOLEAN, JSON)", example = "INTEGER")
    private String configType;

    @Schema(description = "Configuration description")
    private String description;

    @Schema(description = "Whether the value is a secret (if true, use credentialVaultId instead of configValue)", example = "false")
    private Boolean isSecret;

    @Schema(description = "Configuration category", example = "database")
    private String category;

    @Schema(description = "Additional metadata (JSON)")
    private String metadata;

    @Schema(description = "Whether the config is active", example = "true")
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

