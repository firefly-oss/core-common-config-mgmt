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
 * Entity representing webhook configuration for tenants and providers.
 *
 * <p>Manages webhook endpoints, events, authentication, retry policies, and monitoring
 * for event-driven integrations in the Firefly core banking system.</p>
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Flexible event subscription and filtering</li>
 *   <li>Multiple authentication methods (API Key, OAuth, HMAC)</li>
 *   <li>Intelligent retry with exponential backoff</li>
 *   <li>Circuit breaker pattern for resilience</li>
 *   <li>Dead letter queue for failed events</li>
 *   <li>Comprehensive metrics and health monitoring</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("webhook_configs")
public class WebhookConfig {

    /**
     * Unique identifier for the webhook configuration (Primary Key)
     */
    @Id
    private UUID id;

    /**
     * Reference to the tenant (Foreign Key to tenants)
     */
    @Column("tenant_id")
    private UUID tenantId;

    /**
     * Reference to the provider (Foreign Key to providers)
     * Optional - used when webhook is provider-specific
     */
    @Column("provider_id")
    private UUID providerId;

    // ========================================
    // Webhook Configuration
    // ========================================

    /**
     * Descriptive name for the webhook
     */
    @Column("webhook_name")
    private String webhookName;

    /**
     * Target URL for webhook delivery
     */
    @Column("webhook_url")
    private String webhookUrl;

    /**
     * Detailed description of webhook purpose and usage
     */
    @Column("description")
    private String description;

    /**
     * Webhook version (e.g., "v1", "v2", "2024-01-01")
     */
    @Column("webhook_version")
    private String webhookVersion;

    /**
     * Content type for webhook payload (e.g., "application/json", "application/xml")
     */
    @Column("content_type")
    private String contentType;

    /**
     * Comma-separated list of event types to subscribe to
     * Examples: "transaction.created", "payment.completed", "kyc.verified"
     */
    @Column("event_types")
    private String eventTypes;

    /**
     * HTTP method for webhook delivery (POST, PUT, PATCH)
     */
    @Column("http_method")
    private String httpMethod;

    // ========================================
    // Authentication
    // ========================================

    /**
     * Authentication type: NONE, API_KEY, BEARER_TOKEN, BASIC_AUTH, HMAC_SHA256, OAUTH2
     */
    @Column("auth_type")
    private String authType;

    /**
     * Name of the authentication header (e.g., "Authorization", "X-API-Key")
     */
    @Column("auth_header_name")
    private String authHeaderName;

    /**
     * Value of the authentication header (encrypted)
     */
    @Column("auth_header_value")
    private String authHeaderValue;

    /**
     * Secret key for HMAC signature generation (encrypted)
     */
    @Column("secret_key")
    private String secretKey;

    /**
     * Signature algorithm for payload verification (e.g., "HMAC-SHA256", "HMAC-SHA512")
     */
    @Column("signature_algorithm")
    private String signatureAlgorithm;

    /**
     * Indicates if SSL certificate verification is enabled
     */
    @Column("verify_ssl")
    private Boolean verifySsl;

    /**
     * Indicates if HTTP redirects should be followed
     */
    @Column("follow_redirects")
    private Boolean followRedirects;

    /**
     * Custom HTTP headers in JSON format
     */
    @Column("custom_headers")
    private String customHeaders;


    // ========================================
    // Retry and Timeout Configuration
    // ========================================

    /**
     * Request timeout in seconds
     */
    @Column("timeout_seconds")
    private Integer timeoutSeconds;

    /**
     * Indicates if retry mechanism is enabled
     */
    @Column("retry_enabled")
    private Boolean retryEnabled;

    /**
     * Maximum number of retry attempts
     */
    @Column("max_retry_attempts")
    private Integer maxRetryAttempts;

    /**
     * Initial delay between retries in seconds
     */
    @Column("retry_delay_seconds")
    private Integer retryDelaySeconds;

    /**
     * Multiplier for exponential backoff (e.g., 2.0 doubles delay each retry)
     */
    @Column("retry_backoff_multiplier")
    private Double retryBackoffMultiplier;

    // ========================================
    // Batching Configuration
    // ========================================

    /**
     * Indicates if events should be batched
     */
    @Column("batch_events")
    private Boolean batchEvents;

    /**
     * Maximum number of events per batch
     */
    @Column("batch_size")
    private Integer batchSize;

    /**
     * Maximum time to wait before sending partial batch (in seconds)
     */
    @Column("batch_timeout_seconds")
    private Integer batchTimeoutSeconds;

    // ========================================
    // Processing Configuration
    // ========================================

    /**
     * Priority for webhook processing (1 = highest priority)
     */
    @Column("priority")
    private Integer priority;

    /**
     * Indicates if webhook should be processed asynchronously
     */
    @Column("async_processing")
    private Boolean asyncProcessing;

    /**
     * Indicates if dead letter queue is enabled for failed events
     */
    @Column("dead_letter_queue_enabled")
    private Boolean deadLetterQueueEnabled;

    /**
     * Maximum number of retries before moving to dead letter queue
     */
    @Column("max_dead_letter_retries")
    private Integer maxDeadLetterRetries;

    // ========================================
    // Circuit Breaker
    // ========================================

    /**
     * Indicates if circuit breaker pattern is enabled
     */
    @Column("circuit_breaker_enabled")
    private Boolean circuitBreakerEnabled;

    /**
     * Number of consecutive failures before circuit breaker opens
     */
    @Column("circuit_breaker_threshold")
    private Integer circuitBreakerThreshold;

    /**
     * Timeout in seconds before circuit breaker attempts to close
     */
    @Column("circuit_breaker_timeout_seconds")
    private Integer circuitBreakerTimeoutSeconds;

    // ========================================
    // Metrics and Monitoring
    // ========================================

    /**
     * Timestamp of last webhook trigger
     */
    @Column("last_triggered_at")
    private LocalDateTime lastTriggeredAt;

    /**
     * Timestamp of last successful delivery
     */
    @Column("last_success_at")
    private LocalDateTime lastSuccessAt;

    /**
     * Timestamp of last failed delivery
     */
    @Column("last_failure_at")
    private LocalDateTime lastFailureAt;

    /**
     * Total number of webhook triggers
     */
    @Column("total_triggers_count")
    private Long totalTriggersCount;

    /**
     * Total number of successful deliveries
     */
    @Column("total_success_count")
    private Long totalSuccessCount;

    /**
     * Total number of failed deliveries
     */
    @Column("total_failure_count")
    private Long totalFailureCount;

    /**
     * Average response time in milliseconds
     */
    @Column("average_response_time_ms")
    private Integer averageResponseTimeMs;

    // ========================================
    // Health Monitoring
    // ========================================

    /**
     * Indicates if health check is enabled
     */
    @Column("health_check_enabled")
    private Boolean healthCheckEnabled;

    /**
     * Interval in seconds between health checks
     */
    @Column("health_check_interval_seconds")
    private Integer healthCheckIntervalSeconds;

    // ========================================
    // System Fields
    // ========================================

    /**
     * Indicates if webhook is enabled
     */
    @Column("enabled")
    private Boolean enabled;

    /**
     * Additional metadata in JSON format for extensibility
     */
    @Column("metadata")
    private String metadata;

    /**
     * Indicates if webhook configuration is active
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
     * Timestamp when webhook was created
     */
    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp when webhook was last updated
     */
    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}


