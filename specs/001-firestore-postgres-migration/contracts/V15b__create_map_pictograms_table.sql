-- Map Pictograms Table
-- Purpose: Icon markers on maps (facilities, points of interest)
-- Dependencies: maps table
-- Cascade: CASCADE on map deletion (pictograms belong to map)

CREATE TABLE map_pictograms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    map_id UUID NOT NULL REFERENCES maps(id) ON DELETE CASCADE,
    display_order INT NOT NULL DEFAULT 0,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    position_x DECIMAL(10, 2) NOT NULL,
    position_y DECIMAL(10, 2) NOT NULL,
    type VARCHAR(50) NOT NULL,  -- PictogramType enum value
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_map_pictograms_map_id ON map_pictograms(map_id);

COMMENT ON TABLE map_pictograms IS 'Icon markers on maps for facilities and POIs';
COMMENT ON COLUMN map_pictograms.position_x IS 'Pictogram X coordinate';
COMMENT ON COLUMN map_pictograms.position_y IS 'Pictogram Y coordinate';
COMMENT ON COLUMN map_pictograms.type IS 'Pictogram type from PictogramType enum';
