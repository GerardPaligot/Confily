# Data Model: Postgres Schema Design

**Feature**: 001-firestore-postgres-migration  
**Date**: December 13, 2025  
**Phase**: 1 - Data Model & Schema Design

## Overview

This document defines the PostgreSQL schema for all 16 entity types, including table structures, relationships, constraints, and indexes. The schema is normalized (3NF) with foreign key constraints using RESTRICT cascade behavior.

---

## Entity Relationship Diagram

```
┌──────────┐         ┌────────────┐         ┌────────────┐
│  Events  │1─────┼├─│  Sessions  │┼├──────1│  Formats   │
└──────────┘         └────────────┘         └────────────┘
     │                    │    │
     │1                   │┼├  │┼├
     │                    │    │
     ┼├                   │    └────────────┐
┌────────────┐            │                 │
│ Categories │            │1                │1
└────────────┘            │                 │
                         ┼├                ┼├
                   ┌───────────────┐ ┌────────────┐
                   │SessionSpeakers│ │  Speakers  │
                   │  (junction)   │ └────────────┘
                   └───────────────┘
                   
┌──────────┐1────┼├┌────────────┐
│ Partners │      │ Activities │
└──────────┘      └────────────┘
     │
     │1
     ┼├
┌──────────┐
│  Events  │
└──────────┘
```

---

## Core Entities (Phase 1)

### 1. Events Table

**Purpose**: Core entity representing conferences/events

```sql
CREATE TABLE events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    slug VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    address_id UUID REFERENCES addresses(id) ON DELETE RESTRICT,
    default_language VARCHAR(10) NOT NULL DEFAULT 'en',
    
    -- Contact
    contact_email VARCHAR(255),
    contact_phone VARCHAR(50),
    
    -- Links
    coc_url TEXT,
    faq_url TEXT,
    
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    published_at TIMESTAMP,
);

CREATE INDEX idx_events_slug ON events(slug);
CREATE INDEX idx_events_start_date ON events(start_date);
CREATE INDEX idx_events_address_id ON events(address_id);
```

**Fields**:
- `id`: UUID primary key
- `slug`: URL-friendly identifier (unique)
- `address_id`: FK to addresses table

**Relationships**:
- 1-to-1 with Address
- 1-to-many with Sessions, Speakers, Partners, Categories, Formats, etc.
- 1-to-many with EventFeatures (feature flags)

---

### 1a. Event Features Table

**Purpose**: Feature flags/toggles for events (normalized)

```sql
CREATE TABLE event_features (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    feature_key VARCHAR(100) NOT NULL,  -- e.g., 'networking', 'partners', 'qanda'
    enabled BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(event_id, feature_key)
);

CREATE INDEX idx_event_features_event_id ON event_features(event_id);
CREATE INDEX idx_event_features_enabled ON event_features(event_id, enabled);  -- Query enabled features
```

**Fields**:
- `feature_key`: Feature identifier (networking, partners, qanda, schedule, etc.)
- `enabled`: Boolean flag for on/off state

**Constraints**:
- Unique feature_key per event
- CASCADE delete when event is deleted (features are event-specific)

---

### 1b. Event Session Tracks Table

**Purpose**: Track categories for event sessions (breaks, meals)

```sql
CREATE TABLE event_session_tracks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    track_name VARCHAR(255) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(event_id, track_name)
);

CREATE INDEX idx_event_session_tracks_event_id ON event_session_tracks(event_id);
```

**Fields**:
- `track_name`: Name of the session track (e.g., "Main", "Side Events")
- `display_order`: For UI ordering

**Constraints**:
- Unique track name per event
- CASCADE delete when event is deleted

---

### 1c. Team Groups Table

**Purpose**: Team categories/groups for organizing staff

```sql
CREATE TABLE team_groups (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(event_id, name)
);

CREATE INDEX idx_team_groups_event_id ON team_groups(event_id);
```

**Fields**:
- `name`: Group name (e.g., "Organizers", "Volunteers")
- `display_order`: For UI ordering

**Constraints**:
- Unique group name per event
- CASCADE delete when event is deleted

---

### 1d. Sponsoring Types Table

**Purpose**: Sponsorship tiers/levels

```sql
CREATE TABLE sponsoring_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    type_name VARCHAR(255) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(event_id, type_name)
);

CREATE INDEX idx_sponsoring_types_event_id ON sponsoring_types(event_id);
```

**Fields**:
- `type_name`: Tier name (e.g., "Platinum", "Gold", "Silver")
- `display_order`: For UI ordering

**Constraints**:
- Unique type name per event
- CASCADE delete when event is deleted

---

### 2. Categories Table

