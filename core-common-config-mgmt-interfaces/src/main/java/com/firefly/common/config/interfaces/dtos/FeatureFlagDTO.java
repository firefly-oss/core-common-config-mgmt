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

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.fireflyframework.utils.annotations.FilterableId;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for FeatureFlag entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Feature flag for controlling feature availability")
public class FeatureFlagDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Feature flag ID")
    private UUID id;

    @FilterableId
    @Schema(description = "Tenant ID (null for global feature flags)")
    private UUID tenantId;

    @NotBlank(message = "Feature key is required")
    @Size(min = 2, max = 100, message = "Feature key must be between 2 and 100 characters")
    @Schema(description = "Unique feature key", example = "ENABLE_INSTANT_PAYMENTS")
    private String featureKey;

    @NotBlank(message = "Feature name is required")
    @Size(min = 2, max = 200, message = "Feature name must be between 2 and 200 characters")
    @Schema(description = "Human-readable feature name", example = "Enable Instant Payments")
    private String featureName;

    @Schema(description = "Feature description")
    private String description;

    @Schema(description = "Whether the feature is enabled", example = "true")
    private Boolean enabled;

    @Schema(description = "Environment (dev, staging, prod, all)", example = "prod")
    private String environment;

    @Min(value = 0, message = "Rollout percentage must be between 0 and 100")
    @Max(value = 100, message = "Rollout percentage must be between 0 and 100")
    @Schema(description = "Percentage of users to enable feature for (0-100)", example = "50")
    private Integer rolloutPercentage;

    @Schema(description = "Feature start date")
    private LocalDateTime startDate;

    @Schema(description = "Feature end date")
    private LocalDateTime endDate;

    @Schema(description = "Target user segments (JSON array)", example = "[\"premium\", \"enterprise\"]")
    private String targetUserSegments;

    @Schema(description = "Additional metadata (JSON)")
    private String metadata;

    @Schema(description = "Whether the feature flag is active", example = "true")
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

