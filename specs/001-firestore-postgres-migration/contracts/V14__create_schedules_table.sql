-- Schedules Table
-- Purpose: Time slots for both talk sessions and event sessions
-- Dependencies: events, sessions, event_sessions, event_session_tracks tables
-- Cascade: SET NULL on session deletion (preserve schedule slot)

CREATE TABLE schedules (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    session_id UUID REFERENCES sessions(id) ON DELETE SET NULL,
    event_session_id UUID REFERENCES event_sessions(id) ON DELETE SET NULL,
    event_session_track_id UUID NOT NULL REFERENCES event_session_tracks(id) ON DELETE RESTRICT,
    display_order INT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT check_time_order CHECK (end_time > start_time),
    CONSTRAINT check_session_type CHECK (
        (session_id IS NOT NULL AND event_session_id IS NULL) OR
        (session_id IS NULL AND event_session_id IS NOT NULL) OR
        (session_id IS NULL AND event_session_id IS NULL)
    )
);

CREATE INDEX idx_schedules_event_id ON schedules(event_id);
CREATE INDEX idx_schedules_session_id ON schedules(session_id);
CREATE INDEX idx_schedules_event_session_id ON schedules(event_session_id);
CREATE INDEX idx_schedules_track_id ON schedules(event_session_track_id);
CREATE INDEX idx_schedules_time_range ON schedules(event_id, start_time, end_time);

COMMENT ON TABLE schedules IS 'Time slots for talk sessions and event sessions';
COMMENT ON COLUMN schedules.session_id IS 'Reference to talk session (nullable)';
COMMENT ON COLUMN schedules.event_session_id IS 'Reference to event session (nullable)';
COMMENT ON COLUMN schedules.event_session_track_id IS 'Track assignment (normalized from room VARCHAR)';
COMMENT ON COLUMN schedules.start_time IS 'Schedule start time (TIMESTAMP for proper temporal operations)';
COMMENT ON COLUMN schedules.end_time IS 'Schedule end time (TIMESTAMP)';
COMMENT ON CONSTRAINT check_time_order ON schedules IS 'Ensure end_time is after start_time';
COMMENT ON CONSTRAINT check_session_type ON schedules IS 'Ensure only one session type is referenced';
