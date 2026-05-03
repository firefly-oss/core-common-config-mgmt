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

package com.firefly.common.config.models.repositories;

import com.firefly.common.config.models.entities.ApiProcessMapping;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for API-to-Process mappings.
 * 
 * <p>Provides reactive data access for process mappings that determine
 * which plugin handles each API operation.</p>
 * 
 * @author Firefly Development Team
 * @since 1.0.0
 */
@Repository
public interface ApiProcessMappingRepository extends BaseRepository<ApiProcessMapping, UUID> {

    /**
     * Finds all active mappings.
     * 
     * @return Flux of active mappings
     */
    Flux<ApiProcessMapping> findByIsActiveTrue();

    /**
     * Finds all mappings for a tenant.
     * 
     * @param tenantId the tenant ID
     * @return Flux of mappings for the tenant
     */
    Flux<ApiProcessMapping> findByTenantId(UUID tenantId);

    /**
     * Finds active mappings for a tenant.
     * 
     * @param tenantId the tenant ID
     * @return Flux of active mappings
     */
    Flux<ApiProcessMapping> findByTenantIdAndIsActiveTrue(UUID tenantId);

    /**
     * Finds all vanilla (default) mappings.
     * 
     * @return Flux of vanilla mappings
     */
    Flux<ApiProcessMapping> findByTenantIdIsNullAndIsActiveTrue();

    /**
     * Finds mappings by operation ID.
     * 
     * @param operationId the operation ID
     * @return Flux of mappings
     */
    Flux<ApiProcessMapping> findByOperationIdAndIsActiveTrue(String operationId);

    /**
     * Finds mappings by process ID.
     * 
     * @param processId the process ID
     * @return Flux of mappings pointing to this process
     */
    Flux<ApiProcessMapping> findByProcessId(String processId);

    /**
     * Finds the best matching mapping for an operation with full context.
     * Returns mappings ordered by specificity (most specific first).
     * 
     * @param tenantId the tenant ID (can be null for vanilla lookup)
     * @param operationId the operation ID
     * @param productId the product ID (can be null)
     * @param channelType the channel type (can be null)
     * @return Flux of matching mappings, most specific first
     */
    @Query("""
        SELECT * FROM api_process_mappings
        WHERE is_active = TRUE
          AND operation_id = :operationId
          AND (tenant_id = :tenantId OR tenant_id IS NULL)
          AND (product_id = :productId OR product_id IS NULL)
          AND (channel_type = :channelType OR channel_type IS NULL)
          AND (effective_from IS NULL OR effective_from <= CURRENT_TIMESTAMP)
          AND (effective_to IS NULL OR effective_to > CURRENT_TIMESTAMP)
        ORDER BY 
          CASE WHEN tenant_id = :tenantId THEN 0 ELSE 1 END,
          CASE WHEN product_id = :productId THEN 0 ELSE 1 END,
          CASE WHEN channel_type = :channelType THEN 0 ELSE 1 END,
          priority ASC
        """)
    Flux<ApiProcessMapping> findBestMatch(
            UUID tenantId, 
            String operationId, 
            UUID productId, 
            String channelType);

    /**
     * Finds the best matching mapping without product/channel context.
     * 
     * @param tenantId the tenant ID
     * @param operationId the operation ID
     * @return Flux of matching mappings
     */
    @Query("""
        SELECT * FROM api_process_mappings
        WHERE is_active = TRUE
          AND operation_id = :operationId
          AND (tenant_id = :tenantId OR tenant_id IS NULL)
          AND product_id IS NULL
          AND channel_type IS NULL
          AND (effective_from IS NULL OR effective_from <= CURRENT_TIMESTAMP)
          AND (effective_to IS NULL OR effective_to > CURRENT_TIMESTAMP)
        ORDER BY 
          CASE WHEN tenant_id = :tenantId THEN 0 ELSE 1 END,
          priority ASC
        """)
    Flux<ApiProcessMapping> findBestMatchSimple(UUID tenantId, String operationId);

    /**
     * Finds the vanilla (default) mapping for an operation.
     * 
     * @param operationId the operation ID
     * @return Mono of the vanilla mapping
     */
    @Query("""
        SELECT * FROM api_process_mappings
        WHERE is_active = TRUE
          AND operation_id = :operationId
          AND tenant_id IS NULL
          AND (effective_from IS NULL OR effective_from <= CURRENT_TIMESTAMP)
          AND (effective_to IS NULL OR effective_to > CURRENT_TIMESTAMP)
        ORDER BY priority ASC
        LIMIT 1
        """)
    Mono<ApiProcessMapping> findVanillaMapping(String operationId);

    /**
     * Checks if a mapping exists for the given context.
     * 
     * @param tenantId the tenant ID
     * @param operationId the operation ID
     * @param productId the product ID
     * @param channelType the channel type
     * @return Mono<Boolean> true if mapping exists
     */
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM api_process_mappings
            WHERE is_active = TRUE
              AND operation_id = :operationId
              AND tenant_id = :tenantId
              AND (product_id = :productId OR (:productId IS NULL AND product_id IS NULL))
              AND (channel_type = :channelType OR (:channelType IS NULL AND channel_type IS NULL))
        )
        """)
    Mono<Boolean> existsByContext(
            UUID tenantId, 
            String operationId, 
            UUID productId, 
            String channelType);

    /**
     * Counts active mappings by process ID.
     * 
     * @param processId the process ID
     * @return count of mappings
     */
    Mono<Long> countByProcessIdAndIsActiveTrue(String processId);

    /**
     * Finds mappings by API path and method.
     * 
     * @param apiPath the API path
     * @param httpMethod the HTTP method
     * @return Flux of mappings
     */
    Flux<ApiProcessMapping> findByApiPathAndHttpMethodAndIsActiveTrue(
            String apiPath, 
            String httpMethod);
}
