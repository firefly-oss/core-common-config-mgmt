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

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.fireflyframework.utils.annotations.FilterableId;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for WebhookConfig entity.
 *
 * <p>Represents webhook configuration for event notifications from Firefly to external systems.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Webhook configuration for event-driven notifications. Defines how Firefly " +
        "sends real-time event notifications to external systems via HTTP callbacks.")
public class WebhookConfigDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier for the webhook configuration",
            example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @FilterableId
    @Schema(description = "Reference to tenant owning this webhook")
    private UUID tenantId;

    @FilterableId
    @Schema(description = "Reference to provider if webhook is provider-specific")
    private UUID providerId;

    @NotBlank(message = "Webhook name is required")
    @Size(min = 2, max = 100, message = "Webhook name must be between 2 and 100 characters")
    @Schema(description = "Descriptive name for the webhook", example = "Payment Notification Webhook", required = true)
    private String webhookName;

    @NotBlank(message = "Webhook URL is required")
    @Pattern(regexp = "^https?://.*", message = "Webhook URL must be a valid HTTP(S) URL")
    @Schema(description = "Target URL for webhook delivery", example = "https://api.example.com/webhooks/payments", required = true)
    private String webhookUrl;

    @Schema(description = "Detailed description of webhook purpose and usage",
            example = "Sends notifications for all payment-related events")
    private String description;

    @Schema(description = "Comma-separated list of event types to trigger this webhook",
            example = "payment.created,payment.completed,payment.failed")
    private String eventTypes;

    @Schema(description = "HTTP method for webhook delivery", example = "POST")
    private String httpMethod;

    @Schema(description = "Content-Type header for webhook requests", example = "application/json")
    private String contentType;

    // Enhanced Authentication

    @Schema(description = "Webhook protocol version", example = "1.0")
    private String webhookVersion;

    @Schema(description = "Authentication type: NONE, BASIC, BEARER, API_KEY, HMAC, OAUTH2", example = "HMAC")
    private String authType;

    @Schema(description = "Authentication header name", example = "Authorization")
    private String authHeaderName;

    @Schema(description = "Authentication header value (encrypted at rest)", example = "Bearer sk_live_...")
    private String authHeaderValue;

    @Schema(description = "Secret key for HMAC signature generation (encrypted at rest)")
    private String secretKey;

    @Schema(description = "Signature algorithm: HMAC-SHA256, HMAC-SHA512, RSA-SHA256", example = "HMAC-SHA256")
    private String signatureAlgorithm;

    @Schema(description = "HTTP header name for signature", example = "X-Webhook-Signature")
    private String signatureHeaderName;

    @Schema(description = "Indicates if SSL certificate verification is enabled", example = "true")
    private Boolean verifySsl;

    @Schema(description = "Indicates if HTTP redirects should be followed", example = "true")
    private Boolean followRedirects;

    @Schema(description = "Custom HTTP headers in JSON format", example = "{\"X-Custom-Header\": \"value\"}")
    private String customHeaders;

    // Retry and Timeout Configuration

    @Min(value = 1, message = "Timeout must be at least 1 second")
    @Schema(description = "Request timeout in seconds", example = "30")
    private Integer timeoutSeconds;

    @Schema(description = "Indicates if retry mechanism is enabled on failure", example = "true")
    private Boolean retryEnabled;

    @Min(value = 0, message = "Max retry attempts must be non-negative")
    @Schema(description = "Maximum number of retry attempts", example = "3")
    private Integer maxRetryAttempts;

    @Min(value = 1, message = "Retry delay must be at least 1 second")
    @Schema(description = "Initial retry delay in seconds", example = "60")
    private Integer retryDelaySeconds;

    @Min(value = 1, message = "Backoff multiplier must be at least 1")
    @Schema(description = "Exponential backoff multiplier for retries", example = "2.0")
    private Double retryBackoffMultiplier;

    @Schema(description = "Maximum retry delay in seconds (caps exponential backoff)", example = "3600")
    private Integer maxRetryDelaySeconds;

    // Batching Configuration

    @Schema(description = "Indicates if events should be batched before delivery", example = "false")
    private Boolean batchEvents;

    @Schema(description = "Maximum number of events per batch", example = "100")
    private Integer batchSize;

    @Schema(description = "Maximum time in seconds to wait before sending partial batch", example = "60")
    private Integer batchWindowSeconds;

    @Schema(description = "Timeout in seconds for batch processing", example = "300")
    private Integer batchTimeoutSeconds;

    // Processing Configuration

    @Schema(description = "Indicates if failed events are sent to dead letter queue", example = "true")
    private Boolean deadLetterQueueEnabled;

    @Schema(description = "Maximum number of retries for dead letter queue processing", example = "5")
    private Integer maxDeadLetterRetries;

    @Schema(description = "Indicates if webhook processing is asynchronous", example = "true")
    private Boolean asyncProcessing;

    @Schema(description = "Priority level for webhook delivery: LOW, NORMAL, HIGH, CRITICAL", example = "NORMAL")
    private String priority;

    // Circuit Breaker

    @Schema(description = "Indicates if circuit breaker pattern is enabled", example = "true")
    private Boolean circuitBreakerEnabled;

    @Schema(description = "Failure threshold percentage to open circuit breaker", example = "50")
    private Integer circuitBreakerThreshold;

    @Schema(description = "Timeout in seconds before attempting to close circuit breaker", example = "300")
    private Integer circuitBreakerTimeoutSeconds;


    // Metrics and Monitoring

    @Schema(description = "Timestamp of last webhook trigger", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime lastTriggeredAt;

    @Schema(description = "Total number of times webhook was triggered", example = "1523", accessMode = Schema.AccessMode.READ_ONLY)
    private Long totalTriggersCount;

    @Schema(description = "Total number of successful deliveries", example = "1498", accessMode = Schema.AccessMode.READ_ONLY)
    private Long totalSuccessCount;

    @Schema(description = "Total number of failed deliveries", example = "25", accessMode = Schema.AccessMode.READ_ONLY)
    private Long totalFailureCount;

    @Schema(description = "Average response time in milliseconds", example = "245", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer averageResponseTimeMs;

    @Schema(description = "Timestamp of last successful delivery", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime lastSuccessAt;

    @Schema(description = "Timestamp of last failed delivery", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime lastFailureAt;

    @Schema(description = "Last failure reason or error message", accessMode = Schema.AccessMode.READ_ONLY)
    private String lastFailureReason;

    // Health Monitoring

    @Schema(description = "Current health status: HEALTHY, DEGRADED, DOWN, UNKNOWN", example = "HEALTHY", accessMode = Schema.AccessMode.READ_ONLY)
    private String healthStatus;

    @Schema(description = "Consecutive failure count (resets on success)", example = "0", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer consecutiveFailureCount;

    @Schema(description = "Indicates if health check monitoring is enabled", example = "true")
    private Boolean healthCheckEnabled;

    @Schema(description = "Health check interval in seconds", example = "300")
    private Integer healthCheckIntervalSeconds;

    // Control Flags

    @Schema(description = "Indicates if the webhook is enabled for event delivery", example = "true")
    private Boolean enabled;

    // System Fields

    @Schema(description = "Additional metadata in JSON format for extensibility", example = "{\"custom_field\":\"value\"}")
    private String metadata;

    @Schema(description = "Indicates if the webhook configuration is active", example = "true")
    private Boolean active;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Version number for optimistic locking", accessMode = Schema.AccessMode.READ_ONLY)
    private Long version;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Timestamp when webhook was created", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Timestamp when webhook was last updated", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}

