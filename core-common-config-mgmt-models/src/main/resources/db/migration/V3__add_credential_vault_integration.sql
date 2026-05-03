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

-- =====================================================
-- V3: Add Credential Vault Integration
-- =====================================================
-- This migration adds support for integrating with the
-- core-common-security-vault microservice.
-- Instead of storing credentials directly, we now store
-- a reference ID to credentials managed by the vault.
-- =====================================================

-- =====================================================
-- ADD CREDENTIAL_VAULT_ID TO PROVIDER_PARAMETERS
-- =====================================================
-- Add column to store reference to credentials in security-vault
-- When is_secret = true, use credential_vault_id instead of parameter_value
ALTER TABLE provider_parameters 
ADD COLUMN IF NOT EXISTS credential_vault_id VARCHAR(255);

-- Add index for faster lookups by vault ID
CREATE INDEX IF NOT EXISTS idx_provider_parameters_vault_id 
ON provider_parameters(credential_vault_id) 
WHERE credential_vault_id IS NOT NULL;

-- Add comment to clarify usage
COMMENT ON COLUMN provider_parameters.credential_vault_id IS 
'Reference ID to credential stored in core-common-security-vault. Use this field when is_secret=true instead of parameter_value.';

COMMENT ON COLUMN provider_parameters.parameter_value IS 
'Direct value for non-secret parameters. For secret parameters (is_secret=true), use credential_vault_id instead.';

-- =====================================================
-- ADD CREDENTIAL_VAULT_ID TO ENVIRONMENT_CONFIGS
-- =====================================================
-- Add column to store reference to credentials in security-vault
-- When is_secret = true, use credential_vault_id instead of config_value
ALTER TABLE environment_configs 
ADD COLUMN IF NOT EXISTS credential_vault_id VARCHAR(255);

-- Add index for faster lookups by vault ID
CREATE INDEX IF NOT EXISTS idx_environment_configs_vault_id 
ON environment_configs(credential_vault_id) 
WHERE credential_vault_id IS NOT NULL;

-- Add comment to clarify usage
COMMENT ON COLUMN environment_configs.credential_vault_id IS 
'Reference ID to credential stored in core-common-security-vault. Use this field when is_secret=true instead of config_value.';

COMMENT ON COLUMN environment_configs.config_value IS 
'Direct value for non-secret configurations. For secret configurations (is_secret=true), use credential_vault_id instead.';

-- =====================================================
-- MIGRATION NOTES
-- =====================================================
-- 1. Existing data is preserved - no data is deleted
-- 2. For existing secret parameters/configs, you should:
--    a) Register the credential in security-vault
--    b) Update the record with the vault credential ID
--    c) Clear the parameter_value/config_value field
-- 3. New secret parameters should only use credential_vault_id
-- 4. Non-secret parameters continue using parameter_value/config_value
-- =====================================================

