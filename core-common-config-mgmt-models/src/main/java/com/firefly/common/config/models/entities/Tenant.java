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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a Tenant in the Firefly core banking system.
 *
 * <p>A Tenant represents a logical separation within the Firefly platform, allowing multiple
 * banking organizations, business units, or environments to share the same infrastructure
 * while maintaining separate configurations, providers, branding, and operational settings.</p>
 *
 * <p>Tenants enable multi-tenancy where different banking entities can operate on the same
 * Firefly deployment with their own customized settings, without physical infrastructure isolation.</p>
 *
 * <p>Examples of Tenants:</p>
 * <ul>
 *   <li>acme-bank - Acme Bank's configuration</li>
 *   <li>beta-fintech - Beta Fintech's configuration</li>
 *   <li>gamma-payments - Gamma Payments' configuration</li>
 *   <li>internal-dev - Internal development environment</li>
 * </ul>
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Logical data separation (row-level security via tenant_id)</li>
 *   <li>Independent configuration and provider management per tenant</li>
 *   <li>Custom branding and settings per tenant</li>
 *   <li>Hierarchical tenant structures (parent-child relationships)</li>
 *   <li>Subscription and billing management per tenant</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("tenants")
public class Tenant {

    /**
     * Unique identifier for the tenant (Primary Key)
     */
    @Id
    private UUID id;

    /**
     * Unique tenant code (e.g., "acme-bank", "beta-fintech", "internal-dev")
     * Used for programmatic identification, API references, and data filtering
     */
    @Column("code")
    private String code;

    /**
     * Display name of the tenant (e.g., "Acme Bank", "Beta Fintech", "Internal Development")
     */
    @Column("name")
    private String name;

    /**
     * Detailed description of the tenant and its purpose
     */
    @Column("description")
    private String description;

    /**
     * Reference to the tenant's current status (Foreign Key to tenant_statuses)
     * Determines operational state: ACTIVE, SUSPENDED, INACTIVE, TRIAL, EXPIRED
     */
    @Column("tenant_status_id")
    private UUID tenantStatusId;

    /**
     * Reference to the tenant's primary operating country (Foreign Key to countries)
     * Used for regulatory compliance, localization, and regional settings
     */
    @Column("country_id")
    private UUID countryId;

    /**
     * IANA timezone identifier for the tenant (e.g., "America/New_York", "Europe/London")
     * Used for scheduling, reporting, and time-sensitive operations
     */
    @Column("timezone")
    private String timezone;

    /**
     * Default ISO 4217 currency code for tenant operations (e.g., "USD", "EUR", "GBP")
     */
    @Column("default_currency_code")
    private String defaultCurrencyCode;

    /**
     * Default ISO 639-1 language code for tenant UI and communications (e.g., "en", "es", "fr")
     */
    @Column("default_language_code")
    private String defaultLanguageCode;

    // ========================================
    // Legal Entity & Ownership Information
    // ========================================

    /**
     * Official legal name of the organization using this tenant
     */
    @Column("legal_entity_name")
    private String legalEntityName;

    /**
     * Tax identification number (TIN/EIN) for the organization
     */
    @Column("tax_identification_number")
    private String taxIdentificationNumber;

    /**
     * Business registration number from government authority
     */
    @Column("registration_number")
    private String registrationNumber;

    /**
     * Date when the organization was incorporated
     */
    @Column("incorporation_date")
    private LocalDate incorporationDate;

    /**
     * Banking or financial services license number (if applicable)
     */
    @Column("regulatory_license_number")
    private String regulatoryLicenseNumber;

    /**
     * Name of the regulatory authority overseeing this tenant (e.g., "Federal Reserve", "FCA", "BaFin")
     */
    @Column("regulatory_authority")
    private String regulatoryAuthority;

    // ========================================
    // Instance Contact Information
    // ========================================

    /**
     * Name of primary business/operations contact for this instance
     */
    @Column("business_contact_name")
    private String businessContactName;

    /**
     * Email address of primary business/operations contact
     */
    @Column("business_contact_email")
    private String businessContactEmail;

