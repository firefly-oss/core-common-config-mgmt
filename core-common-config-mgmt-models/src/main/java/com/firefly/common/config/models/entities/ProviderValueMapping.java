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
 * Entity representing value mappings between Firefly internal values and provider-specific values.
 * For example: Firefly status "APPROVED" might map to provider status "SUCCESS" or "ACCEPTED"
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("provider_value_mappings")
public class ProviderValueMapping {

    @Id
    private UUID id;

    @Column("provider_id")
    private UUID providerId;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("mapping_type")
    private String mappingType;

    @Column("firefly_value")
    private String fireflyValue;

    @Column("provider_value")
    private String providerValue;

    @Column("direction")
    private String direction;

    @Column("description")
    private String description;

    @Column("priority")
    private Integer priority;

    @Column("metadata")
    private String metadata;

    @Column("active")
    private Boolean active;

    @Version
    @Column("version")
    private Long version;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}

