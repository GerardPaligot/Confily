-- Addresses Table
-- Purpose: Physical locations for events and venues
-- Dependencies: None (referenced by events, partners)
-- Cascade: N/A (no parent entities)

CREATE TABLE addresses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    street TEXT NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(100) NOT NULL,
    country_code VARCHAR(3) NOT NULL,  -- ISO country code
    latitude DECIMAL(10, 7),
    longitude DECIMAL(10, 7),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_addresses_city_country ON addresses(city, country);
CREATE INDEX idx_addresses_coordinates ON addresses(latitude, longitude);  -- GeoSpatial queries

COMMENT ON TABLE addresses IS 'Physical addresses for events and venues';
COMMENT ON COLUMN addresses.street IS 'Street address (structured field - user preference)';
COMMENT ON COLUMN addresses.postal_code IS 'Postal/ZIP code (structured field - user preference)';
COMMENT ON COLUMN addresses.country_code IS 'ISO country code (2 or 3 characters)';
COMMENT ON COLUMN addresses.latitude IS 'Geographic latitude for mapping';
COMMENT ON COLUMN addresses.longitude IS 'Geographic longitude for mapping';
