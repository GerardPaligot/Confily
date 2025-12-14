-- V1__create_events_table.sql
-- Phase 1: Foundation - Events (core entity)
-- Note: External integrations removed - reserved for future contribution

CREATE EXTENSION IF NOT EXISTS "pgcrypto";  -- For gen_random_uuid()

CREATE TABLE events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    slug VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    address_id UUID REFERENCES addresses(id) ON DELETE SET NULL,
    default_language VARCHAR(10) NOT NULL DEFAULT 'en',
    contact_email VARCHAR(255),
    contact_phone VARCHAR(50),
    coc_url TEXT,
    faq_url TEXT,
    published_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Indexes for common queries
CREATE INDEX idx_events_slug ON events(slug);
CREATE INDEX idx_events_start_date ON events(start_date);
CREATE INDEX idx_events_date_range ON events(start_date, end_date);

-- Trigger to auto-update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_events_updated_at
    BEFORE UPDATE ON events
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE events IS 'Core conference/event metadata';
COMMENT ON COLUMN events.slug IS 'URL-friendly unique identifier (e.g., kotlinconf2024)';
COMMENT ON COLUMN events.address_id IS 'Reference to event venue address';
COMMENT ON COLUMN events.default_language IS 'Default language for event (ISO code)';
COMMENT ON COLUMN events.published_at IS 'Timestamp when event was published (nullable)';
