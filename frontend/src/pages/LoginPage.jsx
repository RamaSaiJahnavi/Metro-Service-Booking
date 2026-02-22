import { useState } from "react";
import { Link, Navigate, useNavigate } from "react-router-dom";
import { login } from "../api";
import { getAuthRole, isLoggedIn, setAuthSession } from "../auth";
import { LoginIcon, ShieldIcon, TicketIcon, UserIcon } from "../components/Icons";

export default function LoginPage() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("USER");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  if (isLoggedIn()) {
    return <Navigate to={getAuthRole() === "ADMIN" ? "/admin" : "/user"} replace />;
  }

  async function onSubmit(event) {
    event.preventDefault();
    setError("");
    try {
      setLoading(true);
      const data = await login({ username, password, role });
      setAuthSession({ token: data.token, role: data.role, username });
      navigate(data.role === "ADMIN" ? "/admin" : "/user", { replace: true });
    } catch (err) {
      setError(err.message || "Login failed");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="auth-page">
      <section className="hero auth-hero">
        <p className="hero-pill">Welcome</p>
        <h1>Metro Service Portal Login</h1>
        <p className="hero-subtitle">
          Sign in to continue with ticket booking, trip management, and real-time travel status tracking.
        </p>
        <div className="hero-badges">
          <span className="hero-badge">
            <TicketIcon className="icon icon-xs" />
            Fast Ticket Booking
          </span>
          <span className="hero-badge">
            <ShieldIcon className="icon icon-xs" />
            Protected Access
          </span>
          <span className="hero-badge">
            <UserIcon className="icon icon-xs" />
            Unified User Portal
          </span>
        </div>
      </section>

      <section className="panel auth-panel">
        <form className="auth-form" onSubmit={onSubmit}>
          <label>
            Username
            <input
              value={username}
              onChange={(event) => setUsername(event.target.value)}
              placeholder="Enter username"
              required
            />
          </label>
          <label>
            Password
            <input
              type="password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              placeholder="Enter password"
              required
            />
          </label>
          <label>
            Role
            <select value={role} onChange={(event) => setRole(event.target.value)} required>
              <option value="USER">USER</option>
              <option value="ADMIN">ADMIN</option>
            </select>
          </label>
          <button className="primary-btn" disabled={loading}>
            <LoginIcon className="icon icon-xs" />
            {loading ? "Logging In..." : "Login"}
          </button>
        </form>
        {error && <p className="error">{error}</p>}
        <p className="hint auth-switch">
          New to Metro Booking Service? <Link to="/signup">Create your account</Link>
        </p>
      </section>
    </div>
  );
}
