DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS route_stop;
DROP TABLE IF EXISTS route;
DROP TABLE IF EXISTS stop;
DROP TABLE IF EXISTS app_user;

-- ======================
-- STOP TABLE
-- ======================
CREATE TABLE stop (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(100) NOT NULL
);

-- ======================
-- ROUTE TABLE
-- ======================
CREATE TABLE route (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       color VARCHAR(50) NOT NULL
);

-- ======================
-- ROUTE_STOP TABLE
-- ======================
CREATE TABLE route_stop (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            route_id BIGINT NOT NULL,
                            stop_id BIGINT NOT NULL,
                            stop_order INT NOT NULL,
                            FOREIGN KEY (route_id) REFERENCES route(id),
                            FOREIGN KEY (stop_id) REFERENCES stop(id)
);

-- ======================
-- APP USER TABLE
-- ======================
CREATE TABLE app_user (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          username VARCHAR(100) NOT NULL UNIQUE,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          role VARCHAR(20) NOT NULL
);

-- ======================
-- BOOKING TABLE
-- ======================
CREATE TABLE booking (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         source_id BIGINT NOT NULL,
                         destination_id BIGINT NOT NULL,
                         user_id BIGINT NOT NULL,
                         path_json CLOB,
                         qr_string VARCHAR(255),
                         travelled BOOLEAN NOT NULL DEFAULT FALSE,
                         created_at TIMESTAMP,
                         FOREIGN KEY (source_id) REFERENCES stop(id),
                         FOREIGN KEY (destination_id) REFERENCES stop(id),
                         FOREIGN KEY (user_id) REFERENCES app_user(id)
);
