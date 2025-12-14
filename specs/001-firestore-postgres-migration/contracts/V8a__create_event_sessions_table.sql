-- V8a__create_event_sessions_table.sql
-- Phase 2: Content - Event Sessions (breaks, meals, social events)

CREATE TABLE event_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    title VARCHAR(500) NOT NULL,
    description TEXT,
    address VARCHAR(500),  -- Optional location/address for this event session
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_event_sessions_event_id ON event_sessions(event_id);

-- Auto-update trigger
CREATE TRIGGER update_event_sessions_updated_at
    BEFORE UPDATE ON event_sessions
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE event_sessions IS 'Event-type sessions like breaks, meals, networking events (no speakers or formats)';
COMMENT ON COLUMN event_sessions.title IS 'Event session name (e.g., "Lunch Break", "Networking Reception")';
COMMENT ON COLUMN event_sessions.address IS 'Optional specific location/address for this event session';