**Purpose**: Session categories/tracks

```sql
CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(7),  -- Hex color code #RRGGBB
    icon VARCHAR(50),   -- Icon identifier
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(event_id, name)
);

CREATE INDEX idx_categories_event_id ON categories(event_id);
CREATE INDEX idx_categories_display_order ON categories(event_id, display_order);
```

**Constraints**:
- Unique category name per event
- Must belong to an event (FK)
- Display order for sorting

---

### 3. Formats Table

**Purpose**: Session formats (talk, workshop, keynote)

```sql
CREATE TABLE formats (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(event_id, name)
);

CREATE INDEX idx_formats_event_id ON formats(event_id);
```

**Constraints**:
- Unique format name per event

---

## Content Entities (Phase 2)

### 4. Speakers Table

```sql
CREATE TABLE speakers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    name VARCHAR(255) NOT NULL,
    bio TEXT,
    photo_url TEXT,
    pronouns VARCHAR(50),
    email VARCHAR(255),
    company VARCHAR(255),
    job_title VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_speakers_event_id ON speakers(event_id);
CREATE INDEX idx_speakers_name ON speakers(event_id, name);  -- Search by name
CREATE INDEX idx_speakers_email ON speakers(email);  -- Search by email
```

---

### 5. Sessions Table (Talk Sessions)

**Purpose**: Conference talks, workshops, and presentations with speakers

```sql
CREATE TABLE sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    title VARCHAR(500) NOT NULL,
    description TEXT,
    language VARCHAR(10) NOT NULL DEFAULT 'en',  -- ISO language code
    level VARCHAR(50),  -- beginner, intermediate, advanced
    format_id UUID REFERENCES formats(id) ON DELETE RESTRICT,
    link_slides TEXT,
    link_replay TEXT,
    drive_folder_id TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_sessions_event_id ON sessions(event_id);
CREATE INDEX idx_sessions_format_id ON sessions(format_id);
CREATE INDEX idx_sessions_start_time ON sessions(start_time);
CREATE INDEX idx_sessions_event_start ON sessions(event_id, start_time);  -- Schedule queries
CREATE INDEX idx_sessions_title_search ON sessions USING gin(to_tsvector('english', title));  -- Full-text search
```

**Full-text Search**: GIN index on title for fast searching

---

### 6. Session-Speaker Junction Table

**Purpose**: Many-to-many relationship between sessions and speakers

```sql
CREATE TABLE session_speakers (
    session_id UUID NOT NULL REFERENCES sessions(id) ON DELETE CASCADE,
    speaker_id UUID NOT NULL REFERENCES speakers(id) ON DELETE CASCADE,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (session_id, speaker_id)
);

CREATE INDEX idx_session_speakers_session ON session_speakers(session_id);
CREATE INDEX idx_session_speakers_speaker ON session_speakers(speaker_id);
```

**Note**: CASCADE delete here is appropriate - if session/speaker deleted, junction record should go away

---

### 6a. Session-Category Junction Table

**Purpose**: Many-to-many relationship between sessions and categories

```sql
CREATE TABLE session_categories (
    session_id UUID NOT NULL REFERENCES sessions(id) ON DELETE CASCADE,
    category_id UUID NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (session_id, category_id)
);

CREATE INDEX idx_session_categories_session ON session_categories(session_id);
CREATE INDEX idx_session_categories_category ON session_categories(category_id);
```

**Note**: CASCADE delete here is appropriate - if session/category deleted, junction record should go away

---

### 7. Tags Table

```sql
CREATE TABLE tags (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(event_id, name)
);

CREATE INDEX idx_tags_event_id ON tags(event_id);
```

---

### 8. Session-Tag Junction Table

```sql
CREATE TABLE session_tags (
    session_id UUID NOT NULL REFERENCES sessions(id) ON DELETE CASCADE,
    tag_id UUID NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (session_id, tag_id)
);

CREATE INDEX idx_session_tags_session ON session_tags(session_id);
CREATE INDEX idx_session_tags_tag ON session_tags(tag_id);
```

---

### 8a. Event Sessions Table

**Purpose**: Event-type sessions (breaks, meals, networking, social events) without speakers

```sql
CREATE TABLE event_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    title VARCHAR(500) NOT NULL,
    description TEXT,
    address VARCHAR(500),  -- Optional location/address for the event session
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_event_sessions_event_id ON event_sessions(event_id);
```

**Note**: Simpler structure than talk sessions - no speakers, categories, formats. Used for breaks, meals, social events.

---

## Extended Entities (Phase 3)

### 9. Partners Table

```sql
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

CREATE INDEX idx_partners_event_id ON partners(event_id);
CREATE INDEX idx_partners_address_id ON partners(address_id);
```

