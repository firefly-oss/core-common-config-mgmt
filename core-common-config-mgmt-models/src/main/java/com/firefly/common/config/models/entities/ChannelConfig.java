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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Entity representing channel configuration for a tenant.
 *
 * <p>Manages configuration for different banking channels (Web, Mobile, ATM, Branch, API, etc.)
 * in the Firefly core banking system. Each tenant can have multiple channels with specific
 * configurations, security settings, and operational parameters.</p>
 *
 * <p>This entity contains essential channel fields. Additional dynamic configuration parameters
 * are stored in ChannelConfigParameter using the EAV (Entity-Attribute-Value) pattern for
 * maximum flexibility without schema changes.</p>
 *
 * <p>Channel Types:</p>
 * <ul>
 *   <li>WEB_BANKING - Web browser-based banking</li>
 *   <li>MOBILE_BANKING - Mobile app banking</li>
 *   <li>ATM - Automated Teller Machine</li>
 *   <li>BRANCH - Physical branch operations</li>
 *   <li>CALL_CENTER - Phone banking</li>
 *   <li>API - Programmatic API access</li>
 *   <li>OPEN_BANKING - Open Banking API</li>
 *   <li>PARTNER - Partner integration channel</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("channel_configs")
public class ChannelConfig {

    /**
     * Unique identifier for the channel configuration (Primary Key)
     */
    @Id
    private UUID id;

    /**
     * Reference to the tenant (Foreign Key to tenants)
     */
    @Column("tenant_id")
    private UUID tenantId;

    // ========================================
    // Channel Identification
    // ========================================

    /**
     * Channel code for programmatic reference
     * Examples: WEB_BANKING, MOBILE_BANKING, ATM, BRANCH, CALL_CENTER, API, OPEN_BANKING
     */
    @Column("channel_code")
    private String channelCode;

    /**
     * Display name of the channel
     */
    @Column("channel_name")
    private String channelName;

    /**
     * Detailed description of the channel and its purpose
     */
    @Column("description")
    private String description;

    /**
     * Channel type: DIGITAL, PHYSICAL, API, PARTNER
     */
    @Column("channel_type")
    private String channelType;

    // ========================================
    // Authentication and Security
    // ========================================

    /**
     * Indicates if authentication is required for this channel
     */
    @Column("requires_authentication")
    private Boolean requiresAuthentication;

    /**
     * Indicates if biometric authentication is supported
     */
    @Column("supports_biometric_auth")
    private Boolean supportsBiometricAuth;

    /**
     * Maximum number of concurrent sessions allowed per user
     */
    @Column("max_concurrent_sessions")
    private Integer maxConcurrentSessions;

    /**
     * Session timeout in minutes (absolute timeout)
     */
    @Column("session_timeout_minutes")
    private Integer sessionTimeoutMinutes;

    /**
     * Idle timeout in minutes (inactivity timeout)
     */
    @Column("idle_timeout_minutes")
    private Integer idleTimeoutMinutes;

    // ========================================
    // Rate Limiting
    // ========================================

    /**
     * Maximum requests per minute per user
     */
    @Column("rate_limit_per_minute")
    private Integer rateLimitPerMinute;

    /**
     * Maximum requests per hour per user
     */
    @Column("rate_limit_per_hour")
    private Integer rateLimitPerHour;

    // ========================================
    // Transaction Limits
    // ========================================

    /**
     * Maximum transaction amount allowed per transaction
     */
    @Column("max_transaction_amount")
    private BigDecimal maxTransactionAmount;

    /**
     * Daily transaction limit (total amount)
     */
    @Column("daily_transaction_limit")
    private BigDecimal dailyTransactionLimit;

    /**
     * Monthly transaction limit (total amount)
     */
    @Column("monthly_transaction_limit")
    private BigDecimal monthlyTransactionLimit;


    // ========================================
    // Platform and Version Support
    // ========================================

    /**
     * Minimum app version required (for mobile channels)
     */
    @Column("min_app_version")
    private String minAppVersion;

    /**
     * Comma-separated list of supported platforms (e.g., "iOS,Android,Web")
     */
    @Column("supported_platforms")
    private String supportedPlatforms;

    // ========================================
    // Availability and Maintenance
    // ========================================

    /**
     * Start time of maintenance window
     */
    @Column("maintenance_window_start")
    private LocalTime maintenanceWindowStart;

    /**
     * End time of maintenance window
     */
    @Column("maintenance_window_end")
    private LocalTime maintenanceWindowEnd;

    /**
     * Availability schedule in JSON format (e.g., business hours, 24/7)
     */
    @Column("availability_schedule")
    private String availabilitySchedule;

    // ========================================
    // Geographic Restrictions
    // ========================================

    /**
     * Indicates if geographic restrictions are enabled
     */
    @Column("geo_restrictions_enabled")
    private Boolean geoRestrictionsEnabled;

    /**
     * Comma-separated list of allowed country codes (ISO 3166-1 alpha-2)
     */
    @Column("allowed_countries")
    private String allowedCountries;

    /**
     * Comma-separated list of blocked country codes (ISO 3166-1 alpha-2)
     */
    @Column("blocked_countries")
    private String blockedCountries;

    /**
     * IP whitelist in JSON array format
     */
    @Column("ip_whitelist")
    private String ipWhitelist;

    // ========================================
    // Feature Management
    // ========================================

    /**
     * Comma-separated list of enabled features for this channel
     */
    @Column("features_enabled")
    private String featuresEnabled;

    /**
     * Comma-separated list of disabled features for this channel
     */
    @Column("features_disabled")
    private String featuresDisabled;

    /**
     * Indicates if custom branding is enabled for this channel
     */
    @Column("custom_branding_enabled")
    private Boolean customBrandingEnabled;

    /**
     * Indicates if analytics tracking is enabled
     */
    @Column("analytics_enabled")
    private Boolean analyticsEnabled;

    /**
     * Logging level: DEBUG, INFO, WARN, ERROR
     */
    @Column("logging_level")
    private String loggingLevel;

    // ========================================
    // Failover and Priority
    // ========================================

    /**
     * Indicates if this channel is enabled
     */
    @Column("enabled")
    private Boolean enabled;

    /**
     * Channel priority for failover (1 = highest priority)
     */
    @Column("priority")
    private Integer priority;

    /**
     * Reference to backup channel if this one fails (Foreign Key to channel_configs)
     */
    @Column("failover_channel_id")
    private UUID failoverChannelId;

    // ========================================
    // System Fields
    // ========================================

    /**
     * Additional metadata in JSON format for extensibility
     */
    @Column("metadata")
    private String metadata;

    /**
     * Indicates if this channel configuration is active
     */
    @Column("active")
    private Boolean active;

    /**
     * Version number for optimistic locking
     */
    @Version
    private Long version;

    /**
     * Timestamp when channel configuration was created
     */
    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp when channel configuration was last updated
     */
    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}


