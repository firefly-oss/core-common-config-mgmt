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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Tenant entity.
 *
 * <p>Represents a logical separation within the Firefly platform, allowing multiple
 * banking organizations to share infrastructure while maintaining separate configurations.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tenant (logical separation) in the Firefly core banking system. " +
        "Each tenant has independent configuration, providers, branding, and operational settings.")
public class TenantDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier for the tenant", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @NotBlank(message = "Code is required")
    @Size(min = 2, max = 50, message = "Code must be between 2 and 50 characters")
    @Schema(description = "Unique tenant code for programmatic identification", example = "acme-bank", required = true)
    private String code;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Display name of the tenant", example = "Acme Bank", required = true)
    private String name;

    @Schema(description = "Detailed description of the tenant and its purpose", example = "Acme Bank's production configuration")
    private String description;

    @NotNull(message = "Tenant status ID is required")
    @FilterableId
    @Schema(description = "Reference to tenant status (ACTIVE, SUSPENDED, INACTIVE, TRIAL, EXPIRED)", required = true)
    private UUID tenantStatusId;

    @FilterableId
    @Schema(description = "Reference to primary operating country for regional settings and compliance")
    private UUID countryId;

    @Schema(description = "IANA timezone identifier for scheduling and time-sensitive operations", example = "America/New_York")
    private String timezone;

    @Size(max = 3, message = "Currency code must be 3 characters")
    @Schema(description = "Default ISO 4217 currency code for tenant operations", example = "USD")
    private String defaultCurrencyCode;

    @Size(max = 10, message = "Language code must not exceed 10 characters")
    @Schema(description = "Default ISO 639-1 language code for tenant UI and communications", example = "en")
    private String defaultLanguageCode;

    // Legal Entity & Ownership Information

    @Schema(description = "Official legal name of the organization using this tenant", example = "Acme Bank Corporation")
    private String legalEntityName;

    @Schema(description = "Tax identification number (TIN/EIN) for the organization", example = "12-3456789")
    private String taxIdentificationNumber;

    @Schema(description = "Business registration number from government authority", example = "REG-123456")
    private String registrationNumber;

    @Schema(description = "Date when the organization was incorporated", example = "2020-01-15")
    private LocalDate incorporationDate;

    @Schema(description = "Banking or financial services license number", example = "BNK-LIC-789")
    private String regulatoryLicenseNumber;

    @Schema(description = "Name of regulatory authority overseeing this tenant", example = "Federal Reserve")
    private String regulatoryAuthority;

    // Contact Information

    @Schema(description = "Business contact person name", example = "John Doe")
    private String businessContactName;

    @Email(message = "Business contact email must be valid")
    @Schema(description = "Business contact email address", example = "business@acmebank.com")
    private String businessContactEmail;

    @Schema(description = "Business contact phone number", example = "+1-555-0100")
    private String businessContactPhone;

    @Schema(description = "Technical contact person name", example = "Jane Smith")
    private String technicalContactName;

    @Email(message = "Technical contact email must be valid")
    @Schema(description = "Technical contact email address", example = "tech@acmebank.com")
    private String technicalContactEmail;

    @Schema(description = "Technical contact phone number", example = "+1-555-0101")
    private String technicalContactPhone;

    // Hierarchical Structure

    @FilterableId
    @Schema(description = "Reference to parent tenant for hierarchical structures (e.g., main org -> departments)")
    private UUID parentTenantId;

    @Schema(description = "Type of tenant: STANDARD, ENTERPRISE, PARTNER, SUBSIDIARY, INTERNAL, DEMO", example = "ENTERPRISE")
    private String tenantType;

    // Organization Classification

    @Schema(description = "Industry sector the tenant operates in", example = "RETAIL_BANKING")
    private String industrySector;

    @Schema(description = "Expected user/customer capacity range for resource planning", example = "1001-10000")
    private String employeeCountRange;

    @Schema(description = "Expected transaction volume range for capacity planning", example = "HIGH")
    private String annualRevenueRange;

    // Risk and Compliance

    @Schema(description = "Risk rating: LOW, MEDIUM, HIGH, CRITICAL", example = "MEDIUM")
    private String riskRating;

    @Schema(description = "Compliance tier for regulatory requirements", example = "TIER_1")
    private String complianceTier;

    @Schema(description = "Data classification level: PUBLIC, INTERNAL, CONFIDENTIAL, RESTRICTED", example = "CONFIDENTIAL")
    private String dataClassification;

    @Schema(description = "Service Level Agreement tier", example = "GOLD")
    private String slaTier;

    // Subscription and Billing

    @Schema(description = "Subscription tier level", example = "ENTERPRISE")
    private String subscriptionTier;

    @Schema(description = "Subscription start date and time")
    private LocalDateTime subscriptionStartDate;

    @Schema(description = "Subscription end date and time")
    private LocalDateTime subscriptionEndDate;

    @Schema(description = "Indicates if tenant is in trial mode", example = "false")
    private Boolean isTrial;

    @Schema(description = "Trial period end date and time")
    private LocalDateTime trialEndDate;

    @Size(max = 3, message = "Billing currency code must be 3 characters")
    @Schema(description = "ISO 4217 currency code for billing", example = "USD")
    private String billingCurrencyCode;

    @Schema(description = "Billing cycle: MONTHLY, QUARTERLY, ANNUALLY", example = "MONTHLY")
    private String billingCycle;

    @Schema(description = "Payment terms in days", example = "30")
    private Integer paymentTermsDays;

    // System Fields

    @Schema(description = "Additional metadata in JSON format for extensibility", example = "{\"custom_field\":\"value\"}")
    private String metadata;

    @Schema(description = "Indicates if the tenant is active", example = "true")
    private Boolean active;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Version number for optimistic locking", accessMode = Schema.AccessMode.READ_ONLY)
    private Long version;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Timestamp when tenant was created", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Timestamp when tenant was last updated", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}

