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

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.fireflyframework.utils.annotations.FilterableId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Provider entity.
 *
 * <p>Represents an external service provider that Firefly integrates with to deliver
 * banking functionality (payment gateways, KYC providers, card issuers, etc.).</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "External service provider integrated with Firefly core banking system. " +
        "Includes payment gateways, KYC providers, card issuers, fraud detection services, etc.")
public class ProviderDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier for the provider", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @NotBlank(message = "Code is required")
    @Size(min = 2, max = 50, message = "Code must be between 2 and 50 characters")
    @Schema(description = "Unique business code for programmatic reference", example = "STRIPE", required = true)
    private String code;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Display name of the provider", example = "Stripe Payment Gateway", required = true)
    private String name;

    @Schema(description = "Detailed description of the provider and its capabilities", example = "Stripe is a payment processing platform")
    private String description;

    @NotNull(message = "Provider type ID is required")
    @FilterableId
    @Schema(description = "Reference to provider type (PAYMENT_GATEWAY, KYC, CARD_ISSUING, etc.)", required = true)
    private UUID providerTypeId;

    @NotNull(message = "Provider status ID is required")
    @FilterableId
    @Schema(description = "Reference to provider status (ACTIVE, INACTIVE, MAINTENANCE, DEPRECATED)", required = true)
    private UUID providerStatusId;

    // API Configuration

    @Schema(description = "Base URL for API endpoints (deprecated - use productionUrl)", example = "https://api.stripe.com")
    private String baseUrl;

    @Schema(description = "Production environment API URL", example = "https://api.stripe.com")
    private String productionUrl;

    @Schema(description = "Sandbox/test environment API URL", example = "https://api.stripe.com/test")
    private String sandboxUrl;

    @Schema(description = "Indicates if provider supports sandbox environment", example = "true")
    private Boolean supportsSandbox;

    @Schema(description = "API version being used", example = "2023-10-16")
    private String apiVersion;

    @Schema(description = "URL to provider's API documentation", example = "https://stripe.com/docs/api")
    private String documentationUrl;

    // Capabilities and Features

    @Schema(description = "Business category: PAYMENT, COMPLIANCE, INFRASTRUCTURE, etc.", example = "PAYMENT")
    private String providerCategory;

    @Schema(description = "Indicates if provider supports webhooks", example = "true")
    private Boolean webhookSupport;

    @Schema(description = "Indicates if provider supports batch processing", example = "true")
    private Boolean batchProcessingSupport;

    @Schema(description = "Indicates if provider supports real-time processing", example = "true")
    private Boolean realTimeProcessingSupport;

    @Schema(description = "Comma-separated list of supported country codes (ISO 3166-1 alpha-2)", example = "US,GB,FR,DE")
    private String supportedCountries;

    @Schema(description = "Comma-separated list of supported currency codes (ISO 4217)", example = "USD,EUR,GBP")
    private String supportedCurrencies;

    @Schema(description = "Comma-separated list of supported language codes (ISO 639-1)", example = "en,es,fr")
    private String supportedLanguages;

    // SLA and Performance

    @Schema(description = "Guaranteed uptime percentage per SLA", example = "99.9")
    private BigDecimal slaUptimePercentage;

    @Schema(description = "Maximum acceptable response time in milliseconds", example = "500")
    private Integer maxResponseTimeMs;

    @Schema(description = "Rate limit: requests per second", example = "100")
    private Integer rateLimitPerSecond;

    @Schema(description = "Rate limit: requests per minute", example = "6000")
    private Integer rateLimitPerMinute;

    @Schema(description = "Rate limit: requests per hour", example = "360000")
    private Integer rateLimitPerHour;

    // Compliance and Certification

    @Schema(description = "Certification level: PCI_DSS, ISO27001, SOC2, etc.", example = "PCI_DSS_LEVEL_1")
    private String certificationLevel;

    @Schema(description = "Comma-separated list of compliance certifications", example = "PCI-DSS,ISO27001,SOC2")
    private String complianceCertifications;

    // Contract Management

    @Schema(description = "Pricing model: PAY_PER_USE, SUBSCRIPTION, TIERED, CUSTOM", example = "PAY_PER_USE")
    private String pricingModel;

    @Schema(description = "Contract start date", example = "2024-01-01")
    private LocalDate contractStartDate;

    @Schema(description = "Contract end date", example = "2025-12-31")
    private LocalDate contractEndDate;

    @Schema(description = "Indicates if contract auto-renews", example = "true")
    private Boolean autoRenewal;

    @Schema(description = "Notice period in days for contract termination", example = "90")
    private Integer noticePeriodDays;

    // Contact Information

    @Schema(description = "Primary contact person name", example = "John Doe")
    private String primaryContactName;

    @Email(message = "Primary contact email must be valid")
    @Schema(description = "Primary contact email address", example = "support@stripe.com")
    private String primaryContactEmail;

    @Schema(description = "Primary contact phone number", example = "+1-888-926-2289")
    private String primaryContactPhone;

    @Schema(description = "Escalation contact person name", example = "Jane Smith")
    private String escalationContactName;

    @Email(message = "Escalation contact email must be valid")
    @Schema(description = "Escalation contact email address", example = "escalation@stripe.com")
    private String escalationContactEmail;

    @Schema(description = "Escalation contact phone number", example = "+1-888-926-2290")
    private String escalationContactPhone;

    @Email(message = "Support email must be valid")
    @Schema(description = "General support email (deprecated - use primaryContactEmail)", example = "support@stripe.com")
    private String supportEmail;

    @Schema(description = "General support phone (deprecated - use primaryContactPhone)", example = "+1-888-926-2289")
    private String supportPhone;

    // Health Monitoring

    @Schema(description = "URL endpoint for health check monitoring", example = "https://status.stripe.com/api/v2/status.json")
    private String healthCheckUrl;

    @Schema(description = "Interval in seconds between health checks", example = "60")
    private Integer healthCheckIntervalSeconds;

    @Schema(description = "Timeout in seconds for health check requests", example = "10")
    private Integer healthCheckTimeoutSeconds;

    @Schema(description = "Current health status: HEALTHY, DEGRADED, DOWN, UNKNOWN", example = "HEALTHY")
    private String healthCheckStatus;

    @Schema(description = "Timestamp of last successful health check", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime lastHealthCheckAt;

    @Schema(description = "Timestamp of last health check failure", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime lastHealthCheckFailureAt;

    @Schema(description = "Consecutive health check failure count", example = "0", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer consecutiveFailureCount;

    // System Fields

    @Schema(description = "Additional metadata in JSON format for extensibility", example = "{\"custom_field\":\"value\"}")
    private String metadata;

    @Schema(description = "Indicates if the provider is active and available for use", example = "true")
    private Boolean active;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Version number for optimistic locking", accessMode = Schema.AccessMode.READ_ONLY)
    private Long version;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Timestamp when provider was created", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Timestamp when provider was last updated", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}
