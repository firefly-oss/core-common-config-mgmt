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


package com.firefly.common.config.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing dynamic configuration parameters for a channel.
 * Allows flexible, extensible configuration without schema changes.
 * 
 * Examples of parameters:
 * - max_transaction_amount: 50000.00
 * - session_timeout_minutes: 15
 * - requires_mfa: true
 * - api_version: v2.1
 * - mobile_app_version_min: 3.0.0
 * - supports_transfers: true
 * - rate_limit_requests_per_minute: 100
 * - maintenance_mode_enabled: false
 * - biometric_retry_attempts: 3
 * - custom_feature_xyz: enabled
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("channel_config_parameters")
public class ChannelConfigParameter {

    @Id
    private UUID id;

    @Column("channel_config_id")
    private UUID channelConfigId;

    @Column("channel_code")
    private String channelCode; // e.g., "WEB_BANKING", "MOBILE_BANKING", "ATM"

    @Column("parameter_key")
    private String parameterKey; // e.g., "max_transaction_amount", "session_timeout_minutes"

    @Column("parameter_value")
    private String parameterValue; // Stored as string, can be parsed as needed

    @Column("parameter_type")
    private String parameterType; // STRING, INTEGER, DECIMAL, BOOLEAN, JSON, etc.

    @Column("description")
    private String description;

    @Column("is_sensitive")
    private Boolean isSensitive; // If true, value should be encrypted/masked

    @Column("is_required")
    private Boolean isRequired; // If true, parameter must have a value

    @Column("validation_regex")
    private String validationRegex; // Optional regex for value validation

    @Column("default_value")
    private String defaultValue; // Default value if not set

    @Column("category")
    private String category; // SECURITY, LIMITS, FEATURES, AVAILABILITY, MONITORING, etc.

    @Column("active")
    private Boolean active;

    @Version
    private Long version;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}

