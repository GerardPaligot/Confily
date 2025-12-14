-- V9__create_partners_table.sql
-- Phase 3: Extended - Partners/Sponsors

CREATE TABLE partners (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    logo_url TEXT,
    website_url TEXT,
    
    -- Media (logos in different formats)
    media_svg TEXT,
    media_png_250 TEXT,  -- 250px PNG
    media_png_500 TEXT,  -- 500px PNG
    media_png_1000 TEXT, -- 1000px PNG
    
    -- Video content
    video_url TEXT,
    
    -- Address
    address_id UUID REFERENCES addresses(id) ON DELETE SET NULL,
    
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_partners_event_id ON partners(event_id);
CREATE INDEX idx_partners_address_id ON partners(address_id);

-- Auto-update trigger
CREATE TRIGGER update_partners_updated_at
    BEFORE UPDATE ON partners
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE partners IS 'Event sponsors and partners with detailed media and contact information';
COMMENT ON COLUMN partners.media_svg IS 'SVG logo for scalable rendering';
