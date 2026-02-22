# Metro Frontend (React + Vite)

Professional booking UI for the Metro Spring Boot backend.

## Prerequisites

- Node.js 18+ and npm
- Backend running on `http://localhost:8081` (default)

## Setup

```bash
cd frontend
npm install
```

Create env file:

```bash
cp .env.example .env
```

Update `.env` only if backend URL is different.

## Run

```bash
npm run dev
```

App runs on `http://localhost:5173`.

## Auth Flow

- First page: `/login`
- If not registered: `/signup`
- Signup fields:
  - `username`
  - `password`
  - `email`
  - `role` (`USER` or `ADMIN`)
  - `adminPassKey` (required only for `ADMIN`)
- After login:
  - `USER` -> redirected to `/user`
  - `ADMIN` -> redirected to `/admin`

## Build

```bash
npm run build
npm run preview
```
