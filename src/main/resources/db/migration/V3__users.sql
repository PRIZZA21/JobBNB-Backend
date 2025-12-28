CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,

  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,

  password_hash TEXT NOT NULL,

  role_id BIGINT NOT NULL REFERENCES roles(id),


  is_active BOOLEAN DEFAULT true,

  created_at TIMESTAMP DEFAULT now()
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role_id);
