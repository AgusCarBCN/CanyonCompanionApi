
-- ==============================================
-- SEQUENCES
-- ==============================================
CREATE SEQUENCE roles_seq START 1;
CREATE SEQUENCE users_seq START 1;
CREATE SEQUENCE descents_seq START 1;
CREATE SEQUENCE descent_images_seq START 1;
CREATE SEQUENCE refresh_seq START 1;
CREATE SEQUENCE maps_seq START 1 INCREMENT 1;

-- ==============================================
-- MAPS
-- ==============================================

CREATE TABLE maps (
                      id BIGINT PRIMARY KEY DEFAULT nextval('maps_seq'),
                      name VARCHAR(120) NOT NULL,
                      mbtiles_path VARCHAR(255) NOT NULL,
                      image_path VARCHAR(255) NOT NULL
);

-- ==============================================
-- ROLES
-- ==============================================
CREATE TABLE roles (
                       id BIGINT PRIMARY KEY DEFAULT nextval('roles_seq'),
                       role VARCHAR(30) NOT NULL UNIQUE
);

-- ==============================================
-- USERS
-- ==============================================
CREATE TABLE users (
                       id BIGINT PRIMARY KEY DEFAULT nextval('users_seq'),

                       username VARCHAR(50) NOT NULL,
                       surname VARCHAR(200) NOT NULL,

                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,

                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                       status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
                       status_description VARCHAR(255)
);

-- ==============================================
-- REFRESH TOKENS
-- ==============================================
CREATE TABLE refresh_tokens (
                                id BIGINT PRIMARY KEY DEFAULT nextval('refresh_seq'),

                                token VARCHAR(255) NOT NULL UNIQUE,
                                user_id BIGINT NOT NULL,
                                expiry_date TIMESTAMP NOT NULL,

                                CONSTRAINT fk_refresh_user
                                    FOREIGN KEY (user_id)
                                        REFERENCES users(id)
                                        ON DELETE CASCADE
);

-- ==============================================
-- USER - ROLES (MANY TO MANY)
-- ==============================================
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,

                            PRIMARY KEY (user_id, role_id),

                            CONSTRAINT fk_user_roles_user
                                FOREIGN KEY (user_id)
                                    REFERENCES users(id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_user_roles_role
                                FOREIGN KEY (role_id)
                                    REFERENCES roles(id)
                                    ON DELETE CASCADE
);

-- ==============================================
-- DESCENTS (USER OWNED)@PrimaryKey(autoGenerate = true)
--     @ColumnInfo("id")  val id: Long = 0,
--     @ColumnInfo("name") val name: String,
--     @ColumnInfo("location") val location: String,
--     @ColumnInfo("province") val province: String,
--     @ColumnInfo(name = "date") val date: String = Date().dateFormat(),
--     @ColumnInfo(name = "vertical_character") val verticalCharacter: String,
--     @ColumnInfo(name = "aquatic_character") val aquaticCharacter: String,
--     @ColumnInfo(name = "commitment") val commitment: String,
--     @ColumnInfo(name = "description_link") val descriptionLink: String,
--     @ColumnInfo(name = "comments") val comments: String


-- ==============================================
CREATE TABLE descents (
                          id BIGINT PRIMARY KEY DEFAULT nextval('descents_seq'),
                          user_id BIGINT NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          location VARCHAR(255) NOT NULL,
                          province VARCHAR(255) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          vertical_character VARCHAR(3),
                          aquatic_character VARCHAR(3),
                          commitment VARCHAR(3),
                          description_link VARCHAR(255),
                          comments TEXT,
                          CONSTRAINT fk_descents_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE
);

-- ==============================================
-- DESCENT IMAGES
-- ==============================================
CREATE TABLE descent_images (
                                id BIGINT PRIMARY KEY DEFAULT nextval('descent_images_seq'),

                                descent_id BIGINT NOT NULL,

                                image_url TEXT NOT NULL,

                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_descent_images_descent
                                    FOREIGN KEY (descent_id)
                                        REFERENCES descents(id)
                                        ON DELETE CASCADE
);

-- ==============================================
-- ROUTES (GPX TRACKS)
-- ==============================================
CREATE TABLE routes (
                        id BIGSERIAL PRIMARY KEY,

                        descent_id BIGINT NOT NULL,

                        name VARCHAR(255) NOT NULL,

                        resource_path TEXT NOT NULL,

                        description TEXT,

                        time BIGINT,
                        distance DOUBLE PRECISION,
                        ascent REAL,
                        descent REAL,

                        date TIMESTAMP DEFAULT NOW(),

                        CONSTRAINT fk_routes_descent
                            FOREIGN KEY (descent_id)
                                REFERENCES descents(id)
                                ON DELETE CASCADE
);

-- ==============================================
-- WAYPOINTS
-- ==============================================
CREATE TABLE waypoints (
                           id BIGSERIAL PRIMARY KEY,

                           route_id BIGINT NOT NULL,

                           name VARCHAR(255),
                           description TEXT,

                           latitude DOUBLE PRECISION NOT NULL,
                           longitude DOUBLE PRECISION NOT NULL,
                           elevation DOUBLE PRECISION,

                           icon INTEGER,
                           image_path TEXT,

                           time TIMESTAMP,

                           CONSTRAINT fk_waypoints_route
                               FOREIGN KEY (route_id)
                                   REFERENCES routes(id)
                                   ON DELETE CASCADE
);

-- ==============================================
-- INDICES (PERFORMANCE)
-- ==============================================

-- USERS
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_status ON users(status);

-- DESCENTS
CREATE INDEX idx_descents_user_id ON descents(user_id);
CREATE INDEX idx_descents_location ON descents(location);
CREATE INDEX idx_descents_name ON descents(name);

-- IMAGES
CREATE INDEX idx_descent_images_descent_id ON descent_images(descent_id);

-- ROUTES
CREATE INDEX idx_routes_descent_id ON routes(descent_id);

-- WAYPOINTS
CREATE INDEX idx_waypoints_route_id ON waypoints(route_id);
CREATE INDEX idx_waypoints_coords ON waypoints(latitude, longitude);