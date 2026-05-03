-- =============================================================================
-- V4: API-to-Process Mappings for Plugin Architecture
-- =============================================================================
-- This migration adds support for the Firefly Plugin Architecture, enabling
-- dynamic resolution of which process plugin handles each API operation.
--
-- Copyright 2025 Firefly Software Foundation
-- Licensed under the Apache License, Version 2.0
-- =============================================================================

-- =============================================================================
-- TABLE: api_process_mappings
-- =============================================================================
-- Maps API operations to process plugins based on tenant, product, and channel.
-- Supports the BaaS (Banking as a Service) plugin architecture where API
-- endpoints are "containers" and business logic is pluggable.

CREATE TABLE api_process_mappings (
    -- Primary key
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Tenant association (nullable for default/vanilla mappings)
    tenant_id UUID REFERENCES tenants(id) ON DELETE CASCADE,
    
    -- Product association (nullable for all-product mappings)
    product_id UUID,  -- References product table in another microservice
    
    -- Channel type (e.g., MOBILE, WEB, API, BRANCH) - nullable for all channels
    channel_type VARCHAR(50),
    
    -- API path pattern (e.g., "/api/v1/accounts")
    api_path VARCHAR(255),
    
    -- HTTP method (GET, POST, PUT, DELETE, PATCH)
    http_method VARCHAR(10),
    
    -- Operation ID within the API (e.g., "createAccount", "getAccountBalance")
    operation_id VARCHAR(100) NOT NULL,
    
    -- Process plugin to execute
    process_id VARCHAR(100) NOT NULL,
    
    -- Optional version constraint (null = use latest)
    process_version VARCHAR(20),
    
    -- Priority for ordering when multiple mappings match (lower = higher priority)
    priority INTEGER NOT NULL DEFAULT 0,
    
    -- Whether this mapping is active
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    -- Effective dates for time-limited mappings
    effective_from TIMESTAMP WITH TIME ZONE,
    effective_to TIMESTAMP WITH TIME ZONE,
    
    -- Additional configuration parameters (JSON)
    parameters JSONB,
    
    -- Description for documentation
    description TEXT,
    
    -- Metadata for audit trail
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    
    -- Version for optimistic locking
    version BIGINT NOT NULL DEFAULT 0,
    
    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- INDEXES
-- =============================================================================

-- Primary lookup: operation by tenant
CREATE INDEX idx_api_process_mappings_tenant_operation 
    ON api_process_mappings(tenant_id, operation_id) 
    WHERE is_active = TRUE;

-- Lookup including product and channel for specific resolution
CREATE INDEX idx_api_process_mappings_full_context 
    ON api_process_mappings(tenant_id, operation_id, product_id, channel_type) 
    WHERE is_active = TRUE;

-- Lookup by process_id (useful for finding all mappings to a process)
CREATE INDEX idx_api_process_mappings_process 
    ON api_process_mappings(process_id);

-- API path based lookup
CREATE INDEX idx_api_process_mappings_api_path 
    ON api_process_mappings(api_path, http_method) 
    WHERE api_path IS NOT NULL AND is_active = TRUE;

-- Active mappings with effective dates (for time-based queries)
CREATE INDEX idx_api_process_mappings_effective 
    ON api_process_mappings(is_active, effective_from, effective_to) 
    WHERE is_active = TRUE;

-- =============================================================================
-- UNIQUE CONSTRAINT
-- =============================================================================
-- Ensure unique mappings per tenant/product/channel/operation combination
-- NULL values are treated as distinct in PostgreSQL, so we use COALESCE

CREATE UNIQUE INDEX uq_api_process_mappings_context 
    ON api_process_mappings(
        COALESCE(tenant_id, '00000000-0000-0000-0000-000000000000'::UUID),
        operation_id,
        COALESCE(product_id, '00000000-0000-0000-0000-000000000000'::UUID),
        COALESCE(channel_type, '__ALL__')
    ) 
    WHERE is_active = TRUE;

-- =============================================================================
-- COMMENTS
-- =============================================================================

COMMENT ON TABLE api_process_mappings IS 
    'Maps API operations to process plugins for the BaaS plugin architecture';

COMMENT ON COLUMN api_process_mappings.tenant_id IS 
    'Tenant ID - NULL means this is a default/vanilla mapping';

COMMENT ON COLUMN api_process_mappings.product_id IS 
    'Product ID - NULL means mapping applies to all products';

COMMENT ON COLUMN api_process_mappings.channel_type IS 
    'Channel type (MOBILE, WEB, API, BRANCH) - NULL means all channels';

COMMENT ON COLUMN api_process_mappings.operation_id IS 
    'The API operation identifier (e.g., createAccount, getBalance)';

COMMENT ON COLUMN api_process_mappings.process_id IS 
    'The process plugin ID to execute for this operation';

COMMENT ON COLUMN api_process_mappings.process_version IS 
    'Optional version constraint - NULL uses the latest version';

COMMENT ON COLUMN api_process_mappings.priority IS 
    'Lower values have higher priority when multiple mappings match';

COMMENT ON COLUMN api_process_mappings.parameters IS 
    'JSON object with additional configuration for the process';

-- =============================================================================
-- SEED DATA: Default vanilla mappings for common operations
-- =============================================================================
-- These are fallback mappings when no tenant-specific mapping exists

INSERT INTO api_process_mappings (
    operation_id, 
    process_id, 
    description, 
    created_by
) VALUES 
    ('createAccount', 'vanilla-account-creation', 
     'Default account creation process', 'system'),
    ('getAccount', 'vanilla-account-retrieval', 
     'Default account retrieval process', 'system'),
    ('listAccounts', 'vanilla-account-list', 
     'Default account listing process', 'system'),
    ('initiateTransfer', 'vanilla-transfer-initiation', 
     'Default transfer initiation process', 'system'),
    ('getTransfer', 'vanilla-transfer-retrieval', 
     'Default transfer retrieval process', 'system'),
    ('initiatePayment', 'vanilla-payment-initiation', 
     'Default payment initiation process', 'system'),
    ('getPaymentStatus', 'vanilla-payment-status', 
     'Default payment status process', 'system');
