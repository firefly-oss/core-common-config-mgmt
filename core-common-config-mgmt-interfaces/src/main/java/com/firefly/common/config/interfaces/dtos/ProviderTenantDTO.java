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

import jakarta.validation.constraints.NotNull;
import org.fireflyframework.utils.annotations.FilterableId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for ProviderTenant entity.
 *
 * <p>Represents the relationship between a Provider and a Tenant, including configuration,
 * billing, resilience settings, and usage metrics.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Provider-Tenant relationship configuration. Defines how a specific provider " +
        "is configured and used by a tenant, including billing, failover, and usage tracking.")
public class ProviderTenantDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier for the provider-tenant relationship",
            example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @NotNull(message = "Provider ID is required")
    @FilterableId
    @Schema(description = "Reference to the provider in this relationship", required = true)
    private UUID providerId;

    @NotNull(message = "Tenant ID is required")
    @FilterableId
    @Schema(description = "Reference to the tenant in this relationship", required = true)
    private UUID tenantId;

    // Relationship Configuration

    @Schema(description = "Indicates if this is the primary provider for the tenant's operations", example = "true")
    private Boolean isPrimary;

    @Schema(description = "Priority for provider selection (higher = preferred). Used for load balancing and failover",
            example = "10")
    private Integer priority;

    @Schema(description = "Type of relationship: STANDARD, PREMIUM, TRIAL, CUSTOM", example = "PREMIUM")
    private String relationshipType;

    @Schema(description = "Tenant-specific configuration overrides in JSON format",
            example = "{\"api_key\":\"tenant_specific_key\",\"timeout\":5000}")
    private String configurationOverride;

    @Schema(description = "Indicates if this provider is enabled for the tenant", example = "true")
    private Boolean enabled;

    @Schema(description = "Relationship start date and time")
    private LocalDateTime startDate;

    @Schema(description = "Relationship end date and time (null for indefinite)")
    private LocalDateTime endDate;

    @Schema(description = "Notes about this provider-tenant relationship",
            example = "Special configuration for US market compliance")
    private String notes;

    // Billing and Cost Management

    @Schema(description = "Billing model: TRANSACTION_BASED, VOLUME_BASED, FLAT_FEE, HYBRID", example = "TRANSACTION_BASED")
    private String billingModel;

    @Schema(description = "Maximum transactions allowed per month (null for unlimited)", example = "100000")
    private Integer monthlyTransactionLimit;

    @Schema(description = "Cost per transaction in billing currency", example = "0.05")
    private BigDecimal costPerTransaction;

    @Schema(description = "Fixed monthly fee in billing currency", example = "499.99")
    private BigDecimal monthlyFee;

    @Schema(description = "Minimum monthly commitment in billing currency", example = "1000.00")
    private BigDecimal minimumMonthlyCommitment;

    @Schema(description = "Monthly volume limit in billing currency (null for unlimited)", example = "50000.00")
    private BigDecimal monthlyVolumeLimit;

    @Schema(description = "One-time setup fee in billing currency", example = "250.00")
    private BigDecimal setupFee;

    @Schema(description = "ISO 4217 currency code for billing", example = "USD")
    private String billingCurrencyCode;

    @Schema(description = "Alternative currency code field (deprecated, use billingCurrencyCode)", example = "USD")
    private String currencyCode;

    // Resilience and Failover

    @Schema(description = "Indicates if automatic failover is enabled on provider failure", example = "true")
    private Boolean autoFailoverEnabled;

    @FilterableId
    @Schema(description = "Reference to fallback provider if this provider fails")
    private UUID fallbackProviderId;

    @Schema(description = "Maximum consecutive failures before triggering failover", example = "3")
    private Integer maxConsecutiveFailures;

    @Schema(description = "Indicates if circuit breaker pattern is enabled", example = "true")
    private Boolean circuitBreakerEnabled;

    @Schema(description = "Threshold for opening circuit breaker (failure percentage)", example = "50")
    private Integer circuitBreakerThreshold;

    @Schema(description = "Timeout in seconds before attempting to close circuit breaker", example = "60")
    private Integer circuitBreakerTimeoutSeconds;

    @Schema(description = "Health check interval in seconds for this provider-tenant relationship", example = "300")
    private Integer healthCheckIntervalSeconds;

    // Usage Metrics

    @Schema(description = "Timestamp of last successful use of this provider", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime lastUsedAt;

    @Schema(description = "Total number of requests made to this provider", example = "15234", accessMode = Schema.AccessMode.READ_ONLY)
    private Long totalRequestsCount;

    @Schema(description = "Total number of failed requests", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
    private Long totalFailuresCount;

    @Schema(description = "Average response time in milliseconds", example = "245", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer averageResponseTimeMs;

    // Approval and Governance

    @FilterableId
    @Schema(description = "Reference to user who approved this provider-tenant relationship")
    private UUID approvedByUserId;

    @Schema(description = "Timestamp when relationship was approved", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime approvedAt;

    @Schema(description = "Approval notes or justification", example = "Approved for Q1 2024 payment processing")
    private String approvalNotes;

    // System Fields

    @Schema(description = "Additional metadata in JSON format for extensibility", example = "{\"custom_field\":\"value\"}")
    private String metadata;

    @Schema(description = "Indicates if the relationship is active", example = "true")
    private Boolean active;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Version number for optimistic locking", accessMode = Schema.AccessMode.READ_ONLY)
    private Long version;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Timestamp when relationship was created", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Timestamp when relationship was last updated", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}

