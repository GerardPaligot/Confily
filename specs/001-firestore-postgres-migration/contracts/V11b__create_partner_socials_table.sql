-- Partner Socials Junction Table
-- Purpose: Link social media accounts to partners (many-to-many)
-- Dependencies: socials table, partners table
-- Cascade: CASCADE on deletion (junction record cleanup)

CREATE TABLE partner_socials (
    social_id UUID NOT NULL REFERENCES socials(id) ON DELETE CASCADE,
    partner_id UUID NOT NULL REFERENCES partners(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (social_id, partner_id)
);

CREATE INDEX idx_partner_socials_social ON partner_socials(social_id);
CREATE INDEX idx_partner_socials_partner ON partner_socials(partner_id);

COMMENT ON TABLE partner_socials IS 'Junction table linking socials to partners';
COMMENT ON COLUMN partner_socials.social_id IS 'Reference to social account';
COMMENT ON COLUMN partner_socials.partner_id IS 'Reference to partner';
