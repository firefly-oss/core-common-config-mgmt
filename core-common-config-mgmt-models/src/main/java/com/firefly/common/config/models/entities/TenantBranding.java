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
 * Entity representing branding configuration for a Tenant.
 * Separates visual/branding concerns from core tenant configuration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("tenant_brandings")
public class TenantBranding {

    @Id
    private UUID id;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("logo_url")
    private String logoUrl;

    @Column("logo_dark_url")
    private String logoDarkUrl;

    @Column("favicon_url")
    private String faviconUrl;

    @Column("primary_color")
    private String primaryColor;

    @Column("secondary_color")
    private String secondaryColor;

    @Column("accent_color")
    private String accentColor;

    @Column("background_color")
    private String backgroundColor;

    @Column("text_color")
    private String textColor;

    @Column("font_family")
    private String fontFamily;

    @Column("custom_css")
    private String customCss;

    @Column("email_header_url")
    private String emailHeaderUrl;

    @Column("email_footer_text")
    private String emailFooterText;

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

