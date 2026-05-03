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


package com.firefly.common.config.web.controllers;

import com.firefly.common.config.core.services.WebhookConfigService;
import com.firefly.common.config.interfaces.dtos.WebhookConfigDTO;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.util.UUID;

/**
 * REST controller for managing Webhook Configurations in the Firefly core banking platform.
 *
 * <p>Webhooks enable real-time event-driven notifications from Firefly to external systems via HTTP callbacks.
 * Configure webhooks to receive notifications for payment events, account changes, KYC updates, fraud alerts,
 * and other critical banking events. Each webhook supports advanced features including authentication (HMAC, OAuth2),
 * retry logic with exponential backoff, event filtering, batching, circuit breaker patterns, and comprehensive
 * delivery tracking.</p>
 */
@RestController
@RequestMapping("/api/v1/webhook-configs")
@RequiredArgsConstructor
@Tag(
    name = "Webhook Configuration",
    description = "Configure event-driven HTTP callbacks for real-time notifications. " +
                  "Webhooks enable Firefly to push events (payments, account changes, KYC updates, fraud alerts) " +
                  "to external systems in real-time. Configure target URLs, authentication (HMAC, OAuth2, API keys), " +
                  "event filtering, retry policies with exponential backoff, batching, circuit breakers, and SSL verification. " +
                  "Monitor webhook health, delivery success rates, response times, and troubleshoot failures with detailed metrics."
)
public class WebhookConfigController {

    private final WebhookConfigService webhookConfigService;

    @GetMapping("/{id}")
    @Operation(
            operationId = "getWebhookConfigById",
            summary = "Retrieve webhook configuration by ID",
            description = "Fetches complete webhook configuration including target URL, event filters, authentication settings, " +
                         "retry policies, batching configuration, circuit breaker settings, and delivery metrics (success rate, " +
                         "average response time, failure count). Sensitive fields like secret keys are masked in the response.",
            responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Webhook configuration successfully retrieved",
                        content = @Content(schema = @Schema(implementation = WebhookConfigDTO.class))
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Webhook configuration not found - The specified webhook ID does not exist"
                    ),
                    @ApiResponse(
                        responseCode = "403",
                        description = "Access denied - Insufficient permissions to view this webhook configuration"
                    )
            }
    )
    public ResponseEntity<Mono<WebhookConfigDTO>> getById(
            @Parameter(
                description = "Unique identifier (UUID) of the webhook configuration to retrieve",
                required = true,
                example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        return ResponseEntity.ok(webhookConfigService.getById(id));
    }

    @PostMapping("/filter")
    @Operation(
            operationId = "filterWebhookConfigs",
            summary = "Search and filter webhook configurations",
            description = "Performs advanced filtering and pagination of webhook configurations. Supports filtering by tenant, " +
                         "provider, event types, enabled status, health status, and authentication type. Results include delivery " +
                         "metrics for monitoring webhook performance. Useful for webhook management dashboards and troubleshooting.",
            responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Filtered webhook configurations successfully retrieved with pagination metadata"
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Invalid filter criteria - Check filter syntax and field names"
                    )
            }
    )
    public ResponseEntity<Mono<PaginationResponse<WebhookConfigDTO>>> filter(
            @ParameterObject @ModelAttribute FilterRequest<WebhookConfigDTO> filterRequest) {
        return ResponseEntity.ok(webhookConfigService.filter(filterRequest));
    }

    @PostMapping
    @Operation(
            operationId = "createWebhookConfig",
            summary = "Create a new webhook configuration",
            description = "Creates a new webhook for receiving real-time event notifications. Required fields include webhook name, " +
                         "target URL, and event types to subscribe to. Configure authentication (NONE, BASIC, BEARER, API_KEY, HMAC, OAUTH2), " +
                         "retry behavior (max attempts, delay, exponential backoff), timeout settings, batching options, and circuit breaker " +
                         "thresholds. Webhooks can be tenant-specific or provider-specific. SSL verification is enabled by default for security.",
            responses = {
                    @ApiResponse(
                        responseCode = "201",
                        description = "Webhook configuration successfully created - Returns the created webhook with generated ID",
                        content = @Content(schema = @Schema(implementation = WebhookConfigDTO.class))
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Invalid input - Validation errors (e.g., invalid URL, unsupported event types, invalid auth config)"
                    ),
                    @ApiResponse(
                        responseCode = "409",
                        description = "Conflict - A webhook with similar configuration already exists for this tenant/provider"
                    )
            }
    )
    public ResponseEntity<Mono<WebhookConfigDTO>> create(
            @Parameter(
                description = "Webhook configuration to create. Must include name, target URL, and event types. " +
                             "Optionally configure authentication, retry policies, batching, and circuit breaker settings.",
                required = true
            )
            @Valid @RequestBody WebhookConfigDTO webhookConfigDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(webhookConfigService.create(webhookConfigDTO));
    }

    @PutMapping("/{id}")
    @Operation(
            operationId = "updateWebhookConfig",
            summary = "Update existing webhook configuration",
            description = "Updates webhook configuration including URL, event filters, authentication settings, retry policies, " +
                         "and circuit breaker thresholds. Uses optimistic locking to prevent concurrent modifications. " +
                         "Changing authentication settings or URL may require re-verification. Delivery metrics are preserved " +
                         "but can be reset if needed. Disabling a webhook stops event delivery but preserves configuration.",
            responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Webhook configuration successfully updated - Returns updated webhook with new version number",
                        content = @Content(schema = @Schema(implementation = WebhookConfigDTO.class))
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Webhook configuration not found - The specified webhook ID does not exist"
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Invalid input - Validation errors or business rule violations"
                    ),
                    @ApiResponse(
                        responseCode = "409",
                        description = "Conflict - Version mismatch due to concurrent modification"
                    )
            }
    )
    public ResponseEntity<Mono<WebhookConfigDTO>> update(
            @Parameter(description = "ID of the webhook configuration to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated webhook configuration data", required = true)
            @Valid @RequestBody WebhookConfigDTO webhookConfigDTO) {
        return ResponseEntity.ok(webhookConfigService.update(id, webhookConfigDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(
            operationId = "deleteWebhookConfig",
            summary = "Delete a webhook configuration",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Webhook configuration deleted"),
                    @ApiResponse(responseCode = "404", description = "Webhook configuration not found")
            }
    )
    public ResponseEntity<Mono<Void>> delete(
            @Parameter(description = "ID of the webhook configuration to delete", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(webhookConfigService.delete(id));
    }
}

