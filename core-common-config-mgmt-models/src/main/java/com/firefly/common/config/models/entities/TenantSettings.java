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
 * Entity representing comprehensive operational settings for a tenant.
 *
 * <p>Manages security policies, rate limits, compliance settings, fraud detection,
 * maintenance windows, circuit breakers, and other operational parameters for each tenant.</p>
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Security and authentication policies</li>
 *   <li>Rate limiting and throttling</li>
 *   <li>Compliance and regulatory settings (GDPR, PCI-DSS, AML, KYC)</li>
 *   <li>Fraud detection and monitoring</li>
 *   <li>Data retention and backup policies</li>
 *   <li>Circuit breaker and resilience configuration</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("tenant_settings")
public class TenantSettings {

    /**
     * Unique identifier for the tenant settings (Primary Key)
     */
    @Id
    private UUID id;

    /**
     * Reference to the tenant (Foreign Key to tenants)
     */
    @Column("tenant_id")
    private UUID tenantId;

    // ========================================
    // Rate Limiting
    // ========================================

    /**
     * Maximum API requests allowed per minute
     */
    @Column("api_rate_limit_per_minute")
    private Integer apiRateLimitPerMinute;

    /**
     * Maximum API requests allowed per hour
     */
    @Column("api_rate_limit_per_hour")
    private Integer apiRateLimitPerHour;

    /**
     * Maximum API requests allowed per day
     */
    @Column("api_rate_limit_per_day")
    private Integer apiRateLimitPerDay;

    // ========================================
    // Security Policies - Password
    // ========================================

    /**
     * Minimum password length required
     */
    @Column("password_min_length")
    private Integer passwordMinLength;

    /**
     * Indicates if password must contain uppercase letters
     */
    @Column("password_require_uppercase")
    private Boolean passwordRequireUppercase;

    /**
     * Indicates if password must contain lowercase letters
     */
    @Column("password_require_lowercase")
    private Boolean passwordRequireLowercase;

    /**
     * Indicates if password must contain numbers
     */
    @Column("password_require_numbers")
    private Boolean passwordRequireNumbers;

    /**
     * Indicates if password must contain special characters
     */
    @Column("password_require_special_chars")
    private Boolean passwordRequireSpecialChars;

    /**
     * Number of days before password expires
     */
    @Column("password_expiry_days")
    private Integer passwordExpiryDays;

    /**
     * Number of previous passwords to check against reuse
     */
    @Column("password_history_count")
    private Integer passwordHistoryCount;

    // ========================================
    // Security Policies - Authentication
    // ========================================

    /**
     * Indicates if multi-factor authentication is enabled
     */
    @Column("mfa_enabled")
    private Boolean mfaEnabled;

    /**
     * Indicates if multi-factor authentication is required for all users
     */
    @Column("mfa_required")
    private Boolean mfaRequired;

    /**
     * Comma-separated list of allowed 2FA methods (SMS, EMAIL, TOTP, BIOMETRIC)
     */
    @Column("two_factor_auth_methods")
    private String twoFactorAuthMethods;

    /**
     * Session timeout in minutes (absolute timeout)
     */
    @Column("session_timeout_minutes")
    private Integer sessionTimeoutMinutes;

    /**
     * Idle timeout in minutes (inactivity timeout)
     */
    @Column("session_idle_timeout_minutes")
    private Integer sessionIdleTimeoutMinutes;

    /**
     * Maximum number of concurrent sessions per user
     */
    @Column("concurrent_sessions_limit")
    private Integer concurrentSessionsLimit;

    /**
     * Maximum number of failed login attempts before lockout
     */
    @Column("max_login_attempts")
    private Integer maxLoginAttempts;

    /**
     * Account lockout duration in minutes after max login attempts
     */
    @Column("account_lockout_duration_minutes")
    private Integer accountLockoutDurationMinutes;

    /**
     * Indicates if users must change password on first login
     */
    @Column("force_password_change_on_first_login")
    private Boolean forcePasswordChangeOnFirstLogin;

    /**
     * Number of days before API keys must be rotated
     */
    @Column("api_key_rotation_days")
    private Integer apiKeyRotationDays;

    /**
     * Encryption algorithm used (e.g., "AES-256-GCM", "RSA-2048")
     */
    @Column("encryption_algorithm")
    private String encryptionAlgorithm;

