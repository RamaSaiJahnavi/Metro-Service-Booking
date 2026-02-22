import { clearAuthSession, getAuthToken } from "./auth";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL?.trim() || "http://localhost:8081";

async function parseApiResponse(response) {
  const contentType = response.headers.get("content-type") || "";
  const isJson = contentType.includes("application/json");
  const body = isJson ? await response.json() : await response.text();

  if (response.ok) {
    return body;
  }

  if (response.status === 401 || response.status === 403) {
    clearAuthSession();
  }

  if (typeof body === "string") {
    throw new Error(body || "Request failed");
  }

  const message =
    body?.message ||
    body?.details ||
    (body?.errors && Object.values(body.errors).join(", ")) ||
    "Request failed";

  throw new Error(message);
}

function authHeaders() {
  const token = getAuthToken();
  return {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`
  };
}

export async function login(payload) {
  const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
  return parseApiResponse(response);
}

export async function signup(payload) {
  const response = await fetch(`${API_BASE_URL}/api/auth/signup`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
  return parseApiResponse(response);
}

export async function fetchStops() {
  const response = await fetch(`${API_BASE_URL}/api/stops`, {
    headers: authHeaders()
  });
  return parseApiResponse(response);
}

export async function fetchRoutes() {
  const response = await fetch(`${API_BASE_URL}/api/routes`, {
    headers: authHeaders()
  });
  return parseApiResponse(response);
}

export async function createBooking(payload) {
  const response = await fetch(`${API_BASE_URL}/api/bookings`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify(payload)
  });
  return parseApiResponse(response);
}

export async function fetchMyBookings() {
  const response = await fetch(`${API_BASE_URL}/api/bookings/me`, {
    headers: authHeaders()
  });
  return parseApiResponse(response);
}

export async function updateMyBookingTravelStatus(bookingId, travelled) {
  const response = await fetch(`${API_BASE_URL}/api/bookings/${bookingId}/travelled`, {
    method: "PUT",
    headers: authHeaders(),
    body: JSON.stringify({ travelled })
  });
  return parseApiResponse(response);
}

export async function fetchAdminRouteStops() {
  const response = await fetch(`${API_BASE_URL}/api/admin/route-stops`, {
    headers: authHeaders()
  });
  return parseApiResponse(response);
}

export async function createAdminStop(payload) {
  const response = await fetch(`${API_BASE_URL}/api/admin/stops`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify(payload)
  });
  return parseApiResponse(response);
}

export async function deleteAdminStop(stopId) {
  const response = await fetch(`${API_BASE_URL}/api/admin/stops/${stopId}`, {
    method: "DELETE",
    headers: authHeaders()
  });
  return parseApiResponse(response);
}

export async function createAdminRoute(payload) {
  const response = await fetch(`${API_BASE_URL}/api/admin/routes`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify(payload)
  });
  return parseApiResponse(response);
}

export async function deleteAdminRoute(routeId) {
  const response = await fetch(`${API_BASE_URL}/api/admin/routes/${routeId}`, {
    method: "DELETE",
    headers: authHeaders()
  });
  return parseApiResponse(response);
}

export async function createAdminRouteStop(payload) {
  const response = await fetch(`${API_BASE_URL}/api/admin/route-stops`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify(payload)
  });
  return parseApiResponse(response);
}

export async function deleteAdminRouteStop(mappingId) {
  const response = await fetch(`${API_BASE_URL}/api/admin/route-stops/${mappingId}`, {
    method: "DELETE",
    headers: authHeaders()
  });
  return parseApiResponse(response);
}

export async function fetchAdminUsers() {
  const response = await fetch(`${API_BASE_URL}/api/admin/users`, {
    headers: authHeaders()
  });
  return parseApiResponse(response);
}

export async function fetchAdminBookings() {
  const response = await fetch(`${API_BASE_URL}/api/admin/bookings`, {
    headers: authHeaders()
  });
  return parseApiResponse(response);
}

export async function updateAdminBookingTravelStatus(bookingId, travelled) {
  const response = await fetch(`${API_BASE_URL}/api/admin/bookings/${bookingId}/travelled`, {
    method: "PUT",
    headers: authHeaders(),
    body: JSON.stringify({ travelled })
  });
  return parseApiResponse(response);
}

export async function searchEverything(query) {
  const response = await fetch(
    `${API_BASE_URL}/api/search?q=${encodeURIComponent(query ?? "")}`,
    {
      headers: authHeaders()
    }
  );
  return parseApiResponse(response);
}
