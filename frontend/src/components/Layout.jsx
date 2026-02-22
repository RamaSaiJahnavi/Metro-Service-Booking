import { Outlet } from "react-router-dom";
import Navbar from "./Navbar";

export default function Layout() {
    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-blue-100">
            {/* Navbar */}
            <Navbar />

            {/* Main Content */}
            <main className="max-w-7xl mx-auto px-6 py-10">
                <div className="bg-white shadow-xl rounded-2xl p-8 min-h-[75vh] transition-all duration-300">
                    <Outlet />
                </div>
            </main>
        </div>
    );
}