    // ========================================
    // Security Policies - Network
    // ========================================

    /**
     * IP whitelist in JSON array format
     */
    @Column("ip_whitelist")
    private String ipWhitelist;

    /**
     * IP blacklist in JSON array format
     */
    @Column("ip_blacklist")
    private String ipBlacklist;

    /**
     * Indicates if geo-blocking is enabled
     */
    @Column("geo_blocking_enabled")
    private Boolean geoBlockingEnabled;

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

    // ========================================
    // Compliance and Regulatory
    // ========================================

    /**
     * Indicates if GDPR compliance features are enabled
     */
    @Column("gdpr_enabled")
    private Boolean gdprEnabled;

    /**
     * Indicates if PCI-DSS compliance features are enabled
     */
    @Column("pci_dss_enabled")
    private Boolean pciDssEnabled;

    /**
     * Indicates if SOX compliance features are enabled
     */
    @Column("sox_compliance_enabled")
    private Boolean soxComplianceEnabled;

    /**
     * Indicates if HIPAA compliance features are enabled
     */
    @Column("hipaa_compliance_enabled")
    private Boolean hipaaComplianceEnabled;

    /**
     * Indicates if ISO 27001 compliance features are enabled
     */
    @Column("iso27001_compliance_enabled")
    private Boolean iso27001ComplianceEnabled;

    /**
     * Country code for data residency requirements (ISO 3166-1 alpha-2)
     */
    @Column("data_residency_country")
    private String dataResidencyCountry;

    /**
     * Indicates if regulatory reporting is enabled
     */
    @Column("regulatory_reporting_enabled")
    private Boolean regulatoryReportingEnabled;

    /**
     * Frequency of regulatory reporting: DAILY, WEEKLY, MONTHLY, QUARTERLY
     */
    @Column("regulatory_reporting_frequency")
    private String regulatoryReportingFrequency;

    // ========================================
    // Fraud Detection and Monitoring
    // ========================================

    /**
     * Indicates if fraud detection is enabled
     */
    @Column("fraud_detection_enabled")
    private Boolean fraudDetectionEnabled;

    /**
     * Fraud score threshold (0-100) for flagging transactions
     */
    @Column("fraud_score_threshold")
    private Integer fraudScoreThreshold;

    /**
     * Indicates if transaction monitoring is enabled
     */
    @Column("transaction_monitoring_enabled")
    private Boolean transactionMonitoringEnabled;

    /**
     * Indicates if AML (Anti-Money Laundering) screening is enabled
     */
    @Column("aml_screening_enabled")
    private Boolean amlScreeningEnabled;

    /**
     * Indicates if sanctions screening is enabled
     */
    @Column("sanctions_screening_enabled")
    private Boolean sanctionsScreeningEnabled;

    /**
     * Indicates if KYC (Know Your Customer) verification is required
     */
    @Column("kyc_verification_required")
    private Boolean kycVerificationRequired;

    /**
     * Number of days before KYC information must be refreshed
     */
    @Column("kyc_refresh_interval_days")
    private Integer kycRefreshIntervalDays;

    /**
     * Indicates if document verification is required
     */
    @Column("document_verification_required")
    private Boolean documentVerificationRequired;

    /**
     * Indicates if biometric verification is enabled
     */
    @Column("biometric_verification_enabled")
    private Boolean biometricVerificationEnabled;

    // ========================================
    // Circuit Breaker Configuration
    // ========================================

    /**
     * Indicates if circuit breaker pattern is enabled
     */
    @Column("circuit_breaker_enabled")
    private Boolean circuitBreakerEnabled;

    /**
     * Number of failures before circuit breaker opens
     */
    @Column("circuit_breaker_failure_threshold")
    private Integer circuitBreakerFailureThreshold;

    /**
     * Timeout in seconds before circuit breaker attempts to close
     */
    @Column("circuit_breaker_timeout_seconds")
    private Integer circuitBreakerTimeoutSeconds;

    /**
     * Reset timeout in seconds for circuit breaker
     */
    @Column("circuit_breaker_reset_timeout_seconds")
    private Integer circuitBreakerResetTimeoutSeconds;

    // ========================================
    // Maintenance Windows
    // ========================================

