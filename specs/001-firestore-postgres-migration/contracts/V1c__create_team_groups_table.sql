-- Team Groups Table
-- Purpose: Categorize team members into groups (e.g., Organizers, Volunteers)
-- Dependencies: events table
-- Cascade: CASCADE on event deletion (groups are event-specific)

CREATE TABLE team_groups (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(event_id, name)
);

CREATE INDEX idx_team_groups_event_id ON team_groups(event_id);

COMMENT ON TABLE team_groups IS 'Groups for organizing team members (Organizers, Volunteers, etc.)';
COMMENT ON COLUMN team_groups.name IS 'Name of the team group';
COMMENT ON COLUMN team_groups.display_order IS 'UI ordering for group display';
