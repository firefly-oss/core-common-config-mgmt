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
import java.util.UUID;

/**
 * Entity representing the relationship between a Provider and a Tenant.
 *
 * <p>This entity manages which providers are available for each tenant in the Firefly
 * core banking system, enabling multi-tenant provider management with tenant-specific
 * configurations, billing, and operational settings.</p>
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Provider assignment and priority management per tenant</li>
 *   <li>Tenant-specific billing and cost tracking</li>
 *   <li>Failover and circuit breaker configuration</li>
 *   <li>Usage metrics and performance monitoring</li>
 *   <li>Approval workflow for provider assignments</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("provider_tenants")
public class ProviderTenant {

    /**
     * Unique identifier for the provider-tenant relationship (Primary Key)
     */
    @Id
    private UUID id;

    /**
     * Reference to the provider (Foreign Key to providers)
     */
    @Column("provider_id")
    private UUID providerId;

    /**
     * Reference to the tenant (Foreign Key to tenants)
     */
    @Column("tenant_id")
    private UUID tenantId;

    // ========================================
    // Relationship Configuration
    // ========================================

    /**
     * Indicates if this is the primary provider for this tenant
     * Only one provider of each type should be primary per tenant
     */
    @Column("is_primary")
    private Boolean isPrimary;

    /**
     * Priority for provider selection (1 = highest priority)
     * Used for failover scenarios when multiple providers are available
     */
    @Column("priority")
    private Integer priority;

    /**
     * Type of relationship: STANDARD, PREMIUM, TRIAL, CUSTOM
     * Determines service level and feature access
     */
    @Column("relationship_type")
    private String relationshipType;

    /**
     * Tenant-specific configuration overrides in JSON format
     * Overrides global provider configuration for this tenant
     */
    @Column("configuration_override")
    private String configurationOverride;

    /**
     * Indicates if this provider is enabled for the tenant
     */
    @Column("enabled")
    private Boolean enabled;

    /**
     * Date when the provider relationship starts
     */
    @Column("start_date")
    private LocalDateTime startDate;

    /**
     * Date when the provider relationship ends
     */
    @Column("end_date")
    private LocalDateTime endDate;

    // ========================================
    // Billing and Cost Management
    // ========================================

    /**
     * Billing model: TRANSACTION_BASED, VOLUME_BASED, FLAT_FEE, HYBRID
     */
    @Column("billing_model")
    private String billingModel;

    /**
     * Maximum number of transactions allowed per month
     */
    @Column("monthly_transaction_limit")
    private Integer monthlyTransactionLimit;

    /**
     * Maximum transaction volume allowed per month (in currency units)
     */
    @Column("monthly_volume_limit")
    private BigDecimal monthlyVolumeLimit;

    /**
     * Cost per transaction for this tenant
     */
    @Column("cost_per_transaction")
    private BigDecimal costPerTransaction;

    /**
     * Monthly subscription fee for this tenant
     */
    @Column("monthly_fee")
    private BigDecimal monthlyFee;

    /**
     * One-time setup fee
     */
    @Column("setup_fee")
    private BigDecimal setupFee;

    /**
     * Currency code for billing (ISO 4217)
     */
    @Column("currency_code")
    private String currencyCode;

    // ========================================
    // Resilience and Failover
    // ========================================

    /**
     * Indicates if automatic failover is enabled
     */
    @Column("auto_failover_enabled")
    private Boolean autoFailoverEnabled;

    /**
     * Reference to fallback provider (Foreign Key to providers)
     * Used when primary provider fails
     */
    @Column("fallback_provider_id")
    private UUID fallbackProviderId;


    /**
     * Indicates if circuit breaker pattern is enabled
     */
    @Column("circuit_breaker_enabled")
    private Boolean circuitBreakerEnabled;

    /**
     * Number of failures before circuit breaker opens
     */
    @Column("circuit_breaker_threshold")
    private Integer circuitBreakerThreshold;

    /**
     * Timeout in seconds before circuit breaker attempts to close
     */
    @Column("circuit_breaker_timeout_seconds")
    private Integer circuitBreakerTimeoutSeconds;

    /**
     * Interval in seconds between health checks
     */
    @Column("health_check_interval_seconds")
    private Integer healthCheckIntervalSeconds;

    // ========================================
    // Usage Metrics and Monitoring
    // ========================================

    /**
     * Timestamp of last usage of this provider by the tenant
     */
    @Column("last_used_at")
    private LocalDateTime lastUsedAt;

    /**
     * Total number of requests made to this provider by the tenant
     */
    @Column("total_requests_count")
    private Long totalRequestsCount;

    /**
     * Total number of failed requests
     */
    @Column("total_failures_count")
    private Long totalFailuresCount;

    /**
     * Average response time in milliseconds
     */
    @Column("average_response_time_ms")
    private Integer averageResponseTimeMs;

    // ========================================
    // Approval and Governance
    // ========================================

    /**
     * Reference to user who approved this provider assignment (Foreign Key to users)
     */
    @Column("approved_by_user_id")
    private UUID approvedByUserId;

    /**
     * Timestamp when the assignment was approved
     */
    @Column("approved_at")
    private LocalDateTime approvedAt;

    /**
     * Notes from the approval process
     */
    @Column("approval_notes")
    private String approvalNotes;

    /**
     * General notes about this provider-tenant relationship
     */
    @Column("notes")
    private String notes;

    // ========================================
    // System Fields
    // ========================================

    /**
     * Additional metadata in JSON format for extensibility
     */
    @Column("metadata")
    private String metadata;

    /**
     * Indicates if this relationship is active
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
     * Timestamp when relationship was created
     */
    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp when relationship was last updated
     */
    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}


