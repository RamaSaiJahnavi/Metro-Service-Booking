# Metro Booking Service (Easy-Level Implementation)

This project now has:
- `metro/` -> Spring Boot backend
- `frontend/` -> React frontend (User + Admin portal)

## What is implemented

1. Authentication (JWT)
- Signup API: `POST /api/auth/signup`
- Login API: `POST /api/auth/login`
- JWT-based security:
  - User/Admin must login for user APIs
  - Admin role required for admin APIs (`/api/admin/**`)
- Admin registration requires pass key from `application.properties`:
  - `app.admin.pass-key=MOVEINSYNC_ADMIN_2026`

2. User Portal
- Book metro from source to destination
- Shows computed path, transfers, and QR payload
- Shows user booking history with full details
- User can mark booking as `Travelled` / `Not Travelled`

3. Admin Portal
- Common login page: `/login`
- Signup page: `/signup`
- Admin dashboard: `/admin`
- Admin can manage:
  - Stops
  - Routes
  - Route stop mappings (path order)
- Admin can view all registered users
- Admin can view all bookings with user details
- Admin can see/update travelled status for each booking
- Any admin change refreshes graph cache dynamically (no restart needed)

4. Path Optimization
- Graph model:
  - Nodes = stops
  - Edges = consecutive stops in route
- Dijkstra-based shortest path
- Deterministic tie handling
- Handles:
  - same source/destination
  - stop not found
  - no path found

5. Monitoring
- Health API: `GET /api/health`
- Returns status, uptime, graph node/edge counts

6. Caching
- Metro graph is cached in memory (`GraphService`)
- Rebuilt automatically after admin updates

7. Error Handling
- Global exception handler with meaningful error responses
- Validation errors and auth errors handled with proper status codes

## Complexity (Easy explanation)

- Graph build: `O(V + E)` roughly based on total route-stop entries
- Path search (Dijkstra): `O((V + E) log V)` with priority queue
- Space:
  - Graph cache: `O(V + E)`
  - Path arrays/maps: `O(V)`

## Trade-offs made

- In-memory graph cache for speed (fast reads, periodic rebuild on admin writes)
- In-memory admin user for simplicity (easy setup, not multi-user production IAM)
- H2 in-memory DB for easy local run (not persistent across restarts)

## Failure handling (easy-level)

- Invalid/expired token -> blocked admin actions
- Invalid booking request -> structured error response
- No path -> booking not created
- Graph refresh on each admin mutation reduces stale routing risk

## Run Instructions

1. Start backend
```powershell
cd "metro"
.\mvnw.cmd spring-boot:run
```

2. Start frontend
```powershell
cd "frontend"
npm install
npm run dev
```

3. Open app
- Login page: `http://localhost:5173/login`
- Signup page: `http://localhost:5173/signup`
- After login:
  - USER -> redirected to `/user`
  - ADMIN -> redirected to `/admin`

## Important APIs

- Public:
  - `GET /api/stops`
  - `GET /api/routes`
  - `POST /api/bookings`
  - `GET /api/health`
- Auth:
  - `POST /api/auth/login`
  - `POST /api/auth/signup`
- Admin (JWT required):
  - `POST/PUT/DELETE /api/admin/stops`
  - `POST/PUT/DELETE /api/admin/routes`
  - `GET/POST/PUT/DELETE /api/admin/route-stops`
  - `GET /api/admin/users`
  - `GET /api/admin/bookings`
  - `PUT /api/admin/bookings/{id}/travelled`
- User (JWT required):
  - `GET /api/bookings/me`
  - `PUT /api/bookings/{id}/travelled`
- Search (JWT required):
  - `GET /api/search?q=...`
