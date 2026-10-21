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
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.UUID;

/**
 * Example demonstrating the @ValidSecretConfiguration validation
 * 
 * This shows how the validation works for both ProviderParameterDTO and EnvironmentConfigDTO
 */
public class ValidationExample {

    private static final Validator validator;
    
    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static void main(String[] args) {
        System.out.println("=== Secret Configuration Validation Examples ===\n");
        
        // Example 1: Valid secret parameter
        testValidSecretParameter();
        
        // Example 2: Valid non-secret parameter
        testValidNonSecretParameter();
        
        // Example 3: Invalid - secret without credentialVaultId
        testInvalidSecretWithoutVaultId();
        
        // Example 4: Invalid - secret with both values
        testInvalidSecretWithBothValues();
        
        // Example 5: Invalid - non-secret without value
        testInvalidNonSecretWithoutValue();
        
        // Example 6: Valid secret environment config
        testValidSecretEnvironmentConfig();
        
        // Example 7: Invalid - non-secret with vaultId
        testInvalidNonSecretWithVaultId();
    }

    private static void testValidSecretParameter() {
        System.out.println("1. Testing VALID secret parameter:");
        
        ProviderParameterDTO param = ProviderParameterDTO.builder()
            .providerId(UUID.randomUUID())
            .parameterName("api_key")
            .isSecret(true)
            .credentialVaultId("550e8400-e29b-41d4-a716-446655440000")
            .parameterValue(null)
            .build();
        
        Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(param);
        
        if (violations.isEmpty()) {
            System.out.println("   ✅ PASS - No validation errors\n");
        } else {
            System.out.println("   ❌ FAIL - Unexpected validation errors:");
            violations.forEach(v -> System.out.println("      - " + v.getMessage()));
            System.out.println();
        }
    }

    private static void testValidNonSecretParameter() {
        System.out.println("2. Testing VALID non-secret parameter:");
        
        ProviderParameterDTO param = ProviderParameterDTO.builder()
            .providerId(UUID.randomUUID())
            .parameterName("api_timeout")
            .isSecret(false)
            .credentialVaultId(null)
            .parameterValue("30000")
            .build();
        
        Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(param);
        
        if (violations.isEmpty()) {
            System.out.println("   ✅ PASS - No validation errors\n");
        } else {
            System.out.println("   ❌ FAIL - Unexpected validation errors:");
            violations.forEach(v -> System.out.println("      - " + v.getMessage()));
            System.out.println();
        }
    }

    private static void testInvalidSecretWithoutVaultId() {
        System.out.println("3. Testing INVALID secret parameter (missing credentialVaultId):");
        
        ProviderParameterDTO param = ProviderParameterDTO.builder()
            .providerId(UUID.randomUUID())
            .parameterName("api_key")
            .isSecret(true)
            .credentialVaultId(null)  // ❌ Missing!
            .parameterValue(null)
            .build();
        
        Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(param);
        
        if (!violations.isEmpty()) {
            System.out.println("   ✅ PASS - Validation correctly caught the error:");
            violations.forEach(v -> System.out.println("      - " + v.getMessage()));
            System.out.println();
        } else {
            System.out.println("   ❌ FAIL - Should have validation errors but got none\n");
        }
    }

    private static void testInvalidSecretWithBothValues() {
        System.out.println("4. Testing INVALID secret parameter (has both credentialVaultId and parameterValue):");
        
        ProviderParameterDTO param = ProviderParameterDTO.builder()
            .providerId(UUID.randomUUID())
            .parameterName("api_key")
            .isSecret(true)
            .credentialVaultId("550e8400-e29b-41d4-a716-446655440000")
            .parameterValue("sk_live_abc123")  // ❌ Should be null!
            .build();
        
        Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(param);
        
        if (!violations.isEmpty()) {
            System.out.println("   ✅ PASS - Validation correctly caught the security issue:");
            violations.forEach(v -> System.out.println("      - " + v.getMessage()));
            System.out.println();
        } else {
            System.out.println("   ❌ FAIL - Should have validation errors but got none\n");
        }
    }

    private static void testInvalidNonSecretWithoutValue() {
        System.out.println("5. Testing INVALID non-secret parameter (missing parameterValue):");
        
        ProviderParameterDTO param = ProviderParameterDTO.builder()
            .providerId(UUID.randomUUID())
            .parameterName("api_timeout")
            .isSecret(false)
            .credentialVaultId(null)
            .parameterValue(null)  // ❌ Missing!
            .build();
        
        Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(param);
        
        if (!violations.isEmpty()) {
            System.out.println("   ✅ PASS - Validation correctly caught the error:");
            violations.forEach(v -> System.out.println("      - " + v.getMessage()));
            System.out.println();
        } else {
            System.out.println("   ❌ FAIL - Should have validation errors but got none\n");
        }
    }

    private static void testValidSecretEnvironmentConfig() {
        System.out.println("6. Testing VALID secret environment config:");
        
        EnvironmentConfigDTO config = EnvironmentConfigDTO.builder()
            .tenantId(UUID.randomUUID())
            .environmentName("production")
            .configKey("database.password")
            .isSecret(true)
            .credentialVaultId("660e8400-e29b-41d4-a716-446655440001")
            .configValue(null)
            .build();
        
        Set<ConstraintViolation<EnvironmentConfigDTO>> violations = validator.validate(config);
        
        if (violations.isEmpty()) {
            System.out.println("   ✅ PASS - No validation errors\n");
        } else {
            System.out.println("   ❌ FAIL - Unexpected validation errors:");
            violations.forEach(v -> System.out.println("      - " + v.getMessage()));
            System.out.println();
        }
    }

    private static void testInvalidNonSecretWithVaultId() {
        System.out.println("7. Testing INVALID non-secret parameter (has credentialVaultId):");
        
        ProviderParameterDTO param = ProviderParameterDTO.builder()
            .providerId(UUID.randomUUID())
            .parameterName("api_timeout")
            .isSecret(false)
            .credentialVaultId("550e8400-e29b-41d4-a716-446655440000")  // ❌ Should be null!
            .parameterValue("30000")
            .build();
        
        Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(param);
        
        if (!violations.isEmpty()) {
            System.out.println("   ✅ PASS - Validation correctly caught the error:");
            violations.forEach(v -> System.out.println("      - " + v.getMessage()));
            System.out.println();
        } else {
            System.out.println("   ❌ FAIL - Should have validation errors but got none\n");
        }
    }
}

