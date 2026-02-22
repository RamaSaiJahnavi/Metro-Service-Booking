const AUTH_TOKEN_KEY = "metro_auth_token";
const AUTH_ROLE_KEY = "metro_auth_role";
const AUTH_USERNAME_KEY = "metro_auth_username";

export function getAuthToken() {
  return localStorage.getItem(AUTH_TOKEN_KEY) || "";
}

export function getAuthRole() {
  return (localStorage.getItem(AUTH_ROLE_KEY) || "").toUpperCase();
}

export function getAuthUsername() {
  return localStorage.getItem(AUTH_USERNAME_KEY) || "";
}

export function setAuthSession({ token, role, username }) {
  localStorage.setItem(AUTH_TOKEN_KEY, token || "");
  localStorage.setItem(AUTH_ROLE_KEY, (role || "").toUpperCase());
  localStorage.setItem(AUTH_USERNAME_KEY, username || "");
}

export function clearAuthSession() {
  localStorage.removeItem(AUTH_TOKEN_KEY);
  localStorage.removeItem(AUTH_ROLE_KEY);
  localStorage.removeItem(AUTH_USERNAME_KEY);
}

export function isLoggedIn() {
  return Boolean(getAuthToken());
}

export function isAdmin() {
  return getAuthRole() === "ADMIN";
}

export function isUser() {
  return getAuthRole() === "USER";
}
