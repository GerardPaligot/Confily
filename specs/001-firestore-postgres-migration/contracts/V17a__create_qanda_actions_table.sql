-- QAndA Actions Table
-- Purpose: Action links/buttons for Q&A responses
-- Dependencies: qanda table
-- Cascade: CASCADE on Q&A deletion (actions belong to Q&A)

CREATE TABLE qanda_actions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    qanda_id UUID NOT NULL REFERENCES qanda(id) ON DELETE CASCADE,
    display_order INT NOT NULL DEFAULT 0,
    label VARCHAR(255) NOT NULL,
    url TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_qanda_actions_qanda_id ON qanda_actions(qanda_id);

COMMENT ON TABLE qanda_actions IS 'Action links/buttons for Q&A responses';
COMMENT ON COLUMN qanda_actions.label IS 'Button/link label text';
COMMENT ON COLUMN qanda_actions.url IS 'Action URL/link';
COMMENT ON COLUMN qanda_actions.display_order IS 'UI ordering for action buttons';
