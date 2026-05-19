import { useState } from "react";

const roles = ["LABORATORY", "PRODUCTION", "MANAGEMENT", "ADMIN"];

export default function AdminSearchPanel({ onSearch }) {
    const [username, setUsername] = useState("");
    const [name, setName] = useState("");
    const [surname, setSurname] = useState("");
    const [role, setRole] = useState("");

    const handleSubmit = () => {
        onSearch({
            username: username || null,
            name: name || null,
            surname: surname || null,
            role: role || null
        });
    };

    const resetFilter = () => {
        setUsername("");
        setName("");
        setSurname("");
        setRole("");
        onSearch({
            username: null,
            name: null,
            surname: null,
            role: null
        });
    };

    const handleKeyDown = (e) => {
        if (e.key === "Enter") {
            handleSubmit();
        }
    };

    return (
        <div className="card search-panel-wrapper">
            <div className="search-panel-actions">
                <button
                    className="btn"
                    onClick={resetFilter}
                >
                    Сброс
                </button>

                <button
                    className="btn btn-primary"
                    onClick={handleSubmit}
                >
                    Найти
                </button>
            </div>

            <div className="search-panel">
                <input
                    className="input"
                    placeholder="Username"
                    value={username}
                    onChange={(e) =>
                        setUsername(
                            e.target.value
                        )
                    }
                    onKeyDown={handleKeyDown}
                />

                <input
                    className="input"
                    placeholder="Имя"
                    value={name}
                    onChange={(e) =>
                        setName(
                            e.target.value
                        )
                    }
                    onKeyDown={handleKeyDown}
                />

                <input
                    className="input"
                    placeholder="Фамилия"
                    value={surname}
                    onChange={(e) =>
                        setSurname(
                            e.target.value
                        )
                    }
                    onKeyDown={handleKeyDown}
                />

                <select
                    className="select search-select"
                    value={role}
                    onChange={(e) =>
                        setRole(
                            e.target.value
                        )
                    }
                    onKeyDown={handleKeyDown}
                >
                    <option value="">
                        Все роли
                    </option>
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
            </div>
        </div>
    );
}