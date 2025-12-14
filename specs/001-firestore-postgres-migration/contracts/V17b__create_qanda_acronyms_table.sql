-- QAndA Acronyms Table
-- Purpose: Acronyms and their definitions for Q&A content
-- Dependencies: qanda table
-- Cascade: CASCADE on Q&A deletion (acronyms belong to Q&A)

CREATE TABLE qanda_acronyms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    qanda_id UUID NOT NULL REFERENCES qanda(id) ON DELETE CASCADE,
    key VARCHAR(50) NOT NULL,
    value TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_qanda_acronyms_qanda_id ON qanda_acronyms(qanda_id);

COMMENT ON TABLE qanda_acronyms IS 'Acronyms and definitions for Q&A content';
COMMENT ON COLUMN qanda_acronyms.key IS 'Acronym abbreviation';
COMMENT ON COLUMN qanda_acronyms.value IS 'Full meaning/definition';
