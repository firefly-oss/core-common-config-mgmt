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
 * Manual test to verify @ValidSecretConfiguration validation works correctly
 * 
 * This can be run as a standalone Java application to verify the validation logic.
 * 
 * To run:
 * 1. Ensure the project is compiled: mvn clean compile -DskipTests
 * 2. Run this class with all required dependencies on the classpath
 */
public class ManualValidationTest {

    private static final Validator validator;
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    
    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("MANUAL VALIDATION TEST FOR @ValidSecretConfiguration");
        System.out.println("=".repeat(80));
        System.out.println();

        // ProviderParameterDTO Tests
        System.out.println("PROVIDER PARAMETER TESTS");
        System.out.println("-".repeat(80));
        testValidSecretParameter();
        testValidNonSecretParameter();
        testInvalidSecretParameterMissingVaultId();
        testInvalidSecretParameterWithBothValues();
        testInvalidNonSecretParameterMissingValue();
        testInvalidNonSecretParameterWithVaultId();
        
        System.out.println();
        
        // EnvironmentConfigDTO Tests
        System.out.println("ENVIRONMENT CONFIG TESTS");
        System.out.println("-".repeat(80));
        testValidSecretConfig();
        testValidNonSecretConfig();
        testInvalidSecretConfigMissingVaultId();
        testInvalidSecretConfigWithBothValues();
        testInvalidNonSecretConfigMissingValue();
        testInvalidNonSecretConfigWithVaultId();
        
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("TEST SUMMARY");
        System.out.println("=".repeat(80));
        System.out.println("✅ Tests Passed: " + testsPassed);
        System.out.println("❌ Tests Failed: " + testsFailed);
        System.out.println("Total Tests: " + (testsPassed + testsFailed));
        System.out.println();
        
