import { Navigate } from "react-router-dom";

export default function RoleRedirect() {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');

    if (!token) {
        return <Navigate to="/login" replace />;
    }

    switch (role) {
        case 'LABORATORY':
            return <Navigate to="/laboratory" replace />;
        case 'PRODUCTION':
            return <Navigate to="/production" replace />;
        case 'MANAGEMENT':
            return <Navigate to="/management" replace />;
        case 'ADMIN':
            return <Navigate to="/admin" replace />;
        default:
            return <Navigate to="/login" replace />;
    }
}