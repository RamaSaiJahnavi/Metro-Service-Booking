import { Navigate } from "react-router-dom";
import { getAuthRole, isLoggedIn } from "../auth";

export default function RoleRouteGuard({ allowedRoles, children }) {
  if (!isLoggedIn()) {
    return <Navigate to="/login" replace />;
  }

  const role = getAuthRole();
  if (!allowedRoles.includes(role)) {
    return <Navigate to={role === "ADMIN" ? "/admin" : "/user"} replace />;
  }

  return children;
}