    /**
     * Indicates if maintenance mode is currently enabled
     */
    @Column("maintenance_mode_enabled")
    private Boolean maintenanceModeEnabled;

    /**
     * Start time of maintenance window
     */
    @Column("maintenance_start_time")
    private LocalDateTime maintenanceStartTime;

    /**
     * End time of maintenance window
     */
    @Column("maintenance_end_time")
    private LocalDateTime maintenanceEndTime;

    /**
     * Message to display during maintenance
     */
    @Column("maintenance_message")
    private String maintenanceMessage;

    // ========================================
    // Audit and Logging
    // ========================================

    /**
     * Indicates if audit logging is enabled
     */
    @Column("audit_enabled")
    private Boolean auditEnabled;

    /**
     * Number of days to retain audit logs
     */
    @Column("audit_retention_days")
    private Integer auditRetentionDays;

    /**
     * Logging level: DEBUG, INFO, WARN, ERROR
     */
    @Column("log_level")
    private String logLevel;

    /**
     * Indicates if sensitive data masking is enabled in logs
     */
    @Column("sensitive_data_masking_enabled")
    private Boolean sensitiveDataMaskingEnabled;

    // ========================================
    // Data Retention and Backup
    // ========================================

    /**
     * Data retention policy: STANDARD, EXTENDED, MINIMAL, CUSTOM
     */
    @Column("data_retention_policy")
    private String dataRetentionPolicy;

    /**
     * Number of days to retain transaction data
     */
    @Column("transaction_retention_days")
    private Integer transactionRetentionDays;

    /**
     * Number of days to retain document data
     */
    @Column("document_retention_days")
    private Integer documentRetentionDays;

    /**
     * Indicates if automatic archiving is enabled
     */
    @Column("auto_archive_enabled")
    private Boolean autoArchiveEnabled;

    /**
     * Number of days before data is automatically archived
     */
    @Column("archive_after_days")
    private Integer archiveAfterDays;

    /**
     * Indicates if automatic deletion is enabled
     */
    @Column("auto_delete_enabled")
    private Boolean autoDeleteEnabled;

    /**
     * Number of days before data is automatically deleted
     */
    @Column("delete_after_days")
    private Integer deleteAfterDays;

    /**
     * Indicates if backup is enabled
     */
    @Column("backup_enabled")
    private Boolean backupEnabled;

    /**
     * Backup frequency in hours
     */
    @Column("backup_frequency_hours")
    private Integer backupFrequencyHours;

    /**
     * Number of days to retain backups
     */
    @Column("backup_retention_days")
    private Integer backupRetentionDays;

    // ========================================
    // Disaster Recovery
    // ========================================

    /**
     * Indicates if disaster recovery is enabled
     */
    @Column("disaster_recovery_enabled")
    private Boolean disasterRecoveryEnabled;

    /**
     * Indicates if point-in-time recovery is enabled
     */
    @Column("point_in_time_recovery_enabled")
    private Boolean pointInTimeRecoveryEnabled;

    /**
     * Indicates if cross-region replication is enabled
     */
    @Column("cross_region_replication_enabled")
    private Boolean crossRegionReplicationEnabled;

    /**
     * Comma-separated list of replication regions
     */
    @Column("replication_regions")
    private String replicationRegions;

    // ========================================
    // Notification Settings
    // ========================================

    /**
     * Indicates if email notifications are enabled
     */
    @Column("email_notifications_enabled")
    private Boolean emailNotificationsEnabled;

    /**
     * Indicates if SMS notifications are enabled
     */
    @Column("sms_notifications_enabled")
    private Boolean smsNotificationsEnabled;

    /**
     * Indicates if push notifications are enabled
     */
    @Column("push_notifications_enabled")
    private Boolean pushNotificationsEnabled;

    /**
     * Indicates if webhook notifications are enabled
     */
    @Column("webhook_notifications_enabled")
    private Boolean webhookNotificationsEnabled;

    // ========================================
    // System Fields
    // ========================================

    /**
     * Additional metadata in JSON format for extensibility
     */
    @Column("metadata")
    private String metadata;

    /**
     * Indicates if these settings are active
     */
    @Column("active")
    private Boolean active;

    /**
     * Version number for optimistic locking
     */
    @Version
    @Column("version")
    private Long version;

    /**
     * Timestamp when settings were created
     */
    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp when settings were last updated
     */
    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}


