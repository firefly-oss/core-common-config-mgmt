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

package com.firefly.common.config.core.services.impl;

import com.firefly.common.config.core.mappers.ApiProcessMappingMapper;
import com.firefly.common.config.interfaces.dtos.ApiProcessMappingDTO;
import com.firefly.common.config.models.entities.ApiProcessMapping;
import com.firefly.common.config.models.repositories.ApiProcessMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApiProcessMappingServiceImpl Tests")
class ApiProcessMappingServiceImplTest {
    
    @Mock
    private ApiProcessMappingRepository repository;
    
    @Mock
    private ApiProcessMappingMapper mapper;
    
    private ApiProcessMappingServiceImpl service;
    
    private UUID testId;
    private UUID testTenantId;
    private UUID testProductId;
    
    @BeforeEach
    void setUp() {
        service = new ApiProcessMappingServiceImpl(repository, mapper);
        testId = UUID.randomUUID();
        testTenantId = UUID.randomUUID();
        testProductId = UUID.randomUUID();
    }
    
    private ApiProcessMapping createEntity() {
        ApiProcessMapping entity = new ApiProcessMapping();
        entity.setId(testId);
        entity.setTenantId(testTenantId);
        entity.setOperationId("createAccount");
        entity.setProcessId("vanilla-account-creation");
        entity.setProcessVersion("1.0.0");
        entity.setIsActive(true);
        entity.setPriority(0);
        return entity;
    }
    
    private ApiProcessMappingDTO createDTO() {
        return ApiProcessMappingDTO.builder()
                .id(testId)
                .tenantId(testTenantId)
                .operationId("createAccount")
                .processId("vanilla-account-creation")
                .processVersion("1.0.0")
                .isActive(true)
                .priority(0)
                .build();
    }
    
    @Nested
    @DisplayName("GetById Tests")
    class GetByIdTests {
        
        @Test
        @DisplayName("Should return mapping when found")
        void shouldReturnMappingWhenFound() {
            ApiProcessMapping entity = createEntity();
            ApiProcessMappingDTO dto = createDTO();
            
            when(repository.findById(testId)).thenReturn(Mono.just(entity));
            when(mapper.toDTO(entity)).thenReturn(dto);
            
            StepVerifier.create(service.getById(testId))
                    .assertNext(result -> {
                        assertEquals(testId, result.getId());
                        assertEquals("createAccount", result.getOperationId());
                        assertEquals("vanilla-account-creation", result.getProcessId());
                    })
                    .verifyComplete();
        }
        
        @Test
        @DisplayName("Should error when mapping not found")
        void shouldErrorWhenNotFound() {
            when(repository.findById(testId)).thenReturn(Mono.empty());
            
            StepVerifier.create(service.getById(testId))
                    .expectErrorMatches(error -> 
                            error instanceof RuntimeException &&
                            error.getMessage().contains("not found"))
                    .verify();
        }
    }
    
    @Nested
    @DisplayName("Create Tests")
    class CreateTests {
        
        @Test
        @DisplayName("Should create mapping with defaults")
        void shouldCreateMappingWithDefaults() {
            ApiProcessMappingDTO inputDto = ApiProcessMappingDTO.builder()
                    .operationId("createAccount")
                    .processId("vanilla-account-creation")
                    .build();
            
            ApiProcessMapping savedEntity = createEntity();
            ApiProcessMappingDTO resultDto = createDTO();
            
            when(mapper.toEntity(any(ApiProcessMappingDTO.class))).thenReturn(savedEntity);
            when(repository.save(any(ApiProcessMapping.class))).thenReturn(Mono.just(savedEntity));
            when(mapper.toDTO(savedEntity)).thenReturn(resultDto);
            
            StepVerifier.create(service.create(inputDto))
                    .assertNext(result -> {
                        assertNotNull(result.getId());
                        assertEquals("createAccount", result.getOperationId());
                    })
                    .verifyComplete();
        }
        
