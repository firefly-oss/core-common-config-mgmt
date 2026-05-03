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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing an external service provider in the Firefly core banking system.
 *
 * <p>Providers are third-party services that Firefly integrates with to deliver
 * banking functionality. Examples include payment gateways, KYC providers, card issuers,
 * fraud detection services, and core banking systems.</p>
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Multi-environment support (sandbox, production)</li>
 *   <li>SLA and performance tracking</li>
 *   <li>Health monitoring and circuit breaker support</li>
 *   <li>Contract and compliance management</li>
 *   <li>Rate limiting and capacity planning</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("providers")
public class Provider {

    /**
     * Unique identifier for the provider (Primary Key)
     */
    @Id
    private UUID id;

    /**
     * Unique business code for the provider (e.g., "STRIPE", "TREEZOR", "PLAID")
     * Used for programmatic references and configuration
     */
    @Column("code")
    private String code;

    /**
     * Display name of the provider (e.g., "Stripe Payment Gateway")
     */
    @Column("name")
    private String name;

    /**
     * Detailed description of the provider and its capabilities
     */
    @Column("description")
    private String description;

    /**
     * Reference to provider type (Foreign Key to provider_types)
     * Categories: PAYMENT_GATEWAY, KYC, CARD_ISSUING, BANKING_CORE, etc.
     */
    @Column("provider_type_id")
    private UUID providerTypeId;

    /**
     * Reference to provider status (Foreign Key to provider_statuses)
     * States: ACTIVE, INACTIVE, MAINTENANCE, DEPRECATED
     */
    @Column("provider_status_id")
    private UUID providerStatusId;

    // ========================================
    // API Configuration
    // ========================================

    /**
     * Base URL for API endpoints (deprecated - use productionUrl instead)
     */
    @Column("base_url")
    private String baseUrl;

    /**
     * Production environment API URL
     */
    @Column("production_url")
    private String productionUrl;

    /**
     * Sandbox/test environment API URL
     */
    @Column("sandbox_url")
    private String sandboxUrl;

    /**
     * Indicates if provider supports sandbox environment
     */
    @Column("supports_sandbox")
    private Boolean supportsSandbox;

    /**
     * API version being used (e.g., "v1", "2023-10-16")
     */
    @Column("api_version")
    private String apiVersion;

    /**
     * URL to provider's API documentation
     */
    @Column("documentation_url")
    private String documentationUrl;

    // ========================================
    // Capabilities and Features
    // ========================================

    /**
     * Business category: PAYMENT, COMPLIANCE, INFRASTRUCTURE, etc.
     */
    @Column("provider_category")
    private String providerCategory;

    /**
     * Indicates if provider supports webhooks
     */
    @Column("webhook_support")
    private Boolean webhookSupport;

    /**
     * Indicates if provider supports batch processing
     */
    @Column("batch_processing_support")
    private Boolean batchProcessingSupport;

    /**
     * Indicates if provider supports real-time processing
     */
    @Column("real_time_processing_support")
    private Boolean realTimeProcessingSupport;

    /**
     * Comma-separated list of supported country codes (ISO 3166-1 alpha-2)
     */
    @Column("supported_countries")
    private String supportedCountries;

    /**
     * Comma-separated list of supported currency codes (ISO 4217)
     */
    @Column("supported_currencies")
    private String supportedCurrencies;

    /**
     * Comma-separated list of supported language codes (ISO 639-1)
     */
    @Column("supported_languages")
    private String supportedLanguages;
    // ========================================
    // SLA and Performance
    // ========================================

    /**
     * Guaranteed uptime percentage per SLA (e.g., 99.9, 99.99)
     */
    @Column("sla_uptime_percentage")
    private BigDecimal slaUptimePercentage;

    /**
     * Maximum acceptable response time in milliseconds
     */
    @Column("max_response_time_ms")
    private Integer maxResponseTimeMs;

    /**
     * Rate limit: requests per second
     */
    @Column("rate_limit_per_second")
    private Integer rateLimitPerSecond;

    /**
     * Rate limit: requests per minute
     */
    @Column("rate_limit_per_minute")
    private Integer rateLimitPerMinute;

    /**
     * Rate limit: requests per hour
     */
    @Column("rate_limit_per_hour")
    private Integer rateLimitPerHour;

    // ========================================
    // Compliance and Certification
    // ========================================

    /**
     * Certification level: PCI_DSS, ISO27001, SOC2, etc.
     */
    @Column("certification_level")
    private String certificationLevel;

    /**
     * Comma-separated list of compliance certifications
     */
    @Column("compliance_certifications")
    private String complianceCertifications;

    // ========================================
    // Contract Management
    // ========================================

    /**
     * Pricing model: PAY_PER_USE, SUBSCRIPTION, TIERED, CUSTOM
     */
    @Column("pricing_model")
    private String pricingModel;

    /**
     * Contract start date
     */
    @Column("contract_start_date")
    private LocalDate contractStartDate;

    /**
     * Contract end date
     */
    @Column("contract_end_date")
    private LocalDate contractEndDate;

    /**
     * Indicates if contract auto-renews
     */
    @Column("auto_renewal")
    private Boolean autoRenewal;

    /**
     * Notice period in days for contract termination
     */
    @Column("notice_period_days")
    private Integer noticePeriodDays;

    // ========================================
    // Contact Information
    // ========================================

    /**
     * Primary contact person name
     */
    @Column("primary_contact_name")
    private String primaryContactName;

    /**
     * Primary contact email address
     */
    @Column("primary_contact_email")
    private String primaryContactEmail;

    /**
     * Primary contact phone number
     */
    @Column("primary_contact_phone")
    private String primaryContactPhone;

    /**
     * Escalation contact person name
     */
    @Column("escalation_contact_name")
    private String escalationContactName;

    /**
     * Escalation contact email address
     */
    @Column("escalation_contact_email")
    private String escalationContactEmail;

    /**
     * Escalation contact phone number
     */
    @Column("escalation_contact_phone")
    private String escalationContactPhone;

    /**
     * General support email (deprecated - use primaryContactEmail)
     */
    @Column("support_email")
    private String supportEmail;

    /**
     * General support phone (deprecated - use primaryContactPhone)
     */
    @Column("support_phone")
    private String supportPhone;

    // ========================================
    // Health Monitoring
    // ========================================

    /**
     * URL for health check endpoint
     */
    @Column("health_check_url")
    private String healthCheckUrl;

    /**
     * Current health status: HEALTHY, DEGRADED, DOWN, UNKNOWN
     */
    @Column("health_check_status")
    private String healthCheckStatus;

    /**
     * Timestamp of last health check
     */
    @Column("last_health_check_at")
    private LocalDateTime lastHealthCheckAt;

    // ========================================
    // System Fields
    // ========================================

    /**
     * Additional metadata in JSON format for extensibility
     */
    @Column("metadata")
    private String metadata;

    /**
     * Indicates if provider is active and available for use
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
     * Timestamp when provider was created
     */
    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp when provider was last updated
     */
    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}

