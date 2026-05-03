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

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.fireflyframework.utils.annotations.FilterableId;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Tenant Branding entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tenant branding and visual configuration")
public class TenantBrandingDTO {
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Tenant branding ID")
    private UUID id;
    
    @NotNull(message = "Tenant ID is required")
    @FilterableId
    @Schema(description = "ID of the tenant")
    private UUID tenantId;
    
    @Schema(description = "URL of the logo", example = "https://example.com/logo.png")
    private String logoUrl;
    
    @Schema(description = "URL of the dark mode logo", example = "https://example.com/logo-dark.png")
    private String logoDarkUrl;
    
    @Schema(description = "URL of the favicon", example = "https://example.com/favicon.ico")
    private String faviconUrl;
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Primary color must be a valid hex color")
    @Schema(description = "Primary brand color in hex format", example = "#FF5733")
    private String primaryColor;
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Secondary color must be a valid hex color")
    @Schema(description = "Secondary brand color in hex format", example = "#33FF57")
    private String secondaryColor;
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Accent color must be a valid hex color")
    @Schema(description = "Accent color in hex format", example = "#3357FF")
    private String accentColor;
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Background color must be a valid hex color")
    @Schema(description = "Background color in hex format", example = "#FFFFFF")
    private String backgroundColor;
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Text color must be a valid hex color")
    @Schema(description = "Text color in hex format", example = "#000000")
    private String textColor;
    
    @Size(max = 100, message = "Font family must not exceed 100 characters")
    @Schema(description = "Font family for the tenant", example = "Roboto, sans-serif")
    private String fontFamily;
    
    @Schema(description = "Custom CSS for advanced styling")
    private String customCss;
    
    @Schema(description = "URL of the email header image", example = "https://example.com/email-header.png")
    private String emailHeaderUrl;
    
    @Schema(description = "Footer text for emails", example = "© 2025 Company Name. All rights reserved.")
    private String emailFooterText;
    
    @Schema(description = "Additional metadata in JSON format")
    private String metadata;
    
    @Schema(description = "Whether the branding is active", defaultValue = "true")
    private Boolean active;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Version for optimistic locking")
    private Long version;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}

