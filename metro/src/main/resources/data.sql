-- ======================
-- INSERT STOPS
-- ======================
INSERT INTO stop (name) VALUES ('Vijayawada');
INSERT INTO stop (name) VALUES ('Guntur');
INSERT INTO stop (name) VALUES ('Tenali');
INSERT INTO stop (name) VALUES ('Mangalagiri');
INSERT INTO stop (name) VALUES ('Amaravati');

-- ======================
-- INSERT ROUTES
-- ======================
INSERT INTO route (color) VALUES ('Yellow');
INSERT INTO route (color) VALUES ('Blue');

-- ======================
-- INSERT ROUTE_STOPS
-- ======================

-- Yellow Line (Route ID = 1)
INSERT INTO route_stop (route_id, stop_id, stop_order) VALUES (1, 1, 1);
INSERT INTO route_stop (route_id, stop_id, stop_order) VALUES (1, 2, 2);
INSERT INTO route_stop (route_id, stop_id, stop_order) VALUES (1, 3, 3);

-- Blue Line (Route ID = 2)
INSERT INTO route_stop (route_id, stop_id, stop_order) VALUES (2, 3, 1);
INSERT INTO route_stop (route_id, stop_id, stop_order) VALUES (2, 4, 2);
INSERT INTO route_stop (route_id, stop_id, stop_order) VALUES (2, 5, 3);