    /**
     * Phone number of primary business/operations contact
     */
    @Column("business_contact_phone")
    private String businessContactPhone;

    /**
     * Name of primary technical/DevOps contact for this instance
     */
    @Column("technical_contact_name")
    private String technicalContactName;

    /**
     * Email address of primary technical/DevOps contact
     */
    @Column("technical_contact_email")
    private String technicalContactEmail;

    /**
     * Phone number of primary technical/DevOps contact
     */
    @Column("technical_contact_phone")
    private String technicalContactPhone;

    // ========================================
    // Tenant Hierarchy & Classification
    // ========================================

    /**
     * Reference to parent tenant for hierarchical structures (Foreign Key to tenants)
     * Enables multi-level tenant hierarchies (e.g., main organization -> departments/subsidiaries)
     */
    @Column("parent_tenant_id")
    private UUID parentTenantId;

    /**
     * Type of tenant: STANDARD, ENTERPRISE, PARTNER, SUBSIDIARY, INTERNAL, DEMO
     * Determines feature access, resource limits, and operational capabilities
     */
    @Column("tenant_type")
    private String tenantType;

    /**
     * Industry sector the tenant operates in (e.g., "RETAIL_BANKING", "FINTECH", "PAYMENT_SERVICES", "NEOBANK")
     */
    @Column("industry_sector")
    private String industrySector;

    /**
     * Expected user/customer capacity range for resource planning (e.g., "1-1000", "1001-10000", "10000+")
     */
    @Column("employee_count_range")
    private String employeeCountRange;

    /**
     * Expected transaction volume range for capacity planning
     */
    @Column("annual_revenue_range")
    private String annualRevenueRange;

    // ========================================
    // Risk and Compliance
    // ========================================

    /**
     * Risk assessment rating: LOW, MEDIUM, HIGH, CRITICAL
     * Used for monitoring and compliance requirements
     */
    @Column("risk_rating")
    private String riskRating;

    /**
     * Compliance tier determining regulatory requirements level
     */
    @Column("compliance_tier")
    private String complianceTier;

    /**
     * Data classification level: PUBLIC, INTERNAL, CONFIDENTIAL, RESTRICTED
     * Determines data handling and security requirements
     */
    @Column("data_classification")
    private String dataClassification;

    /**
     * Service Level Agreement tier (e.g., "BASIC", "STANDARD", "PREMIUM", "ENTERPRISE")
     */
    @Column("sla_tier")
    private String slaTier;

    // ========================================
    // Subscription and Billing
    // ========================================

    /**
     * Subscription tier (e.g., "STARTER", "PROFESSIONAL", "ENTERPRISE")
     */
    @Column("subscription_tier")
    private String subscriptionTier;

    /**
     * Date when subscription started
     */
    @Column("subscription_start_date")
    private LocalDateTime subscriptionStartDate;

    /**
     * Date when subscription ends or renews
     */
    @Column("subscription_end_date")
    private LocalDateTime subscriptionEndDate;

    /**
     * Indicates if tenant is in trial period
     */
    @Column("is_trial")
    private Boolean isTrial;

    /**
     * Date when trial period ends
     */
    @Column("trial_end_date")
    private LocalDateTime trialEndDate;

    /**
     * ISO 4217 currency code for billing (e.g., "USD", "EUR")
     */
    @Column("billing_currency_code")
    private String billingCurrencyCode;

    /**
     * Billing cycle: MONTHLY, QUARTERLY, ANNUALLY
     */
    @Column("billing_cycle")
    private String billingCycle;

    /**
     * Payment terms in days (e.g., 30 for Net 30)
     */
    @Column("payment_terms_days")
    private Integer paymentTermsDays;

    // ========================================
    // System Fields
    // ========================================

    /**
     * Additional metadata in JSON format for extensibility
     */
    @Column("metadata")
    private String metadata;

    /**
     * Indicates if tenant is active and operational
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
     * Timestamp when tenant was created
     */
    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp when tenant was last updated
     */
    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}

