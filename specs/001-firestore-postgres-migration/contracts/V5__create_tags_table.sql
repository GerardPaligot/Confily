-- V5__create_tags_table.sql
-- Phase 2: Content - Session Tags

CREATE TABLE tags (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    CONSTRAINT unique_tag_per_event UNIQUE (event_id, name)
);

-- Indexes
CREATE INDEX idx_tags_event_id ON tags(event_id);
CREATE INDEX idx_tags_name ON tags(name);

-- Auto-update trigger
CREATE TRIGGER update_tags_updated_at
    BEFORE UPDATE ON tags
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE tags IS 'Freeform tags for sessions (e.g., beginner, advanced, hands-on)';
