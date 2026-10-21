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


package io.fireflysoftware.common.config.examples;

import io.fireflysoftware.common.config.interfaces.dtos.EnvironmentConfigDTO;
import io.fireflysoftware.common.config.interfaces.dtos.ProviderParameterDTO;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Examples of using secure configurations with Security Vault integration
 * 
 * This file demonstrates best practices for:
 * - Consuming secret parameters from providers
 * - Consuming secret environment configurations
 * - Handling errors securely
 * - Implementing caching for credentials
 * 
 * IMPORTANT: These examples assume you have:
 * 1. ConfigManagementClient - client for this service
 * 2. SecurityVaultClient - client for core-common-security-vault
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SecureConfigurationExample {

    // Injected clients (these would be your actual clients)
    // private final ConfigManagementClient configClient;
    // private final SecurityVaultClient vaultClient;

    /**
     * EXAMPLE 1: Get API Key from a provider (e.g., Stripe)
     * 
     * This is the most common pattern for retrieving external API credentials
     */
    public Mono<String> getProviderApiKey(UUID providerId, UUID tenantId, String parameterName) {
        log.debug("Retrieving API key for provider={}, tenant={}, param={}", 
                  providerId, tenantId, parameterName);
        
        // 1. Get the parameter configuration
        return Mono.just(new ProviderParameterDTO()) // configClient.getProviderParameter(providerId, tenantId, parameterName)
            .flatMap(parameter -> {
                // 2. Check if it's a secret
                if (Boolean.TRUE.equals(parameter.getIsSecret())) {
                    // 3a. It's a secret: decrypt from vault
                    log.debug("Parameter is secret, decrypting from vault: {}", 
                              parameter.getCredentialVaultId());
                    
                    UUID credentialId = UUID.fromString(parameter.getCredentialVaultId());
                    
                    // Build access request for audit trail
                    AccessRequest accessRequest = AccessRequest.builder()
                        .userId(getCurrentUserId())
                        .serviceName(getCurrentServiceName())
                        .ipAddress(getCurrentIpAddress())
                        .environment("production")
                        .reason("Retrieving API key for payment processing")
                        .build();
                    
                    return decryptCredentialFromVault(credentialId, accessRequest);
                } else {
                    // 3b. Not a secret: use direct value
                    log.debug("Parameter is not secret, using direct value");
                    return Mono.justOrEmpty(parameter.getParameterValue());
                }
            })
            .doOnSuccess(value -> log.info("Successfully retrieved API key"))
            .doOnError(error -> log.error("Failed to retrieve API key", error));
    }

    /**
     * EXAMPLE 2: Get environment configuration (e.g., Database Password)
     */
    public Mono<String> getEnvironmentConfig(UUID tenantId, String environment, String configKey) {
        log.debug("Retrieving environment config for tenant={}, env={}, key={}", 
                  tenantId, environment, configKey);
        
        return Mono.just(new EnvironmentConfigDTO()) // configClient.getEnvironmentConfig(tenantId, environment, configKey)
            .flatMap(config -> {
                if (Boolean.TRUE.equals(config.getIsSecret())) {
                    log.debug("Config is secret, decrypting from vault: {}", 
                              config.getCredentialVaultId());
                    
                    UUID credentialId = UUID.fromString(config.getCredentialVaultId());
                    
                    AccessRequest accessRequest = AccessRequest.builder()
                        .userId(getCurrentUserId())
                        .serviceName(getCurrentServiceName())
                        .ipAddress(getCurrentIpAddress())
                        .environment(environment)
                        .reason("Retrieving database configuration")
                        .build();
                    
                    return decryptCredentialFromVault(credentialId, accessRequest);
                } else {
                    return Mono.justOrEmpty(config.getConfigValue());
                }
            });
    }

    /**
     * EXAMPLE 3: Generic helper for getting parameter value
     * 
     * This method can be reused throughout your application
     */
    public Mono<String> getParameterValue(ProviderParameterDTO parameter, AccessRequest accessRequest) {
        // Input validation
        if (parameter == null) {
            return Mono.error(new IllegalArgumentException("Parameter cannot be null"));
        }

        // Consistency validation
        if (Boolean.TRUE.equals(parameter.getIsSecret())) {
            if (parameter.getCredentialVaultId() == null || parameter.getCredentialVaultId().isBlank()) {
                return Mono.error(new IllegalStateException(
                    String.format("Parameter '%s' is marked as secret but credentialVaultId is missing",
                                  parameter.getParameterName())
                ));
            }
            
            // Security warning if both values are set
            if (parameter.getParameterValue() != null && !parameter.getParameterValue().isBlank()) {
                log.warn("SECURITY WARNING: Parameter '{}' has both credentialVaultId and parameterValue set. " +
                         "The parameterValue should be null for secret parameters!",
                         parameter.getParameterName());
            }
            
            UUID credentialId = UUID.fromString(parameter.getCredentialVaultId());
            return decryptCredentialFromVault(credentialId, accessRequest);
        } else {
            // Not a secret: use direct value
            if (parameter.getParameterValue() == null || parameter.getParameterValue().isBlank()) {
                return Mono.error(new IllegalStateException(
                    String.format("Parameter '%s' is not secret but parameterValue is missing",
                                  parameter.getParameterName())
                ));
            }
            return Mono.just(parameter.getParameterValue());
        }
    }

    /**
     * EXAMPLE 4: Generic helper for getting environment config value
     */
    public Mono<String> getConfigValue(EnvironmentConfigDTO config, AccessRequest accessRequest) {
        if (config == null) {
            return Mono.error(new IllegalArgumentException("Config cannot be null"));
        }

        if (Boolean.TRUE.equals(config.getIsSecret())) {
            if (config.getCredentialVaultId() == null || config.getCredentialVaultId().isBlank()) {
                return Mono.error(new IllegalStateException(
                    String.format("Config '%s' is marked as secret but credentialVaultId is missing",
                                  config.getConfigKey())
                ));
            }
            
            UUID credentialId = UUID.fromString(config.getCredentialVaultId());
            return decryptCredentialFromVault(credentialId, accessRequest);
        } else {
            if (config.getConfigValue() == null || config.getConfigValue().isBlank()) {
                return Mono.error(new IllegalStateException(
                    String.format("Config '%s' is not secret but configValue is missing",
                                  config.getConfigKey())
                ));
            }
            return Mono.just(config.getConfigValue());
        }
    }

    /**
     * EXAMPLE 5: Real-world usage in a payment service
     */
    public Mono<PaymentResult> processStripePayment(UUID tenantId, PaymentRequest request) {
        UUID stripeProviderId = UUID.fromString("your-stripe-provider-uuid");
        
        // Build access request
        AccessRequest accessRequest = AccessRequest.builder()
            .userId(getCurrentUserId())
            .serviceName("payment-service")
            .ipAddress(getCurrentIpAddress())
            .environment("production")
            .reason("Processing payment transaction #" + request.transactionId())
            .build();
        
        // 1. Get Stripe API Key
        return getProviderApiKey(stripeProviderId, tenantId, "api_key")
            .flatMap(apiKey -> {
                // 2. Get Webhook Secret (optional)
                return getProviderApiKey(stripeProviderId, tenantId, "webhook_secret")
                    .map(webhookSecret -> new StripeCredentials(apiKey, webhookSecret));
            })
            .flatMap(credentials -> {
                // 3. Use credentials to process payment
                return processPaymentWithStripe(credentials, request);
            })
            .doOnSuccess(result -> log.info("Payment processed successfully: {}", result.transactionId()))
            .doOnError(error -> log.error("Payment processing failed", error));
    }

    /**
     * EXAMPLE 6: Configure database connection with vault credentials
     */
    public Mono<DatabaseConnection> createDatabaseConnection(UUID tenantId, String environment) {
        AccessRequest accessRequest = AccessRequest.builder()
            .userId("system")
            .serviceName(getCurrentServiceName())
            .ipAddress(getCurrentIpAddress())
            .environment(environment)
            .reason("Initializing database connection pool")
            .build();
        
        // Get all configurations in parallel
        Mono<String> hostMono = getEnvironmentConfig(tenantId, environment, "database.host");
        Mono<String> portMono = getEnvironmentConfig(tenantId, environment, "database.port");
        Mono<String> usernameMono = getEnvironmentConfig(tenantId, environment, "database.username");
        
        // Password is secret - will be decrypted from vault
        Mono<String> passwordMono = Mono.just(new EnvironmentConfigDTO()) // configClient.getEnvironmentConfig(...)
            .flatMap(config -> getConfigValue(config, accessRequest));
        
        return Mono.zip(hostMono, portMono, usernameMono, passwordMono)
            .map(tuple -> DatabaseConnection.builder()
                .host(tuple.getT1())
                .port(Integer.parseInt(tuple.getT2()))
                .username(tuple.getT3())
                .password(tuple.getT4())
                .build())
            .doOnSuccess(conn -> log.info("Database connection created for tenant={}, env={}", 
                                          tenantId, environment));
    }

    /**
     * EXAMPLE 7: Error handling with fallback
     */
    public Mono<String> getApiKeyWithFallback(UUID providerId, UUID tenantId) {
        return getProviderApiKey(providerId, tenantId, "api_key")
            .onErrorResume(VaultNotFoundException.class, error -> {
                log.error("Credential not found in vault, using fallback", error);
                return getFallbackApiKey(providerId, tenantId);
            })
            .onErrorResume(VaultAccessDeniedException.class, error -> {
                log.error("Access denied to vault credential", error);
                return Mono.error(new UnauthorizedException("Access denied to credential"));
            })
            .onErrorResume(error -> {
                log.error("Unexpected error retrieving API key", error);
                return Mono.error(error);
            });
    }

    /**
     * EXAMPLE 8: Validation before creating/updating parameters
     */
    public Mono<ProviderParameterDTO> createSecureParameter(CreateParameterRequest request) {
        // Validate that if it's secret, it has credentialVaultId
        if (Boolean.TRUE.equals(request.getIsSecret())) {
            if (request.getCredentialVaultId() == null || request.getCredentialVaultId().isBlank()) {
                return Mono.error(new IllegalArgumentException(
                    "When isSecret=true, credentialVaultId must be provided"
                ));
            }
            
            // Ensure parameterValue is empty
            if (request.getParameterValue() != null && !request.getParameterValue().isBlank()) {
                log.warn("Clearing parameterValue because isSecret=true");
                request.setParameterValue(null);
            }
            
            // Verify credential exists in vault
            UUID credentialId = UUID.fromString(request.getCredentialVaultId());
            return verifyCredentialExists(credentialId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new IllegalArgumentException(
                            "Credential not found in vault: " + credentialId
                        ));
                    }
                    return createParameter(request);
                });
        } else {
            // Not secret: validate that it has parameterValue
            if (request.getParameterValue() == null || request.getParameterValue().isBlank()) {
                return Mono.error(new IllegalArgumentException(
                    "When isSecret=false, parameterValue must be provided"
                ));
            }
            
            // Ensure credentialVaultId is empty
            if (request.getCredentialVaultId() != null) {
                log.warn("Clearing credentialVaultId because isSecret=false");
                request.setCredentialVaultId(null);
            }
            
            return createParameter(request);
        }
    }

    // ========== Helper methods (simulated) ==========

    private Mono<String> decryptCredentialFromVault(UUID credentialId, AccessRequest accessRequest) {
        // In production, this would call the SecurityVaultClient
        // return vaultClient.decryptCredential(credentialId, accessRequest);
        
        log.debug("Decrypting credential from vault: {}", credentialId);
        return Mono.just("simulated-decrypted-value");
    }

    private Mono<Boolean> verifyCredentialExists(UUID credentialId) {
        // return vaultClient.credentialExists(credentialId);
        return Mono.just(true);
    }

    private Mono<String> getFallbackApiKey(UUID providerId, UUID tenantId) {
        return Mono.just("fallback-api-key");
    }

    private Mono<PaymentResult> processPaymentWithStripe(StripeCredentials credentials, PaymentRequest request) {
        return Mono.just(new PaymentResult("txn-123", "SUCCESS"));
    }

    private Mono<ProviderParameterDTO> createParameter(CreateParameterRequest request) {
        // return configClient.createProviderParameter(request);
        return Mono.just(new ProviderParameterDTO());
    }

    private String getCurrentUserId() {
        // Extract from security context
        return "user-123";
    }

    private String getCurrentServiceName() {
        // Extract from application context
        return "config-mgmt-service";
    }

    private String getCurrentIpAddress() {
        // Extract from request context
        return "10.0.0.1";
    }

    // ========== Supporting classes ==========

    @Data
    @Builder
    public static class AccessRequest {
        private String userId;
        private String serviceName;
        private String ipAddress;
        private String environment;
        private String reason;
    }

    private record StripeCredentials(String apiKey, String webhookSecret) {}
    
    private record PaymentRequest(String transactionId, String amount, String currency) {}
    
    private record PaymentResult(String transactionId, String status) {}
    
    @Data
    public static class CreateParameterRequest {
        private Boolean isSecret;
        private String credentialVaultId;
        private String parameterValue;
    }
    
    @Data
    @Builder
    public static class DatabaseConnection {
        private String host;
        private Integer port;
        private String username;
        private String password;
    }
    
    public static class VaultNotFoundException extends RuntimeException {
        public VaultNotFoundException(String message) {
            super(message);
        }
    }
    
    public static class VaultAccessDeniedException extends RuntimeException {
        public VaultAccessDeniedException(String message) {
            super(message);
        }
    }
    
    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}

