-- V11__create_socials_table.sql
-- Phase 3: Extended - Social Media Links (Event-Scoped)

CREATE TABLE socials (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    platform VARCHAR(50) NOT NULL,  -- twitter, linkedin, github, mastodon, etc.
    url TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_socials_event ON socials(event_id);
CREATE INDEX idx_socials_platform ON socials(platform);

COMMENT ON TABLE socials IS 'Social media links scoped to events - linked to speakers/partners via junction tables';
COMMENT ON COLUMN socials.event_id IS 'Event scope for social links';
COMMENT ON COLUMN socials.platform IS 'Social platform identifier (twitter, linkedin, github, mastodon, etc.)';
COMMENT ON TABLE socials IS 'Social links are linked to speakers via speaker_socials and to partners via partner_socials junction tables';
