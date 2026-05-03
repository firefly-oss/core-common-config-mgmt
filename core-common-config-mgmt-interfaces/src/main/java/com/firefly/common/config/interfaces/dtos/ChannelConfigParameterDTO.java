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
 * DTO for ChannelConfigParameter entity.
 * Represents dynamic configuration parameters for a channel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dynamic configuration parameter for a channel")
public class ChannelConfigParameterDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Parameter ID")
    private UUID id;

    @FilterableId
    @NotNull(message = "Channel config ID is required")
    @Schema(description = "Channel config ID", required = true)
    private UUID channelConfigId;

    @NotBlank(message = "Channel code is required")
    @Size(max = 50, message = "Channel code must not exceed 50 characters")
    @Schema(description = "Channel code (WEB_BANKING, MOBILE_BANKING, ATM, BRANCH, CALL_CENTER, API, OPEN_BANKING)", required = true, example = "WEB_BANKING")
    private String channelCode;

    @NotBlank(message = "Parameter key is required")
    @Size(max = 100, message = "Parameter key must not exceed 100 characters")
    @Schema(description = "Parameter key", required = true, example = "max_transaction_amount")
    private String parameterKey;

    @NotBlank(message = "Parameter value is required")
    @Size(max = 1000, message = "Parameter value must not exceed 1000 characters")
    @Schema(description = "Parameter value (stored as string)", required = true, example = "50000.00")
    private String parameterValue;

    @NotBlank(message = "Parameter type is required")
    @Size(max = 50, message = "Parameter type must not exceed 50 characters")
    @Schema(description = "Parameter type (STRING, INTEGER, DECIMAL, BOOLEAN, JSON)", required = true, example = "DECIMAL")
    private String parameterType;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Schema(description = "Parameter description", example = "Maximum transaction amount allowed for this channel")
    private String description;

    @Schema(description = "Whether the parameter value is sensitive (should be encrypted/masked)", example = "false")
    private Boolean isSensitive;

    @Schema(description = "Whether the parameter is required", example = "true")
    private Boolean isRequired;

    @Size(max = 500, message = "Validation regex must not exceed 500 characters")
    @Schema(description = "Optional regex for value validation", example = "^[0-9]+(\\.[0-9]{1,2})?$")
    private String validationRegex;

    @Size(max = 1000, message = "Default value must not exceed 1000 characters")
    @Schema(description = "Default value if not set", example = "10000.00")
    private String defaultValue;

    @Size(max = 50, message = "Category must not exceed 50 characters")
    @Schema(description = "Parameter category (SECURITY, LIMITS, FEATURES, AVAILABILITY, MONITORING)", example = "LIMITS")
    private String category;

    @Schema(description = "Whether the parameter is active", example = "true")
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

