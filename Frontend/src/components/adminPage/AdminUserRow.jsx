import { useState } from "react";
import Loader from "../general/Loader";
import { updateUser } from "../../api/api";

const roles = ["LABORATORY", "PRODUCTION", "MANAGEMENT", "ADMIN"];

export default function AdminUserRow({ user }) {
    const [editData, setEditData] = useState(user);
    const [loading, setLoading] = useState(false);

    const handleChange = e => {
        setEditData(prev => ({
            ...prev,
            [e.target.name]:
                e.target.value
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
        <tr>
            <td>{user.userId}</td>
            <td>{user.username}</td>
            <td>
                <input
                    className="input"
                    name="surname"
                    value={editData.surname}
                    onChange={handleChange}
                />
            </td>
            <td>
                <input
                    className="input"
                    name="name"
                    value={editData.name}
                    onChange={handleChange}
                />
            </td>
            <td>
                <input
                    className="input"
                    name="patronymic"
                    value={editData.patronymic}
                    onChange={handleChange}
                />
            </td>
            <td>
                <input
                    className="input"
                    name="email"
                    value={editData.email}
                    onChange={handleChange}
                />
            </td>
            <td>
                <select
                    className="select"
                    name="role"
                    value={editData.role}
                    onChange={handleChange}
                >
                    {
                        roles.map(role => (
                            <option
                                key={role}
                                value={role}
                            >
                                {role}
                            </option>
                        ))
                    }
                </select>
            </td>
            <td>{user.createdAt}</td>
            <td>
                {
                    loading
                        ? <Loader size="supersmall" />
                        : <button
                            className="btn btn-primary"
                            disabled={loading}
                            onClick={handleSave}
                        >
                            Сохранить
                        </button>
                }
            </td>
        </tr>
    );
}