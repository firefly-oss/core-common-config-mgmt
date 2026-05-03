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

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.utils.annotations.FilterableId;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for TenantSettings entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Operational settings for a tenant")
public class TenantSettingsDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Settings ID")
    private UUID id;

    @NotNull(message = "Tenant ID is required")
    @FilterableId
    @Schema(description = "Tenant ID")
    private UUID tenantId;

    // Rate Limiting
    @Min(value = 0, message = "Rate limit must be non-negative")
    @Schema(description = "API rate limit per minute", example = "100")
    private Integer apiRateLimitPerMinute;

    @Min(value = 0, message = "Rate limit must be non-negative")
    @Schema(description = "API rate limit per hour", example = "5000")
    private Integer apiRateLimitPerHour;

    @Min(value = 0, message = "Rate limit must be non-negative")
    @Schema(description = "API rate limit per day", example = "100000")
    private Integer apiRateLimitPerDay;

    // Security Policies
    @Min(value = 6, message = "Password minimum length must be at least 6")
    @Schema(description = "Minimum password length", example = "12")
    private Integer passwordMinLength;

    @Schema(description = "Require uppercase letters in password", example = "true")
    private Boolean passwordRequireUppercase;

    @Schema(description = "Require lowercase letters in password", example = "true")
    private Boolean passwordRequireLowercase;

    @Schema(description = "Require numbers in password", example = "true")
    private Boolean passwordRequireNumbers;

    @Schema(description = "Require special characters in password", example = "true")
    private Boolean passwordRequireSpecialChars;

    @Min(value = 0, message = "Password expiry days must be non-negative")
    @Schema(description = "Password expiry in days (0 = never)", example = "90")
    private Integer passwordExpiryDays;

    @Min(value = 0, message = "Password history count must be non-negative")
    @Schema(description = "Number of previous passwords to remember (prevents reuse)", example = "5")
    private Integer passwordHistoryCount;

    @Schema(description = "Whether MFA is enabled", example = "true")
    private Boolean mfaEnabled;

    @Schema(description = "Whether MFA is required", example = "false")
    private Boolean mfaRequired;

    @Schema(description = "Comma-separated list of allowed 2FA methods: SMS, EMAIL, TOTP, BIOMETRIC", example = "TOTP,SMS")
    private String twoFactorAuthMethods;

    @Min(value = 1, message = "Session timeout must be at least 1 minute")
    @Schema(description = "Session timeout in minutes", example = "30")
    private Integer sessionTimeoutMinutes;

    @Min(value = 1, message = "Session idle timeout must be at least 1 minute")
    @Schema(description = "Session idle timeout in minutes before automatic logout", example = "15")
    private Integer sessionIdleTimeoutMinutes;

    @Min(value = 1, message = "Concurrent sessions limit must be at least 1")
    @Schema(description = "Maximum number of concurrent sessions allowed per user", example = "3")
    private Integer concurrentSessionsLimit;

    @Min(value = 1, message = "Max login attempts must be at least 1")
    @Schema(description = "Maximum login attempts before lockout", example = "5")
    private Integer maxLoginAttempts;

    @Min(value = 0, message = "Lockout duration must be non-negative")
    @Schema(description = "Account lockout duration in minutes", example = "30")
    private Integer accountLockoutDurationMinutes;

    @Schema(description = "Force password change on first login", example = "true")
    private Boolean forcePasswordChangeOnFirstLogin;

    @Min(value = 1, message = "API key rotation days must be at least 1")
    @Schema(description = "API key rotation interval in days", example = "90")
    private Integer apiKeyRotationDays;

    @Schema(description = "Encryption algorithm used: AES256, RSA2048, etc.", example = "AES256")
    private String encryptionAlgorithm;

    // Network Security

    @Schema(description = "Comma-separated list of whitelisted IP addresses or CIDR ranges", example = "192.168.1.0/24,10.0.0.1")
    private String ipWhitelist;

    @Schema(description = "Comma-separated list of blacklisted IP addresses or CIDR ranges", example = "203.0.113.0/24")
    private String ipBlacklist;

    @Schema(description = "Whether geographic blocking is enabled", example = "false")
    private Boolean geoBlockingEnabled;

    @Schema(description = "Comma-separated list of allowed country codes (ISO 3166-1 alpha-2)", example = "US,CA,GB")
    private String allowedCountries;

    @Schema(description = "Comma-separated list of blocked country codes (ISO 3166-1 alpha-2)", example = "KP,IR")
    private String blockedCountries;

    // Compliance

    @Schema(description = "Whether GDPR compliance is enabled", example = "true")
    private Boolean gdprEnabled;

    @Schema(description = "Whether PCI-DSS compliance is enabled", example = "true")
    private Boolean pciDssEnabled;

    @Schema(description = "Whether SOX compliance is enabled", example = "false")
    private Boolean soxComplianceEnabled;

    @Schema(description = "Whether HIPAA compliance is enabled", example = "false")
    private Boolean hipaaComplianceEnabled;

    @Schema(description = "Whether ISO 27001 compliance is enabled", example = "true")
    private Boolean iso27001ComplianceEnabled;

    @Schema(description = "Data residency country code (ISO 3166-1 alpha-2)", example = "US")
    private String dataResidencyCountry;

    @Schema(description = "Whether regulatory reporting is enabled", example = "true")
    private Boolean regulatoryReportingEnabled;

    @Schema(description = "Regulatory reporting frequency: DAILY, WEEKLY, MONTHLY, QUARTERLY", example = "MONTHLY")
    private String regulatoryReportingFrequency;

    // Fraud Detection and Monitoring

    @Schema(description = "Whether fraud detection is enabled", example = "true")
    private Boolean fraudDetectionEnabled;

    @Min(value = 0, message = "Fraud score threshold must be non-negative")
    @Schema(description = "Fraud score threshold (0-100) for flagging transactions", example = "75")
    private Integer fraudScoreThreshold;

    @Schema(description = "Whether transaction monitoring is enabled", example = "true")
    private Boolean transactionMonitoringEnabled;

    @Schema(description = "Whether AML (Anti-Money Laundering) screening is enabled", example = "true")
    private Boolean amlScreeningEnabled;

    @Schema(description = "Whether sanctions screening is enabled", example = "true")
    private Boolean sanctionsScreeningEnabled;

    @Schema(description = "Whether KYC (Know Your Customer) verification is required", example = "true")
    private Boolean kycVerificationRequired;

    @Min(value = 1, message = "KYC refresh interval must be at least 1 day")
    @Schema(description = "KYC refresh interval in days", example = "365")
    private Integer kycRefreshIntervalDays;

    @Schema(description = "Whether document verification is required", example = "true")
    private Boolean documentVerificationRequired;

    @Schema(description = "Whether biometric verification is enabled", example = "false")
    private Boolean biometricVerificationEnabled;

    // Circuit Breaker Configuration
    @Schema(description = "Whether circuit breaker is enabled", example = "true")
    private Boolean circuitBreakerEnabled;

    @Min(value = 1, message = "Failure threshold must be at least 1")
    @Schema(description = "Circuit breaker failure threshold", example = "5")
    private Integer circuitBreakerFailureThreshold;

    @Min(value = 1, message = "Timeout must be at least 1 second")
    @Schema(description = "Circuit breaker timeout in seconds", example = "30")
    private Integer circuitBreakerTimeoutSeconds;

    @Min(value = 1, message = "Reset timeout must be at least 1 second")
    @Schema(description = "Circuit breaker reset timeout in seconds", example = "60")
    private Integer circuitBreakerResetTimeoutSeconds;

    // Maintenance Windows
    @Schema(description = "Whether maintenance mode is enabled", example = "false")
    private Boolean maintenanceModeEnabled;

    @Schema(description = "Maintenance start time")
    private LocalDateTime maintenanceStartTime;

    @Schema(description = "Maintenance end time")
    private LocalDateTime maintenanceEndTime;

    @Schema(description = "Maintenance message to display", example = "System under maintenance")
    private String maintenanceMessage;

    // Audit and Logging
    @Schema(description = "Whether audit is enabled", example = "true")
    private Boolean auditEnabled;

    @Min(value = 1, message = "Retention days must be at least 1")
    @Schema(description = "Audit retention in days", example = "365")
    private Integer auditRetentionDays;

    @Schema(description = "Log level (DEBUG, INFO, WARN, ERROR)", example = "INFO")
    private String logLevel;

    @Schema(description = "Whether sensitive data masking is enabled", example = "true")
    private Boolean sensitiveDataMaskingEnabled;

    // Data Retention and Archiving

    @Schema(description = "Data retention policy description or JSON configuration", example = "STANDARD_7_YEARS")
    private String dataRetentionPolicy;

    @Min(value = 1, message = "Retention days must be at least 1")
    @Schema(description = "Transaction retention in days", example = "2555")
    private Integer transactionRetentionDays;

    @Min(value = 1, message = "Retention days must be at least 1")
    @Schema(description = "Document retention in days", example = "2555")
    private Integer documentRetentionDays;

    @Schema(description = "Whether auto-archiving is enabled", example = "true")
    private Boolean autoArchiveEnabled;

    @Min(value = 1, message = "Archive after days must be at least 1")
    @Schema(description = "Number of days after which data is automatically archived", example = "365")
    private Integer archiveAfterDays;

    @Schema(description = "Whether auto-deletion is enabled", example = "false")
    private Boolean autoDeleteEnabled;

    @Min(value = 1, message = "Delete after days must be at least 1")
    @Schema(description = "Number of days after which archived data is automatically deleted", example = "2555")
    private Integer deleteAfterDays;

    @Schema(description = "Whether backup is enabled", example = "true")
    private Boolean backupEnabled;

    @Min(value = 1, message = "Backup frequency must be at least 1 hour")
    @Schema(description = "Backup frequency in hours", example = "24")
    private Integer backupFrequencyHours;

    @Min(value = 1, message = "Backup retention days must be at least 1")
    @Schema(description = "Backup retention in days", example = "90")
    private Integer backupRetentionDays;

    // Disaster Recovery

    @Schema(description = "Whether disaster recovery is enabled", example = "true")
    private Boolean disasterRecoveryEnabled;

    @Schema(description = "Whether point-in-time recovery is enabled", example = "true")
    private Boolean pointInTimeRecoveryEnabled;

    @Schema(description = "Whether cross-region replication is enabled", example = "false")
    private Boolean crossRegionReplicationEnabled;

    @Schema(description = "Comma-separated list of replication regions", example = "us-east-1,eu-west-1")
    private String replicationRegions;

    // Notification Settings

    @Schema(description = "Whether email notifications are enabled", example = "true")
    private Boolean emailNotificationsEnabled;

    @Schema(description = "Whether SMS notifications are enabled", example = "true")
    private Boolean smsNotificationsEnabled;

    @Schema(description = "Whether push notifications are enabled", example = "true")
    private Boolean pushNotificationsEnabled;

    @Schema(description = "Whether webhook notifications are enabled", example = "true")
    private Boolean webhookNotificationsEnabled;

    // System Fields

    @Schema(description = "Additional metadata (JSON)")
    private String metadata;

    @Schema(description = "Whether the settings are active", example = "true")
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