        if (testsFailed == 0) {
            System.out.println("🎉 ALL TESTS PASSED!");
            System.exit(0);
        } else {
            System.out.println("⚠️  SOME TESTS FAILED");
            System.exit(1);
        }
    }

    // ========== ProviderParameterDTO Tests ==========

    private static void testValidSecretParameter() {
        System.out.print("Test 1: Valid secret parameter (isSecret=true, has vaultId, no value)... ");
        
        ProviderParameterDTO parameter = ProviderParameterDTO.builder()
            .id(UUID.randomUUID())
            .providerId(UUID.randomUUID())
            .tenantId(UUID.randomUUID())
            .parameterName("api_key")
            .isSecret(true)
            .credentialVaultId("550e8400-e29b-41d4-a716-446655440000")
            .parameterValue(null)
            .parameterType("STRING")
            .build();

        Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);
        
        if (violations.isEmpty()) {
            pass();
        } else {
            fail("Expected no violations but got: " + violations.iterator().next().getMessage());
        }
    }

    private static void testValidNonSecretParameter() {
        System.out.print("Test 2: Valid non-secret parameter (isSecret=false, has value, no vaultId)... ");
        
        ProviderParameterDTO parameter = ProviderParameterDTO.builder()
            .id(UUID.randomUUID())
            .providerId(UUID.randomUUID())
            .tenantId(UUID.randomUUID())
            .parameterName("api_timeout")
            .isSecret(false)
            .credentialVaultId(null)
            .parameterValue("30000")
            .parameterType("INTEGER")
            .build();

        Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);
        
        if (violations.isEmpty()) {
            pass();
        } else {
            fail("Expected no violations but got: " + violations.iterator().next().getMessage());
        }
    }

    private static void testInvalidSecretParameterMissingVaultId() {
        System.out.print("Test 3: Invalid secret parameter (isSecret=true but no vaultId)... ");
        
        ProviderParameterDTO parameter = ProviderParameterDTO.builder()
            .id(UUID.randomUUID())
            .providerId(UUID.randomUUID())
            .tenantId(UUID.randomUUID())
            .parameterName("api_key")
            .isSecret(true)
            .credentialVaultId(null)
            .parameterValue(null)
            .parameterType("STRING")
            .build();

        Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);
        
        if (!violations.isEmpty() && violations.iterator().next().getMessage().contains("credentialVaultId is required")) {
            pass();
        } else {
            fail("Expected violation about missing credentialVaultId");
        }
    }

    private static void testInvalidSecretParameterWithBothValues() {
        System.out.print("Test 4: Invalid secret parameter (has both vaultId and value)... ");
        
        ProviderParameterDTO parameter = ProviderParameterDTO.builder()
            .id(UUID.randomUUID())
            .providerId(UUID.randomUUID())
            .tenantId(UUID.randomUUID())
            .parameterName("api_key")
            .isSecret(true)
            .credentialVaultId("550e8400-e29b-41d4-a716-446655440000")
            .parameterValue("sk_live_secret")
            .parameterType("STRING")
            .build();

        Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);
        
        if (!violations.isEmpty() && violations.iterator().next().getMessage().contains("parameterValue must be null")) {
            pass();
        } else {
            fail("Expected violation about parameterValue must be null");
        }
    }

    private static void testInvalidNonSecretParameterMissingValue() {
        System.out.print("Test 5: Invalid non-secret parameter (isSecret=false but no value)... ");
        
        ProviderParameterDTO parameter = ProviderParameterDTO.builder()
            .id(UUID.randomUUID())
            .providerId(UUID.randomUUID())
            .tenantId(UUID.randomUUID())
            .parameterName("api_timeout")
            .isSecret(false)
            .credentialVaultId(null)
            .parameterValue(null)
            .parameterType("INTEGER")
            .build();

        Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);
        
        if (!violations.isEmpty() && violations.iterator().next().getMessage().contains("parameterValue is required")) {
            pass();
        } else {
            fail("Expected violation about missing parameterValue");
        }
    }

    private static void testInvalidNonSecretParameterWithVaultId() {
        System.out.print("Test 6: Invalid non-secret parameter (has vaultId when shouldn't)... ");
        
        ProviderParameterDTO parameter = ProviderParameterDTO.builder()
            .id(UUID.randomUUID())
            .providerId(UUID.randomUUID())
            .tenantId(UUID.randomUUID())
            .parameterName("api_timeout")
            .isSecret(false)
            .credentialVaultId("550e8400-e29b-41d4-a716-446655440000")
            .parameterValue("30000")
            .parameterType("INTEGER")
            .build();

        Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);
        
        if (!violations.isEmpty() && violations.iterator().next().getMessage().contains("credentialVaultId must be null")) {
            pass();
        } else {
            fail("Expected violation about credentialVaultId must be null");
        }
    }

    // ========== EnvironmentConfigDTO Tests ==========

    private static void testValidSecretConfig() {
        System.out.print("Test 7: Valid secret config (isSecret=true, has vaultId, no value)... ");
        
        EnvironmentConfigDTO config = EnvironmentConfigDTO.builder()
            .id(UUID.randomUUID())
            .tenantId(UUID.randomUUID())
            .environmentName("production")
            .configKey("database.password")
            .isSecret(true)
            .credentialVaultId("660e8400-e29b-41d4-a716-446655440001")
            .configValue(null)
            .configType("STRING")
            .build();

        Set<ConstraintViolation<EnvironmentConfigDTO>> violations = validator.validate(config);
        
        if (violations.isEmpty()) {
            pass();
        } else {
            fail("Expected no violations but got: " + violations.iterator().next().getMessage());
        }
    }

    private static void testValidNonSecretConfig() {
        System.out.print("Test 8: Valid non-secret config (isSecret=false, has value, no vaultId)... ");
        
        EnvironmentConfigDTO config = EnvironmentConfigDTO.builder()
            .id(UUID.randomUUID())
            .tenantId(UUID.randomUUID())
            .environmentName("production")
            .configKey("database.host")
            .isSecret(false)
            .credentialVaultId(null)
            .configValue("localhost")
            .configType("STRING")
            .build();

        Set<ConstraintViolation<EnvironmentConfigDTO>> violations = validator.validate(config);
        
        if (violations.isEmpty()) {
            pass();
        } else {
            fail("Expected no violations but got: " + violations.iterator().next().getMessage());
        }
    }

    private static void testInvalidSecretConfigMissingVaultId() {
        System.out.print("Test 9: Invalid secret config (isSecret=true but no vaultId)... ");
        
        EnvironmentConfigDTO config = EnvironmentConfigDTO.builder()
            .id(UUID.randomUUID())
            .tenantId(UUID.randomUUID())
            .environmentName("production")
            .configKey("database.password")
            .isSecret(true)
            .credentialVaultId(null)
            .configValue(null)
            .configType("STRING")
            .build();

        Set<ConstraintViolation<EnvironmentConfigDTO>> violations = validator.validate(config);
        
        if (!violations.isEmpty() && violations.iterator().next().getMessage().contains("credentialVaultId is required")) {
            pass();
        } else {
            fail("Expected violation about missing credentialVaultId");
        }
    }

    private static void testInvalidSecretConfigWithBothValues() {
        System.out.print("Test 10: Invalid secret config (has both vaultId and value)... ");
        
        EnvironmentConfigDTO config = EnvironmentConfigDTO.builder()
            .id(UUID.randomUUID())
            .tenantId(UUID.randomUUID())
            .environmentName("production")
            .configKey("database.password")
            .isSecret(true)
            .credentialVaultId("660e8400-e29b-41d4-a716-446655440001")
            .configValue("super_secret_password")
            .configType("STRING")
            .build();

        Set<ConstraintViolation<EnvironmentConfigDTO>> violations = validator.validate(config);
        
        if (!violations.isEmpty() && violations.iterator().next().getMessage().contains("configValue must be null")) {
            pass();
        } else {
            fail("Expected violation about configValue must be null");
        }
    }

    private static void testInvalidNonSecretConfigMissingValue() {
        System.out.print("Test 11: Invalid non-secret config (isSecret=false but no value)... ");
        
        EnvironmentConfigDTO config = EnvironmentConfigDTO.builder()
            .id(UUID.randomUUID())
            .tenantId(UUID.randomUUID())
            .environmentName("production")
            .configKey("database.host")
            .isSecret(false)
            .credentialVaultId(null)
            .configValue(null)
            .configType("STRING")
            .build();

        Set<ConstraintViolation<EnvironmentConfigDTO>> violations = validator.validate(config);
        
        if (!violations.isEmpty() && violations.iterator().next().getMessage().contains("configValue is required")) {
            pass();
        } else {
            fail("Expected violation about missing configValue");
        }
    }

    private static void testInvalidNonSecretConfigWithVaultId() {
        System.out.print("Test 12: Invalid non-secret config (has vaultId when shouldn't)... ");
        
        EnvironmentConfigDTO config = EnvironmentConfigDTO.builder()
            .id(UUID.randomUUID())
            .tenantId(UUID.randomUUID())
            .environmentName("production")
            .configKey("database.host")
            .isSecret(false)
            .credentialVaultId("660e8400-e29b-41d4-a716-446655440001")
            .configValue("localhost")
            .configType("STRING")
            .build();

        Set<ConstraintViolation<EnvironmentConfigDTO>> violations = validator.validate(config);
        
        if (!violations.isEmpty() && violations.iterator().next().getMessage().contains("credentialVaultId must be null")) {
            pass();
        } else {
            fail("Expected violation about credentialVaultId must be null");
        }
    }

    // ========== Helper Methods ==========

    private static void pass() {
        System.out.println("✅ PASS");
        testsPassed++;
    }

    private static void fail(String reason) {
        System.out.println("❌ FAIL: " + reason);
        testsFailed++;
    }
}

