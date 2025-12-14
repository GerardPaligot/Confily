-- V12__create_jobs_table.sql
-- Phase 3: Extended - Job Listings (Always Linked to Partners)

CREATE TABLE jobs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    partner_id UUID NOT NULL REFERENCES partners(id) ON DELETE CASCADE,
    url TEXT NOT NULL,
    title VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    
    -- Salary information
    salary_min INT,
    salary_max INT,
    salary_recurrence VARCHAR(50),  -- yearly, monthly, hourly, etc.
    
    requirements DECIMAL(3, 2),  -- 0.0 to 9.99 (years of experience)
    propulsed VARCHAR(255),  -- Platform/service powering the job listing
    publish_date DATE,
    
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_jobs_partner_id ON jobs(partner_id);
CREATE INDEX idx_jobs_location ON jobs(location);
CREATE INDEX idx_jobs_publish_date ON jobs(publish_date);

-- Auto-update trigger
CREATE TRIGGER update_jobs_updated_at
    BEFORE UPDATE ON jobs
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE jobs IS 'Job listings always linked to event partners/sponsors';
COMMENT ON COLUMN jobs.partner_id IS 'Required link to partner offering the job (company name from partners.name)';
COMMENT ON COLUMN jobs.requirements IS 'Years of experience or skill level requirement';
COMMENT ON COLUMN jobs.propulsed IS 'External job platform/service (e.g., LinkedIn, WelcomeToTheJungle)';
COMMENT ON COLUMN jobs.salary_recurrence IS 'Salary payment frequency (yearly, monthly, hourly, daily, weekly)';
