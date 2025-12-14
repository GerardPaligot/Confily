-- V6__create_sessions_table.sql
-- Phase 2: Content - Conference Sessions (Talk Sessions)

CREATE TABLE sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    format_id UUID REFERENCES formats(id) ON DELETE RESTRICT,
    
    title VARCHAR(500) NOT NULL,
    description TEXT,
    language VARCHAR(10) NOT NULL DEFAULT 'en',  -- ISO 639-1 language code
    level VARCHAR(50),  -- beginner, intermediate, advanced
    
    -- Optional session media links
    link_slides TEXT,
    link_replay TEXT,
    drive_folder_id TEXT,
    
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_sessions_event_id ON sessions(event_id);
CREATE INDEX idx_sessions_format_id ON sessions(format_id);
CREATE INDEX idx_sessions_language ON sessions(language);
CREATE INDEX idx_sessions_level ON sessions(level);

-- Full-text search on session titles and descriptions
CREATE INDEX idx_sessions_search ON sessions USING GIN(to_tsvector('english', title || ' ' || COALESCE(description, '')));

-- Auto-update trigger
CREATE TRIGGER update_sessions_updated_at
    BEFORE UPDATE ON sessions
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE sessions IS 'Conference session talks, workshops, and presentations with speakers';
COMMENT ON COLUMN sessions.language IS 'Session presentation language (ISO 639-1 code)';
COMMENT ON COLUMN sessions.link_slides IS 'URL to presentation slides';
COMMENT ON COLUMN sessions.link_replay IS 'URL to session video recording';
COMMENT ON COLUMN sessions.drive_folder_id IS 'Google Drive folder ID for session materials';
COMMENT ON INDEX idx_sessions_search IS 'Full-text search for session titles and descriptions';
