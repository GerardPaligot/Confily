-- V2__create_categories_table.sql
-- Phase 1: Foundation - Session Categories

CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    name VARCHAR(255) NOT NULL,
    icon VARCHAR(50),  -- Icon identifier
    color VARCHAR(20),  -- Hex color code or color name
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    CONSTRAINT unique_category_per_event UNIQUE (event_id, name)
);

-- Indexes
CREATE INDEX idx_categories_event_id ON categories(event_id);
CREATE INDEX idx_categories_name ON categories(name);

-- Auto-update trigger
CREATE TRIGGER update_categories_updated_at
    BEFORE UPDATE ON categories
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE categories IS 'Session categories (e.g., Android, Kotlin, Architecture)';
COMMENT ON CONSTRAINT unique_category_per_event ON categories IS 'Prevents duplicate category names within same event';
