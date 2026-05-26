import { useState } from "react";
import Loader from "../general/Loader";
import { updateNotification } from "../../api/api";

const STATUSES_NAMES = {
    CREATED: "СОЗДАНО",
    IN_PROGRESS: "НА ПРОВЕРКЕ",
    FALSE_POSITIVE: "ЛОЖНОЕ СРАБАТЫВАНИЕ",
    RESOLVED: "ПРИЧИНА УСТРАНЕНА"
};
const STATUS_BADGES = {
    CREATED: "badge-danger",
    IN_PROGRESS: "badge-warning",
    FALSE_POSITIVE: "badge-info",
    RESOLVED: "badge-success"
};

export default function NotificationCard({ item, reload }) {
    const [loading, setLoading] = useState(false);
    const [status, setStatus] = useState(item.status);
    const [comment, setComment] = useState(item.comment || "");

    const handleSave = async () => {
        setLoading(true);
        try {
            await updateNotification(
                item.id,
                {
                    status,
                    comment
                }
            );
            reload();
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="user-card">
            <div className="user-card-header">
                <div className="flex gap-sm">
                    <div
                        className={"badge badge-" + (item.severity === 'WARNING' ? "warning"
                            : (item.severity === "ALARM" ? "danger" : "info"))}
                        style={{ borderRadius: '50%', width: '30px', alignSelf: 'center' }}
                    >
                        <span className="material-symbols-outlined">
                            priority_high
                        </span>
                    </div>
                    <div>
                        <div className="user-id">
                            #{item.id}
                        </div>
                        <div className="user-username">
                            {item.signalSource}
                        </div>
                    </div>
                </div>

                <div className={`badge ${STATUS_BADGES[status]}`}>
                    {STATUSES_NAMES[status]}
                </div>
            </div>

            <div className="user-card-body">
                <label>Сообщение</label>
                <div className="user-card-text">
                    {item.message}
                </div>

                <label>ID оборудования</label>
                <div className="user-card-text">
                    {item.equipmentId || "—"}
                </div>

                <label>ID датчика</label>
                <div className="user-card-text">
                    {item.sensorId || "—"}
                </div>

                <label>Статус</label>
                <select
                    className="select search-select"
                    value={status}
                    onChange={(e) =>
                        setStatus(
                            e.target.value
                        )
                    }
                >
                    {
                        Object.entries(
                            STATUSES_NAMES
                        ).map(([key, value]) => (
                            <option
                                key={key}
                                value={key}
                            >
                                {value}
                            </option>
                        ))
                    }
                </select>

                <label>Комментарий</label>
                <textarea
                    className="input"
                    value={comment}
                    onChange={(e) =>
                        setComment(
                            e.target.value
                        )
                    }
                />

                <div className="created-at">
                    Создано: {new Date(item.createdAt).toLocaleString()}
                </div>

                <div className="created-at">
                    Обновлено: {item.updatedAt ? new Date(item.updatedAt).toLocaleString() : "—"}
                </div>

                <div className="created-at">
                    Изменил: {item.updatedBy || "—"}
                </div>
            </div>

            <div className="user-card-footer">
                {
                    loading
                        ? <Loader size="supersmall" />
                        : (
                            <button
                                className="btn btn-primary"
                                onClick={handleSave}
                            >
                                Сохранить
                            </button>
                        )
                }
            </div>
        </div>
    );
}