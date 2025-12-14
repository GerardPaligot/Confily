-- V8__create_session_tags_table.sql
-- Phase 2: Content - Many-to-Many relationship between Sessions and Tags

CREATE TABLE session_tags (
    session_id UUID NOT NULL REFERENCES sessions(id) ON DELETE CASCADE,
    tag_id UUID NOT NULL REFERENCES tags(id) ON DELETE RESTRICT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    PRIMARY KEY (session_id, tag_id)
);

-- Indexes for bidirectional queries
CREATE INDEX idx_session_tags_session_id ON session_tags(session_id);
CREATE INDEX idx_session_tags_tag_id ON session_tags(tag_id);

COMMENT ON TABLE session_tags IS 'Junction table linking sessions to tags (many-to-many)';
COMMENT ON CONSTRAINT session_tags_pkey ON session_tags IS 'Composite primary key prevents duplicate tag assignments';
