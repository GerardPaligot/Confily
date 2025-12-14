-- QAndA Table
-- Purpose: Frequently asked questions for events
-- Dependencies: events table
-- Cascade: RESTRICT on event deletion (explicit cleanup required)

CREATE TABLE qanda (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    display_order INT NOT NULL DEFAULT 0,
    language VARCHAR(10) NOT NULL,
    question TEXT NOT NULL,
    response TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_qanda_event_id ON qanda(event_id);
CREATE INDEX idx_qanda_language ON qanda(language);
CREATE INDEX idx_qanda_display_order ON qanda(event_id, display_order);

COMMENT ON TABLE qanda IS 'Frequently asked questions for events';
COMMENT ON COLUMN qanda.display_order IS 'UI ordering for Q&A display';
COMMENT ON COLUMN qanda.language IS 'Language code for question/answer';
