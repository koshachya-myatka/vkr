import AdminUserRow from "./AdminUserRow";

export default function AdminUsersTable({ data }) {
    if (!data || data.length === 0) {
        return (
            <div className="page-section">
                <h4>
                    Нет данных
                </h4>
            </div>
        );
    }

    return (
        <div className="table-wrapper">
            <table className="table">
                <thead>
                    <tr>
                        <th>ID пользователя</th>
                        <th>Username</th>
                        <th>Фамилия</th>
                        <th>Имя</th>                        
                        <th>Отчество</th>
                        <th>Email</th>
                        <th>Роль</th>
                        <th>Дата создания</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {
                        data && data.map(user => (
                            <AdminUserRow key={user.userId} user={user} />
                        ))
                    }
                </tbody>
            </table>
        </div>
    );
}