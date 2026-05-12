import AdminUserCard from "./AdminUserCard";

export default function AdminUsersGrid({ data }) {
    if (!data || data.length === 0) {
        return (
            <div className="page-section">
                <h4>Нет данных</h4>
            </div>
        );
    }

    return (
        <div className="users-grid">
            {data.map(user => (
                <AdminUserCard key={user.userId} user={user} />
            ))}
        </div>
    );
}