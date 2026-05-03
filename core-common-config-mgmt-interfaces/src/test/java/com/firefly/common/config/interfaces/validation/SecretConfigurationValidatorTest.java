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

package com.firefly.common.config.interfaces.validation;

import com.firefly.common.config.interfaces.dtos.EnvironmentConfigDTO;
import com.firefly.common.config.interfaces.dtos.ProviderParameterDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SecretConfigurationValidator}
 * 
 * Tests the validation logic for secret configuration in both
 * ProviderParameterDTO and EnvironmentConfigDTO.
 */
@DisplayName("SecretConfigurationValidator Tests")
class SecretConfigurationValidatorTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("ProviderParameterDTO Validation Tests")
    class ProviderParameterDTOTests {

        @Test
        @DisplayName("Should pass validation when isSecret=true with credentialVaultId and null parameterValue")
        void testValidSecretParameter() {
            // Given
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

            // When
            Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);

            // Then
            assertTrue(violations.isEmpty(), 
                "Valid secret parameter should not have validation errors");
        }

        @Test
        @DisplayName("Should pass validation when isSecret=false with parameterValue and null credentialVaultId")
        void testValidNonSecretParameter() {
            // Given
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

            // When
            Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);

            // Then
            assertTrue(violations.isEmpty(), 
                "Valid non-secret parameter should not have validation errors");
        }

        @Test
        @DisplayName("Should fail validation when isSecret=true but credentialVaultId is null")
        void testInvalidSecretParameterMissingVaultId() {
            // Given
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

            // When
            Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);

            // Then
            assertFalse(violations.isEmpty(), 
                "Secret parameter without credentialVaultId should have validation errors");
            assertEquals(1, violations.size());
            assertTrue(violations.iterator().next().getMessage().contains("credentialVaultId is required"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        @DisplayName("Should fail validation when isSecret=true but credentialVaultId is blank")
        void testInvalidSecretParameterBlankVaultId(String blankValue) {
            // Given
            ProviderParameterDTO parameter = ProviderParameterDTO.builder()
                .id(UUID.randomUUID())
                .providerId(UUID.randomUUID())
                .tenantId(UUID.randomUUID())
                .parameterName("api_key")
                .isSecret(true)
                .credentialVaultId(blankValue)
                .parameterValue(null)
                .parameterType("STRING")
                .build();

            // When
            Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);

            // Then
            assertFalse(violations.isEmpty(), 
                "Secret parameter with blank credentialVaultId should have validation errors");
        }

        @Test
        @DisplayName("Should fail validation when isSecret=true but parameterValue is not null")
        void testInvalidSecretParameterWithValue() {
            // Given
            ProviderParameterDTO parameter = ProviderParameterDTO.builder()
                .id(UUID.randomUUID())
                .providerId(UUID.randomUUID())
                .tenantId(UUID.randomUUID())
                .parameterName("api_key")
                .isSecret(true)
                .credentialVaultId("550e8400-e29b-41d4-a716-446655440000")
                .parameterValue("sk_live_secret_key_12345")
                .parameterType("STRING")
                .build();

            // When
            Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);

            // Then
            assertFalse(violations.isEmpty(), 
                "Secret parameter with both credentialVaultId and parameterValue should have validation errors");
            assertEquals(1, violations.size());
            assertTrue(violations.iterator().next().getMessage().contains("parameterValue must be null"));
        }

        @Test
        @DisplayName("Should fail validation when isSecret=false but parameterValue is null")
        void testInvalidNonSecretParameterMissingValue() {
            // Given
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

            // When
            Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);

            // Then
            assertFalse(violations.isEmpty(), 
                "Non-secret parameter without parameterValue should have validation errors");
            assertEquals(1, violations.size());
            assertTrue(violations.iterator().next().getMessage().contains("parameterValue is required"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        @DisplayName("Should fail validation when isSecret=false but parameterValue is blank")
        void testInvalidNonSecretParameterBlankValue(String blankValue) {
            // Given
            ProviderParameterDTO parameter = ProviderParameterDTO.builder()
                .id(UUID.randomUUID())
                .providerId(UUID.randomUUID())
                .tenantId(UUID.randomUUID())
                .parameterName("api_timeout")
                .isSecret(false)
                .credentialVaultId(null)
                .parameterValue(blankValue)
                .parameterType("INTEGER")
                .build();

            // When
            Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);

            // Then
            assertFalse(violations.isEmpty(), 
                "Non-secret parameter with blank parameterValue should have validation errors");
        }

        @Test
        @DisplayName("Should fail validation when isSecret=false but credentialVaultId is not null")
        void testInvalidNonSecretParameterWithVaultId() {
            // Given
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

            // When
            Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);

            // Then
            assertFalse(violations.isEmpty(), 
                "Non-secret parameter with credentialVaultId should have validation errors");
            assertEquals(1, violations.size());
            assertTrue(violations.iterator().next().getMessage().contains("credentialVaultId must be null"));
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("Should pass validation when isSecret is null (treated as false)")
        void testNullIsSecretTreatedAsFalse(Boolean isSecret) {
            // Given
            ProviderParameterDTO parameter = ProviderParameterDTO.builder()
                .id(UUID.randomUUID())
                .providerId(UUID.randomUUID())
                .tenantId(UUID.randomUUID())
                .parameterName("api_timeout")
                .isSecret(isSecret)
                .credentialVaultId(null)
                .parameterValue("30000")
                .parameterType("INTEGER")
                .build();

            // When
            Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);

            // Then
            assertTrue(violations.isEmpty(), 
                "Parameter with null isSecret and parameterValue should be valid");
        }
    }

    @Nested
    @DisplayName("EnvironmentConfigDTO Validation Tests")
    class EnvironmentConfigDTOTests {

        @Test
        @DisplayName("Should pass validation when isSecret=true with credentialVaultId and null configValue")
        void testValidSecretConfig() {
            // Given
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

            // When
            Set<ConstraintViolation<EnvironmentConfigDTO>> violations = validator.validate(config);

            // Then
            assertTrue(violations.isEmpty(),
                "Valid secret config should not have validation errors");
        }

        @Test
        @DisplayName("Should pass validation when isSecret=false with configValue and null credentialVaultId")
        void testValidNonSecretConfig() {
            // Given
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

            // When
            Set<ConstraintViolation<EnvironmentConfigDTO>> violations = validator.validate(config);

            // Then
            assertTrue(violations.isEmpty(),
                "Valid non-secret config should not have validation errors");
        }

        @Test
        @DisplayName("Should fail validation when isSecret=true but credentialVaultId is null")
        void testInvalidSecretConfigMissingVaultId() {
            // Given
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

            // When
            Set<ConstraintViolation<EnvironmentConfigDTO>> violations = validator.validate(config);

            // Then
            assertFalse(violations.isEmpty(),
                "Secret config without credentialVaultId should have validation errors");
            assertEquals(1, violations.size());
            assertTrue(violations.iterator().next().getMessage().contains("credentialVaultId is required"));
        }

        @Test
        @DisplayName("Should fail validation when isSecret=true but configValue is not null")
        void testInvalidSecretConfigWithValue() {
            // Given
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

            // When
            Set<ConstraintViolation<EnvironmentConfigDTO>> violations = validator.validate(config);

            // Then
            assertFalse(violations.isEmpty(),
                "Secret config with both credentialVaultId and configValue should have validation errors");
            assertEquals(1, violations.size());
            assertTrue(violations.iterator().next().getMessage().contains("configValue must be null"));
        }

        @Test
        @DisplayName("Should fail validation when isSecret=false but configValue is null")
        void testInvalidNonSecretConfigMissingValue() {
            // Given
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

            // When
            Set<ConstraintViolation<EnvironmentConfigDTO>> violations = validator.validate(config);

            // Then
            assertFalse(violations.isEmpty(),
                "Non-secret config without configValue should have validation errors");
            assertEquals(1, violations.size());
            assertTrue(violations.iterator().next().getMessage().contains("configValue is required"));
        }

        @Test
        @DisplayName("Should fail validation when isSecret=false but credentialVaultId is not null")
        void testInvalidNonSecretConfigWithVaultId() {
            // Given
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

            // When
            Set<ConstraintViolation<EnvironmentConfigDTO>> violations = validator.validate(config);

            // Then
            assertFalse(violations.isEmpty(),
                "Non-secret config with credentialVaultId should have validation errors");
            assertEquals(1, violations.size());
            assertTrue(violations.iterator().next().getMessage().contains("credentialVaultId must be null"));
        }

        @Test
        @DisplayName("Should pass validation for encryption key stored in vault")
        void testValidEncryptionKeyInVault() {
            // Given
            EnvironmentConfigDTO config = EnvironmentConfigDTO.builder()
                .id(UUID.randomUUID())
                .tenantId(UUID.randomUUID())
                .environmentName("production")
                .configKey("encryption.master.key")
                .isSecret(true)
                .credentialVaultId("770e8400-e29b-41d4-a716-446655440002")
                .configValue(null)
                .configType("STRING")
                .category("security")
                .build();

            // When
            Set<ConstraintViolation<EnvironmentConfigDTO>> violations = validator.validate(config);

            // Then
            assertTrue(violations.isEmpty(),
                "Valid encryption key config should not have validation errors");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Special Scenarios")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle UUID format in credentialVaultId")
        void testUuidFormatCredentialVaultId() {
            // Given
            String validUuid = UUID.randomUUID().toString();
            ProviderParameterDTO parameter = ProviderParameterDTO.builder()
                .id(UUID.randomUUID())
                .providerId(UUID.randomUUID())
                .tenantId(UUID.randomUUID())
                .parameterName("api_key")
                .isSecret(true)
                .credentialVaultId(validUuid)
                .parameterValue(null)
                .parameterType("STRING")
                .build();

            // When
            Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);

            // Then
            assertTrue(violations.isEmpty(),
                "Valid UUID format in credentialVaultId should pass validation");
        }

        @Test
        @DisplayName("Should validate multiple DTOs independently")
        void testMultipleDTOsValidation() {
            // Given
            ProviderParameterDTO validParam = ProviderParameterDTO.builder()
                .id(UUID.randomUUID())
                .providerId(UUID.randomUUID())
                .tenantId(UUID.randomUUID())
                .parameterName("api_key")
                .isSecret(true)
                .credentialVaultId("550e8400-e29b-41d4-a716-446655440000")
                .parameterValue(null)
                .parameterType("STRING")
                .build();

            ProviderParameterDTO invalidParam = ProviderParameterDTO.builder()
                .id(UUID.randomUUID())
                .providerId(UUID.randomUUID())
                .tenantId(UUID.randomUUID())
                .parameterName("api_key_2")
                .isSecret(true)
                .credentialVaultId(null)
                .parameterValue(null)
                .parameterType("STRING")
                .build();

            // When
            Set<ConstraintViolation<ProviderParameterDTO>> validViolations = validator.validate(validParam);
            Set<ConstraintViolation<ProviderParameterDTO>> invalidViolations = validator.validate(invalidParam);

            // Then
            assertTrue(validViolations.isEmpty(), "Valid parameter should pass");
            assertFalse(invalidViolations.isEmpty(), "Invalid parameter should fail");
        }

        @Test
        @DisplayName("Should handle very long credentialVaultId values")
        void testLongCredentialVaultId() {
            // Given
            String longVaultId = "a".repeat(255); // Max VARCHAR(255)
            ProviderParameterDTO parameter = ProviderParameterDTO.builder()
                .id(UUID.randomUUID())
                .providerId(UUID.randomUUID())
                .tenantId(UUID.randomUUID())
                .parameterName("api_key")
                .isSecret(true)
                .credentialVaultId(longVaultId)
                .parameterValue(null)
                .parameterType("STRING")
                .build();

            // When
            Set<ConstraintViolation<ProviderParameterDTO>> violations = validator.validate(parameter);

            // Then
            // Should pass @ValidSecretConfiguration but may fail @Size if present
            // This test verifies the validator doesn't crash on long values
            assertNotNull(violations);
        }
    }
}

