-- Activities Table
-- Purpose: Store partner-sponsored activities at events
-- Dependencies: events table, partners table
-- Cascade: RESTRICT on deletion (explicit cleanup required)

CREATE TABLE activities (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    partner_id UUID NOT NULL REFERENCES partners(id) ON DELETE RESTRICT,
    name VARCHAR(255) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_activities_event_id ON activities(event_id);
CREATE INDEX idx_activities_partner_id ON activities(partner_id);
CREATE INDEX idx_activities_start_time ON activities(event_id, start_time);

COMMENT ON TABLE activities IS 'Partner-sponsored activities at events';
COMMENT ON COLUMN activities.name IS 'Activity name/title';
COMMENT ON COLUMN activities.start_time IS 'Activity start time (TIMESTAMP for proper temporal operations)';
COMMENT ON COLUMN activities.end_time IS 'Activity end time (optional)';
