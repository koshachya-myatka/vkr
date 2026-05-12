import { useState } from "react";
import Loader from "../general/Loader";
import { updateUser } from "../../api/api";

const roles = ["LABORATORY", "PRODUCTION", "MANAGEMENT", "ADMIN"];

export default function AdminUserCard({ user }) {
    const [editData, setEditData] = useState(user);
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setEditData(prev => ({
            ...prev,
            [e.target.name]: e.target.value
        }));
    };

    const handleSave = async () => {
        setLoading(true);
        try {
            await updateUser(user.userId, editData);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="user-card">
            <div className="user-card-header">
                <div>
                    <div className="user-id">ID: {user.userId}</div>
                    <div className="user-username">@{user.username}</div>
                </div>

                <div className="user-role-badge">
                    {editData.role}
                </div>
            </div>

            <div className="user-card-body">
                <label>Фамилия</label>
                <input
                    className="input"
                    name="surname"
                    value={editData.surname}
                    onChange={handleChange}
                />

                <label>Имя</label>
                <input
                    className="input"
                    name="name"
                    value={editData.name}
                    onChange={handleChange}
                />

                <label>Отчество</label>
                <input
                    className="input"
                    name="patronymic"
                    value={editData.patronymic}
                    onChange={handleChange}
                />

                <label>Email</label>
                <input
                    className="input"
                    name="email"
                    value={editData.email}
                    onChange={handleChange}
                />

                <label>Роль</label>
                <select
                    className="select"
                    name="role"
                    value={editData.role}
                    onChange={handleChange}
                >
                    {roles.map(role => (
                        <option key={role} value={role}>
                            {role}
                        </option>
                    ))}
                </select>

                <div className="created-at">
                    Создан: {user.createdAt}
                </div>
            </div>

            <div className="user-card-footer">
                {loading ? (
                    <Loader size="supersmall" />
                ) : (
                    <button
                        className="btn btn-primary"
                        onClick={handleSave}
                    >
                        Сохранить
                    </button>
                )}
            </div>
        </div>
    );
}