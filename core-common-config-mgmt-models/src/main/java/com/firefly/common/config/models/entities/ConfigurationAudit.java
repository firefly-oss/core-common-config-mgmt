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
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing comprehensive audit trail for configuration changes.
 *
 * <p>Tracks all changes to configuration entities with before/after values, enabling
 * compliance reporting, change tracking, and rollback capabilities.</p>
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Complete change history with before/after values</li>
 *   <li>User attribution and session tracking</li>
 *   <li>Approval workflow support</li>
 *   <li>Rollback capability</li>
 *   <li>Compliance and regulatory reporting</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("configuration_audits")
public class ConfigurationAudit {

    /**
     * Unique identifier for the audit record (Primary Key)
     */
    @Id
    private UUID id;

    /**
     * Reference to the tenant (Foreign Key to tenants)
     */
    @Column("tenant_id")
    private UUID tenantId;

    // ========================================
    // Entity Information
    // ========================================

    /**
     * Type of entity being audited (e.g., "TENANT", "PROVIDER", "WEBHOOK_CONFIG")
     */
    @Column("entity_type")
    private String entityType;

    /**
     * Unique identifier of the entity being audited
     */
    @Column("entity_id")
    private UUID entityId;

    /**
     * Action performed: CREATE, UPDATE, DELETE, ACTIVATE, DEACTIVATE, APPROVE, REJECT
     */
    @Column("action")
    private String action;

    /**
     * Name of the field that was changed (for UPDATE actions)
     */
    @Column("field_name")
    private String fieldName;

    /**
     * Previous value before the change (JSON format for complex objects)
     */
    @Column("old_value")
    private String oldValue;

    /**
     * New value after the change (JSON format for complex objects)
     */
    @Column("new_value")
    private String newValue;

    // ========================================
    // User Attribution
    // ========================================

    /**
     * Reference to user who made the change (Foreign Key to users)
     */
    @Column("changed_by_user_id")
    private UUID changedByUserId;

    /**
     * Username of the user who made the change
     */
    @Column("changed_by_username")
    private String changedByUsername;

    /**
     * Reason provided for the change
     */
    @Column("change_reason")
    private String changeReason;

    // ========================================
    // Session and Request Tracking
    // ========================================

    /**
     * Session identifier for grouping related changes
     */
    @Column("session_id")
    private String sessionId;

    /**
     * Request identifier for tracing
     */
    @Column("request_id")
    private String requestId;

    /**
     * Correlation identifier for distributed tracing
     */
    @Column("correlation_id")
    private String correlationId;

    /**
     * Source system that initiated the change (e.g., "WEB_UI", "API", "BATCH_JOB")
     */
    @Column("source_system")
    private String sourceSystem;

    /**
     * IP address of the client that made the change
     */
    @Column("ip_address")
    private String ipAddress;

    /**
     * User agent string from the client
     */
    @Column("user_agent")
    private String userAgent;

    // ========================================
    // Change Classification
    // ========================================

    /**
     * Category of change: CONFIGURATION, SECURITY, COMPLIANCE, OPERATIONAL
     */
    @Column("change_category")
    private String changeCategory;

    /**
     * Severity of change: LOW, MEDIUM, HIGH, CRITICAL
     */
    @Column("change_severity")
    private String changeSeverity;

    // ========================================
    // Approval Workflow
    // ========================================

    /**
     * Indicates if this change requires approval
     */
    @Column("requires_approval")
    private Boolean requiresApproval;

    /**
     * Reference to user who approved the change (Foreign Key to users)
     */
    @Column("approved_by_user_id")
    private UUID approvedByUserId;

    /**
     * Timestamp when the change was approved
     */
    @Column("approved_at")
    private LocalDateTime approvedAt;

    /**
     * Notes from the approval process
     */
    @Column("approval_notes")
    private String approvalNotes;

    // ========================================
    // Rollback Support
    // ========================================

    /**
     * Indicates if this change can be rolled back
     */
    @Column("rollback_available")
    private Boolean rollbackAvailable;

    /**
     * Indicates if this change has been rolled back
     */
    @Column("rolled_back")
    private Boolean rolledBack;

    /**
     * Reference to user who performed the rollback (Foreign Key to users)
     */
    @Column("rolled_back_by_user_id")
    private UUID rolledBackByUserId;

    /**
     * Timestamp when the rollback was performed
     */
    @Column("rolled_back_at")
    private LocalDateTime rolledBackAt;

    /**
     * Reason for the rollback
     */
    @Column("rollback_reason")
    private String rollbackReason;

    // ========================================
    // System Fields
    // ========================================

    /**
     * Additional metadata in JSON format for extensibility
     */
    @Column("metadata")
    private String metadata;

    /**
     * Timestamp when audit record was created
     */
    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;
}


