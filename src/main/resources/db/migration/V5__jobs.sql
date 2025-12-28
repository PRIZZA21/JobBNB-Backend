CREATE TABLE jobs (
  id BIGSERIAL PRIMARY KEY,

  employer_id BIGINT NOT NULL
    REFERENCES users(id),

  title VARCHAR(150) NOT NULL,
  description TEXT NOT NULL,

  location VARCHAR(100),
  employment_type VARCHAR(30), -- FULL_TIME, PART_TIME, CONTRACT

  min_salary INTEGER,
  max_salary INTEGER,

  is_active BOOLEAN DEFAULT true,

  created_at TIMESTAMP DEFAULT now(),
  updated_at TIMESTAMP DEFAULT now()
);

CREATE INDEX idx_jobs_employer ON jobs(employer_id);
CREATE INDEX idx_jobs_location ON jobs(location);
