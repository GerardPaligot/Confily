-- V4__create_speakers_table.sql
-- Phase 2: Content - Speakers

CREATE TABLE speakers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    name VARCHAR(255) NOT NULL,
    bio TEXT,
    photo_url TEXT,
    pronouns VARCHAR(50),
    email VARCHAR(255),
    company VARCHAR(255),
    job_title VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_speakers_event_id ON speakers(event_id);
CREATE INDEX idx_speakers_name ON speakers(event_id, name);
CREATE INDEX idx_speakers_email ON speakers(email);
CREATE INDEX idx_speakers_company ON speakers(company);

-- Full-text search on speaker names and bios
CREATE INDEX idx_speakers_search ON speakers USING GIN(to_tsvector('english', name || ' ' || COALESCE(bio, '')));

-- Auto-update trigger
CREATE TRIGGER update_speakers_updated_at
    BEFORE UPDATE ON speakers
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE speakers IS 'Conference speakers and their professional information';
COMMENT ON INDEX idx_speakers_search IS 'Full-text search index for finding speakers by name or bio keywords';