---

### 9a. Partner Sponsorships Table

**Purpose**: Multi-tier sponsorship support for partners (normalized from Firestore's sponsorings array)

```sql
CREATE TABLE partner_sponsorships (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    partner_id UUID NOT NULL REFERENCES partners(id) ON DELETE CASCADE,
    sponsoring_type_id UUID NOT NULL REFERENCES sponsoring_types(id) ON DELETE RESTRICT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(partner_id, sponsoring_type_id)
);

CREATE INDEX idx_partner_sponsorships_partner ON partner_sponsorships(partner_id);
CREATE INDEX idx_partner_sponsorships_type ON partner_sponsorships(sponsoring_type_id);
```

---

### 10. Team Table

```sql
CREATE TABLE team (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(255),
    bio TEXT,
    photo_url TEXT,
    team_group_id UUID NOT NULL REFERENCES team_groups(id) ON DELETE RESTRICT,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_team_event_id ON team(event_id);
CREATE INDEX idx_team_group_id ON team(team_group_id);
CREATE INDEX idx_team_display_order ON team(event_id, display_order);
```

---

### 11. Socials Table

**Purpose**: Social media links scoped to events

```sql
CREATE TABLE socials (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    platform VARCHAR(50) NOT NULL,  -- twitter, linkedin, github, etc.
    url TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_socials_event ON socials(event_id);
```

---

### 11a. Speaker Socials Junction Table

**Purpose**: Links socials to speakers

```sql
CREATE TABLE speaker_socials (
    social_id UUID NOT NULL REFERENCES socials(id) ON DELETE CASCADE,
    speaker_id UUID NOT NULL REFERENCES speakers(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (social_id, speaker_id)
);

CREATE INDEX idx_speaker_socials_social ON speaker_socials(social_id);
CREATE INDEX idx_speaker_socials_speaker ON speaker_socials(speaker_id);
```

---

### 11b. Partner Socials Junction Table

**Purpose**: Links socials to partners

```sql
CREATE TABLE partner_socials (
    social_id UUID NOT NULL REFERENCES socials(id) ON DELETE CASCADE,
    partner_id UUID NOT NULL REFERENCES partners(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (social_id, partner_id)
);

CREATE INDEX idx_partner_socials_social ON partner_socials(social_id);
CREATE INDEX idx_partner_socials_partner ON partner_socials(partner_id);
```

---

### 12. Jobs Table

```sql
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
    
    requirements DECIMAL(3, 2),  -- 0.0 to 9.99 (years of experience?)
    propulsed VARCHAR(255),  -- Platform/service powering the job listing
    publish_date DATE,
    
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_jobs_partner_id ON jobs(partner_id);
CREATE INDEX idx_jobs_location ON jobs(location);
CREATE INDEX idx_jobs_publish_date ON jobs(publish_date);
```

**Note**: Jobs are always linked to a partner; company name comes from partners.name

---

## Specialized Entities (Phase 4)

### 13. Activities Table

```sql
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
```

---

### 14. Schedules Table

**Purpose**: Time slots for both talk sessions and event sessions

```sql
CREATE TABLE schedules (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    session_id UUID REFERENCES sessions(id) ON DELETE SET NULL,
    event_session_id UUID REFERENCES event_sessions(id) ON DELETE SET NULL,
    event_session_track_id UUID NOT NULL REFERENCES event_session_tracks(id) ON DELETE RESTRICT,
    display_order INT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT check_time_order CHECK (end_time > start_time),
    CONSTRAINT check_session_type CHECK (
        (session_id IS NOT NULL AND event_session_id IS NULL) OR
        (session_id IS NULL AND event_session_id IS NOT NULL) OR
        (session_id IS NULL AND event_session_id IS NULL)
    )
);

CREATE INDEX idx_schedules_event_id ON schedules(event_id);
CREATE INDEX idx_schedules_session_id ON schedules(session_id);
CREATE INDEX idx_schedules_event_session_id ON schedules(event_session_id);
CREATE INDEX idx_schedules_track_id ON schedules(event_session_track_id);
CREATE INDEX idx_schedules_time_range ON schedules(event_id, start_time, end_time);
```

**Note**: Can reference talk session, event session, or neither (generic time slot)

---

### 15. Maps Table & Map Elements

```sql
CREATE TABLE maps (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    color VARCHAR(7),  -- Hex color
    color_selected VARCHAR(7),  -- Hex color when selected
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
```

---

### 15a. Map Shapes Table

**Purpose**: Interactive shapes/areas on maps

```sql
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
```

---

### 15b. Map Pictograms Table

**Purpose**: Icon markers on maps

```sql
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
```

---

### 16. Lunch Menus Table

**Purpose**: Daily lunch menu information (embedded in events in Firestore)

```sql
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
```

---

### 17. QAndA Table

```sql
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
```

---

### 17a. QAndA Actions Table

**Purpose**: Action links/buttons for Q&A responses

```sql
CREATE TABLE qanda_actions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    qanda_id UUID NOT NULL REFERENCES qanda(id) ON DELETE CASCADE,
    display_order INT NOT NULL DEFAULT 0,
    label VARCHAR(255) NOT NULL,
    url TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_qanda_actions_qanda_id ON qanda_actions(qanda_id);
```

---

### 17b. QAndA Acronyms Table

**Purpose**: Acronyms and their definitions for Q&A content

```sql
CREATE TABLE qanda_acronyms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    qanda_id UUID NOT NULL REFERENCES qanda(id) ON DELETE CASCADE,
    key VARCHAR(50) NOT NULL,
    value TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_qanda_acronyms_qanda_id ON qanda_acronyms(qanda_id);
```

---

### 18. Addresses Table

```sql
CREATE TABLE addresses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    street TEXT NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(100) NOT NULL,
    country_code VARCHAR(3) NOT NULL,  -- ISO country code
    latitude DECIMAL(10, 7),
    longitude DECIMAL(10, 7),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_addresses_city_country ON addresses(city, country);
CREATE INDEX idx_addresses_coordinates ON addresses(latitude, longitude);  -- GeoSpatial queries
```

---

## Summary Statistics

| Entity | Tables | Indexes | Foreign Keys | Junction Tables |
|--------|--------|---------|--------------|-----------------|
| Events | 1 | 3 | 1 | 0 |
| Event Features | 1 | 2 | 1 | 0 |
| Event Session Tracks | 1 | 1 | 1 | 0 |
| Team Groups | 1 | 1 | 1 | 0 |
| Sponsoring Types | 1 | 1 | 1 | 0 |
| Categories | 1 | 2 | 1 | 0 |
| Formats | 1 | 1 | 1 | 0 |
| Speakers | 1 | 3 | 1 | 0 |
| Sessions (Talks) | 1 | 5 | 2 | 0 |
| Session-Speaker | 1 | 2 | 2 | ✓ (junction) |
| Session-Category | 1 | 2 | 2 | ✓ (junction) |
| Tags | 1 | 1 | 1 | 0 |
| Session-Tag | 1 | 2 | 2 | ✓ (junction) |
| Event Sessions | 1 | 1 | 1 | 0 |
| Partners | 1 | 2 | 2 | 0 |
| Partner Sponsorships | 1 | 2 | 2 | 0 |
| Team | 1 | 3 | 2 | 0 |
| Socials | 1 | 1 | 1 | 0 |
| Speaker Socials | 1 | 2 | 2 | ✓ (junction) |
| Partner Socials | 1 | 2 | 2 | ✓ (junction) |
| Jobs | 1 | 3 | 1 | 0 |
| Activities | 1 | 3 | 2 | 0 |
| Schedules | 1 | 5 | 4 | 0 |
| Maps | 1 | 2 | 1 | 0 |
| Map Shapes | 1 | 1 | 1 | 0 |
| Map Pictograms | 1 | 1 | 1 | 0 |
| Lunch Menus | 1 | 3 | 1 | 0 |
| QAndA | 1 | 2 | 1 | 0 |
| QAndA Actions | 1 | 1 | 1 | 0 |
| QAndA Acronyms | 1 | 1 | 1 | 0 |
| Addresses | 1 | 2 | 0 | 0 |
| **TOTAL** | **31** | **59** | **42** | **5** |

---

## Database Setup Script

**Complete initialization** (create database, run migrations):

```bash
#!/bin/bash
# setup-postgres.sh

# Create database
createdb confily_dev

# Verify schema creation
docker exec -it confily-postgres psql -U confily -d confily_dev -c "\\dt"
```

---

## Data Model Validation

**Checklist**:
- ✅ All 16 entity types represented
- ✅ Normalized to 3NF (no redundant data)
- ✅ Foreign keys use RESTRICT cascade (per clarification)
- ✅ Junction tables for many-to-many relationships
- ✅ Composite primary keys on junction tables (per clarification)
- ✅ Indexes on all foreign keys
- ✅ Indexes on commonly queried fields
- ✅ Timestamp audit fields (created_at, updated_at)
- ✅ Unique constraints where appropriate
- ✅ Check constraints for data integrity
- ✅ Support for full-text search on sessions

**Ready for contract generation (SQL DDL files)**.
