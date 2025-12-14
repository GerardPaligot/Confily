-- V9a__create_partner_sponsorships_table.sql
-- Phase 3: Extended - Partner Multi-Tier Sponsorships (Normalized)

CREATE TABLE partner_sponsorships (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    partner_id UUID NOT NULL REFERENCES partners(id) ON DELETE CASCADE,
    sponsoring_type_id UUID NOT NULL REFERENCES sponsoring_types(id) ON DELETE RESTRICT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(partner_id, sponsoring_type_id)
);

-- Indexes
CREATE INDEX idx_partner_sponsorships_partner ON partner_sponsorships(partner_id);
CREATE INDEX idx_partner_sponsorships_type ON partner_sponsorships(sponsoring_type_id);

COMMENT ON TABLE partner_sponsorships IS 'Multi-tier sponsorship support - partners can sponsor at multiple levels';
COMMENT ON COLUMN partner_sponsorships.sponsoring_type_id IS 'Reference to sponsorship tier (normalized from VARCHAR tier)';
