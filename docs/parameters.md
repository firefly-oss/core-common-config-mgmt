# Provider Parameter Configuration

**Complete guide to dynamic configuration management in Firefly**

---

## Table of Contents

- [Overview](#overview)
- [What are Provider Parameters?](#what-are-provider-parameters)
- [Parameter Hierarchy](#parameter-hierarchy)
- [Parameter Types](#parameter-types)
- [Parameter Categories](#parameter-categories)
- [Parameter Configuration](#parameter-configuration)
- [Security and Encryption](#security-and-encryption)
- [Validation and Constraints](#validation-and-constraints)
- [API Reference](#api-reference)
- [Best Practices](#best-practices)
- [Use Cases](#use-cases)

---

## Overview

**Provider Parameters** are dynamic, configurable values that control how Firefly interacts with external providers. They enable **flexible, environment-specific, and tenant-specific configurations** without code changes or redeployment.

### Purpose

Parameters solve critical configuration challenges:

- **Flexibility**: Change configurations without redeploying code
- **Environment Management**: Different settings for dev, staging, production
- **Tenant Customization**: Override defaults for specific tenants
- **Security**: Encrypted storage of sensitive credentials
- **Auditability**: Track all configuration changes with timestamps
- **Validation**: Type-safe parameters with validation rules
- **Versioning**: Track parameter changes over time

### Key Characteristics

Each parameter has:

- **Scope**: Global (all tenants) or tenant-specific
- **Type Safety**: Strongly typed (STRING, INTEGER, BOOLEAN, JSON, URL, EMAIL)
- **Validation**: Regex patterns, min/max values, required/optional
- **Security**: Encryption for sensitive values (API keys, secrets)
- **Categorization**: Organized by category (connection, authentication, etc.)
- **Metadata**: Additional context and documentation

---

## What are Provider Parameters?

### Conceptual Model

Think of parameters as **configuration knobs** that control provider behavior:

```
┌─────────────────────────────────────────────────────────────┐
│                    Provider: Stripe                         │
│                                                             │
│  Global Parameters (Default for all tenants)                │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ api_timeout: 30000 (milliseconds)                     │  │
│  │ retry_attempts: 3                                     │  │
│  │ environment: production                               │  │
│  │ webhook_url: https://api.firefly.com/webhooks/stripe  │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                             │
│  Tenant-Specific Parameters (Override for Tenant A)         │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ api_key: sk_live_tenant_a_key (encrypted)             │  │
│  │ api_timeout: 45000 (longer timeout for Tenant A)      │  │
│  │ webhook_url: https://tenant-a.com/webhooks/stripe     │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                             │
│  Tenant-Specific Parameters (Override for Tenant B)         │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ api_key: sk_live_tenant_b_key (encrypted)             │  │
│  │ environment: sandbox (testing for Tenant B)           │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Parameter Components

#### 1. Core Configuration

```yaml
Parameter: "api_timeout"
├── Identification
│   ├── ID: 550e8400-e29b-41d4-a716-446655440500
│   ├── Provider: Stripe (UUID)
│   ├── Tenant: null (global) or specific tenant UUID
│   └── Name: api_timeout
├── Value
│   ├── Value: "30000"
│   ├── Type: INTEGER
│   └── Default: "30000"
├── Metadata
│   ├── Description: "API request timeout in milliseconds"
│   ├── Category: connection
│   ├── Display Order: 1
│   └── Environment: production
├── Security
│   ├── Is Secret: false
│   ├── Is Required: true
│   └── Is Editable: true
└── Validation
    ├── Validation Regex: "^[0-9]+$"
    ├── Min Value: 1000
    └── Max Value: 60000
```

#### 2. Scope Levels

**Global Parameters** (tenantId = null):
- Apply to all tenants by default
- Provide sensible defaults
- Can be overridden by tenant-specific parameters

**Tenant-Specific Parameters** (tenantId = specific UUID):
- Override global parameters for a specific tenant
- Enable tenant-specific customization
- Take precedence over global parameters

---

## Parameter Hierarchy

### Resolution Order

Parameters follow a **hierarchical resolution** to determine the effective value:

```
┌─────────────────────────────────────────────────────────────┐
│  Request: Get parameter "api_timeout" for Tenant A          │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  Step 1: Check Tenant-Specific Parameter                    │
│  Query: providerId = X, tenantId = A, name = "api_timeout" │
│  Result: Found → "45000"                                    │
│  RETURN "45000"                                          │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│  Request: Get parameter "api_timeout" for Tenant B          │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  Step 1: Check Tenant-Specific Parameter                    │
│  Query: providerId = X, tenantId = B, name = "api_timeout" │
│  Result: Not Found                                          │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  Step 2: Check Global Parameter                             │
│  Query: providerId = X, tenantId = null, name = "api_timeout"│
│  Result: Found → "30000"                                    │
│  RETURN "30000"                                          │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│  Request: Get parameter "new_param" for Tenant C            │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  Step 1: Check Tenant-Specific Parameter                    │
│  Result: Not Found                                          │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  Step 2: Check Global Parameter                             │
│  Result: Not Found                                          │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  Step 3: Check Default Value                                │
│  Result: Found → "30000" (from parameter definition)        │
│  RETURN "30000"                                          │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│  Request: Get required parameter "api_key" for Tenant D     │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  Step 1: Check Tenant-Specific Parameter                    │
│  Result: Not Found                                          │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  Step 2: Check Global Parameter                             │
│  Result: Not Found                                          │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  Step 3: Check Default Value                                │
│  Result: Not Found                                          │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  Step 4: Parameter is Required                              │
│  THROW ConfigurationException                            │
│  "Required parameter 'api_key' not found for tenant D"      │
└─────────────────────────────────────────────────────────────┘
```

### Resolution Algorithm

```java
public Mono<String> getParameterValue(
    UUID providerId,
    UUID tenantId,
    String parameterName
) {
    // Step 1: Check tenant-specific parameter
    return parameterRepository
        .findByProviderIdAndTenantIdAndParameterName(
            providerId, tenantId, parameterName
        )
        .map(ProviderParameter::getParameterValue)
        // Step 2: Check global parameter
        .switchIfEmpty(
            parameterRepository
                .findByProviderIdAndTenantIdAndParameterName(
                    providerId, null, parameterName
                )
                .map(ProviderParameter::getParameterValue)
        )
        // Step 3: Check default value
        .switchIfEmpty(
            parameterRepository
                .findByProviderIdAndParameterName(providerId, parameterName)
                .map(ProviderParameter::getDefaultValue)
                .filter(Objects::nonNull)
        )
        // Step 4: Error if required and not found
        .switchIfEmpty(
            parameterRepository
                .findByProviderIdAndParameterName(providerId, parameterName)
                .flatMap(param -> {
                    if (param.getIsRequired()) {
                        return Mono.error(new ConfigurationException(
                            "Required parameter '" + parameterName + 
                            "' not found for tenant " + tenantId
                        ));
                    }
                    return Mono.empty();
                })
        );
}
```

---

## Parameter Types

Firefly supports **6 parameter types** for type-safe configuration:

### 1. STRING

**Purpose**: Text values (API keys, URLs, names, etc.)

**Example**:
```json
{
  "parameterName": "api_key",
  "parameterValue": "sk_live_abc123xyz789",
  "parameterType": "STRING"
}
```

**Validation**:
- Max length: 1000 characters
- Regex pattern (optional)
- Allowed values (optional)

**Use Cases**:
- API keys and secrets
- Endpoint URLs
- Provider names
- Custom identifiers

---

### 2. INTEGER

**Purpose**: Numeric values (timeouts, limits, counts, etc.)

**Example**:
```json
{
  "parameterName": "api_timeout",
  "parameterValue": "30000",
  "parameterType": "INTEGER",
  "metadata": "{\"unit\":\"milliseconds\",\"min\":1000,\"max\":60000\"}"
}
```

**Validation**:
- Min value (optional)
- Max value (optional)
- Must be valid integer

**Use Cases**:
- Timeouts (milliseconds)
- Retry attempts
- Rate limits
- Page sizes
- Connection pool sizes

---

### 3. BOOLEAN

**Purpose**: True/false flags (feature flags, toggles, etc.)

**Example**:
```json
{
  "parameterName": "enable_webhooks",
  "parameterValue": "true",
  "parameterType": "BOOLEAN"
}
```

**Validation**:
- Must be "true" or "false" (case-insensitive)

**Use Cases**:
- Feature flags
- Enable/disable functionality
- Debug mode
- Sandbox mode
- Retry enabled

---

### 4. JSON

**Purpose**: Complex structured data (objects, arrays, etc.)

**Example**:
```json
{
  "parameterName": "webhook_config",
  "parameterValue": "{\"url\":\"https://api.example.com/webhooks\",\"events\":[\"payment.success\",\"payment.failed\"],\"secret\":\"whsec_abc123\"}",
  "parameterType": "JSON"
}
```

**Validation**:
- Must be valid JSON syntax
- Schema validation (optional)

**Use Cases**:
- Webhook configurations
- Complex settings
- Feature configurations
- Custom metadata
- Provider-specific settings

---

### 5. URL

**Purpose**: Web addresses (API endpoints, webhooks, etc.)

**Example**:
```json
{
  "parameterName": "base_url",
  "parameterValue": "https://api.stripe.com",
  "parameterType": "URL"
}
```

**Validation**:
- Must be valid URL format
- Protocol validation (http/https)
- Domain validation

**Use Cases**:
- API base URLs
- Webhook URLs
- Redirect URLs
- Documentation URLs
- Support URLs

---

### 6. EMAIL

**Purpose**: Email addresses (support, notifications, etc.)

**Example**:
```json
{
  "parameterName": "support_email",
  "parameterValue": "support@example.com",
  "parameterType": "EMAIL"
}
```

**Validation**:
- Must be valid email format
- Domain validation (optional)

**Use Cases**:
- Support emails
- Notification emails
- Alert emails
- Contact emails

---

## Parameter Categories

Parameters are organized into **logical categories** for better management:

### 1. connection

**Purpose**: API connection settings

**Common Parameters**:
- `base_url`: API base URL
- `api_timeout`: Request timeout (milliseconds)
- `retry_attempts`: Number of retry attempts
- `retry_delay`: Delay between retries (milliseconds)
- `connection_pool_size`: Connection pool size
- `keep_alive_timeout`: Keep-alive timeout (seconds)

**Example**:
```json
{
  "parameterName": "api_timeout",
  "parameterValue": "30000",
  "parameterType": "INTEGER",
  "category": "connection"
}
```

---

### 2. authentication

**Purpose**: Authentication and authorization settings

**Common Parameters**:
- `api_key`: API key
- `api_secret`: API secret
- `client_id`: OAuth client ID
- `client_secret`: OAuth client secret
- `auth_type`: Authentication type (API_KEY, OAUTH2, BASIC)
- `token_url`: OAuth token URL
- `scope`: OAuth scope

**Example**:
```json
{
  "parameterName": "api_key",
  "parameterValue": "sk_live_abc123xyz789",
  "parameterType": "STRING",
  "category": "authentication",
  "isSecret": true
}
```

---

### 3. webhook

**Purpose**: Webhook configuration

**Common Parameters**:
- `webhook_url`: Webhook endpoint URL
- `webhook_secret`: Webhook signature secret
- `webhook_events`: Subscribed events (JSON array)
- `webhook_retry_attempts`: Retry attempts for failed webhooks
- `webhook_timeout`: Webhook request timeout

**Example**:
```json
{
  "parameterName": "webhook_config",
  "parameterValue": "{\"url\":\"https://api.example.com/webhooks\",\"events\":[\"payment.success\",\"payment.failed\"]}",
  "parameterType": "JSON",
  "category": "webhook"
}
```

---

### 4. features

**Purpose**: Feature flags and capabilities

**Common Parameters**:
- `enable_webhooks`: Enable webhook notifications
- `enable_retry`: Enable automatic retry
- `enable_sandbox`: Use sandbox environment
- `enable_debug`: Enable debug logging
- `supported_currencies`: Supported currencies (JSON array)
- `supported_countries`: Supported countries (JSON array)

**Example**:
```json
{
  "parameterName": "enable_webhooks",
  "parameterValue": "true",
  "parameterType": "BOOLEAN",
  "category": "features"
}
```

---

### 5. business

**Purpose**: Business rules and limits

**Common Parameters**:
- `min_transaction_amount`: Minimum transaction amount
- `max_transaction_amount`: Maximum transaction amount
- `daily_limit`: Daily transaction limit
- `monthly_limit`: Monthly transaction limit
- `fee_percentage`: Fee percentage
- `fee_fixed`: Fixed fee amount

**Example**:
```json
{
  "parameterName": "max_transaction_amount",
  "parameterValue": "10000",
  "parameterType": "INTEGER",
  "category": "business",
  "metadata": "{\"currency\":\"EUR\"}"
}
```

---

### 6. compliance

**Purpose**: Regulatory and compliance settings

**Common Parameters**:
- `data_residency`: Data residency region
- `retention_period`: Data retention period (days)
- `enable_audit_log`: Enable audit logging
- `enable_encryption`: Enable data encryption
- `compliance_level`: Compliance level (STANDARD, ENHANCED)
- `reporting_frequency`: Reporting frequency (DAILY, WEEKLY, MONTHLY)

**Example**:
```json
{
  "parameterName": "data_residency",
  "parameterValue": "EU",
  "parameterType": "STRING",
  "category": "compliance"
}
```

---

## Parameter Configuration

### Creating a Parameter

**Endpoint**: `POST /api/v1/provider-parameters`

**Request Body** (Global Parameter):
```json
{
  "providerId": "550e8400-e29b-41d4-a716-446655440400",
  "tenantId": null,
  "parameterName": "api_timeout",
  "parameterValue": "30000",
  "parameterType": "INTEGER",
  "description": "API request timeout in milliseconds",
  "isSecret": false,
  "isRequired": true,
  "isEditable": true,
  "validationRegex": "^[0-9]+$",
  "defaultValue": "30000",
  "environment": "production",
  "category": "connection",
  "displayOrder": 1,
  "metadata": "{\"unit\":\"milliseconds\",\"min\":1000,\"max\":60000\"}"
}
```

**Request Body** (Tenant-Specific Parameter):
```json
{
  "providerId": "550e8400-e29b-41d4-a716-446655440400",
  "tenantId": "550e8400-e29b-41d4-a716-446655440000",
  "parameterName": "api_key",
  "parameterValue": "sk_live_tenant_a_key",
  "parameterType": "STRING",
  "description": "Stripe API key for Tenant A",
  "isSecret": true,
  "isRequired": true,
  "isEditable": true,
  "environment": "production",
  "category": "authentication",
  "displayOrder": 1
}
```

**Response**:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440500",
  "providerId": "550e8400-e29b-41d4-a716-446655440400",
  "tenantId": "550e8400-e29b-41d4-a716-446655440000",
  "parameterName": "api_key",
  "parameterValue": "***ENCRYPTED***",
  "parameterType": "STRING",
  "description": "Stripe API key for Tenant A",
  "isSecret": true,
  "isRequired": true,
  "isEditable": true,
  "environment": "production",
  "category": "authentication",
  "displayOrder": 1,
  "createdAt": "2025-01-01T10:00:00Z",
  "updatedAt": "2025-01-01T10:00:00Z"
}
```

**Note**: Secret values are stored in the external `core-common-security-vault` service. When `isSecret=true`, the `parameterValue` must be null and `credentialVaultId` must contain a valid vault credential UUID reference.

### Field Descriptions

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `providerId` | UUID | Yes | Provider this parameter belongs to |
| `tenantId` | UUID | No | Tenant this parameter applies to (null = global) |
| `parameterName` | String | Yes | Parameter name (lowercase, underscore-separated) |
| `parameterValue` | String | Yes | Parameter value (encrypted if isSecret=true) |
| `parameterType` | Enum | Yes | Type: STRING, INTEGER, BOOLEAN, JSON, URL, EMAIL |
| `description` | Text | No | Human-readable description |
| `isSecret` | Boolean | No | Whether value should be encrypted (default: false) |
| `isRequired` | Boolean | No | Whether parameter is required (default: false) |
| `isEditable` | Boolean | No | Whether parameter can be edited (default: true) |
| `validationRegex` | String | No | Regex pattern for validation |
| `defaultValue` | String | No | Default value if not set |
| `environment` | String | No | Environment (development, staging, production) |
| `category` | String | No | Category (connection, authentication, etc.) |
| `displayOrder` | Integer | No | Display order in UI (default: 0) |
| `metadata` | JSON | No | Additional metadata |

### Updating a Parameter

**Endpoint**: `PUT /api/v1/provider-parameters/{id}`

**Request Body**: Same as creation, all fields optional

**Example - Update Timeout**:
```json
{
  "parameterValue": "45000"
}
```

**Example - Update Secret**:
```json
{
  "parameterValue": "sk_live_new_key"
}
```

**Note**: Updating a secret parameter will re-encrypt the value.

### Retrieving Parameters

**Get Single Parameter**: `GET /api/v1/provider-parameters/{id}`

**Filter Parameters**: `POST /api/v1/provider-parameters/filter`

Use the filter endpoint with a `FilterRequest` body to search parameters by provider, tenant, category, or other criteria with pagination.

### Deleting a Parameter

**Endpoint**: `DELETE /api/v1/provider-parameters/{id}`

**Note**: Deleting a tenant-specific parameter will cause the system to fall back to the global parameter or default value.

---

## Security and Encryption

### Sensitive Parameters

Parameters marked as `isSecret=true` are **automatically encrypted** at rest and in transit.

#### Encryption Flow

```
┌─────────────────────────────────────────────────────────────┐
│  1. Create Parameter with isSecret=true                     │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  2. Encrypt Value                                            │
│  Plain: "sk_live_abc123xyz789"                              │
│  Encrypted: "AES256:IV:CIPHERTEXT"                          │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  3. Store in Database                                        │
│  parameter_value: "AES256:IV:CIPHERTEXT"                    │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  4. Retrieve Parameter                                       │
│  Database: "AES256:IV:CIPHERTEXT"                           │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  5. Decrypt Value (in memory)                                │
│  Decrypted: "sk_live_abc123xyz789"                          │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  6. Use in Provider Call                                     │
│  Authorization: Bearer sk_live_abc123xyz789                  │
└─────────────────────────────────────────────────────────────┘
```

#### Secret Storage Architecture

Secret parameters use the `core-common-security-vault` integration pattern:

- When `isSecret=true`, the `parameterValue` field must be `null`
- The `credentialVaultId` field stores a UUID reference to the credential in the security-vault
- Consumer services retrieve the credential UUID from this service, then decrypt the actual value from the vault

This approach ensures credentials are never stored in the configuration database. See the [Security Vault Integration Guide](./SECURITY_VAULT_INTEGRATION.md) for details.

### API Response Masking

Secret parameters are **masked in API responses** to prevent accidental exposure:

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440500",
  "parameterName": "api_key",
  "parameterValue": "***ENCRYPTED***",
  "isSecret": true
}
```

### Audit Logging

All parameter changes are **logged for audit purposes**:

```java
@Aspect
@Component
public class ParameterAuditAspect {

    @AfterReturning(
        pointcut = "execution(* io.fireflysoftware.common.config.core.services.ProviderParameterService.create*(..))",
        returning = "result"
    )
    public void auditCreate(JoinPoint joinPoint, Object result) {
        ProviderParameterDTO param = (ProviderParameterDTO) result;
        auditLog.info(
            "Parameter created: provider={}, tenant={}, name={}, user={}",
            param.getProviderId(),
            param.getTenantId(),
            param.getParameterName(),
            SecurityContextHolder.getContext().getAuthentication().getName()
        );
    }

    @AfterReturning(
        pointcut = "execution(* io.fireflysoftware.common.config.core.services.ProviderParameterService.update*(..))",
        returning = "result"
    )
    public void auditUpdate(JoinPoint joinPoint, Object result) {
        ProviderParameterDTO param = (ProviderParameterDTO) result;
        auditLog.info(
            "Parameter updated: id={}, name={}, user={}",
            param.getId(),
            param.getParameterName(),
            SecurityContextHolder.getContext().getAuthentication().getName()
        );
    }
}
```

### Key Management

Encryption keys are managed using **industry best practices**:

1. **Key Storage**: Keys stored in secure key management system (AWS KMS, HashiCorp Vault)
2. **Key Rotation**: Automatic key rotation every 90 days
3. **Key Versioning**: Multiple key versions supported for seamless rotation
4. **Access Control**: Strict access control to encryption keys

---

## Validation and Constraints

### Type Validation

Each parameter type has **built-in validation**:

```java
public class ParameterValidator {

    public void validate(ProviderParameterDTO param) {
        switch (param.getParameterType()) {
            case STRING:
                validateString(param);
                break;
            case INTEGER:
                validateInteger(param);
                break;
            case BOOLEAN:
                validateBoolean(param);
                break;
            case JSON:
                validateJson(param);
                break;
            case URL:
                validateUrl(param);
                break;
            case EMAIL:
                validateEmail(param);
                break;
        }
    }

    private void validateInteger(ProviderParameterDTO param) {
        try {
            int value = Integer.parseInt(param.getParameterValue());

            // Check min/max from metadata
            if (param.getMetadata() != null) {
                JsonNode metadata = objectMapper.readTree(param.getMetadata());
                if (metadata.has("min")) {
                    int min = metadata.get("min").asInt();
                    if (value < min) {
                        throw new ValidationException(
                            "Value " + value + " is less than minimum " + min
                        );
                    }
                }
                if (metadata.has("max")) {
                    int max = metadata.get("max").asInt();
                    if (value > max) {
                        throw new ValidationException(
                            "Value " + value + " is greater than maximum " + max
                        );
                    }
                }
            }
        } catch (NumberFormatException e) {
            throw new ValidationException(
                "Invalid integer value: " + param.getParameterValue()
            );
        }
    }

    private void validateUrl(ProviderParameterDTO param) {
        try {
            URL url = new URL(param.getParameterValue());
            String protocol = url.getProtocol();
            if (!protocol.equals("http") && !protocol.equals("https")) {
                throw new ValidationException(
                    "URL must use http or https protocol"
                );
            }
        } catch (MalformedURLException e) {
            throw new ValidationException(
                "Invalid URL: " + param.getParameterValue()
            );
        }
    }

    private void validateEmail(ProviderParameterDTO param) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!param.getParameterValue().matches(emailRegex)) {
            throw new ValidationException(
                "Invalid email: " + param.getParameterValue()
            );
        }
    }
}
```

### Regex Validation

Custom validation using **regex patterns**:

```json
{
  "parameterName": "phone_number",
  "parameterValue": "+33123456789",
  "parameterType": "STRING",
  "validationRegex": "^\\+[0-9]{10,15}$"
}
```

### Required Parameters

Parameters marked as `isRequired=true` **must have a value**:

```java
public Mono<String> getRequiredParameter(
    UUID providerId,
    UUID tenantId,
    String parameterName
) {
    return getParameterValue(providerId, tenantId, parameterName)
        .switchIfEmpty(
            Mono.error(new ConfigurationException(
                "Required parameter '" + parameterName +
                "' not found for provider " + providerId +
                " and tenant " + tenantId
            ))
        );
}
```

---

## API Reference

### Provider Parameter Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/provider-parameters/{id}` | Get parameter by ID |
| `POST` | `/api/v1/provider-parameters/filter` | Filter parameters with pagination and criteria |
| `POST` | `/api/v1/provider-parameters` | Create new parameter |
| `PUT` | `/api/v1/provider-parameters/{id}` | Update parameter |
| `DELETE` | `/api/v1/provider-parameters/{id}` | Delete parameter |

---

## Best Practices

### Parameter Naming

#### DO:

1. **Use Descriptive Names**
   ```
   api_timeout
   max_transaction_amount
   webhook_secret

   timeout
   max
   secret
   ```

2. **Use Lowercase with Underscores**
   ```
   api_timeout
   max_retry_attempts

   apiTimeout (camelCase)
   API_TIMEOUT (UPPERCASE)
   api-timeout (kebab-case)
   ```

3. **Group Related Parameters**
   ```
   stripe_api_key
   stripe_api_secret
   stripe_webhook_secret

   api_key
   secret
   webhook
   ```

#### DON'T:

1. **Don't Use Ambiguous Names**
   - Avoid generic names like "value", "data", "config"
   - Be specific about what the parameter controls

2. **Don't Use Special Characters**
   - Stick to alphanumeric and underscores
   - Avoid spaces, hyphens, dots

### Parameter Organization

#### DO:

1. **Use Categories**
   ```yaml
   connection:
     - api_timeout
     - retry_attempts
     - connection_pool_size

   authentication:
     - api_key
     - api_secret
     - client_id

   webhook:
     - webhook_url
     - webhook_secret
     - webhook_events
   ```

2. **Set Display Order**
   ```json
   [
     {"parameterName": "api_key", "displayOrder": 1},
     {"parameterName": "api_secret", "displayOrder": 2},
     {"parameterName": "api_timeout", "displayOrder": 3}
   ]
   ```

3. **Document Parameters**
   ```json
   {
     "parameterName": "api_timeout",
     "description": "Maximum time to wait for API response in milliseconds. Recommended: 30000 (30 seconds). Min: 1000, Max: 60000.",
     "metadata": "{\"unit\":\"milliseconds\",\"min\":1000,\"max\":60000,\"recommended\":30000}"
   }
   ```

### Security

#### DO:

1. **Mark Secrets as Secret**
   ```json
   {
     "parameterName": "api_key",
     "parameterValue": "sk_live_abc123",
     "isSecret": true
   }
   ```

2. **Use Environment-Specific Parameters**
   ```json
   {
     "parameterName": "api_key",
     "parameterValue": "sk_test_abc123",
     "environment": "development"
   },
   {
     "parameterName": "api_key",
     "parameterValue": "sk_live_xyz789",
     "environment": "production"
   }
   ```

3. **Rotate Secrets Regularly**
   ```java
   @Scheduled(cron = "0 0 0 1 * *") // Monthly
   public void rotateSecrets() {
       secretParameters.forEach(param -> {
           String newSecret = generateNewSecret();
           updateParameter(param.getId(), newSecret);
           notifyOps("Secret rotated: " + param.getParameterName());
       });
   }
   ```

#### DON'T:

1. **Don't Store Secrets in Plain Text**
   - Always use `isSecret=true` for sensitive values
   - Never log secret values

2. **Don't Share Secrets Across Environments**
   - Use separate secrets for dev, staging, production
   - Never use production secrets in development

3. **Don't Hardcode Secrets**
   - Always use parameters for secrets
   - Never commit secrets to version control

### Validation

#### DO:

1. **Validate Input**
   ```json
   {
     "parameterName": "api_timeout",
     "parameterType": "INTEGER",
     "validationRegex": "^[0-9]+$",
     "metadata": "{\"min\":1000,\"max\":60000}"
   }
   ```

2. **Provide Defaults**
   ```json
   {
     "parameterName": "retry_attempts",
     "defaultValue": "3",
     "isRequired": false
   }
   ```

3. **Mark Required Parameters**
   ```json
   {
     "parameterName": "api_key",
     "isRequired": true
   }
   ```

#### DON'T:

1. **Don't Skip Validation**
   - Always validate parameter values
   - Use appropriate types and constraints

2. **Don't Allow Invalid Values**
   - Reject invalid values at creation/update time
   - Don't rely on runtime validation only

---

## Use Cases

### Use Case 1: Multi-Environment Configuration

**Scenario**: Different API keys for development, staging, and production.

**Implementation**:

```yaml
Development:
  - Parameter: api_key
    Value: sk_test_dev_key
    Environment: development
    isSecret: true

Staging:
  - Parameter: api_key
    Value: sk_test_staging_key
    Environment: staging
    isSecret: true

Production:
  - Parameter: api_key
    Value: sk_live_prod_key
    Environment: production
    isSecret: true
```

**Retrieval Logic**:
```java
public Mono<String> getApiKey(UUID providerId, UUID tenantId) {
    String environment = getCurrentEnvironment(); // development, staging, production

    return parameterRepository
        .findByProviderIdAndTenantIdAndParameterNameAndEnvironment(
            providerId, tenantId, "api_key", environment
        )
        .map(ProviderParameter::getParameterValue);
}
```

### Use Case 2: Tenant-Specific Overrides

**Scenario**: Most tenants use default timeout, but Tenant A needs longer timeout.

**Implementation**:

```yaml
Global Parameter:
  - Provider: Stripe
    Tenant: null (global)
    Parameter: api_timeout
    Value: 30000

Tenant A Override:
  - Provider: Stripe
    Tenant: Tenant A
    Parameter: api_timeout
    Value: 60000 (longer timeout)
```

**Result**:
- Tenant A: Uses 60000ms timeout
- All other tenants: Use 30000ms timeout (global default)

### Use Case 3: Feature Flags

**Scenario**: Enable webhooks for some tenants, disable for others.

**Implementation**:

```yaml
Global Parameter:
  - Provider: Stripe
    Tenant: null
    Parameter: enable_webhooks
    Value: false (disabled by default)

Tenant A Override:
  - Provider: Stripe
    Tenant: Tenant A
    Parameter: enable_webhooks
    Value: true (enabled for Tenant A)

Tenant B Override:
  - Provider: Stripe
    Tenant: Tenant B
    Parameter: enable_webhooks
    Value: true (enabled for Tenant B)
```

**Usage**:
```java
public Mono<Void> processPayment(PaymentRequest request) {
    return getParameterValue(providerId, tenantId, "enable_webhooks")
        .map(Boolean::parseBoolean)
        .flatMap(webhooksEnabled -> {
            if (webhooksEnabled) {
                return processWithWebhooks(request);
            } else {
                return processWithoutWebhooks(request);
            }
        });
}
```

### Use Case 4: Complex Configuration

**Scenario**: Webhook configuration with multiple settings.

**Implementation**:

```json
{
  "parameterName": "webhook_config",
  "parameterType": "JSON",
  "parameterValue": "{\"url\":\"https://api.example.com/webhooks\",\"events\":[\"payment.success\",\"payment.failed\",\"payment.refunded\"],\"secret\":\"whsec_abc123\",\"retry_attempts\":3,\"timeout\":5000}"
}
```

**Usage**:
```java
public Mono<WebhookConfig> getWebhookConfig(UUID providerId, UUID tenantId) {
    return getParameterValue(providerId, tenantId, "webhook_config")
        .map(json -> objectMapper.readValue(json, WebhookConfig.class));
}

@Data
public class WebhookConfig {
    private String url;
    private List<String> events;
    private String secret;
    private Integer retryAttempts;
    private Integer timeout;
}
```

---

## Summary

Provider Parameters enable **flexible, secure, and maintainable configuration** for Firefly:

- **Hierarchical**: Tenant-specific overrides global defaults
- **Type-Safe**: Strongly typed with validation
- **Secure**: Automatic encryption for secrets
- **Flexible**: Support for simple and complex configurations
- **Auditable**: All changes logged for compliance
- **Environment-Aware**: Different settings per environment

For more information, see:
- [Provider Management](./providers.md)
- [Tenant Management](./tenants.md)
- [Main Documentation](./README.md)

---

**[Back to Top](#provider-parameter-configuration)**

