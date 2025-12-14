-- V7__create_session_speakers_table.sql
-- Phase 2: Content - Many-to-Many relationship between Sessions and Speakers

CREATE TABLE session_speakers (
    session_id UUID NOT NULL REFERENCES sessions(id) ON DELETE CASCADE,
    speaker_id UUID NOT NULL REFERENCES speakers(id) ON DELETE RESTRICT,
    speaker_order INT NOT NULL DEFAULT 1,  -- Order for multi-speaker sessions
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    PRIMARY KEY (session_id, speaker_id),
    CONSTRAINT valid_speaker_order CHECK (speaker_order > 0)
);

-- Indexes for bidirectional queries
CREATE INDEX idx_session_speakers_session_id ON session_speakers(session_id);
CREATE INDEX idx_session_speakers_speaker_id ON session_speakers(speaker_id);
CREATE INDEX idx_session_speakers_order ON session_speakers(session_id, speaker_order);

COMMENT ON TABLE session_speakers IS 'Junction table linking sessions to their speakers (many-to-many)';
COMMENT ON COLUMN session_speakers.speaker_order IS 'Display order for multiple speakers (1 = first, 2 = second, etc.)';
COMMENT ON CONSTRAINT session_speakers_pkey ON session_speakers IS 'Composite primary key prevents duplicate speaker assignments';
