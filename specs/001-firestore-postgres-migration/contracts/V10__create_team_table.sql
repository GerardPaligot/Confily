-- Team Table
-- Purpose: Store event team members with roles and group assignments
-- Dependencies: events table, team_groups table
-- Cascade: RESTRICT on event/group deletion (explicit cleanup required)

CREATE TABLE team (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(255),
    bio TEXT,
    photo_url TEXT,
    team_group_id UUID NOT NULL REFERENCES team_groups(id) ON DELETE RESTRICT,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_team_event_id ON team(event_id);
CREATE INDEX idx_team_group_id ON team(team_group_id);
CREATE INDEX idx_team_display_order ON team(event_id, display_order);

COMMENT ON TABLE team IS 'Event team members (organizers, volunteers, staff)';
COMMENT ON COLUMN team.name IS 'Team member name';
COMMENT ON COLUMN team.role IS 'Role within the team (e.g., "Lead Organizer")';
COMMENT ON COLUMN team.team_group_id IS 'Reference to team group (normalized from VARCHAR)';
COMMENT ON COLUMN team.display_order IS 'UI ordering for team member display';
