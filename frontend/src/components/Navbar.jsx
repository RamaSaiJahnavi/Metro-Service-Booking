import { NavLink, useNavigate } from "react-router-dom";
import { clearAuthSession, getAuthRole, getAuthUsername, isLoggedIn } from "../auth";
import {
  LoginIcon,
  LogoutIcon,
  SearchIcon,
  ShieldIcon,
  SignupIcon,
  TrainIcon,
  UserIcon
} from "./Icons";

export default function Navbar() {
  const navigate = useNavigate();
  const loggedIn = isLoggedIn();
  const role = getAuthRole();
  const username = getAuthUsername();

  function onLogout() {
    clearAuthSession();
    navigate("/login");
  }

  return (
      <header className="top-nav-wrap">
        <nav className="top-nav">
          <div className="brand">
          <span className="brand-dot">
            <TrainIcon className="icon icon-sm" />
          </span>
            <span>Metro Booking Service</span>
          </div>

          <div className="nav-links">
            {!loggedIn && (
                <>
                  <NavLink to="/login" className={({ isActive }) => navClassName(isActive)}>
                    <LoginIcon className="icon icon-xs" />
                    Login
                  </NavLink>
                  <NavLink to="/signup" className={({ isActive }) => navClassName(isActive)}>
                    <SignupIcon className="icon icon-xs" />
                    Sign Up
                  </NavLink>
                </>
            )}

            {loggedIn && role === "USER" && (
                <>
                  <NavLink to="/user" className={({ isActive }) => navClassName(isActive)}>
                    <UserIcon className="icon icon-xs" />
                    User Portal
                  </NavLink>
                  <NavLink to="/search" className={({ isActive }) => navClassName(isActive)}>
                    <SearchIcon className="icon icon-xs" />
                    Search
                  </NavLink>
                </>
            )}

            {loggedIn && role === "ADMIN" && (
                <>
                  <NavLink to="/admin" className={({ isActive }) => navClassName(isActive)}>
                    <ShieldIcon className="icon icon-xs" />
                    Admin Portal
                  </NavLink>
                  <NavLink to="/search" className={({ isActive }) => navClassName(isActive)}>
                    <SearchIcon className="icon icon-xs" />
                    Search
                  </NavLink>
                </>
            )}

            {loggedIn && (
                <span className="nav-identity">
              {username} ({role})
            </span>
            )}

            {loggedIn && (
                <button className="nav-logout" onClick={onLogout}>
                  <LogoutIcon className="icon icon-xs" />
                  Logout
                </button>
            )}
          </div>
        </nav>
      </header>
  );
}

function navClassName(isActive) {
  return isActive ? "nav-link active" : "nav-link";
}
