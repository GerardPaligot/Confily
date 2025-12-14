-- V3__create_formats_table.sql
-- Phase 1: Foundation - Session Formats

CREATE TABLE formats (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    name VARCHAR(255) NOT NULL,
    icon VARCHAR(50),
    color VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    CONSTRAINT unique_format_per_event UNIQUE (event_id, name)
);

-- Indexes
CREATE INDEX idx_formats_event_id ON formats(event_id);
CREATE INDEX idx_formats_name ON formats(name);

-- Auto-update trigger
CREATE TRIGGER update_formats_updated_at
    BEFORE UPDATE ON formats
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE formats IS 'Session formats (e.g., Keynote, Talk, Workshop)';