        @Test
        @DisplayName("Should set isActive to true by default")
        void shouldSetDefaultIsActive() {
            ApiProcessMappingDTO inputDto = ApiProcessMappingDTO.builder()
                    .operationId("createAccount")
                    .processId("vanilla-account-creation")
                    .build();
            
            ApiProcessMapping savedEntity = createEntity();
            ApiProcessMappingDTO resultDto = createDTO();
            
            when(mapper.toEntity(any(ApiProcessMappingDTO.class))).thenReturn(savedEntity);
            when(repository.save(any(ApiProcessMapping.class))).thenReturn(Mono.just(savedEntity));
            when(mapper.toDTO(savedEntity)).thenReturn(resultDto);
            
            StepVerifier.create(service.create(inputDto))
                    .assertNext(result -> assertTrue(result.getIsActive()))
                    .verifyComplete();
        }
    }
    
    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {
        
        @Test
        @DisplayName("Should update existing mapping")
        void shouldUpdateExistingMapping() {
            ApiProcessMapping existingEntity = createEntity();
            ApiProcessMappingDTO updateDto = createDTO();
            updateDto.setProcessId("updated-process");
            
            ApiProcessMapping updatedEntity = createEntity();
            updatedEntity.setProcessId("updated-process");
            
            ApiProcessMappingDTO resultDto = createDTO();
            resultDto.setProcessId("updated-process");
            
            when(repository.findById(testId)).thenReturn(Mono.just(existingEntity));
            when(repository.save(any(ApiProcessMapping.class))).thenReturn(Mono.just(updatedEntity));
            when(mapper.toDTO(updatedEntity)).thenReturn(resultDto);
            
            StepVerifier.create(service.update(testId, updateDto))
                    .assertNext(result -> assertEquals("updated-process", result.getProcessId()))
                    .verifyComplete();
        }
        
        @Test
        @DisplayName("Should error when updating non-existent mapping")
        void shouldErrorWhenUpdatingNonExistent() {
            when(repository.findById(testId)).thenReturn(Mono.empty());
            
            StepVerifier.create(service.update(testId, createDTO()))
                    .expectErrorMatches(error -> 
                            error instanceof RuntimeException &&
                            error.getMessage().contains("not found"))
                    .verify();
        }
    }
    
    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {
        
        @Test
        @DisplayName("Should delete existing mapping")
        void shouldDeleteExistingMapping() {
            ApiProcessMapping entity = createEntity();
            
            when(repository.findById(testId)).thenReturn(Mono.just(entity));
            when(repository.delete(entity)).thenReturn(Mono.empty());
            
            StepVerifier.create(service.delete(testId))
                    .verifyComplete();
            
            verify(repository).delete(entity);
        }
        
        @Test
        @DisplayName("Should error when deleting non-existent mapping")
        void shouldErrorWhenDeletingNonExistent() {
            when(repository.findById(testId)).thenReturn(Mono.empty());
            
            StepVerifier.create(service.delete(testId))
                    .expectErrorMatches(error -> 
                            error instanceof RuntimeException &&
                            error.getMessage().contains("not found"))
                    .verify();
        }
    }
    
    @Nested
    @DisplayName("ResolveMapping Tests")
    class ResolveMappingTests {
        
        @Test
        @DisplayName("Should resolve tenant-specific mapping")
        void shouldResolveTenantSpecificMapping() {
            ApiProcessMapping tenantMapping = createEntity();
            tenantMapping.setTenantId(testTenantId);
            
            ApiProcessMappingDTO dto = createDTO();
            
            when(repository.findBestMatch(eq(testTenantId), eq("createAccount"), any(), any()))
                    .thenReturn(Flux.just(tenantMapping));
            when(repository.findVanillaMapping(anyString()))
                    .thenReturn(Mono.empty());
            when(mapper.toDTO(tenantMapping)).thenReturn(dto);
            
            StepVerifier.create(service.resolveMapping(testTenantId, "createAccount", null, null))
                    .assertNext(result -> {
                        assertEquals("vanilla-account-creation", result.getProcessId());
                        assertEquals(testTenantId, result.getTenantId());
                    })
                    .verifyComplete();
        }
        
        @Test
        @DisplayName("Should fall back to vanilla mapping when no tenant match")
        void shouldFallBackToVanillaMapping() {
            ApiProcessMapping vanillaMapping = createEntity();
            vanillaMapping.setTenantId(null);  // Vanilla mapping
            
            ApiProcessMappingDTO dto = createDTO();
            dto.setTenantId(null);
            
            when(repository.findBestMatch(any(), eq("createAccount"), any(), any()))
                    .thenReturn(Flux.empty());
            when(repository.findVanillaMapping("createAccount"))
                    .thenReturn(Mono.just(vanillaMapping));
            when(mapper.toDTO(vanillaMapping)).thenReturn(dto);
            
            StepVerifier.create(service.resolveMapping(testTenantId, "createAccount", null, null))
                    .assertNext(result -> {
                        assertNull(result.getTenantId());
                        assertEquals("vanilla-account-creation", result.getProcessId());
                    })
                    .verifyComplete();
        }
        
