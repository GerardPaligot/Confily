-- Lunch Menus Table
-- Purpose: Daily lunch menu information for events
-- Dependencies: events table
-- Cascade: RESTRICT on event deletion (explicit cleanup required)

CREATE TABLE lunch_menus (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    date DATE NOT NULL,
    name VARCHAR(255) NOT NULL,  -- Menu name/title
    dish VARCHAR(500) NOT NULL,
    accompaniment VARCHAR(500) NOT NULL,
    dessert VARCHAR(500) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_lunch_menus_event_id ON lunch_menus(event_id);
CREATE INDEX idx_lunch_menus_date ON lunch_menus(event_id, date);

COMMENT ON TABLE lunch_menus IS 'Daily lunch menu information for events';
COMMENT ON COLUMN lunch_menus.date IS 'Date for this menu (allows different menus per day)';
COMMENT ON COLUMN lunch_menus.name IS 'Menu name or title';
COMMENT ON COLUMN lunch_menus.display_order IS 'UI ordering when multiple menus per day';
