-- Event Session Tracks Table
-- Purpose: Track categories for event sessions (breaks, meals, social events)
-- Dependencies: events table
-- Cascade: CASCADE on event deletion (tracks are event-specific)

CREATE TABLE event_session_tracks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    track_name VARCHAR(255) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(event_id, track_name)
);

CREATE INDEX idx_event_session_tracks_event_id ON event_session_tracks(event_id);

COMMENT ON TABLE event_session_tracks IS 'Categories/tracks for organizing event sessions (breaks, meals, networking)';
COMMENT ON COLUMN event_session_tracks.track_name IS 'Name of the track (e.g., "Main Hall", "Side Events")';
COMMENT ON COLUMN event_session_tracks.display_order IS 'UI ordering for track display';