        @Test
        @DisplayName("Should return empty when no mapping found")
        void shouldReturnEmptyWhenNoMappingFound() {
            when(repository.findBestMatch(any(), eq("unknownOp"), any(), any()))
                    .thenReturn(Flux.empty());
            when(repository.findVanillaMapping("unknownOp"))
                    .thenReturn(Mono.empty());
            
            StepVerifier.create(service.resolveMapping(testTenantId, "unknownOp", null, null))
                    .verifyComplete();
        }
        
        @Test
        @DisplayName("Should resolve with product and channel filters")
        void shouldResolveWithProductAndChannelFilters() {
            ApiProcessMapping mapping = createEntity();
            mapping.setProductId(testProductId);
            mapping.setChannelType("MOBILE");
            
            ApiProcessMappingDTO dto = createDTO();
            dto.setProductId(testProductId);
            dto.setChannelType("MOBILE");
            
            when(repository.findBestMatch(testTenantId, "createAccount", testProductId, "MOBILE"))
                    .thenReturn(Flux.just(mapping));
            when(repository.findVanillaMapping(anyString()))
                    .thenReturn(Mono.empty());
            when(mapper.toDTO(mapping)).thenReturn(dto);
            
            StepVerifier.create(service.resolveMapping(testTenantId, "createAccount", testProductId, "MOBILE"))
                    .assertNext(result -> {
                        assertEquals(testProductId, result.getProductId());
                        assertEquals("MOBILE", result.getChannelType());
                    })
                    .verifyComplete();
        }
    }
    
    @Nested
    @DisplayName("GetByTenantId Tests")
    class GetByTenantIdTests {
        
        @Test
        @DisplayName("Should return mappings for tenant")
        void shouldReturnMappingsForTenant() {
            ApiProcessMapping mapping1 = createEntity();
            ApiProcessMapping mapping2 = createEntity();
            mapping2.setOperationId("closeAccount");
            
            ApiProcessMappingDTO dto1 = createDTO();
            ApiProcessMappingDTO dto2 = createDTO();
            dto2.setOperationId("closeAccount");
            
            when(repository.findByTenantId(testTenantId))
                    .thenReturn(Flux.just(mapping1, mapping2));
            when(mapper.toDTO(mapping1)).thenReturn(dto1);
            when(mapper.toDTO(mapping2)).thenReturn(dto2);
            
            StepVerifier.create(service.getByTenantId(testTenantId))
                    .expectNextCount(2)
                    .verifyComplete();
        }
    }
    
    @Nested
    @DisplayName("GetVanillaMappings Tests")
    class GetVanillaMappingsTests {
        
        @Test
        @DisplayName("Should return vanilla mappings")
        void shouldReturnVanillaMappings() {
            ApiProcessMapping vanillaMapping = createEntity();
            vanillaMapping.setTenantId(null);
            
            ApiProcessMappingDTO dto = createDTO();
            dto.setTenantId(null);
            
            when(repository.findByTenantIdIsNullAndIsActiveTrue())
                    .thenReturn(Flux.just(vanillaMapping));
            when(mapper.toDTO(vanillaMapping)).thenReturn(dto);
            
            StepVerifier.create(service.getVanillaMappings())
                    .assertNext(result -> assertNull(result.getTenantId()))
                    .verifyComplete();
        }
    }
    
    @Nested
    @DisplayName("GetByProcessId Tests")
    class GetByProcessIdTests {
        
        @Test
        @DisplayName("Should return mappings by process ID")
        void shouldReturnMappingsByProcessId() {
            ApiProcessMapping mapping = createEntity();
            ApiProcessMappingDTO dto = createDTO();
            
            when(repository.findByProcessId("vanilla-account-creation"))
                    .thenReturn(Flux.just(mapping));
            when(mapper.toDTO(mapping)).thenReturn(dto);
            
            StepVerifier.create(service.getByProcessId("vanilla-account-creation"))
                    .assertNext(result -> assertEquals("vanilla-account-creation", result.getProcessId()))
                    .verifyComplete();
        }
    }
    
    @Nested
    @DisplayName("InvalidateCache Tests")
    class InvalidateCacheTests {
        
        @Test
        @DisplayName("Should complete successfully for tenant invalidation")
        void shouldCompleteForTenantInvalidation() {
            StepVerifier.create(service.invalidateCache(testTenantId))
                    .verifyComplete();
        }
        
        @Test
        @DisplayName("Should complete successfully for global invalidation")
        void shouldCompleteForGlobalInvalidation() {
            StepVerifier.create(service.invalidateCache(null))
                    .verifyComplete();
        }
    }
}
