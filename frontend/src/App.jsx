import { Navigate, Route, Routes } from "react-router-dom";
import RoleRouteGuard from "./components/RoleRouteGuard";
import Layout from "./components/Layout";
import AdminDashboardPage from "./pages/AdminDashboardPage";
import LoginPage from "./pages/LoginPage";
import NotFoundPage from "./pages/NotFoundPage";
import SearchPage from "./pages/SearchPage";
import SignupPage from "./pages/SignupPage";
import UserPortalPage from "./pages/UserPortalPage";

export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route
          path="/admin"
          element={
            <RoleRouteGuard allowedRoles={["ADMIN"]}>
              <AdminDashboardPage />
            </RoleRouteGuard>
          }
        />
        <Route
          path="/user"
          element={
            <RoleRouteGuard allowedRoles={["USER", "ADMIN"]}>
              <UserPortalPage />
            </RoleRouteGuard>
          }
        />
        <Route
          path="/search"
          element={
            <RoleRouteGuard allowedRoles={["USER", "ADMIN"]}>
              <SearchPage />
            </RoleRouteGuard>
          }
        />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Routes>
  );
}
