-- Speaker Socials Junction Table
-- Purpose: Link social media accounts to speakers (many-to-many)
-- Dependencies: socials table, speakers table
-- Cascade: CASCADE on deletion (junction record cleanup)

CREATE TABLE speaker_socials (
    social_id UUID NOT NULL REFERENCES socials(id) ON DELETE CASCADE,
    speaker_id UUID NOT NULL REFERENCES speakers(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (social_id, speaker_id)
);

CREATE INDEX idx_speaker_socials_social ON speaker_socials(social_id);
CREATE INDEX idx_speaker_socials_speaker ON speaker_socials(speaker_id);

COMMENT ON TABLE speaker_socials IS 'Junction table linking socials to speakers';
COMMENT ON COLUMN speaker_socials.social_id IS 'Reference to social account';
COMMENT ON COLUMN speaker_socials.speaker_id IS 'Reference to speaker';
