-- Maps Table
-- Purpose: Store venue floor plans and interactive maps
-- Dependencies: events table
-- Cascade: RESTRICT on event deletion (explicit cleanup required)

CREATE TABLE maps (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    color VARCHAR(7),  -- Hex color code #RRGGBB
    color_selected VARCHAR(7),  -- Selected state color
    filename VARCHAR(255) NOT NULL,
    url TEXT NOT NULL,  -- Map image/SVG URL
    filled_url TEXT,  -- Filled/highlighted version URL
    display_order INT NOT NULL DEFAULT 0,
    picto_size INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_maps_event_id ON maps(event_id);
CREATE INDEX idx_maps_display_order ON maps(event_id, display_order);

COMMENT ON TABLE maps IS 'Venue floor plans and interactive maps';
COMMENT ON COLUMN maps.color IS 'Default hex color for map elements';
COMMENT ON COLUMN maps.color_selected IS 'Hex color when map element is selected';
COMMENT ON COLUMN maps.picto_size IS 'Size of pictogram icons on map';
