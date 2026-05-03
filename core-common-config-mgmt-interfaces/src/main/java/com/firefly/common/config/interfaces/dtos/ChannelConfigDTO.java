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

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.fireflyframework.utils.annotations.FilterableId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Data Transfer Object for ChannelConfig entity.
 *
 * <p>Represents channel configuration for different banking channels (Web, Mobile, ATM, Branch, API, etc.)
 * in the Firefly core banking system. Each tenant can have multiple channels with specific configurations,
 * security settings, and operational parameters.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Channel configuration for a tenant. Manages configuration for different banking channels " +
        "(Web, Mobile, ATM, Branch, API, etc.) with security settings, rate limits, transaction limits, " +
        "availability schedules, and feature management.")
public class ChannelConfigDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier for the channel configuration",
            example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @FilterableId
    @NotNull(message = "Tenant ID is required")
    @Schema(description = "Reference to the tenant that owns this channel configuration", required = true)
    private UUID tenantId;

    @NotBlank(message = "Channel code is required")
    @Size(max = 50, message = "Channel code must not exceed 50 characters")
    @Schema(description = "Channel code for programmatic reference: WEB_BANKING, MOBILE_BANKING, ATM, BRANCH, CALL_CENTER, API, OPEN_BANKING",
            required = true, example = "WEB_BANKING")
    private String channelCode;

    @NotBlank(message = "Channel name is required")
    @Size(max = 100, message = "Channel name must not exceed 100 characters")
    @Schema(description = "Display name of the channel", required = true, example = "Web Banking")
    private String channelName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Schema(description = "Detailed description of the channel and its purpose",
            example = "Primary web banking channel for desktop users")
    private String description;

    @Schema(description = "Channel type: DIGITAL, PHYSICAL, API, PARTNER", example = "DIGITAL")
    private String channelType;

    // Authentication and Security

    @Schema(description = "Indicates if authentication is required for this channel", example = "true")
    private Boolean requiresAuthentication;

    @Schema(description = "Indicates if biometric authentication is supported", example = "true")
    private Boolean supportsBiometricAuth;

    @Min(value = 1, message = "Max concurrent sessions must be at least 1")
    @Schema(description = "Maximum number of concurrent sessions allowed per user", example = "3")
    private Integer maxConcurrentSessions;

    @Min(value = 1, message = "Session timeout must be at least 1 minute")
    @Schema(description = "Session timeout in minutes", example = "30")
    private Integer sessionTimeoutMinutes;

    @Min(value = 1, message = "Idle timeout must be at least 1 minute")
    @Schema(description = "Idle timeout in minutes before automatic logout", example = "15")
    private Integer idleTimeoutMinutes;

    // Rate Limiting

    @Min(value = 1, message = "Rate limit per minute must be at least 1")
    @Schema(description = "Maximum requests allowed per minute", example = "60")
    private Integer rateLimitPerMinute;

    @Min(value = 1, message = "Rate limit per hour must be at least 1")
    @Schema(description = "Maximum requests allowed per hour", example = "3600")
    private Integer rateLimitPerHour;

    // Transaction Limits

    @DecimalMin(value = "0.0", inclusive = false, message = "Max transaction amount must be positive")
    @Schema(description = "Maximum transaction amount allowed for this channel", example = "50000.00")
    private BigDecimal maxTransactionAmount;

    @DecimalMin(value = "0.0", inclusive = false, message = "Daily transaction limit must be positive")
    @Schema(description = "Daily transaction limit for this channel", example = "100000.00")
    private BigDecimal dailyTransactionLimit;

    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly transaction limit must be positive")
    @Schema(description = "Monthly transaction limit for this channel", example = "1000000.00")
    private BigDecimal monthlyTransactionLimit;

    // Platform and Version Support

    @Size(max = 20, message = "Min app version must not exceed 20 characters")
    @Schema(description = "Minimum app version required (for mobile channels)", example = "3.0.0")
    private String minAppVersion;

    @Size(max = 255, message = "Supported platforms must not exceed 255 characters")
    @Schema(description = "Comma-separated list of supported platforms", example = "iOS,Android,Web")
    private String supportedPlatforms;

    // Availability and Maintenance

    @Schema(description = "Maintenance window start time (HH:mm:ss)", example = "02:00:00")
    private LocalTime maintenanceWindowStart;

    @Schema(description = "Maintenance window end time (HH:mm:ss)", example = "04:00:00")
    private LocalTime maintenanceWindowEnd;

    @Schema(description = "Availability schedule in cron format or JSON", example = "0 0 * * * *")
    private String availabilitySchedule;

    // Geographic Restrictions

    @Schema(description = "Indicates if geographic restrictions are enabled", example = "false")
    private Boolean geoRestrictionsEnabled;

    @Size(max = 500, message = "Allowed countries must not exceed 500 characters")
    @Schema(description = "Comma-separated list of allowed country codes (ISO 3166-1 alpha-2)", example = "US,CA,GB")
    private String allowedCountries;

    @Size(max = 500, message = "Blocked countries must not exceed 500 characters")
    @Schema(description = "Comma-separated list of blocked country codes (ISO 3166-1 alpha-2)", example = "KP,IR")
    private String blockedCountries;

    @Schema(description = "Comma-separated list of whitelisted IP addresses or CIDR ranges", example = "192.168.1.0/24,10.0.0.1")
    private String ipWhitelist;

    // Feature Management

    @Schema(description = "Comma-separated list of enabled features for this channel", example = "TRANSFERS,BILL_PAY,MOBILE_DEPOSIT")
    private String featuresEnabled;

    @Schema(description = "Comma-separated list of disabled features for this channel", example = "WIRE_TRANSFER,INTERNATIONAL_TRANSFER")
    private String featuresDisabled;

    @Schema(description = "Indicates if custom branding is enabled for this channel", example = "true")
    private Boolean customBrandingEnabled;

    @Schema(description = "Indicates if analytics tracking is enabled", example = "true")
    private Boolean analyticsEnabled;

    @Schema(description = "Logging level: DEBUG, INFO, WARN, ERROR", example = "INFO")
    private String loggingLevel;

    // Failover and Priority

    @Schema(description = "Indicates if this channel is enabled", example = "true")
    private Boolean enabled;

    @Min(value = 1, message = "Priority must be at least 1")
    @Schema(description = "Channel priority for failover (1 = highest priority)", example = "1")
    private Integer priority;

    @Schema(description = "Reference to backup channel if this one fails (Foreign Key to channel_configs)")
    private UUID failoverChannelId;

    // System Fields

    @Schema(description = "Additional metadata in JSON format for extensibility", example = "{\"custom_field\":\"value\"}")
    private String metadata;

    @Schema(description = "Indicates if this channel configuration is active", example = "true")
    private Boolean active;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Version number for optimistic locking", accessMode = Schema.AccessMode.READ_ONLY)
    private Long version;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Timestamp when channel configuration was created", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Timestamp when channel configuration was last updated", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}

