import { useMemo, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { signup } from "../api";
import { ShieldIcon, SignupIcon, UserIcon } from "../components/Icons";

export default function SignupPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    username: "",
    password: "",
    email: "",
    role: "USER",
    adminPassKey: ""
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const isAdminRole = useMemo(() => form.role === "ADMIN", [form.role]);

  function updateField(key, value) {
    setForm((prev) => ({ ...prev, [key]: value }));
  }

  async function onSubmit(event) {
    event.preventDefault();
    setError("");
    setSuccess("");
    try {
      setLoading(true);
      await signup(form);
      setSuccess("Signup successful. Redirecting to login...");
      setTimeout(() => navigate("/login", { replace: true }), 900);
    } catch (err) {
      setError(err.message || "Signup failed");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="auth-page">
      <section className="hero auth-hero">
        <p className="hero-pill">Registration</p>
        <h1>Create Your Metro Account</h1>
        <p className="hero-subtitle">
          Create your account to manage bookings, monitor trip activity, and access your authorized portal securely.
        </p>
        <div className="hero-badges">
          <span className="hero-badge">
            <UserIcon className="icon icon-xs" />
            Personal Access
          </span>
          <span className="hero-badge">
            <ShieldIcon className="icon icon-xs" />
            Admin Verification
          </span>
        </div>
      </section>

      <section className="panel auth-panel">
        <form className="auth-form" onSubmit={onSubmit}>
          <label>
            Username
            <input
              value={form.username}
              onChange={(event) => updateField("username", event.target.value)}
              required
            />
          </label>
          <label>
            Password
            <input
              type="password"
              value={form.password}
              onChange={(event) => updateField("password", event.target.value)}
              required
            />
          </label>
          <label>
            Email
            <input
              type="email"
              value={form.email}
              onChange={(event) => updateField("email", event.target.value)}
              required
            />
          </label>
          <label>
            Role
            <select
              value={form.role}
              onChange={(event) => updateField("role", event.target.value)}
              required
            >
              <option value="USER">USER</option>
              <option value="ADMIN">ADMIN</option>
            </select>
          </label>
          {isAdminRole && (
            <label>
              Admin Pass Key
              <input
                type="password"
                value={form.adminPassKey}
                onChange={(event) => updateField("adminPassKey", event.target.value)}
                required
              />
            </label>
          )}
          <button className="primary-btn" disabled={loading}>
            <SignupIcon className="icon icon-xs" />
            {loading ? "Creating Account..." : "Sign Up"}
          </button>
        </form>

        {error && <p className="error">{error}</p>}
        {success && <p className="success">{success}</p>}
        <p className="hint auth-switch">
          Already have an account? <Link to="/login">Sign in</Link>
        </p>
      </section>
    </div>
  );
}
