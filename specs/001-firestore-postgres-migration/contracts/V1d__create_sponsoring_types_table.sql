-- Sponsoring Types Table
-- Purpose: Define sponsorship tiers/levels for partners
-- Dependencies: events table
-- Cascade: CASCADE on event deletion (types are event-specific)

CREATE TABLE sponsoring_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    type_name VARCHAR(255) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(event_id, type_name)
);

CREATE INDEX idx_sponsoring_types_event_id ON sponsoring_types(event_id);

COMMENT ON TABLE sponsoring_types IS 'Sponsorship tiers (Platinum, Gold, Silver, etc.)';
COMMENT ON COLUMN sponsoring_types.type_name IS 'Name of the sponsorship tier';
COMMENT ON COLUMN sponsoring_types.display_order IS 'UI ordering for tier display';
