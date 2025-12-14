-- Map Shapes Table
-- Purpose: Interactive shapes/areas on maps (rooms, zones, clickable regions)
-- Dependencies: maps table
-- Cascade: CASCADE on map deletion (shapes belong to map)

CREATE TABLE map_shapes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    map_id UUID NOT NULL REFERENCES maps(id) ON DELETE CASCADE,
    display_order INT NOT NULL DEFAULT 0,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    start_x DECIMAL(10, 2) NOT NULL,
    start_y DECIMAL(10, 2) NOT NULL,
    end_x DECIMAL(10, 2) NOT NULL,
    end_y DECIMAL(10, 2) NOT NULL,
    type VARCHAR(50) NOT NULL,  -- MappingType enum value
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_map_shapes_map_id ON map_shapes(map_id);

COMMENT ON TABLE map_shapes IS 'Interactive clickable shapes/regions on maps';
COMMENT ON COLUMN map_shapes.start_x IS 'Shape start X coordinate';
COMMENT ON COLUMN map_shapes.start_y IS 'Shape start Y coordinate';
COMMENT ON COLUMN map_shapes.end_x IS 'Shape end X coordinate';
COMMENT ON COLUMN map_shapes.end_y IS 'Shape end Y coordinate';
COMMENT ON COLUMN map_shapes.type IS 'Shape type from MappingType enum';
