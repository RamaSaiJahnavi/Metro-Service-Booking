import { Link } from "react-router-dom";

export default function NotFoundPage() {
  return (
    <section className="panel not-found">
      <h1>Page Not Found</h1>
      <p>The page you requested does not exist.</p>
      <Link className="primary-btn hero-btn" to="/">
        Back To Home
      </Link>
    </section>
  );
}
