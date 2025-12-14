-- V1a__create_event_features_table.sql
-- Phase 1: Foundation - Event Feature Flags (normalized)

CREATE TABLE event_features (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    feature_key VARCHAR(100) NOT NULL,  -- e.g., 'networking', 'partners', 'qanda'
    enabled BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    CONSTRAINT unique_feature_per_event UNIQUE (event_id, feature_key)
);

-- Indexes
CREATE INDEX idx_event_features_event_id ON event_features(event_id);
CREATE INDEX idx_event_features_enabled ON event_features(event_id, enabled);

-- Auto-update trigger
CREATE TRIGGER update_event_features_updated_at
    BEFORE UPDATE ON event_features
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE event_features IS 'Feature flags/toggles for events (normalized from JSONB)';
COMMENT ON COLUMN event_features.feature_key IS 'Feature identifier: networking, partners, qanda, schedule, etc.';
COMMENT ON COLUMN event_features.enabled IS 'Boolean flag indicating if feature is enabled for this event';
COMMENT ON CONSTRAINT unique_feature_per_event ON event_features IS 'Each feature can only be defined once per event';
