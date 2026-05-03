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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.fireflyframework.utils.annotations.FilterableId;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for ConfigurationAudit entity.
 *
 * <p>Represents a complete audit trail entry for configuration changes, including
 * change tracking, approval workflow, and rollback support.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Audit trail record for configuration changes. Provides complete traceability " +
        "of who changed what, when, why, and supports rollback capabilities.")
public class ConfigurationAuditDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier for the audit record",
            example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @FilterableId
    @Schema(description = "Reference to tenant that owns the changed entity")
    private UUID tenantId;

    @NotBlank(message = "Entity type is required")
    @Schema(description = "Type of entity that was changed", example = "Tenant", required = true)
    private String entityType;

    @NotNull(message = "Entity ID is required")
    @Schema(description = "Unique identifier of the changed entity", required = true)
    private UUID entityId;

    @NotBlank(message = "Action is required")
    @Schema(description = "Type of action performed: CREATE, UPDATE, DELETE, ACTIVATE, DEACTIVATE",
            example = "UPDATE", required = true)
    private String action;

    @Schema(description = "Specific field name that was changed (null for CREATE/DELETE)", example = "apiRateLimitPerHour")
    private String fieldName;

    @Schema(description = "Value before the change (null for CREATE)", example = "1000")
    private String oldValue;

    @Schema(description = "Value after the change (null for DELETE)", example = "5000")
    private String newValue;

    // User and Session Tracking

    @FilterableId
    @Schema(description = "Reference to user who made the change")
    private UUID changedByUserId;

    @Schema(description = "Username or email of user who made the change", example = "admin@firefly.com")
    private String changedByUsername;

    @Schema(description = "Business justification or reason for the change",
            example = "Increased rate limit for premium tier customer")
    private String changeReason;

    @Schema(description = "IP address from which the change was made", example = "192.168.1.100")
    private String ipAddress;

    @Schema(description = "User agent string of the client", example = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)...")
    private String userAgent;

    @Schema(description = "Session identifier for grouping related changes", example = "sess_abc123")
    private String sessionId;

    @Schema(description = "Request identifier for distributed tracing", example = "req_xyz789")
    private String requestId;

    @Schema(description = "Correlation identifier for tracking across services", example = "corr_def456")
    private String correlationId;

    @Schema(description = "Source system that initiated the change: WEB_UI, API, BATCH_JOB, MIGRATION, SYSTEM",
            example = "WEB_UI")
    private String sourceSystem;

    // Change Classification

    @Schema(description = "Category of change: CONFIGURATION, SECURITY, COMPLIANCE, OPERATIONAL, EMERGENCY",
            example = "CONFIGURATION")
    private String changeCategory;

    @Schema(description = "Severity level: LOW, MEDIUM, HIGH, CRITICAL", example = "MEDIUM")
    private String changeSeverity;

    @Schema(description = "Impact scope: TENANT, PROVIDER, SYSTEM, GLOBAL", example = "TENANT")
    private String impactScope;

    // Approval Workflow

    @Schema(description = "Indicates if this change required approval", example = "true")
    private Boolean requiresApproval;

    @FilterableId
    @Schema(description = "Reference to user who approved the change")
    private UUID approvedByUserId;

    @Schema(description = "Timestamp when change was approved", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime approvedAt;

    @Schema(description = "Approval notes or comments", example = "Approved for production deployment")
    private String approvalNotes;

    // Rollback Support

    @Schema(description = "Indicates if rollback is available for this change", example = "true")
    private Boolean rollbackAvailable;

    @Schema(description = "Indicates if this change has been rolled back", example = "false")
    private Boolean rolledBack;

    @FilterableId
    @Schema(description = "Reference to user who performed the rollback")
    private UUID rolledBackByUserId;

    @Schema(description = "Timestamp when rollback was performed", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime rolledBackAt;

    @Schema(description = "Reason for rolling back the change", example = "Caused performance degradation")
    private String rollbackReason;

    // System Fields

    @Schema(description = "Additional metadata in JSON format for extensibility", example = "{\"ticket_id\":\"JIRA-1234\"}")
    private String metadata;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Timestamp when audit record was created", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
}

