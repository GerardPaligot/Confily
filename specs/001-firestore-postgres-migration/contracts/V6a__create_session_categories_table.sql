-- V6a__create_session_categories_table.sql
-- Phase 2: Content - Session-Category Junction Table

CREATE TABLE session_categories (
    session_id UUID NOT NULL REFERENCES sessions(id) ON DELETE CASCADE,
    category_id UUID NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (session_id, category_id)
);

-- Indexes for bidirectional queries
CREATE INDEX idx_session_categories_session ON session_categories(session_id);
CREATE INDEX idx_session_categories_category ON session_categories(category_id);

COMMENT ON TABLE session_categories IS 'Many-to-many relationship between sessions and categories';
COMMENT ON COLUMN session_categories.session_id IS 'Reference to the session';
COMMENT ON COLUMN session_categories.category_id IS 'Reference to the category/track';
